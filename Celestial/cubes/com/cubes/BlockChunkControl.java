/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.celestial.World.Chunks.ChunkThreads;
import com.cubes.network.BitInputStream;
import com.cubes.network.BitOutputStream;
import com.cubes.network.BitSerializable;
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
		private int x;
		private int y;
		private int z;
		public CachedBlock(byte data, int x, int y, int z) {
			this.data = data;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public CachedBlock(byte data2, byte[][] x2, byte[] y2, byte z2) {
			// TODO Auto-generated constructor stub
		}
		public byte getByteData() { return data; }
		public int getX() { return x; }
		public int getY() { return y; }
		public int getZ() { return z; }
	}

    public BlockChunkControl(BlockTerrainControl terrain, int x, int y, int z){
        this.terrain = terrain;
        location.set(x, y, z);
        blockLocation.set(location.mult(terrain.getSettings().getChunkSizeX(), terrain.getSettings().getChunkSizeY(), terrain.getSettings().getChunkSizeZ()));
        node.setLocalTranslation(new Vector3f(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()).mult(terrain.getSettings().getBlockSize()));
        blockTypes = new byte[terrain.getSettings().getChunkSizeX()][terrain.getSettings().getChunkSizeY()][terrain.getSettings().getChunkSizeZ()];
        blocks_IsOnSurface = new boolean[terrain.getSettings().getChunkSizeX()][terrain.getSettings().getChunkSizeY()][terrain.getSettings().getChunkSizeZ()];
        lc_Opaque = new LodControl();
        lc_Transparent = new LodControl();
    }
    private BlockTerrainControl terrain;
    private Vector3Int location = new Vector3Int();
    private Vector3Int blockLocation = new Vector3Int();
    private byte[][][] blockTypes;
    private List<CachedBlock> savedBlockTypes = new ArrayList<CachedBlock>();
    private boolean[][][] blocks_IsOnSurface;
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
    
    public BlockType getNeighborBlock_Local(Vector3Int location, Block.Face face){
        Vector3Int neighborLocation = BlockNavigator.getNeighborBlockLocalLocation(location, face);
        return getBlock(neighborLocation);
    }
    
    public BlockType getNeighborBlock_Global(Vector3Int location, Block.Face face){
        return terrain.getBlock(getNeighborBlockGlobalLocation(location, face));
    }
    
    private Vector3Int getNeighborBlockGlobalLocation(Vector3Int location, Block.Face face){
        Vector3Int neighborLocation = BlockNavigator.getNeighborBlockLocalLocation(location, face);
        neighborLocation.addLocal(blockLocation);
        return neighborLocation;
    }
    
    public BlockType getBlock(Vector3Int location){
        if(isValidBlockLocation(location)){
            byte blockType = blockTypes[location.getX()][location.getY()][location.getZ()];
            return BlockManager.getType(blockType);
        }
        return null;
    }
    
    public void setBlock(Vector3Int location, Class<? extends Block> blockClass){
        if(isValidBlockLocation(location)){
            BlockType blockType = BlockManager.getType(blockClass);
            blockTypes[location.getX()][location.getY()][location.getZ()] = blockType.getType();
            updateBlockState(location);
            needsMeshUpdate = true;
            blocks++;
        }
    }
    
    public void removeBlock(Vector3Int location){
        if(isValidBlockLocation(location)){
            blockTypes[location.getX()][location.getY()][location.getZ()] = 0;
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
    	for (int x = 0; x < blockTypes.length; x++)
    	    for (int y = 0; y < blockTypes[x].length; y++)
    	    	for(int z = 0; z < blockTypes[x][y].length; z++) {
    	    		this.savedBlockTypes.add(new CachedBlock(blockTypes[x][y][z], x, y, z));
    	    		removeBlock(new Vector3Int(x,y,z));
    	    	}
    	loaded = false;
    }
    
    public void loadChunk() {
    	if(loaded) {
    		return;
    	}
    	for(CachedBlock block : this.savedBlockTypes) {
    		blockTypes[block.x][block.y][block.z] = block.data;
    		updateBlockState(new Vector3Int(block.x,block.y,block.z));
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
    
    private boolean isValidBlockLocation(Vector3Int location){
        return Util.isValidIndex(blockTypes, location);
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
    		//terrain.getBlockChunkManager().generateLODBake(optimizedGeometry_Opaque);
    		LodGenerator lod = new LodGenerator(optimizedGeometry_Opaque);
    		lod.bakeLods(LodGenerator.TriangleReductionMethod.PROPORTIONAL, 0.5f);
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
    
    private void updateBlockState(Vector3Int location){
        updateBlockInformation(location);
        for(int i=0;i<Block.Face.values().length;i++){
            Vector3Int neighborLocation = getNeighborBlockGlobalLocation(location, Block.Face.values()[i]);
            BlockChunkControl chunk = terrain.getChunk(neighborLocation);
            if(chunk != null){
                chunk.updateBlockInformation(neighborLocation.subtract(chunk.getBlockLocation()));
            }
        }
    }
    
    private void updateBlockInformation(Vector3Int location){
        BlockType neighborBlock_Top = terrain.getBlock(getNeighborBlockGlobalLocation(location, Block.Face.Top));
        blocks_IsOnSurface[location.getX()][location.getY()][location.getZ()] = (neighborBlock_Top == null);
    }

    public boolean isBlockOnSurface(Vector3Int location){
        return blocks_IsOnSurface[location.getX()][location.getY()][location.getZ()];
    }

    public BlockTerrainControl getTerrain(){
        return terrain;
    }

    public Vector3Int getLocation(){
        return location;
    }

    public Vector3Int getBlockLocation(){
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
        for(int x=0;x<blockTypes.length;x++){
            for(int y=0;y<blockTypes[0].length;y++){
                for(int z=0;z<blockTypes[0][0].length;z++){
                    outputStream.writeBits(blockTypes[x][y][z], 8);
                }
            }
        }
    }

    @Override
    public void read(BitInputStream inputStream) throws IOException{
        for(int x=0;x<blockTypes.length;x++){
            for(int y=0;y<blockTypes[0].length;y++){
                for(int z=0;z<blockTypes[0][0].length;z++){
                    blockTypes[x][y][z] = (byte) inputStream.readBits(8);
                }
            }
        }
        Vector3Int tmpLocation = new Vector3Int();
        for(int x=0;x<blockTypes.length;x++){
            for(int y=0;y<blockTypes[0].length;y++){
                for(int z=0;z<blockTypes[0][0].length;z++){
                    tmpLocation.set(x, y, z);
                    updateBlockInformation(tmpLocation);
                }
            }
        }
        needsMeshUpdate = true;
    }
    
    private Vector3Int getNeededBlockChunks(Vector3Int blocksCount){
        int chunksCountX = (int) Math.ceil(((float) blocksCount.getX()) / terrain.getSettings().getChunkSizeX());
        int chunksCountY = (int) Math.ceil(((float) blocksCount.getY()) / terrain.getSettings().getChunkSizeY());
        int chunksCountZ = (int) Math.ceil(((float) blocksCount.getZ()) / terrain.getSettings().getChunkSizeZ());
        return new Vector3Int(chunksCountX, chunksCountY, chunksCountZ);
    }
}
