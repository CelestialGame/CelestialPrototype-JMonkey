/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.celestial.SinglePlayer.Components.Planet;
import com.cubes.Block.Face;
import com.cubes.network.BitInputStream;
import com.cubes.network.BitOutputStream;
import com.cubes.network.BitSerializable;
import com.cubes.render.BlockChunk_MeshOptimizer;
import com.cubes.render.BlockChunk_TransparencyMerger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.control.LodControl;

import jme3tools.optimize.LodGenerator;

/**
 *
 * @author Carl<br>
 * Modified by Kevin Thorne
 */
public class BlockChunkControl extends AbstractControl implements BitSerializable{

	public class CachedBlock {
		private byte data;
		private Vector3i location;

		public CachedBlock(byte data, Vector3i location) {
			this.data = data;
			this.location = location;
		}

		public byte getByteData() { return data; }

		public Vector3i getLocation()
		{
			return this.location;
		}
	}

	public BlockChunkControl(BlockTerrainControl terrain, int x, int y, int z){
		this.terrain = terrain;
		location.set(x, y, z);
		blockLocation.set(location.mult(terrain.getSettings().getChunkSizeX(), terrain.getSettings().getChunkSizeY(), terrain.getSettings().getChunkSizeZ()));
		node.setLocalTranslation(new Vector3f(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()).mult(terrain.getSettings().getBlockSize()));
		blockData = new HashMap<Vector3i, BlockData>();
		lc_Opaque = new LodControl();
		lc_Transparent = new LodControl();
	}
	private BlockTerrainControl terrain;
	private Vector3i location = new Vector3i();
	private Vector3i blockLocation = new Vector3i();
	private HashMap<Vector3i, BlockData> blockData;
	private List<CachedBlock> savedBlockTypes = new ArrayList<CachedBlock>();
	private Node node = new Node();
	private Geometry optimizedGeometry_Opaque;
	private Geometry optimizedGeometry_Transparent;
	private LodControl lc_Opaque;
	private LodControl lc_Transparent;
	private boolean needsMeshUpdate;
	private boolean loaded;
	private int blocks;

	@Override
	public void setSpatial(Spatial spatial){
		Spatial oldSpatial = this.spatial;
		super.setSpatial(spatial);
		if(spatial instanceof Node){
			Node parentNode = (Node) spatial;
			parentNode.attachChild(node);
		}
		else if(oldSpatial instanceof Node){
			Node oldNode = (Node) oldSpatial;
			oldNode.detachChild(node);
		}
	}

	@Override
	protected void controlUpdate(float lastTimePerFrame){

	}

	@Override
	protected void controlRender(RenderManager renderManager, ViewPort viewPort){

	}

	@Override
	public Control cloneForSpatial(Spatial spatial){
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public BlockType getNeighborBlock_Local(Vector3i location, Block.Face face){
		Vector3i neighborLocation = BlockNavigator.getNeighborBlockLocalLocation(location, face);
		return getBlock(neighborLocation);
	}

	public BlockType getNeighborBlock_Global(Vector3i location, Block.Face face){
		return terrain.getBlock(getNeighborBlockGlobalLocation(location, face));
	}

	private Vector3i getNeighborBlockGlobalLocation(Vector3i location, Block.Face face){
		Vector3i neighborLocation = BlockNavigator.getNeighborBlockLocalLocation(location, face);
		neighborLocation.addLocal(blockLocation);
		return neighborLocation;
	}

	public BlockType getBlock(Vector3i location){
		if(isValidBlockLocation(location)){
			if(!blockData.containsKey(location))
				return BlockManager.getInstance().getType((byte)0);
			else
			{
				return BlockManager.getInstance().getType(blockData.get(location).getBlockType());
			}
		}
		return null;
	}

	public void setBlock(Vector3i location, Class<? extends Block> blockClass){
		if(isValidBlockLocation(location)){
			BlockType blockType = BlockManager.getInstance().getType(blockClass);
			blockData.put(location, new BlockData(blockType.getType()));
			updateBlockState(location);
			needsMeshUpdate = true;
			blocks++;
		}
	}

	public void removeBlock(Vector3i location){
		if(isValidBlockLocation(location)){
			blockData.remove(location);
			updateBlockState(location);
			needsMeshUpdate = true;
			blocks--;
		}
	}

	public void unloadChunk() {
		if(!loaded) {
			return;
		}
		this.savedBlockTypes.clear();
		Iterator<Entry<Vector3i, BlockData>> iterator = blockData.entrySet().iterator();
		while(iterator.hasNext()) 
		{
			Entry<Vector3i, BlockData> entry = iterator.next();
			this.savedBlockTypes.add(new CachedBlock(entry.getValue().getBlockType(), entry.getKey()));
			iterator.remove();
		}
		loaded = false;
	}

	public void loadChunk() {
		if(loaded) {
			return;
		}
		for(CachedBlock block : this.savedBlockTypes) {
			blockData.put(block.getLocation(), new BlockData(block.getByteData()));
			updateBlockState(block.getLocation());
			needsMeshUpdate = true;
		}
		loaded = true;
	}

	public int getBlockAmount(boolean live) {
		if(!live)
			if(loaded) 
				return blocks;
			else
				return this.savedBlockTypes.size();
		else
			return blocks;
	}

	private boolean isValidBlockLocation(Vector3i location){
		return true; //TODO: Check whether block is too far away or something idk
	}

	public boolean updateSpatial(){
		if(needsMeshUpdate){
			if(optimizedGeometry_Opaque == null){
				optimizedGeometry_Opaque = new Geometry("");
				optimizedGeometry_Opaque.setQueueBucket(Bucket.Opaque);
				node.attachChild(optimizedGeometry_Opaque);
				updateBlockMaterial();
			}
			if(optimizedGeometry_Transparent == null){
				optimizedGeometry_Transparent = new Geometry("");
				optimizedGeometry_Transparent.setQueueBucket(Bucket.Transparent);
				node.attachChild(optimizedGeometry_Transparent);
				updateBlockMaterial();
			}
			optimizedGeometry_Opaque.setMesh(BlockChunk_MeshOptimizer.generateOptimizedMesh(this, BlockChunk_TransparencyMerger.OPAQUE));
			optimizedGeometry_Transparent.setMesh(BlockChunk_MeshOptimizer.generateOptimizedMesh(this, BlockChunk_TransparencyMerger.TRANSPARENT));
			//optimizedGeometry_Opaque.setMesh(terrain.getMesher().generateMesh(this, terrain.getSettings().getChunkSizeX()));
			//optimizedGeometry_Transparent.setMesh(terrain.getMesher().generateMesh(this, terrain.getSettings().getChunkSizeX()));
			LodGenerator lod = new LodGenerator(optimizedGeometry_Opaque);
			lod.bakeLods(LodGenerator.TriangleReductionMethod.COLLAPSE_COST, 0.5f);
			optimizedGeometry_Opaque.addControl(lc_Opaque);
			//optimizedGeometry_Transparent.addControl(lc);
			needsMeshUpdate = false;
			return true;
		}
		return false;
	}

	public void updateBlockMaterial(){
		if(optimizedGeometry_Opaque != null){
			optimizedGeometry_Opaque.setMaterial(terrain.getSettings().getBlockMaterial());
		}
		if(optimizedGeometry_Transparent != null){
			optimizedGeometry_Transparent.setMaterial(terrain.getSettings().getBlockMaterial());
		}
	}

	private void updateBlockState(Vector3i location){
		updateBlockInformation(location);
		for(int i=0;i<Block.Face.values().length;i++){
			Vector3i neighborLocation = getNeighborBlockGlobalLocation(location, Block.Face.values()[i]);
			BlockChunkControl chunk = terrain.getChunk(neighborLocation);
			if(chunk != null){
				chunk.updateBlockInformation(neighborLocation.subtract(chunk.getBlockLocation()));
			}
		}
	}

	private void updateBlockInformation(Vector3i location){
		BlockType neighborBlock_Top = terrain.getBlock(getNeighborBlockGlobalLocation(location, Block.Face.Top));
		if(blockData.containsKey(location))
			blockData.get(location).setIsOnSurface(neighborBlock_Top == null);
	}

	public boolean isBlockOnSurface(Vector3i location){
		return blockData.get(location).getIsOnSurface();
	}

	public BlockTerrainControl getTerrain(){
		return terrain;
	}

	public Vector3i getLocation(){
		return location;
	}

	public Vector3i getBlockLocation(){
		return blockLocation;
	}

	public Node getNode(){
		return node;
	}

	public Geometry getOptimizedGeometry_Opaque(){
		return optimizedGeometry_Opaque;
	}

	public Geometry getOptimizedGeometry_Transparent(){
		return optimizedGeometry_Transparent;
	}

	@Override
	public void write(BitOutputStream outputStream){
		/*for(int x=0;x<blockData.length;x++){
			for(int y=0;y<blockData[0].length;y++){
				for(int z=0;z<blockData[0][0].length;z++){
					outputStream.writeBits(blockData[x][y][z], 8);
				}
			}
		}*/
		//TODO: Write data to file (Serialize)
	}

	@Override
	public void read(BitInputStream inputStream) throws IOException{
		/*for(int x=0;x<blockData.length;x++){
			for(int y=0;y<blockData[0].length;y++){
				for(int z=0;z<blockData[0][0].length;z++){
					blockData[x][y][z] = (byte) inputStream.readBits(8);
				}
			}
		}
		Vector3i tmpLocation = new Vector3i();
		for(int x=0;x<blockData.length;x++){
			for(int y=0;y<blockData[0].length;y++){
				for(int z=0;z<blockData[0][0].length;z++){
					tmpLocation.set(x, y, z);
					updateBlockInformation(tmpLocation);
				}
			}
		}
		needsMeshUpdate = true;
		*/
		//TODO: Read data from file (Serialize)
	}

	private Vector3i getNeededBlockChunks(Vector3i blocksCount){
		int chunksCountX = (int) Math.ceil(((float) blocksCount.getX()) / terrain.getSettings().getChunkSizeX());
		int chunksCountY = (int) Math.ceil(((float) blocksCount.getY()) / terrain.getSettings().getChunkSizeY());
		int chunksCountZ = (int) Math.ceil(((float) blocksCount.getZ()) / terrain.getSettings().getChunkSizeZ());
		return new Vector3i(chunksCountX, chunksCountY, chunksCountZ);
	}

	public boolean isFaceVisible(Vector3i loc, Face face) {
		Vector3i vec = loc.add(face.getOffsetVector());
		BlockType type;
		if (!isValidBlockLocation(vec)) {
			type = terrain.getBlock(vec.add(blockLocation));
		} else {
			type = getBlock(loc.add(face.getOffsetVector()));
		}
		return type.getType() == 0 || BlockManager.getInstance().getType(type.getType()).getSkin().isTransparent();
	}
	public static boolean isLocationInChunk(Vector3i location, Vector3i chunkLocation) {
		return location.getX() < (chunkLocation.getX() + Planet.CHUNK_SIZE) 
				&& location.getY() < (chunkLocation.getY() + Planet.CHUNK_SIZE) 
				&& location.getZ() < (chunkLocation.getZ() + Planet.CHUNK_SIZE) 
				&& location.getX() > (chunkLocation.getX()-1) 
				&& location.getY() > (chunkLocation.getY()-1) 
				&& location.getZ() > (chunkLocation.getZ()-1);
	}
}
