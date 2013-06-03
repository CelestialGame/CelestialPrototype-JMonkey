/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.SinglePlayer.Components;

import java.util.Random;

import com.celestial.Celestial;
import com.celestial.Blocks.Block_BirchWood;
import com.celestial.Blocks.Block_Dirt;
import com.celestial.Blocks.Block_Grass;
import com.celestial.Blocks.Block_Leaves;
import com.celestial.Blocks.Block_Stone;
import com.celestial.Blocks.Block_Wood;
import com.celestial.World.ChunkListener;
import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.cubes.test.CubesTestAssets;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class Planet implements BlockChunkListener {
	private Star star;
	private int diameter;
	private Vector3f location;

	/**
	 * Create a new Planet
	 * @param star Parent Star
	 * @param diameter Diameter of the planet (in 16 block chunks) MUST BE ODD NUMBER
	 * @param location Center point of planet
	 */
	public Planet(Star star, int diameter, Vector3f location)
	{
		this.star = star;
		this.diameter = diameter;
		this.location = location;
		
		this.generatePlanet();
	}

	private void generatePlanet() {
		BlockTerrainControl Side = new BlockTerrainControl(CubesTestAssets.getSettings(Celestial.self), new Vector3Int(diameter,diameter,diameter));
        Side.addChunkListener(this);
        
        for(int i=0; i<diameter; i++)
        {    
            for(int j=0; j<diameter; j++)
            {
                makeChunk(locx+(i*16), locy, locz+(j*16), Side);
                
            }
        }
        
        //Post-terrain generation here
        for(int i=0; i<PLANETSIDESIZE; i++)
        {    
            for(int j=0; j<PLANETSIDESIZE; j++)
            {
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
                            else
                            {
                            	makeCubeAt(locx+i,locy+j,locz+k, STONE, blockTerrain);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void makeTreeAt(Vector3Int loc, BlockTerrainControl blockTerrain) {
    	int y = loc.getY() +16;
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
        //int y = getTopBlock(loc,blockTerrain);
    	
        if(birchwood) {
        	blockTerrain.setBlock(new Vector3Int(x, y, z), Block_BirchWood.class);
	    	blockTerrain.setBlock(new Vector3Int(x, y+1, z), Block_BirchWood.class);
	    	blockTerrain.setBlock(new Vector3Int(x, y+2, z), Block_BirchWood.class);
	    	blockTerrain.setBlock(new Vector3Int(x, y+3, z), Block_BirchWood.class);
        } else {
	    	blockTerrain.setBlock(new Vector3Int(x, y, z), Block_Wood.class);
	    	blockTerrain.setBlock(new Vector3Int(x, y+1, z), Block_Wood.class);
	    	blockTerrain.setBlock(new Vector3Int(x, y+2, z), Block_Wood.class);
	    	blockTerrain.setBlock(new Vector3Int(x, y+3, z), Block_Wood.class);
        }
    	blockTerrain.setBlock(new Vector3Int(x+1, y+3, z), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x, y+3, z+1), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x+1, y+3, z+1), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x-1, y+3, z), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x, y+3, z-1), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x-1, y+3, z-1), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x+1, y+3, z-1), Block_Leaves.class);
    	blockTerrain.setBlock(new Vector3Int(x-1, y+3, z+1), Block_Leaves.class);
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
		try {
	        int x = +location.getX() / blockTerrain2.getSettings().getChunkSizeX();
	        int y = +location.getY() / blockTerrain2.getSettings().getChunkSizeY();
	        int z = +location.getZ() / blockTerrain2.getSettings().getChunkSizeZ();
	 
	        while (blockTerrain2.getChunk(new Vector3Int(x, y, z)).isBlockOnSurface(location) == false) {
	            location.add(0, 1, 0);
	        }
	 
	        int topBlock;
	        topBlock = location.getY();
	        return topBlock;
		} catch (Exception e) {
			//e.printStackTrace();
	    	return 16;
	    }
    }

	@Override
	public void onSpatialUpdated(BlockChunkControl bcc) {
	       for(Node side : parent.WorldSides) {
	           updateCollisionShape(bcc.getOptimizedGeometry_Opaque());
	           updateCollisionShape(bcc.getOptimizedGeometry_Transparent());
	       }
	   }
	   private void updateCollisionShape(Geometry chunkGeometry){
	       RigidBodyControl rigidBodyControl = chunkGeometry.getControl(RigidBodyControl.class);
	       if(chunkGeometry.getTriangleCount() > 0){
	           if(rigidBodyControl != null){
	               chunkGeometry.removeControl(rigidBodyControl);
	               parent.bulletAppState.getPhysicsSpace().remove(rigidBodyControl);
	           }
	           rigidBodyControl = new RigidBodyControl(0);
	           chunkGeometry.addControl(rigidBodyControl);
	           parent.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
	       }
	       else{
	           if(rigidBodyControl != null){
	               chunkGeometry.removeControl(rigidBodyControl);
	           }
	       }
	   }
	
	
}
