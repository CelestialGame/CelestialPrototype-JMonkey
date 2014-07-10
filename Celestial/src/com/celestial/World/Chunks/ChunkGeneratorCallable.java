package com.celestial.World.Chunks;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import jme3tools.noise.BasicNoise;
import jme3tools.noise.Perlin;
import jme3tools.noise.Ridge;
import jme3tools.noise.SummedNoise;

import com.celestial.Blocks.GameBlock;
import com.celestial.SinglePlayer.Components.Planet.Planet;
import com.celestial.SinglePlayer.Components.Planet.PlanetFace;
import com.celestial.SinglePlayer.Components.Planet.PlanetType;
import com.cubes.BlockData;
import com.cubes.BlockManager;
import com.cubes.Vector3i;
import com.jme3.math.Transform;

public class ChunkGeneratorCallable implements Callable<Map<Vector3i, BlockData>>{

	private int planetDiameter;
	private int chunkSize;
	private Vector3i location;
	private HashMap<Vector3i, BlockData> blockData;
	private int x;
	private int y;
	private int z;
	private PlanetType planetType;
	private long planetSeed;

	public ChunkGeneratorCallable(Vector3i location, int planetDiameter, int chunkSize, PlanetType planetType, long planetSeed)
	{
		this.location = location;
		this.x = this.location.getX();
		this.y = this.location.getY();
		this.z = this.location.getZ();
		this.blockData = new HashMap<Vector3i, BlockData>();
		this.planetDiameter = planetDiameter;
		this.chunkSize = chunkSize;
		this.planetType = planetType;
		this.planetSeed = planetSeed;
	}

	@Override
	public Map<Vector3i, BlockData> call() throws Exception 
	{
		if(x >= planetDiameter-1 || 
				x == 0) {
			return null;
		} else if (y >= planetDiameter-1 || 
				y == 0) {
			return null;
		} else if (z >= planetDiameter-1 || 
				z == 0) {
			return null;
		}//CORNERS 
		else if((x == 1 || x == planetDiameter-2) 
				&& (y == 1 || y == planetDiameter-2) 
				&& (z == 1 || z == planetDiameter-2) ) {
			for(int i=0;i<chunkSize;i++)
				for(int j=0;j<chunkSize;j++)
					for(int k=0;k<chunkSize;k++)
					{
						makeCubeAt(i,j,k, GameBlock.SUBSTRATUS);
					}
			return blockData;
		}

		//doing edges
		//-------------------------------------------------- y-n-z
		else if((y == 1 && z == 1)) {
			for(int i=0;i<chunkSize;i++)
				for(int j=0;j<chunkSize/2;j++)
					for(int k=0;k<chunkSize/2;k++)
					{
						makeCubeAt(i,j+(chunkSize/2),k+(chunkSize/2), GameBlock.SUBSTRATUS);
					}
			return blockData;
		} else if (y == planetDiameter-2 && z == 1) {
			for(int i=0;i<chunkSize;i++)
				for(int j=0;j<chunkSize/2;j++)
					for(int k=0;k<chunkSize/2;k++)
					{
						makeCubeAt(i,j,k+(chunkSize/2), GameBlock.SUBSTRATUS);
					}
			return blockData;
		} else if(y == 1 && z == planetDiameter-2) {
			for(int i=0;i<chunkSize;i++)
				for(int j=0;j<chunkSize/2;j++)
					for(int k=0;k<chunkSize/2;k++)
					{
						makeCubeAt(i,j+(chunkSize/2),k, GameBlock.SUBSTRATUS);
					}
			return blockData;
		} else if (y == planetDiameter-2 && z == planetDiameter-2){
			for(int i=0;i<chunkSize;i++)
				for(int j=0;j<chunkSize/2;j++)
					for(int k=0;k<chunkSize/2;k++)
					{
						makeCubeAt(i,j,k, GameBlock.SUBSTRATUS);
					}
			return blockData;
		} //-------------------------------------------------- x-n-y
		else if((x == 1 && y == 1)) {
			for(int i=0;i<chunkSize/2;i++)
				for(int j=0;j<chunkSize/2;j++)
					for(int k=0;k<chunkSize;k++)
					{
						makeCubeAt(i+(chunkSize/2),j+(chunkSize/2),k, GameBlock.SUBSTRATUS);
					}
			return blockData;
		} else if((x == planetDiameter-2 && y == planetDiameter-2)) {
			for(int i=0;i<chunkSize/2;i++)
				for(int j=0;j<chunkSize/2;j++)
					for(int k=0;k<chunkSize;k++)
					{
						makeCubeAt(i,j,k, GameBlock.SUBSTRATUS);
					}
			return blockData;
		}
		else if (x == 1 && y == planetDiameter-2) {
			for(int i=0;i<chunkSize/2;i++)
				for(int j=0;j<chunkSize/2;j++)
					for(int k=0;k<chunkSize;k++)
					{
						makeCubeAt(i+(chunkSize/2),j,k, GameBlock.SUBSTRATUS);
					}
			return blockData;
		}
		else if (x == planetDiameter-2 && y == 1) {
			for(int i=0;i<chunkSize/2;i++)
				for(int j=0;j<chunkSize/2;j++)
					for(int k=0;k<chunkSize;k++)
					{
						makeCubeAt(i,j+(chunkSize/2),k, GameBlock.SUBSTRATUS);
					}
			return blockData;
		}

		//---------------------------------------------------- x-n-z
		else if ((x == 1 && z == 1)) {
			for(int i=0;i<chunkSize/2;i++)
				for(int j=0;j<chunkSize;j++)
					for(int k=0;k<chunkSize/2;k++)
					{
						makeCubeAt(i+(chunkSize/2),j,k+(chunkSize/2), GameBlock.SUBSTRATUS);
					}
			return blockData;
		} else if((x == planetDiameter-2 && z == planetDiameter-2)) {
			for(int i=0;i<chunkSize/2;i++)
				for(int j=0;j<chunkSize;j++)
					for(int k=0;k<chunkSize/2;k++)
					{
						makeCubeAt(i,j,k, GameBlock.SUBSTRATUS);
					}
			return blockData;
		} else if (x == 1 && z == planetDiameter-2) {
			for(int i=0;i<chunkSize/2;i++)
				for(int j=0;j<chunkSize;j++)
					for(int k=0;k<chunkSize/2;k++)
					{
						makeCubeAt(i+(chunkSize/2),j,k, GameBlock.SUBSTRATUS);
					}
			return blockData;
		} else if ((x == planetDiameter-2 && z == 1)) {
			for(int i=0;i<chunkSize/2;i++)
				for(int j=0;j<chunkSize;j++)
					for(int k=0;k<chunkSize/2;k++)
					{
						makeCubeAt(i,j,k+(chunkSize/2), GameBlock.SUBSTRATUS);
					}
			return blockData;
		}
		// ------------------------------- END EDGE GENERATION ---------------------------------------------

		if(this.planetType.hasAtmosphere()) {
			//Random Terrain Generation chunks
			if(x == planetDiameter-2) {
				generateRandomTerrain(new Vector3i(chunkSize/2, chunkSize, chunkSize), GameBlock.GRASS, PlanetFace.EAST);
				return blockData;
			} else if(x == 1) {
				generateRandomTerrain(new Vector3i(chunkSize/2, chunkSize, chunkSize), GameBlock.GRASS, PlanetFace.WEST);
				return blockData;
			}
			else if (y == planetDiameter-2) {
				generateRandomTerrain(new Vector3i(chunkSize, chunkSize/2, chunkSize), GameBlock.GRASS, PlanetFace.TOP);
				return blockData;
			} else if(y == 1) {
				generateRandomTerrain(new Vector3i(chunkSize, chunkSize/2, chunkSize), GameBlock.GRASS, PlanetFace.BOTTOM);
				return blockData;
			}
			else if (z == planetDiameter-2) {
				generateRandomTerrain(new Vector3i(chunkSize, chunkSize, chunkSize/2), GameBlock.GRASS, PlanetFace.NORTH);
				return blockData;
			} else if (z == 1) {
				generateRandomTerrain(new Vector3i(chunkSize, chunkSize, chunkSize/2), GameBlock.GRASS, PlanetFace.SOUTH);
				return blockData;
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
							makeCubeAt(i,j,k, BlocksEnum.GRASS, blockTerrain);
					}
					else if(k==chunkSize-1 || k==0)
					{
						if(preChunk.getPlanet().getType().equals(planetType.HABITABLE))
							makeCubeAt(i,j,k, BlocksEnum.GRASS, blockTerrain);
					}
					else if(i==chunkSize-1 || i==0)
					{
						if(preChunk.getPlanet().getType().equals(planetType.HABITABLE))
							makeCubeAt(i,j,k, BlocksEnum.GRASS, blockTerrain);
					}
					else
					{*/
					if(x == (planetDiameter-1)/2 &&
							y == (planetDiameter-1)/2 &&
							z == (planetDiameter-1)/2) {
						//core
						if(i == chunkSize-1 || i == 0 ||
								j == chunkSize-1 || j == 0 ||
								k == chunkSize-1 || k == 0)
							makeCubeAt(i,j,k, GameBlock.SUBSTRATUS);
						else if(i == (chunkSize/2)-1 && j == (chunkSize/2)-1 && k == (chunkSize/2)-1)
							makeCubeAt(i,j,k, GameBlock.SUBSTRATUS);
						else
							continue;

					} else if(planetType.hasAtmosphere()) {

						Random randomGenerator = new Random();
						for (int idx = 1; idx <= 10; ++idx){
							int rInt = randomGenerator.nextInt(10);
							if(rInt == 2 || rInt == 5 || rInt == 4)
								makeCubeAt(i,j,k, GameBlock.DIRT);
							else
								makeCubeAt(i,j,k, GameBlock.STONE);
						}
					} else {
						//TODO update with better detail
						if(planetType.equals(PlanetType.INFERNO)) {
							makeCubeAt(i,j,k, GameBlock.DARKSTONE);
						}
						else if(planetType.equals(PlanetType.FRIGID)) {
							//Block_Ice && Block_BlackStone
							Random randomGenerator = new Random();
							for (int idx = 1; idx <= 2; ++idx){
								int rInt = randomGenerator.nextInt(2);
								if(rInt == 1){
									makeCubeAt(i,j,k, GameBlock.ICE);
								} else {
									makeCubeAt(i,j,k, GameBlock.DARKSTONE);
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
				int x1 = (int) (i);
				int z1 = (int) (z+j);
				makeTreeAt(new Vector3Int(x1, 0, z1), blockTerrain);
			}
		}*/

		return blockData;
	}

	private void makeCubeAt(Vector3i location, GameBlock blockType)
	{
		makeCubeAt(location.getX(), location.getY(), location.getZ(), blockType);
	}

	private void makeCubeAt(int x, int y, int z, GameBlock blockType) 
	{
		Vector3i cubeLocation = new Vector3i(x,y,z);

		if(blockType.getBClass() == null)
			return;
		this.blockData.put(cubeLocation, new BlockData(BlockManager.getInstance().getType(blockType.getBClass()).getType(), cubeLocation, null));
	}

	private void generateRandomTerrain(Vector3i size, GameBlock blockType, PlanetFace face) 
	{
		if(blockType.getBClass() == null)
			return;
		setBlocksFromNoise(size, blockType, face);
	}

	private void setBlocksFromNoise(Vector3i size, GameBlock blockClass, PlanetFace face)
	{
		//SimplexNoise simplexNoise=new SimplexNoise(150,0.6,(int) preChunk.getPlanet().getSeed());
		double xStart=this.x;
		double XEnd=this.x+Planet.CHUNK_SIZE;
		double yStart=this.z;
		double yEnd=this.z+Planet.CHUNK_SIZE;
		int resolution=Planet.CHUNK_SIZE;
		double[][] grid=new double[resolution][resolution];

		Perlin base = new Perlin(planetSeed);
		Ridge basis = new Ridge(base);
		Transform rmft = new Transform(); rmft.setScale(1.f/64.f);
		BasicNoise ridgedMF = new BasicNoise(rmft, 0.1f, 0.086f, (int) planetSeed, 3, 3.02f, 2f, true, basis);
		Transform simt = new Transform(); simt.setScale(1.f/64.f);
		BasicNoise perlin = new BasicNoise(simt, 0.1f, 0.032f, (int) planetSeed, 3, 3.02f, 4f, false, base);
		SummedNoise fractalSum = new SummedNoise(ridgedMF, perlin);
		for(int i=0;i<resolution;i++){
			for(int j=0;j<resolution;j++){
				int x=(int)(xStart+i*((XEnd-xStart)/resolution));
				int y=(int)(yStart+j*((yEnd-yStart)/resolution));
				grid[i][j]=0.5*(1+fractalSum.noise(x,y));
			}
		}
		
		switch(face)
		{
		case TOP:
			for(int x=0;x<grid[0].length;x++){
				for(int z=0;z<grid.length;z++){
					int blockHeight = (int) ((grid[x][z]) * size.getY() + 1);
					Vector3i blockLocation = new Vector3i();
					for(int y=0;y<blockHeight;y++){
						blockLocation.set(x, y, z);
						makeCubeAt(blockLocation, blockClass);
					}
				}
			}
			break;
		case BOTTOM:
			for(int x=0;x<grid[0].length;x++){
				for(int z=0;z<grid.length;z++){
					int blockHeight = (int) ((grid[x][z]) * size.getY() + 1);
					Vector3i blockLocation = new Vector3i();
					for(int y=0;y<blockHeight;y++){
						blockLocation.set(x, (y * -1) + Planet.CHUNK_SIZE, z);
						makeCubeAt(blockLocation, blockClass);
					}
				}
			}
			break;
		case NORTH:
			for(int x=0;x<grid[0].length;x++){
				for(int y=0;y<grid.length;y++){
					int blockHeight = (int) ((grid[x][y]) * size.getZ() + 1);
					Vector3i blockLocation = new Vector3i();
					for(int z=0;z<blockHeight;z++){
						blockLocation.set(x, y, z);
						makeCubeAt(blockLocation, blockClass);
					}
				}
			}
			break;
		case SOUTH:
			for(int x=0;x<grid[0].length;x++){
				for(int y=0;y<grid.length;y++){
					int blockHeight = (int) ((grid[x][y]) * size.getZ() + 1);
					Vector3i blockLocation = new Vector3i();
					for(int z=0;z<blockHeight;z++){
						blockLocation.set(x, y, (z * -1) + Planet.CHUNK_SIZE);
						makeCubeAt(blockLocation, blockClass);
					}
				}
			}
			break;
		case EAST:
			for(int y=0;y<grid[0].length;y++){
				for(int z=0;z<grid.length;z++){
					int blockHeight = (int) ((grid[y][z]) * size.getX() + 1);
					Vector3i blockLocation = new Vector3i();
					for(int x=0;x<blockHeight;x++){
						blockLocation.set(x, y, z);
						makeCubeAt(blockLocation, blockClass);
					}
				}
			}
			break;
		case WEST:
			for(int y=0;y<grid[0].length;y++){
				for(int z=0;z<grid.length;z++){
					int blockHeight = (int) ((grid[y][z]) * size.getX() + 1);
					Vector3i blockLocation = new Vector3i();
					for(int x=0;x<blockHeight;x++){
						blockLocation.set((x * -1) + Planet.CHUNK_SIZE, y, z);
						makeCubeAt(blockLocation, blockClass);
					}
				}
			}
			break;
		default:
			break;
		}
	}
}
