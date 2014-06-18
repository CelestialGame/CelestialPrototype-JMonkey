package com.celestial.World.Chunks;

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
						this.preGenChunkList.add(new PreGeneratedChunk(terrControl, (j*Planet.CHUNK_SIZE), (i*Planet.CHUNK_SIZE), (k*Planet.CHUNK_SIZE), planet));
					}                
				}
			}
			return this.preGenChunkList;
		}
		
	}
	public static class GenerateChunkThread extends Thread {
		BlockTerrainControl blockTerrain;
		int x;
		int y;
		int z;
		PreGeneratedChunk preChunk;
		public GenerateChunkThread(BlockTerrainControl bTC, int locx, int locy, int locz, PreGeneratedChunk preChunk) {
			blockTerrain = bTC;
			x = locx;
			y = locy;
			z = locz;
			this.preChunk = preChunk;
		}
		
		public void run() {
			int diameter = Planet.CHUNK_SIZE;

			for(int i=0;i<diameter;i++)
			{
				for(int j=0;j<diameter;j++)
				{
					for(int k=0;k<diameter;k++)
					{
						if(j==diameter-1 || j == 0)
						{
							if(preChunk.getPlanet().getType().equals(planetType.HABITABLE))
								makeCubeAt(x+i,y+j,z+k, BlocksEnum.GRASS, blockTerrain);
						}
						else if(k==diameter-1 || k==0)
						{
							if(preChunk.getPlanet().getType().equals(planetType.HABITABLE))
								makeCubeAt(x+i,y+j,z+k, BlocksEnum.GRASS, blockTerrain);
						}
						else if(i==diameter-1 || i==0)
						{
							if(preChunk.getPlanet().getType().equals(planetType.HABITABLE))
								makeCubeAt(x+i,y+j,z+k, BlocksEnum.GRASS, blockTerrain);
						}
						else
						{
							if(preChunk.getPlanet().getType().hasAtmosphere()) {
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
			}
			try {
				this.join();
			} catch (InterruptedException e) {;}
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
	}

}
