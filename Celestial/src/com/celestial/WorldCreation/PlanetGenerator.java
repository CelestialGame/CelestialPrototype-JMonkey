/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.WorldCreation;

import com.celestial.Blocks.*;
import com.celestial.Celestial;
import com.celestial.World.*;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.cubes.test.CubesTestAssets;
import com.jme3.scene.Node;
import java.util.Random;

/**
 *
 * @author kevint
 */
public class PlanetGenerator {
    
    Celestial parent;
    
    public static int GRASS = 1;
    public static int DIRT = 2;
    public static int STONE = 3;
    public static int WOOD = 4;
    
    public PlanetGenerator(Celestial parent) {
        this.parent = parent;
    }
    
    public BlockTerrainControl makePlanetSide(double locx, double locy, double locz, Node sideNode) {
        BlockTerrainControl Side = new BlockTerrainControl(CubesTestAssets.getSettings(parent), new Vector3Int(16,1,16));
        Side.addChunkListener(new ChunkListener(parent));
        for(int i=0; i<16; i++)
        {    
            for(int j=0; j<16; j++)
            {
                makeChunk(locx+(i*16), locy, locz+(j*16), Side);
                int x1 = (int) locx+(i*16);
                int y1 = (int) locy;
                int z1 = (int) locz+(j*16);
                makeTreeAt(new Vector3Int(x1, y1, z1), Side);
            }
        }
        sideNode.addControl(Side);
        return Side;
    }
    
    public void makeChunk(double locx, double locy, double locz, BlockTerrainControl blockTerrain)
    {
        int diameter = 16;

        for(int i=0;i<diameter;i++)
        {
            for(int j=0;j<diameter;j++)
            {
                for(int k=0;k<diameter;k++)
                {
                    if(j==15)
                    {
                         makeCubeAt(locx+i,locy+j,locz+k, GRASS, blockTerrain);
                    }
                    else
                    {
                        Random randomGenerator = new Random();
                        for (int idx = 1; idx <= 10; ++idx){
                            int rInt = randomGenerator.nextInt(10);
                            if(rInt == 2 || rInt == 5) {
                                makeCubeAt(locx+i,locy+j,locz+k, DIRT, blockTerrain);
                            } else if (rInt == 3 || rInt == 6) {
                                makeCubeAt(locx+i,locy+j,locz+k, DIRT, blockTerrain);
                            } else if (rInt == 4 || rInt == 7) {
                                makeCubeAt(locx+i,locy+j,locz+k, STONE, blockTerrain);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void makeTreeAt(Vector3Int loc, BlockTerrainControl blockTerrain) {
    	//int y = getTopBlock(loc,blockTerrain);
    	int y = loc.getY() +16;
    	int x = 0;
    	int z = 0;
    	Random randomGenerator = new Random();
        for (int idx = 1; idx <= 10; ++idx){
            int rInt = randomGenerator.nextInt(10);
            if(rInt == 2 || rInt == 5) {
            	x = loc.getX() + 13;
            	z = loc.getZ() + 16;
            } else if (rInt == 3 || rInt == 6) {
            	x = loc.getX() + 5;
            	z = loc.getZ() + 7;
            } else if (rInt == 4 || rInt == 7) {
            	x = loc.getX() + 8;
            	z = loc.getZ() + 2;
            } else {
            	x = loc.getX() + 10;
            	z = loc.getZ() + 10;
            }
        }
    	
    	blockTerrain.setBlock(new Vector3Int(x, y, z), Block_Wood.class);
    	blockTerrain.setBlock(new Vector3Int(x, y+1, z), Block_Wood.class);
    	blockTerrain.setBlock(new Vector3Int(x, y+1, z), Block_Wood.class);
    	blockTerrain.setBlock(new Vector3Int(x, y+2, z), Block_Wood.class);
    	blockTerrain.setBlock(new Vector3Int(x, y+3, z), Block_Wood.class);
    	blockTerrain.setBlock(new Vector3Int(x+1, y+3, z), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x, y+3, z+1), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x+1, y+3, z+1), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x-1, y+3, z), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x, y+3, z-1), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x-1, y+3, z-1), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x, y+4, z), Block_Leaves.class);
    	
    }
    
    public void makeCubeAt(double dx, double dy, double dz, int BlockType, BlockTerrainControl blockTerrain) {
        //make ground
         //To set a block, just specify the location and the block object
        //(Existing blocks will be replaced)
        int x = (int) dx;
        int y = (int) dy;
        int z = (int) dz;
        
        if(BlockType == GRASS) {
            blockTerrain.setBlock(new Vector3Int(x, y, z), Block_Grass.class);
        } else if (BlockType == WOOD) {
            blockTerrain.setBlock(new Vector3Int(x, y, z), Block_Wood.class);
        } else if (BlockType == STONE) {
            blockTerrain.setBlock(new Vector3Int(x, y, z), Block_Stone.class);
        } else if (BlockType == DIRT) {
            blockTerrain.setBlock(new Vector3Int(x, y, z), Block_Dirt.class);
        }

    }
    

	private int getTopBlock(Vector3Int location, BlockTerrainControl blockTerrain2) {
	 
        int x = +location.getX() / blockTerrain2.getSettings().getChunkSizeX();
        int y = +location.getY() / blockTerrain2.getSettings().getChunkSizeY();
        int z = +location.getZ() / blockTerrain2.getSettings().getChunkSizeZ();
 
        while (blockTerrain2.getChunk(new Vector3Int(x, y, z)).isBlockOnSurface(location) == false) {
            location.add(0, 1, 0);
        }
 
        int topBlock;
        topBlock = location.getY();
        return topBlock;
    }
    
}
