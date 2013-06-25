/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.celestial.Blocks.BlocksEnum;
import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockTerrainControl;
import com.cubes.BlockType;
import com.cubes.Vector3Int;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

public class Planet implements BlockChunkListener {
	private Star star;
	protected int diameter;
	private String name;
	private Vector3f location;
	protected int centerofdiam;
	private BlockTerrainControl terrcontrol;
	private Node planetNode;
	private Node terrainNode;
	private Vector3f amountRotation;
	private Quaternion originalRotation;
	private Vector3f originalTranslationTerrain;
	private CelestialPortal portal;
	private List<PlanetCorner> cornerList;

	/**
	 * Create a new Planet
	 * @param star Parent Star
	 * @param diameter Diameter of the planet (in 16 block chunks) MUST BE ODD NUMBER
	 * @param location Center point of planet
	 */
	public Planet(Star star, int diameter, Vector3f location, String name)
	{
		this.star = star;
		this.diameter = diameter;
		this.location = location;
		this.centerofdiam = (int)Math.ceil((float)diameter/2);
		this.portal = star.getSolarSystem().getSector().getGalaxy().getPortal();
		this.amountRotation = new Vector3f(0.000f, 0.00f, 0f);
		this.name = name;
		
		if(diameter % 2 == 0)
		{
			System.err.println("Planet Diameter MUST be odd number!");
			return;
		}
		
		this.generatePlanet();
	}

	private void generatePlanet() {
		terrainNode = new Node();
		terrcontrol = new BlockTerrainControl(portal.csettings, new Vector3Int(diameter, diameter, diameter));
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
		this.originalRotation = planetNode.getWorldRotation().clone();
		planetNode.attachChild(terrainNode);
		
		terrainNode.move(((centerofdiam*16)-8)*-3,((centerofdiam*16)-8)*-3,((centerofdiam*16)-8)*-3);
		planetNode.move(location);
		this.originalTranslationTerrain = terrainNode.getWorldTranslation().clone();
		portal.getRootNode().attachChild(planetNode);
		
		/* CORNERS */
		this.cornerList = new ArrayList<PlanetCorner>();
		
		PlanetCorner c1 = new PlanetCorner(0,1,2);
		planetNode.attachChild(c1);
		c1.move(((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*3);
		this.cornerList.add(c1);
		
		PlanetCorner c2 = new PlanetCorner(0,2,4);
		planetNode.attachChild(c2);
		c2.move(((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*-3);
		this.cornerList.add(c2);
		
		PlanetCorner c3 = new PlanetCorner(0,4,3);
		planetNode.attachChild(c3);
		c3.move(((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*-3);
		this.cornerList.add(c3);
		
		PlanetCorner c4 = new PlanetCorner(0,3,1);
		planetNode.attachChild(c4);
		c4.move(((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*3);
		this.cornerList.add(c4);
		
		PlanetCorner c5 = new PlanetCorner(5,1,2);
		planetNode.attachChild(c5);
		c5.move(((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*3);
		this.cornerList.add(c5);
		
		PlanetCorner c6 = new PlanetCorner(5,2,4);
		planetNode.attachChild(c6);
		c6.move(((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*-3);
		this.cornerList.add(c6);
		
		PlanetCorner c7 = new PlanetCorner(5,4,3);
		planetNode.attachChild(c7);
		c7.move(((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*-3);
		this.cornerList.add(c7);
		
		PlanetCorner c8 = new PlanetCorner(5,3,1);
		planetNode.attachChild(c8);
		c8.move(((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*3);
		this.cornerList.add(c8);
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
								makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.STONE, blockTerrain);
							} else if (rInt == 4 || rInt == 7) {
								makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.DIRT, blockTerrain);
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

	public PlanetCorner[] getCornersForFace(int face)
	{
		PlanetCorner[] values = new PlanetCorner[4];
		
		PlanetCorner c1 = null;
		PlanetCorner c2 = null;
		PlanetCorner c3 = null;
		PlanetCorner c4 = null;
		
		int amt = 0;
		for(PlanetCorner c : this.cornerList)
			if(c.getSides().contains(face))
				switch(amt)
				{
				case 0:
					c1 = c;
					amt++;
					break;
				case 1:
					c2 = c;
					amt++;
					break;
				case 2:
					c3 = c;
					amt++;
					break;
				case 3:
					c4 = c;
					amt++;
					break;
				default:
					break;
				}
		
		if(c1 != null && c2 != null && c3 != null && c4 != null)
		{
			
			switch(face)
			{
				case 0:
					values[0] = c1;
					values[1] = c2;
					values[2] = c3;
					values[3] = c4;
					break;
				case 1:
					values[0] = c1;
					values[1] = c2;
					values[2] = c4;
					values[3] = c3;
					break;
				case 2:
					values[0] = c2;
					values[1] = c1;
					values[2] = c3;
					values[3] = c4;
					break;
				case 3:
					values[0] = c1;
					values[1] = c2;
					values[2] = c4;
					values[3] = c3;
					break;
				case 4:
					values[0] = c2;
					values[1] = c1;
					values[2] = c3;
					values[3] = c4;
					break;
				case 5:
					values[0] = c1;
					values[1] = c4;
					values[2] = c3;
					values[3] = c2;
					break;
				default:
					return null;
			}
		}
		else
		{
			return null;
		}
		return values;
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
				portal.getBulletAppState().getPhysicsSpace().remove(rigidBodyControl);
			}
			rigidBodyControl = new RigidBodyControl(0);
			chunkGeometry.addControl(rigidBodyControl);
			portal.getBulletAppState().getPhysicsSpace().add(rigidBodyControl);
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
	
	public String getName() {
		return name;
	}
	
	public Vector3f getSpawnLocation()
	{
		return new Vector3f(
				this.location.getX()+1.5f,
				this.location.getY() + this.diameter*16*3+3f,
				this.location.getZ()+1.5f);
	}

	public void rotate() {
		planetNode.rotate(this.amountRotation.getX()*FastMath.DEG_TO_RAD, this.amountRotation.getY()*FastMath.DEG_TO_RAD, this.amountRotation.getZ()*FastMath.DEG_TO_RAD);
	}
	
	public Quaternion getRotation()
	{
		return planetNode.getWorldRotation();
	}

	public Vector3f getOriginalTranslation() {
		return this.originalTranslationTerrain;
	}

	public Vector3f getWantedLocation() {
		return location;
	}
}
