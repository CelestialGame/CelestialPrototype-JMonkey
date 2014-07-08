/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.celestial.Blocks.DynamicBlock;
import com.celestial.World.BlockChunkManager;
import com.cubes.network.BitInputStream;
import com.cubes.network.BitOutputStream;
import com.cubes.network.BitSerializable;
import com.cubes.network.CubesSerializer;
import com.cubes.render.VoxelMesher;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;

/**
 *
 * @author Carl
 */
public class BlockTerrainControl extends AbstractControl implements BitSerializable{

    public BlockTerrainControl(CubesSettings settings, Vector3i chunksCount, VoxelMesher mesher){
        this.settings = settings;
        this.mesher = mesher;
        initializeChunks(chunksCount);
    }
    public BlockTerrainControl(CubesSettings settings, Vector3i chunksCount){
        this.settings = settings;
        initializeChunks(chunksCount);
    }
    
    private CubesSettings settings;
    private BlockChunkControl[][][] chunks;
    private ArrayList<BlockChunkListener> chunkListeners = new ArrayList<BlockChunkListener>();
    HashMap<Vector3i, DynamicBlock> dynamicBlocks = new HashMap<Vector3i, DynamicBlock>();
	private BlockChunkManager blockChunkManager;
	private VoxelMesher mesher;
    
    public void setBlockChunkManager(BlockChunkManager bcm) {
    	this.blockChunkManager = bcm;
    }
    public BlockChunkManager getBlockChunkManager() {
    	return blockChunkManager;
    }

    private void initializeChunks(Vector3i chunksCount){
        chunks = new BlockChunkControl[chunksCount.getX()][chunksCount.getY()][chunksCount.getZ()];
        for(int x=0;x<chunks.length;x++){
            for(int y=0;y<chunks[0].length;y++){
                for(int z=0;z<chunks[0][0].length;z++){
                    BlockChunkControl chunk = new BlockChunkControl(this, x, y, z);
                    chunks[x][y][z] = chunk;
                }
            }
        }
    }

    @Override
    public void setSpatial(Spatial spatial){
        Spatial oldSpatial = this.spatial;
        super.setSpatial(spatial);
        for(int x=0;x<chunks.length;x++){
            for(int y=0;y<chunks[0].length;y++){
                for(int z=0;z<chunks[0][0].length;z++){
                    if(spatial == null){
                        oldSpatial.removeControl(chunks[x][y][z]);
                    }
                    else{
                        spatial.addControl(chunks[x][y][z]);
                    }
                }
            }
        }
    }

    @Override
    protected void controlUpdate(float lastTimePerFrame){
        updateSpatial();
    }

    @Override
    protected void controlRender(RenderManager renderManager, ViewPort viewPort){
        
    }

    @Override
    public Control cloneForSpatial(Spatial spatial){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public BlockType getBlock(int x, int y, int z){
        return getBlock(new Vector3i(x, y, z));
    }
    
    public BlockType getBlock(Vector3i location){
        BlockTerrain_LocalBlockState localBlockState = getLocalBlockState(location);
        if(localBlockState != null){
            return localBlockState.getBlock();
        }
        return null;
    }
    
    public void setBlockArea(Vector3i location, Vector3i size, Class<? extends Block> blockClass){
        Vector3i tmpLocation = new Vector3i();
        for(int x=0;x<size.getX();x++){
            for(int y=0;y<size.getY();y++){
                for(int z=0;z<size.getZ();z++){
                    tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
                    setBlock(tmpLocation, blockClass);
                }
            }
        }
    }
    
    public void setBlock(int x, int y, int z, Class<? extends Block> blockClass){
        setBlock(new Vector3i(x, y, z), blockClass);
    }
    
    public void setBlock(Vector3i location, Class<? extends Block> blockClass){
        BlockTerrain_LocalBlockState localBlockState = getLocalBlockState(location);
        if(localBlockState != null){
        	if(com.celestial.Blocks.GameBlock.getBlockByClass(blockClass).isDynamic()) {
        		try {
					this.dynamicBlocks.put(location, (DynamicBlock) com.celestial.Blocks.GameBlock.getBlockByClass(blockClass).getBClass().newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
        	}
            localBlockState.setBlock(blockClass);
        }
    }
    
    public void removeBlockArea(Vector3i location, Vector3i size){
        Vector3i tmpLocation = new Vector3i();
        for(int x=0;x<size.getX();x++){
            for(int y=0;y<size.getY();y++){
                for(int z=0;z<size.getZ();z++){
                    tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
                    removeBlock(tmpLocation);
                }
            }
        }
    }
    
    public void removeBlock(int x, int y, int z){
        removeBlock(new Vector3i(x, y, z));
    }
    
    public void removeBlock(Vector3i location){
        BlockTerrain_LocalBlockState localBlockState = getLocalBlockState(location);
        if(localBlockState != null){
        	if(this.dynamicBlocks.containsKey(location)) {
        		this.dynamicBlocks.remove(location);
        	}
            localBlockState.removeBlock();
        }
    }
    
    private BlockTerrain_LocalBlockState getLocalBlockState(Vector3i blockLocation){
        if(blockLocation.hasNegativeCoordinate()){
            return null;
        }
        BlockChunkControl chunk = getChunk(blockLocation);
        if(chunk != null){
            Vector3i localBlockLocation = getLocalBlockLocation(blockLocation, chunk);
            return new BlockTerrain_LocalBlockState(chunk, localBlockLocation);
        }
        return null;
    }
    
    public BlockChunkControl getChunk(Vector3i blockLocation){
        if(blockLocation.hasNegativeCoordinate()){
            return null;
        }
        Vector3i chunkLocation = getChunkLocation(blockLocation);
        if(isValidChunkLocation(chunkLocation)){
            return chunks[chunkLocation.getX()][chunkLocation.getY()][chunkLocation.getZ()];
        }
        return null;
    }
    
    private boolean isValidChunkLocation(Vector3i location){
        return Util.isValidIndex(chunks, location);
    }
    
    private Vector3i getChunkLocation(Vector3i blockLocation){
        Vector3i chunkLocation = new Vector3i();
        int chunkX = (blockLocation.getX() / settings.getChunkSizeX());
        int chunkY = (blockLocation.getY() / settings.getChunkSizeY());
        int chunkZ = (blockLocation.getZ() / settings.getChunkSizeZ());
        chunkLocation.set(chunkX, chunkY, chunkZ);
        return chunkLocation;
    }
    
    public static Vector3i getLocalBlockLocation(Vector3i blockLocation, BlockChunkControl chunk){
        Vector3i localLocation = new Vector3i();
        int localX = (blockLocation.getX() - chunk.getBlockLocation().getX());
        int localY = (blockLocation.getY() - chunk.getBlockLocation().getY());
        int localZ = (blockLocation.getZ() - chunk.getBlockLocation().getZ());
        localLocation.set(localX, localY, localZ);
        return localLocation;
    }
    
    public boolean updateSpatial(){
        boolean wasUpdatedNeeded = false;
        for(int x=0;x<chunks.length;x++){
            for(int y=0;y<chunks[0].length;y++){
                for(int z=0;z<chunks[0][0].length;z++){
                    BlockChunkControl chunk = chunks[x][y][z];
                    if(chunk.updateSpatial()){
                        wasUpdatedNeeded = true;
                        for(int i=0;i<chunkListeners.size();i++){
                            BlockChunkListener blockTerrainListener = chunkListeners.get(i);
                            blockTerrainListener.onSpatialUpdated(chunk);
                        }
                    }
                }
            }
        }
        return wasUpdatedNeeded;
    }
    
    public void updateBlockMaterial(){
        for(int x=0;x<chunks.length;x++){
            for(int y=0;y<chunks[0].length;y++){
                for(int z=0;z<chunks[0][0].length;z++){
                    chunks[x][y][z].updateBlockMaterial();
                }
            }
        }
    }
    
    public void addChunkListener(BlockChunkListener blockChunkListener){
        chunkListeners.add(blockChunkListener);
    }
    
    public void removeChunkListener(BlockChunkListener blockChunkListener){
        chunkListeners.remove(blockChunkListener);
    }
    
    public CubesSettings getSettings(){
        return settings;
    }

    public BlockChunkControl[][][] getChunks(){
        return chunks;
    }
    public HashMap<Vector3i, DynamicBlock> getDynamicBlocks() {
    	return this.dynamicBlocks;
    }
    
    //Tools
    
    public void setBlocksFromHeightmap(Vector3i location, String heightmapPath, int maximumHeight, Class<? extends Block> blockClass){
        try{
            Texture heightmapTexture = settings.getAssetManager().loadTexture(heightmapPath);
            ImageBasedHeightMap heightmap = new ImageBasedHeightMap(heightmapTexture.getImage(), 1f);
            heightmap.load();
            heightmap.setHeightScale(maximumHeight / 255f);
            setBlocksFromHeightmap(location, getHeightmapBlockData(heightmap.getScaledHeightMap(), heightmap.getSize()), blockClass);
        }catch(Exception ex){
            System.out.println("Error while loading heightmap '" + heightmapPath + "'.");
        }
    }

    private static int[][] getHeightmapBlockData(float[] heightmapData, int length){
        int[][] data = new int[heightmapData.length / length][length];
        int x = 0;
        int z = 0;
        for(int i=0;i<heightmapData.length;i++){
            data[x][z] = (int) Math.round(heightmapData[i]);
            x++;
            if((x != 0) && ((x % length) == 0)){
                x = 0;
                z++;
            }
        }
        return data;
    }

    public void setBlocksFromHeightmap(Vector3i location, int[][] heightmap, Class<? extends Block> blockClass){
        Vector3i tmpLocation = new Vector3i();
        Vector3i tmpSize = new Vector3i();
        for(int x=0;x<heightmap.length;x++){
            for(int z=0;z<heightmap[0].length;z++){
                tmpLocation.set(location.getX() + x, location.getY(), location.getZ() + z);
                tmpSize.set(1, heightmap[x][z], 1);
                setBlockArea(tmpLocation, tmpSize, blockClass);
            }
        }
    }
    
    public void setBlocksFromNoise(Vector3i location, Vector3i size, float roughness, Class<? extends Block> blockClass){
        RandomTerrainGenerator noise = new RandomTerrainGenerator(null, roughness, size.getX(), size.getZ());
        noise.initialise();
        float gridMinimum = noise.getMinimum();
        float gridLargestDifference = (noise.getMaximum() - gridMinimum);
        float[][] grid = noise.getGrid();
        for(int x=0;x<grid.length;x++){
            float[] row = grid[x];
            for(int z=0;z<row.length;z++){
                /*---Calculation of block height has been summarized to minimize the java heap---
                float gridGroundHeight = (row[z] - gridMinimum);
                float blockHeightInPercents = ((gridGroundHeight * 100) / gridLargestDifference);
                int blockHeight = ((int) ((blockHeightInPercents / 100) * size.getY())) + 1;
                ---*/
                int blockHeight = (((int) (((((row[z] - gridMinimum) * 100) / gridLargestDifference) / 100) * size.getY())) + 1);
                Vector3i tmpLocation = new Vector3i();
                for(int y=0;y<blockHeight;y++){
                    tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
                    setBlock(tmpLocation, blockClass);
                }
            }
        }
    }
    
    public void setBlocksForMaximumFaces(Vector3i location, Vector3i size, Class<? extends Block> blockClass){
        Vector3i tmpLocation = new Vector3i();
        for(int x=0;x<size.getX();x++){
            for(int y=0;y<size.getY();y++){
                for(int z=0;z<size.getZ();z++){
                    if(((x ^ y ^ z) & 1) == 1){
                        tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
                        setBlock(tmpLocation, blockClass);
                    }
                }
            }
        }
    }

    @Override
    public BlockTerrainControl clone(){
        BlockTerrainControl blockTerrain = new BlockTerrainControl(settings, new Vector3i(), this.mesher);
        blockTerrain.setBlocksFromTerrain(this);
        return blockTerrain;
    }
    
    public VoxelMesher getMesher() {
    	return this.mesher;
    }
    
    public void setBlocksFromTerrain(BlockTerrainControl blockTerrain){
        CubesSerializer.readFromBytes(this, CubesSerializer.writeToBytes(blockTerrain));
    }

    @Override
    public void write(BitOutputStream outputStream){
        outputStream.writeInteger(chunks.length);
        outputStream.writeInteger(chunks[0].length);
        outputStream.writeInteger(chunks[0][0].length);
        for(int x=0;x<chunks.length;x++){
            for(int y=0;y<chunks[0].length;y++){
                for(int z=0;z<chunks[0][0].length;z++){
                    chunks[x][y][z].write(outputStream);
                }
            }
        }
    }

    @Override
    public void read(BitInputStream inputStream) throws IOException{
        int chunksCountX = inputStream.readInteger();
        int chunksCountY = inputStream.readInteger();
        int chunksCountZ = inputStream.readInteger();
        initializeChunks(new Vector3i(chunksCountX, chunksCountY, chunksCountZ));
        for(int x=0;x<chunksCountX;x++){
            for(int y=0;y<chunksCountY;y++){
                for(int z=0;z<chunksCountZ;z++){
                    chunks[x][y][z].read(inputStream);
                }
            }
        }
    }
}
