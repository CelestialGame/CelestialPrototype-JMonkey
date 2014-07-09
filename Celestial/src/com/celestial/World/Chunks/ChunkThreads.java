package com.celestial.World.Chunks;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import jme3tools.noise.*;
import jme3tools.optimize.LodGenerator;

import com.celestial.Blocks.GameBlock;
import com.celestial.SinglePlayer.Components.Planet.Planet;
import com.celestial.SinglePlayer.Components.Planet.Planet.planetType;
import com.celestial.World.BlockChunkManager.PreGeneratedChunk;
import com.celestial.World.SimplexNoise;
import com.cubes.Block;
import com.cubes.BlockChunkControl;
import com.cubes.BlockTerrainControl;
import com.cubes.RandomTerrainGenerator;
import com.cubes.Vector3i;
import com.jme3.math.Transform;
import com.jme3.scene.Geometry;
import com.jme3.terrain.noise.Basis;
import com.jme3.terrain.noise.basis.ImprovedNoise;
import com.jme3.terrain.noise.basis.Noise;

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
						PreGeneratedChunk chunk = new PreGeneratedChunk(terrControl.getChunk(new Vector3i(j,i,k)), j, i, k, planet);
						this.preGenChunkList.add(chunk);
					}                
				}
			}
			return this.preGenChunkList;
		}
		
	}
	
	public static class GenerateChunkThread extends Thread{
		
		public static enum PlanetSide {
			TOP, BOTTOM, LEFT, RIGHT, FRONT, BACK;
			
			public static final EnumSet<PlanetSide> PLANETSIDES = EnumSet.range(TOP, BACK);
			
			public final boolean isPlanetSide() {
	 			return PLANETSIDES.contains(this);
	 		}
		}
		
		BlockChunkControl chunk;
		int x;
		int y;
		int z;
		PreGeneratedChunk preChunk;
		List<Vector3i> randomTerrainGenerationX = new ArrayList<Vector3i>();
		List<Vector3i> randomTerrainGeneration2X = new ArrayList<Vector3i>();
		List<Vector3i> randomTerrainGenerationY = new ArrayList<Vector3i>();
		List<Vector3i> randomTerrainGeneration2Y = new ArrayList<Vector3i>();
		List<Vector3i> randomTerrainGenerationZ = new ArrayList<Vector3i>();
		List<Vector3i> randomTerrainGeneration2Z = new ArrayList<Vector3i>();
		public GenerateChunkThread(int locx, int locy, int locz, PreGeneratedChunk preChunk, long seed) {
			this.chunk = preChunk.getChunk();
			x = locx;
			y = locy;
			z = locz;
			this.preChunk = preChunk;
			this.seed = seed;
			randomSeed = new Random(seed);
		}
		long seed;
		Random randomSeed;
		
		public void run() {
			int chunkSize = Planet.CHUNK_SIZE;
			//TODO use ratios
			
			//Empty ceiling chunks
			if(preChunk.getLocation().getX() >= preChunk.getPlanet().getDiameter()-1 || 
					preChunk.getLocation().getX() == 0) {
				return;
			} else if (preChunk.getLocation().getY() >= preChunk.getPlanet().getDiameter()-1 || 
					preChunk.getLocation().getY() == 0) {
				return;
			} else if (preChunk.getLocation().getZ() >= preChunk.getPlanet().getDiameter()-1 || 
					preChunk.getLocation().getZ() == 0) {
				return;
			}//CORNERS 
			else if((preChunk.getLocation().getX() == 1 || preChunk.getLocation().getX() == preChunk.getPlanet().getDiameter()-2) 
					&& (preChunk.getLocation().getY() == 1 ||preChunk.getLocation().getY() == preChunk.getPlanet().getDiameter()-2) 
					&& (preChunk.getLocation().getZ() == 1 ||preChunk.getLocation().getZ() == preChunk.getPlanet().getDiameter()-2) ) {
				for(int i=0;i<chunkSize;i++)
					for(int j=0;j<chunkSize;j++)
						for(int k=0;k<chunkSize;k++)
						{
							makeCubeAt(x+i,y+j,z+k, GameBlock.SUBSTRATUS, chunk);
						}
				return;
			}
			
			//doing edges
			//-------------------------------------------------- y-n-z
			else if((preChunk.getLocation().getY() == 1 && preChunk.getLocation().getZ() == 1)) {
				for(int i=0;i<chunkSize;i++)
					for(int j=0;j<chunkSize/2;j++)
						for(int k=0;k<chunkSize/2;k++)
						{
							makeCubeAt(x+i,y+j+(chunkSize/2),z+k+(chunkSize/2), GameBlock.SUBSTRATUS, chunk);
						}
				return;
			} else if (preChunk.getLocation().getY() == preChunk.getPlanet().getDiameter()-2 && preChunk.getLocation().getZ() == 1) {
				for(int i=0;i<chunkSize;i++)
					for(int j=0;j<chunkSize/2;j++)
						for(int k=0;k<chunkSize/2;k++)
						{
							makeCubeAt(x+i,y+j,z+k+(chunkSize/2), GameBlock.SUBSTRATUS, chunk);
						}
				return;
			} else if(preChunk.getLocation().getY() == 1 && preChunk.getLocation().getZ() == preChunk.getPlanet().getDiameter()-2) {
				for(int i=0;i<chunkSize;i++)
					for(int j=0;j<chunkSize/2;j++)
						for(int k=0;k<chunkSize/2;k++)
						{
							makeCubeAt(x+i,y+j+(chunkSize/2),z+k, GameBlock.SUBSTRATUS, chunk);
						}
				return;
			} else if (preChunk.getLocation().getY() == preChunk.getPlanet().getDiameter()-2 && preChunk.getLocation().getZ() == preChunk.getPlanet().getDiameter()-2){
				for(int i=0;i<chunkSize;i++)
					for(int j=0;j<chunkSize/2;j++)
						for(int k=0;k<chunkSize/2;k++)
						{
							makeCubeAt(x+i,y+j,z+k, GameBlock.SUBSTRATUS, chunk);
						}
				return;
			} //-------------------------------------------------- x-n-y
			else if((preChunk.getLocation().getX() == 1 && preChunk.getLocation().getY() == 1)) {
				for(int i=0;i<chunkSize/2;i++)
					for(int j=0;j<chunkSize/2;j++)
						for(int k=0;k<chunkSize;k++)
						{
							makeCubeAt(x+i+(chunkSize/2),y+j+(chunkSize/2),z+k, GameBlock.SUBSTRATUS, chunk);
						}
				return;
			} else if((preChunk.getLocation().getX() == preChunk.getPlanet().getDiameter()-2 && preChunk.getLocation().getY() == preChunk.getPlanet().getDiameter()-2)) {
				for(int i=0;i<chunkSize/2;i++)
					for(int j=0;j<chunkSize/2;j++)
						for(int k=0;k<chunkSize;k++)
						{
							makeCubeAt(x+i,y+j,z+k, GameBlock.SUBSTRATUS, chunk);
						}
				return;
			}
			else if (preChunk.getLocation().getX() == 1 && preChunk.getLocation().getY() == preChunk.getPlanet().getDiameter()-2) {
				for(int i=0;i<chunkSize/2;i++)
					for(int j=0;j<chunkSize/2;j++)
						for(int k=0;k<chunkSize;k++)
						{
							makeCubeAt(x+i+(chunkSize/2),y+j,z+k, GameBlock.SUBSTRATUS, chunk);
						}
				return;
			}
			else if (preChunk.getLocation().getX() == preChunk.getPlanet().getDiameter()-2 && preChunk.getLocation().getY() == 1) {
				for(int i=0;i<chunkSize/2;i++)
					for(int j=0;j<chunkSize/2;j++)
						for(int k=0;k<chunkSize;k++)
						{
							makeCubeAt(x+i,y+j+(chunkSize/2),z+k, GameBlock.SUBSTRATUS, chunk);
						}
				return;
			}
			
			//---------------------------------------------------- x-n-z
			else if ((preChunk.getLocation().getX() == 1 && preChunk.getLocation().getZ() == 1)) {
				for(int i=0;i<chunkSize/2;i++)
					for(int j=0;j<chunkSize;j++)
						for(int k=0;k<chunkSize/2;k++)
						{
							makeCubeAt(x+i+(chunkSize/2),y+j,z+k+(chunkSize/2), GameBlock.SUBSTRATUS, chunk);
						}
				return;
			} else if((preChunk.getLocation().getX() == preChunk.getPlanet().getDiameter()-2 && preChunk.getLocation().getZ() == preChunk.getPlanet().getDiameter()-2)) {
				for(int i=0;i<chunkSize/2;i++)
					for(int j=0;j<chunkSize;j++)
						for(int k=0;k<chunkSize/2;k++)
						{
							makeCubeAt(x+i,y+j,z+k, GameBlock.SUBSTRATUS, chunk);
						}
				return;
			} else if (preChunk.getLocation().getX() == 1 && preChunk.getLocation().getZ() == preChunk.getPlanet().getDiameter()-2) {
				for(int i=0;i<chunkSize/2;i++)
					for(int j=0;j<chunkSize;j++)
						for(int k=0;k<chunkSize/2;k++)
						{
							makeCubeAt(x+i+(chunkSize/2),y+j,z+k, GameBlock.SUBSTRATUS, chunk);
						}
				return;
			} else if ((preChunk.getLocation().getX() == preChunk.getPlanet().getDiameter()-2 && preChunk.getLocation().getZ() == 1)) {
				for(int i=0;i<chunkSize/2;i++)
					for(int j=0;j<chunkSize;j++)
						for(int k=0;k<chunkSize/2;k++)
						{
							makeCubeAt(x+i,y+j,z+k+(chunkSize/2), GameBlock.SUBSTRATUS, chunk);
						}
				return;
			}
			// ------------------------------- END EDGE GENERATION ---------------------------------------------
			
			if(preChunk.getPlanet().getType().hasAtmosphere()) {
				//Random Terrain Generation chunks
				if(preChunk.getLocation().getX() == preChunk.getPlanet().getDiameter()-2) {
					generateRandomTerrain(new Vector3i(x,y,z), 
							new Vector3i(chunkSize/2, chunkSize, chunkSize), GameBlock.GRASS, PlanetSide.RIGHT, chunk);
					return;
				} else if(preChunk.getLocation().getX() == 1) {
					generateRandomTerrain(new Vector3i(x,y,z), 
							new Vector3i(chunkSize/2, chunkSize, chunkSize), GameBlock.GRASS, PlanetSide.LEFT, chunk);
					return;
				}
				else if (preChunk.getLocation().getY() == preChunk.getPlanet().getDiameter()-2) {
					generateRandomTerrain(new Vector3i(x,y,z), 
							new Vector3i(chunkSize, chunkSize/2, chunkSize), GameBlock.GRASS, PlanetSide.TOP, chunk);
					return;
				} else if(preChunk.getLocation().getY() == 1) {
					generateRandomTerrain(new Vector3i(x,y,z), 
							new Vector3i(chunkSize, chunkSize/2, chunkSize), GameBlock.GRASS, PlanetSide.BOTTOM, chunk);
					return;
				}
				else if (preChunk.getLocation().getZ() == preChunk.getPlanet().getDiameter()-2) {
					generateRandomTerrain(new Vector3i(x,y,z), 
							new Vector3i(chunkSize, chunkSize, chunkSize/2), GameBlock.GRASS, PlanetSide.FRONT, chunk);
					return;
				} else if (preChunk.getLocation().getZ() == 1) {
					generateRandomTerrain(new Vector3i(x,y,z), 
							new Vector3i(chunkSize, chunkSize, chunkSize/2), GameBlock.GRASS, PlanetSide.BACK, chunk);
					return;
				}
			}

			//random blox
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
						if(preChunk.getLocation().getX() == (preChunk.getPlanet().getDiameter()-1)/2 &&
								preChunk.getLocation().getY() == (preChunk.getPlanet().getDiameter()-1)/2 &&
								preChunk.getLocation().getZ() == (preChunk.getPlanet().getDiameter()-1)/2) {
							//core
							if(i == chunkSize-1 || i == 0 ||
									j == chunkSize-1 || j == 0 ||
									k == chunkSize-1 || k == 0)
								makeCubeAt(x+i,y+j,z+k, GameBlock.SUBSTRATUS, chunk);
							else if(i == (chunkSize/2)-1 && j == (chunkSize/2)-1 && k == (chunkSize/2)-1)
								makeCubeAt(x+i,y+j,z+k, GameBlock.SUBSTRATUS, chunk);
							else
								continue;
							
						} else if(preChunk.getPlanet().getType().hasAtmosphere()) {
							
							Random randomGenerator = new Random();
							for (int idx = 1; idx <= 10; ++idx){
								int rInt = randomGenerator.nextInt(10);
								if(rInt == 2 || rInt == 5 || rInt == 4)
									makeCubeAt(x+i,y+j,z+k, GameBlock.DIRT, chunk);
								else
									makeCubeAt(x+i,y+j,z+k, GameBlock.STONE, chunk);
							}
						} else {
							//TODO update with better detail
							if(preChunk.getPlanet().getType().equals(planetType.INFERNO)) {
								makeCubeAt(x+i,y+j,z+k, GameBlock.DARKSTONE, chunk);
							}
							else if(preChunk.getPlanet().getType().equals(planetType.FRIGID)) {
								//Block_Ice && Block_BlackStone
								Random randomGenerator = new Random();
								for (int idx = 1; idx <= 2; ++idx){
									int rInt = randomGenerator.nextInt(2);
									if(rInt == 1){
										makeCubeAt(x+i,y+j,z+k, GameBlock.ICE, chunk);
									} else {
										makeCubeAt(x+i,y+j,z+k, GameBlock.DARKSTONE, chunk);
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
		public void makeCubeAt(double dx, double dy, double dz, GameBlock BlockType, BlockChunkControl chunk) {
			//make ground
			//To set a block, just specify the location and the block object
			//(Existing blocks will be replaced)
			int x = (int) dx;
			int y = (int) dy;
			int z = (int) dz;
			
			if(BlockType.getBClass() == null)
				return;
			chunk.getTerrain().setBlock(new Vector3i(x, y, z), BlockType.getBClass());

		}
		public void generateRandomTerrain(Vector3i location, Vector3i size, GameBlock BlockType, PlanetSide side, BlockChunkControl chunk) {
			if(BlockType.getBClass() == null)
				return;
			setBlocksFromNoise(location, size, BlockType.getBClass(), side, chunk);
		}
		public void makeTreeAt(Vector3i loc, BlockChunkControl chunk) {
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
			int y = getTopBlock(x,z,chunk);

			if(birchwood) {
				chunk.getTerrain().setBlock(new Vector3i(x, y, z), GameBlock.BIRCHWOOD.getBClass());
				chunk.getTerrain().setBlock(new Vector3i(x, y+1, z), GameBlock.BIRCHWOOD.getBClass());
				chunk.getTerrain().setBlock(new Vector3i(x, y+2, z), GameBlock.BIRCHWOOD.getBClass());
				chunk.getTerrain().setBlock(new Vector3i(x, y+3, z), GameBlock.BIRCHWOOD.getBClass());
			} else {
				chunk.getTerrain().setBlock(new Vector3i(x, y, z), GameBlock.WOOD.getBClass());
				chunk.getTerrain().setBlock(new Vector3i(x, y+1, z), GameBlock.WOOD.getBClass());
				chunk.getTerrain().setBlock(new Vector3i(x, y+2, z), GameBlock.WOOD.getBClass());
				chunk.getTerrain().setBlock(new Vector3i(x, y+3, z), GameBlock.WOOD.getBClass());
			}
			chunk.getTerrain().setBlock(new Vector3i(x+1, y+3, z), GameBlock.LEAVES.getBClass());
			chunk.getTerrain().setBlock(new Vector3i(x, y+3, z+1), GameBlock.LEAVES.getBClass());
			chunk.getTerrain().setBlock(new Vector3i(x+1, y+3, z+1), GameBlock.LEAVES.getBClass());
			chunk.getTerrain().setBlock(new Vector3i(x-1, y+3, z), GameBlock.LEAVES.getBClass());
			chunk.getTerrain().setBlock(new Vector3i(x, y+3, z-1), GameBlock.LEAVES.getBClass());
			chunk.getTerrain().setBlock(new Vector3i(x-1, y+3, z-1), GameBlock.LEAVES.getBClass());
			chunk.getTerrain().setBlock(new Vector3i(x+1, y+3, z-1), GameBlock.LEAVES.getBClass());
			chunk.getTerrain().setBlock(new Vector3i(x-1, y+3, z+1), GameBlock.LEAVES.getBClass());
			chunk.getTerrain().setBlock(new Vector3i(x, y+4, z), GameBlock.LEAVES.getBClass());

		}
		
		public int getTopBlock(int x, int z, BlockChunkControl terrain)
		{
			int height = 0;
			for(int i = 0; i < Planet.CHUNK_SIZE; i++)
			{
				if(terrain.getTerrain().getBlock(new Vector3i(x, i, z)) != null)
				{
					height++;
				}
			}
			return height;
		}
		
		public void setBlocksFromNoise(Vector3i location, Vector3i size, Class<? extends Block> blockClass, PlanetSide side, BlockChunkControl chunk){
			//SimplexNoise simplexNoise=new SimplexNoise(150,0.6,(int) preChunk.getPlanet().getSeed());
		    double xStart=this.x;
		    double XEnd=this.x+Planet.CHUNK_SIZE;
		    double yStart=this.z;
		    double yEnd=this.z+Planet.CHUNK_SIZE;
		    int resolution=Planet.CHUNK_SIZE;
		    double[][] grid=new double[resolution][resolution];

		    Perlin base = new Perlin(preChunk.getPlanet().getSeed());
			Ridge basis = new Ridge(base);
			Transform rmft = new Transform(); rmft.setScale(1.f/64.f);
			BasicNoise ridgedMF = new BasicNoise(rmft, 0.1f, 0.086f, (int) preChunk.getPlanet().getSeed(), 3, 3.02f, 2f, true, basis);
			Transform simt = new Transform(); simt.setScale(1.f/64.f);
			BasicNoise perlin = new BasicNoise(simt, 0.1f, 0.032f, (int) preChunk.getPlanet().getSeed(), 3, 3.02f, 4f, false, base);
			SummedNoise fractalSum = new SummedNoise(ridgedMF, perlin);
		    for(int i=0;i<resolution;i++){
		        for(int j=0;j<resolution;j++){
		            int x=(int)(xStart+i*((XEnd-xStart)/resolution));
		            int y=(int)(yStart+j*((yEnd-yStart)/resolution));
		            grid[i][j]=0.5*(1+fractalSum.noise(x,y));
		        }
		    }
		    
			if(side == PlanetSide.TOP) {
		        for(int x=0;x<grid[0].length;x++) {
		            for(int z=0;z<grid.length;z++){
		                int blockHeight = (int) ((grid[x][z]) * size.getY() + 1);
		                Vector3i tmpLocation = new Vector3i();
		                for(int y=0;y<blockHeight;y++){
		                    tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
		                    if(preChunk.isLocationInChunk(tmpLocation))
		                    	chunk.getTerrain().setBlock(tmpLocation, blockClass);
		                }
		            }
		        }
	        } else if(side == PlanetSide.BOTTOM) {
	        	for(int x=0;x<grid[0].length;x++){
		            for(int z=0;z<grid.length;z++){
		                int blockHeight = (int) ((grid[x][z]) * size.getY() + 1);
		                Vector3i tmpLocation = new Vector3i();
		                for(int y=0;y<blockHeight;y++){
		                    tmpLocation.set(location.getX() + x, location.getY() - y + Planet.CHUNK_SIZE, location.getZ() + z);
		                    if(preChunk.isLocationInChunk(tmpLocation))
		                    	chunk.getTerrain().setBlock(tmpLocation, blockClass);
		                }
		            }
		        }
	        } else if(side == PlanetSide.LEFT) {
	        	for(int y=0;y<grid[0].length;y++){
		            for(int z=0;z<grid.length;z++){
		                int blockHeight = (int) ((grid[y][z]) * size.getX() + 1);
		                Vector3i tmpLocation = new Vector3i();
		                for(int x=0;x<blockHeight;x++){
		                    tmpLocation.set(location.getX() - x + Planet.CHUNK_SIZE, location.getY() + y, location.getZ() + z);
		                    if(preChunk.isLocationInChunk(tmpLocation))
		                    	chunk.getTerrain().setBlock(tmpLocation, blockClass);
		                }
		            }
		        }
	        }
	        else if(side == PlanetSide.RIGHT) {
	        	for(int y=0;y<grid[0].length;y++){
		            for(int z=0;z<grid.length;z++){
		                int blockHeight = (int) ((grid[y][z]) * size.getX() + 1);
		                Vector3i tmpLocation = new Vector3i();
		                for(int x=0;x<blockHeight;x++){
		                    tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
		                    if(preChunk.isLocationInChunk(tmpLocation))
		                    	chunk.getTerrain().setBlock(tmpLocation, blockClass);
		                }
		            }
		        }
	        }
	        else if(side == PlanetSide.FRONT) {
	        	for(int x=0;x<grid[0].length;x++){
		            for(int y=0;y<grid.length;y++){
		                int blockHeight = (int) ((grid[x][y]) * size.getZ() + 1);
		                Vector3i tmpLocation = new Vector3i();
		                for(int z=0;z<blockHeight;z++){
		                    tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() + z);
		                    if(preChunk.isLocationInChunk(tmpLocation))
		                    	chunk.getTerrain().setBlock(tmpLocation, blockClass);
		                }
		            }
		        }
	        }
	        else if(side == PlanetSide.BACK) {
	        	for(int x=0;x<grid[0].length;x++){
		            for(int y=0;y<grid.length;y++){
		                int blockHeight = (int) ((grid[x][y]) * size.getZ() + 1);
		                Vector3i tmpLocation = new Vector3i();
		                for(int z=0;z<blockHeight;z++){
		                    tmpLocation.set(location.getX() + x, location.getY() + y, location.getZ() - z + Planet.CHUNK_SIZE);
		                    if(preChunk.isLocationInChunk(tmpLocation))
		                    	chunk.getTerrain().setBlock(tmpLocation, blockClass);
		                }
		            }
		        }
	        }
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
			this.terrainControl.getChunks().get(new Vector3i(x,y,z)).loadChunk();
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
			this.terrainControl.getChunks().get(new Vector3i(x,y,z)).unloadChunk();
		}
	}

}
