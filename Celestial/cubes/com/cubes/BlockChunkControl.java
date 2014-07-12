/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import jme3tools.optimize.LodGenerator;

import com.celestial.SinglePlayer.Components.Planet.Planet;
import com.celestial.SinglePlayer.Components.Planet.PlanetFace;
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
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author Carl<br>
 * Modified by Kevin Thorne
 */
public class BlockChunkControl extends AbstractControl implements BitSerializable{

	private BlockTerrainControl terrain;
	private Vector3i location = new Vector3i();
	private Vector3i blockLocation = new Vector3i();
	private Map<Vector3i, BlockData> blockData;
	private Node node = new Node();
	private Geometry optimizedGeometry_Opaque;
	private Geometry optimizedGeometry_Transparent;
	private boolean needsMeshUpdate;
	private int blocks;
	private boolean loaded;
	
	LodControl lc_Opaque = new LodControl();
	LodControl lc_Transparent = new LodControl();

	public BlockChunkControl(BlockTerrainControl terrain, Vector3i location){
		this.terrain = terrain;
		this.location = location;
		this.blockLocation.set(location.mult(terrain.getSettings().getChunkSizeX(), terrain.getSettings().getChunkSizeY(), terrain.getSettings().getChunkSizeZ()));
		this.node.setLocalTranslation(new Vector3f(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()).mult(terrain.getSettings().getBlockSize()));
		
		this.blockData = new HashMap<Vector3i, BlockData>();
		this.blocks = 0;
		this.loaded = false;
	}
	
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
		if(isValidBlockLocation(location))
		{
			if(!(blockData.containsKey(location)))
				return BlockManager.getInstance().getType((byte)0);
			else
			{
				BlockData data = blockData.get(location);
				byte type = data.getBlockType();
				return BlockManager.getInstance().getType(type);
			}
		}
		return BlockManager.getInstance().getType((byte)0);
	}

	public void setBlock(Vector3i location, Class<? extends Block> blockClass){
		if(isValidBlockLocation(location)){
			BlockType blockType = BlockManager.getInstance().getType(blockClass);
			blockData.put(location, new BlockData(blockType.getType(), location, this));
			needsMeshUpdate = true;
			blocks++;
		}
	}

	public void removeBlock(Vector3i location){
		if(isValidBlockLocation(location)){
			blockData.remove(location);
			needsMeshUpdate = true;
			blocks--;
		}
	}

	public boolean isLoaded()
	{
		return this.loaded;
	}
	
	public void load(Map<Vector3i, BlockData> blockData) 
	{
		if(this.loaded)
			return;
		this.blockData = blockData;
		for(Iterator<Entry<Vector3i, BlockData>> iterator = this.blockData.entrySet().iterator(); iterator.hasNext();)
		{
			Entry<Vector3i, BlockData> entry = iterator.next();
			entry.getValue().setChunk(this);
		}
		this.blocks = this.blockData.size();
		this.needsMeshUpdate = true;
		this.loaded = true;
	}
	
	public void unLoad()
	{
		if(!this.loaded)
			return;
		this.blockData = new HashMap<Vector3i, BlockData>();
		this.blocks = 0;
		this.needsMeshUpdate = true;
		this.loaded = false;
	}
	
	public int getBlockAmount() {
		return blocks;
	}

	private boolean isValidBlockLocation(Vector3i location){
		int xMax = terrain.getSettings().getChunkSizeX();
		int yMax = terrain.getSettings().getChunkSizeY();
		int zMax = terrain.getSettings().getChunkSizeZ();
		Vector3i max = new Vector3i(xMax, yMax, zMax);
		
		if(location.hasNegativeCoordinate())
			return false;
		if(max.subtract(location).hasNegativeCoordinate())
			return false;
		
		return true;
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
			try {
				optimizedGeometry_Opaque.setMesh(terrain.getMesher().generateMesh(this, terrain.getSettings().getChunkSizeX(), false));
				optimizedGeometry_Transparent.setMesh(terrain.getMesher().generateMesh(this, terrain.getSettings().getChunkSizeX(), true));
			} catch (IllegalArgumentException e) {
				optimizedGeometry_Opaque.setMesh(BlockChunk_MeshOptimizer.generateOptimizedMesh(this, BlockChunk_TransparencyMerger.OPAQUE));
				optimizedGeometry_Transparent.setMesh(BlockChunk_MeshOptimizer.generateOptimizedMesh(this, BlockChunk_TransparencyMerger.TRANSPARENT));
			}
			LodGenerator lod = new LodGenerator(optimizedGeometry_Opaque);
			lod.bakeLods(LodGenerator.TriangleReductionMethod.COLLAPSE_COST, 0.9f);
			lod = new LodGenerator(optimizedGeometry_Transparent);
			lod.bakeLods(LodGenerator.TriangleReductionMethod.COLLAPSE_COST, 0.9f);
			optimizedGeometry_Opaque.addControl(lc_Opaque);
			optimizedGeometry_Transparent.addControl(lc_Transparent);
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

	public boolean isBlockOnSurface(Vector3i location)
	{
		return blockData.get(location).getIsOnSurface();
	}
	
	public PlanetFace getBlockPlanetFace(Vector3i location)
	{
		return blockData.get(location).getCurrentFaceOfPlanet();
	}

	public Map<Vector3i, BlockData> getBlockData()
	{
		return this.blockData;
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

	public boolean isFaceVisible(Vector3i loc, Face face, boolean isTransparent) {
		BlockType block = getBlock(loc);
        if(block.getSkin().isTransparent() == isTransparent){
            BlockType neighborBlock = getNeighborBlock_Local(loc, face);
            if(neighborBlock != null){
                if(block.getSkin().isTransparent() != neighborBlock.getSkin().isTransparent()){
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
	}
	public boolean isFaceVisible(Vector3i loc, Face face) {
		try {
			Vector3i vec = loc.add(face.getOffsetVector());
			byte type;
			if (!isLocationInChunk(vec.getX(), vec.getY(), vec.getZ())) {
				type = terrain.getBlock(vec.add(blockLocation)).getType();
			} else {
				type = getBlock(loc.add(face.getOffsetVector())).getType();
			}
			return type == 0 || BlockManager.getInstance().getType(type).getSkin().isTransparent();
		} catch (NullPointerException e) {
			return true;
		}
	}
	public boolean isLocationInChunk(int x, int y, int z) {
		return x < this.terrain.getSettings().getChunkSizeX() 
				&& y < this.terrain.getSettings().getChunkSizeY()  
				&& z < this.terrain.getSettings().getChunkSizeZ()  
				&& x > -1 && y > -1 && z > -1;
	}
	public boolean isLocationInChunk(Vector3i loc) {
		return isLocationInChunk(loc.getX(), loc.getY(), loc.getZ());
	}
	
	@Override 
	public int hashCode()
	{
		final int prime = 2;
		int result = 1;
		
		result = prime * result + (this.enabled ? 1 : 0);
		result = prime * result + (this.needsMeshUpdate ? 1 : 0);
		result = prime * result + this.blockData.hashCode();
		result = prime * result + this.blocks;
		result = prime * result + this.location.hashCode();
		
		return result;
	}
	
	@Override 
	public boolean equals(Object other)
	{
		if(other == null)
			return false;
		if(!(other instanceof BlockChunkControl))
			return false;
		BlockChunkControl otherChunk = (BlockChunkControl) other;
		if(otherChunk.enabled == this.enabled && otherChunk.needsMeshUpdate == this.needsMeshUpdate && otherChunk.blockData.equals(this.blockData) && otherChunk.blocks == this.blocks && otherChunk.location == this.location)
			return true;
		return false;
	}

}
