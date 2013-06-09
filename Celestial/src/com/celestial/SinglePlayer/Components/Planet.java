/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components;

import java.util.Random;

import com.celestial.Celestial;
import com.celestial.Blocks.BlocksEnum;
import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockTerrainControl;
import com.cubes.BlockType;
import com.cubes.Vector3Int;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Planet implements BlockChunkListener {
	private Star star;
	private int diameter;
	private Vector3f location;
	private int centerofdiam;
	private BlockTerrainControl terrcontrol;
	private Node planetNode;
	private Node terrainNode;

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
		this.centerofdiam = (int)Math.ceil((float)diameter/2);

		if(diameter % 2 == 0)
		{
			System.err.println("Planet Diameter MUST be odd number!");
			return;
		}
		
		this.generatePlanet();
	}

	private void generatePlanet() {
		terrainNode = new Node();
		System.out.println(terrainNode.getWorldTranslation());
		terrcontrol = new BlockTerrainControl(Celestial.portal.csettings, new Vector3Int(diameter, diameter, diameter));
		terrcontrol.addChunkListener(this);

		for(int i=0; i<diameter; i++) //y
		{    
			for(int j=0; j<diameter; j++) //x
			{
				for(int k=0; k<diameter; k++) //z
				{
					//System.out.println("Make Chunk: "+((j*16))+" "+((i*16))+" "+((k*16)));
					makeChunk((j*16), (i*16), (k*16), terrcontrol);
				}                
			}
		}

		//Post-terrain generation here
		for(int i=0; i<diameter*2; i++)
		{    
			for(int j=0; j<diameter*2; j++)
			{
				int x1 = (int) (i*8);
				int y1 = (int) diameter*16*3;
				int z1 = (int) (j*8);
				//System.out.println("Make Tree At: "+new Vector3Int(x1, y1, z1));
				makeTreeAt(new Vector3Int(x1, y1, z1), terrcontrol);
			}
		}
		terrainNode.addControl(terrcontrol);
		
		planetNode = new Node();
		planetNode.attachChild(terrainNode);
		
		terrainNode.move(((centerofdiam*16)-8)*-3,((centerofdiam*16)-8)*-3,((centerofdiam*16)-8)*-3);
		planetNode.move(location);
		Celestial.portal.getRootNode().attachChild(planetNode);
	}

	public void makeChunk(int locx, int locy, int locz, BlockTerrainControl blockTerrain)
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
						makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.GRASS, blockTerrain);
					}
					else
					{
						Random randomGenerator = new Random();
						for (int idx = 1; idx <= 10; ++idx){
							int rInt = randomGenerator.nextInt(10);
							if(rInt == 2 || rInt == 5) {
								makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.DIRT, blockTerrain);
							} else if (rInt == 3 || rInt == 6) {
								makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.DIRT, blockTerrain);
							} else if (rInt == 4 || rInt == 7) {
								makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.STONE, blockTerrain);
							}
							else
							{
								makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.STONE, blockTerrain);
							}
						}
					}
				}
			}
		}
	}

	public void makeTreeAt(Vector3Int loc, BlockTerrainControl blockTerrain) {
		int y = loc.getY();
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


	private int getTopBlock(Vector3Int location, BlockChunkControl blockTerrain2) {
        int height = 0;
        for (int i = 0; i < 256; i++) {
            BlockType block = blockTerrain2.getBlock(new Vector3Int(location.getX(), i, location.getZ()));
            if (block != null) {
                height++;
            }
        }
        return height;
    }

	@Override
	public void onSpatialUpdated(BlockChunkControl bcc) {
		bcc.getOptimizedGeometry_Opaque().setQueueBucket(Bucket.Opaque);
		bcc.getOptimizedGeometry_Transparent().setQueueBucket(Bucket.Transparent);
		updateCollisionShape(bcc.getOptimizedGeometry_Opaque());
		updateCollisionShape(bcc.getOptimizedGeometry_Transparent());
	}
	private void updateCollisionShape(Geometry chunkGeometry){
		RigidBodyControl rigidBodyControl = chunkGeometry.getControl(RigidBodyControl.class);
		if(chunkGeometry.getTriangleCount() > 0){
			if(rigidBodyControl != null){
				chunkGeometry.removeControl(rigidBodyControl);
				Celestial.portal.getBulletAppState().getPhysicsSpace().remove(rigidBodyControl);
			}
			rigidBodyControl = new RigidBodyControl(0);
			chunkGeometry.addControl(rigidBodyControl);
			Celestial.portal.getBulletAppState().getPhysicsSpace().add(rigidBodyControl);
		}
		else{
			if(rigidBodyControl != null){
				chunkGeometry.removeControl(rigidBodyControl);
			}
		}
	}

	public BlockTerrainControl getTerrControl() {
		return terrcontrol;
	}

	public Node getPlanetNode() {
		return planetNode;
	}
	
	public Node getTerrainNode()
	{
		return terrainNode;
	}

	public int getDiameter() {
		return diameter;
	}
	
	public Vector3f getSpawnLocation()
	{
		return new Vector3f(
				this.location.getX()+1.5f,
				this.location.getY() + this.diameter*16*3+3f,
				this.location.getZ()+1.5f);
	}
}
