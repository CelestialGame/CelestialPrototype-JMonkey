package com.celestial.World.Chunks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import com.celestial.Blocks.BlocksEnum;
import com.celestial.SinglePlayer.Components.Planet;
import com.celestial.SinglePlayer.Components.Planet.planetType;
import com.celestial.World.BlockChunkManager.PreGeneratedChunk;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;

public class ChunkThreads {
	
	public static class PreGenChunksThread implements Callable<List<PreGeneratedChunk>> {
		int diameter;
		List<PreGeneratedChunk> preGenChunkList;
		BlockTerrainControl terrControl;
		Planet planet;
		public PreGenChunksThread (int diameter, BlockTerrainControl terrControl, List<PreGeneratedChunk> preGenChunkList, Planet planet) {
			this.diameter = diameter;
			this.preGenChunkList = preGenChunkList;
			this.terrControl = terrControl;
			this.planet = planet;
		}

		@Override
		public List<PreGeneratedChunk> call() throws Exception {
			for(int i=0; i<diameter; i++) //y
			{    
				for(int j=0; j<diameter; j++) //x
				{
					for(int k=0; k<diameter; k++) //z
					{
						PreGeneratedChunk chunk = new PreGeneratedChunk(terrControl, j, i, k, planet);
						this.preGenChunkList.add(chunk);
					}                
				}
			}
			return this.preGenChunkList;
		}
		
	}
	public static class GenerateChunkThread extends Thread{
		BlockTerrainControl blockTerrain;
		int x;
		int y;
		int z;
		PreGeneratedChunk preChunk;
		List<Vector3Int> randomTerrainGenerationX = new ArrayList<Vector3Int>();
		List<Vector3Int> randomTerrainGeneration2X = new ArrayList<Vector3Int>();
		List<Vector3Int> randomTerrainGenerationY = new ArrayList<Vector3Int>();
		List<Vector3Int> randomTerrainGeneration2Y = new ArrayList<Vector3Int>();
		List<Vector3Int> randomTerrainGenerationZ = new ArrayList<Vector3Int>();
		List<Vector3Int> randomTerrainGeneration2Z = new ArrayList<Vector3Int>();
		public GenerateChunkThread(BlockTerrainControl bTC, int locx, int locy, int locz, PreGeneratedChunk preChunk) {
			blockTerrain = bTC;
			x = locx;
			y = locy;
			z = locz;
			this.preChunk = preChunk;
		}
		
		public void run() {
			int chunkSize = Planet.CHUNK_SIZE;

			for(int i=0;i<chunkSize;i++)
			{
				for(int j=0;j<chunkSize;j++)
				{
					for(int k=0;k<chunkSize;k++)
					{
						/*if(j==chunkSize-1 || j == 0)
						{
							if(preChunk.getPlanet().getType().equals(planetType.HABITABLE))
								makeCubeAt(x+i,y+j,z+k, BlocksEnum.GRASS, blockTerrain);
						}
						else if(k==chunkSize-1 || k==0)
						{
							if(preChunk.getPlanet().getType().equals(planetType.HABITABLE))
								makeCubeAt(x+i,y+j,z+k, BlocksEnum.GRASS, blockTerrain);
						}
						else if(i==chunkSize-1 || i==0)
						{
							if(preChunk.getPlanet().getType().equals(planetType.HABITABLE))
								makeCubeAt(x+i,y+j,z+k, BlocksEnum.GRASS, blockTerrain);
						}
						else
						{*/
						if(preChunk.getPlanet().getType().hasAtmosphere()) {
							//System.out.println("Chunk Generation at " + preChunk.getLocation().toString());
							if(preChunk.getLocation().getX() == preChunk.getPlanet().getDiameter()-1 || 
									preChunk.getLocation().getX() == 0) {
								return;
							} else if (preChunk.getLocation().getY() == preChunk.getPlanet().getDiameter()-1 || 
									preChunk.getLocation().getY() == 0) {
								return;
							} else if (preChunk.getLocation().getZ() == preChunk.getPlanet().getDiameter()-1 || 
									preChunk.getLocation().getZ() == 0) {
								return;
							}
							if(preChunk.getLocation().getX() == preChunk.getPlanet().getDiameter()-2 || 
									preChunk.getLocation().getX() == 1) {
								generateRandomTerrain(new Vector3Int(x,y,z), new Vector3Int(chunkSize, chunkSize, chunkSize), 5, BlocksEnum.GRASS, blockTerrain);
								return;
							} else if (preChunk.getLocation().getY() == preChunk.getPlanet().getDiameter()-2 || 
									preChunk.getLocation().getY() == 1) {
								generateRandomTerrain(new Vector3Int(x,y,z), new Vector3Int(chunkSize, chunkSize, chunkSize), 5, BlocksEnum.GRASS, blockTerrain);
								return;
							} else if (preChunk.getLocation().getZ() == preChunk.getPlanet().getDiameter()-2 || 
									preChunk.getLocation().getZ() == 1) {
								generateRandomTerrain(new Vector3Int(x,y,z), new Vector3Int(chunkSize, chunkSize, chunkSize), 5, BlocksEnum.GRASS, blockTerrain);
								return;
							}
							Random randomGenerator = new Random();
							for (int idx = 1; idx <= 10; ++idx){
								int rInt = randomGenerator.nextInt(10);
								if(rInt == 2 || rInt == 5) {
									makeCubeAt(x+i,y+j,z+k, BlocksEnum.DIRT, blockTerrain);
								} else if (rInt == 3 || rInt == 6) {
									makeCubeAt(x+i,y+j,z+k, BlocksEnum.STONE, blockTerrain);
								} else if (rInt == 4 || rInt == 7) {
									makeCubeAt(x+i,y+j,z+k, BlocksEnum.DIRT, blockTerrain);
								}
								else
								{
									makeCubeAt(x+i,y+j,z+k, BlocksEnum.STONE, blockTerrain);
								}
							}
						} else {
							//TODO update with better detail
							if(preChunk.getPlanet().getType().equals(planetType.INFERNO)) {
								makeCubeAt(x+i,y+j,z+k, BlocksEnum.DARKSTONE, blockTerrain);
							}
							else if(preChunk.getPlanet().getType().equals(planetType.FRIGID)) {
								//Block_Ice && Block_BlackStone
								Random randomGenerator = new Random();
								for (int idx = 1; idx <= 2; ++idx){
									int rInt = randomGenerator.nextInt(2);
									if(rInt == 1){
										makeCubeAt(x+i,y+j,z+k, BlocksEnum.ICE, blockTerrain);
									} else {
										makeCubeAt(x+i,y+j,z+k, BlocksEnum.DARKSTONE, blockTerrain);
									}
								}
							}
						}
					}
				}
			}
			/*//Post-terrain generation here
			for(int i=0; i<chunkSize; i++)
			{    
				for(int j=0; j<chunkSize; j++)
				{
					int x1 = (int) (x+i);
					int z1 = (int) (z+j);
					makeTreeAt(new Vector3Int(x1, 0, z1), blockTerrain);
				}
			}*/
		}
		public void makeCubeAt(double dx, double dy, double dz, BlocksEnum BlockType, BlockTerrainControl chunk) {
			//make ground
			//To set a block, just specify the location and the block object
			//(Existing blocks will be replaced)
			int x = (int) dx;
			int y = (int) dy;
			int z = (int) dz;
			
			if(BlockType.getBClass() == null)
				return;
			chunk.setBlock(new Vector3Int(x, y, z), BlockType.getBClass());

		}
		public void generateRandomTerrain(Vector3Int location, Vector3Int size, float roughness, BlocksEnum BlockType, BlockTerrainControl chunk) {
			if(BlockType.getBClass() == null)
				return;
			chunk.setBlocksFromNoise(location, size, roughness, BlockType.getBClass());
		}
		public void makeTreeAt(Vector3Int loc, BlockTerrainControl blockTerrain) {
			int x = 0;
			int z = 0;
			boolean birchwood = false;
			Random randomGenerator = new Random();
			for (int idx = 1; idx <= 10; ++idx){
				int rInt = randomGenerator.nextInt(10);
				if(rInt == 2 || rInt == 5) {
					x = loc.getX() + 13;
					z = loc.getZ() + 16;
					birchwood = false;
				} else if (rInt == 3 || rInt == 6) {
					x = loc.getX() + 5;
					z = loc.getZ() + 7;
					birchwood = true;
				} else if (rInt == 4 || rInt == 1) {
					x = loc.getX() + 8;
					z = loc.getZ() + 2;
					birchwood = false;
				} else if (rInt == 8 || rInt == 9 || rInt == 7) {
					x = loc.getX() + 2;
					z = loc.getZ() + 8;
					birchwood = true;
				} else {
					return;
				}
			}
			int y = getTopBlock(x,z,blockTerrain);

			if(birchwood) {
				blockTerrain.setBlock(new Vector3Int(x, y, z), BlocksEnum.BIRCHWOOD.getBClass());
				blockTerrain.setBlock(new Vector3Int(x, y+1, z), BlocksEnum.BIRCHWOOD.getBClass());
				blockTerrain.setBlock(new Vector3Int(x, y+2, z), BlocksEnum.BIRCHWOOD.getBClass());
				blockTerrain.setBlock(new Vector3Int(x, y+3, z), BlocksEnum.BIRCHWOOD.getBClass());
			} else {
				blockTerrain.setBlock(new Vector3Int(x, y, z), BlocksEnum.WOOD.getBClass());
				blockTerrain.setBlock(new Vector3Int(x, y+1, z), BlocksEnum.WOOD.getBClass());
				blockTerrain.setBlock(new Vector3Int(x, y+2, z), BlocksEnum.WOOD.getBClass());
				blockTerrain.setBlock(new Vector3Int(x, y+3, z), BlocksEnum.WOOD.getBClass());
			}
			blockTerrain.setBlock(new Vector3Int(x+1, y+3, z), BlocksEnum.LEAVES.getBClass());
			blockTerrain.setBlock(new Vector3Int(x, y+3, z+1), BlocksEnum.LEAVES.getBClass());
			blockTerrain.setBlock(new Vector3Int(x+1, y+3, z+1), BlocksEnum.LEAVES.getBClass());
			blockTerrain.setBlock(new Vector3Int(x-1, y+3, z), BlocksEnum.LEAVES.getBClass());
			blockTerrain.setBlock(new Vector3Int(x, y+3, z-1), BlocksEnum.LEAVES.getBClass());
			blockTerrain.setBlock(new Vector3Int(x-1, y+3, z-1), BlocksEnum.LEAVES.getBClass());
			blockTerrain.setBlock(new Vector3Int(x+1, y+3, z-1), BlocksEnum.LEAVES.getBClass());
			blockTerrain.setBlock(new Vector3Int(x-1, y+3, z+1), BlocksEnum.LEAVES.getBClass());
			blockTerrain.setBlock(new Vector3Int(x, y+4, z), BlocksEnum.LEAVES.getBClass());

		}
		
		public int getTopBlock(int x, int z, BlockTerrainControl terrain)
		{
			int height = 0;
			for(int i = 0; i < Planet.CHUNK_SIZE; i++)
			{
				if(terrain.getBlock(new Vector3Int(x, i, z)) != null)
				{
					height++;
				}
			}
			return height;
		}
	}
	
	public static class LoadChunkThread extends Thread{
		BlockTerrainControl terrainControl;
		int x;
		int y;
		int z;
		public LoadChunkThread(BlockTerrainControl terrainControl, int locx, int locy, int locz) {
			this.terrainControl = terrainControl;
			x = locx;
			y = locy;
			z = locz;
		}
		
		public void run() 
		{
			this.terrainControl.getChunks()[x][y][z].loadChunk();
		}
	}
	
	public static class UnLoadChunkThread extends Thread{
		BlockTerrainControl terrainControl;
		int x;
		int y;
		int z;
		public UnLoadChunkThread(BlockTerrainControl terrainControl, int locx, int locy, int locz) {
			this.terrainControl = terrainControl;
			x = locx;
			y = locy;
			z = locz;
		}
		
		public void run() 
		{
			this.terrainControl.getChunks()[x][y][z].unloadChunk();
		}
	}

}
