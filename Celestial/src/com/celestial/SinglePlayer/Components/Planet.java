/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import com.celestial.CelestialPortal;
import com.celestial.Blocks.BlocksEnum;
import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockTerrainControl;
import com.cubes.BlockType;
import com.cubes.Vector3Int;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class Planet implements BlockChunkListener {
	public static final int TOP = 0;
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int SOUTH = 3;
	public static final int WEST = 4;
	public static final int BOTTOM = 5;
	public float atmosphereSizeFactor;
	public enum planetType {
		HABITABLE, INNER, OUTER, INFERNO, FRIGID, MOON;
 
 		public static final EnumSet<planetType> PLANETTYPES = EnumSet.range(HABITABLE, FRIGID);
 		public static final EnumSet<planetType> HOSTILETYPES = EnumSet.range(INNER, MOON);
 		public static final EnumSet<planetType> ATMOSPHERETYPES = EnumSet.range(HABITABLE, OUTER);
 
 		public final boolean isPlanetType() {
 			return PLANETTYPES.contains(this);
 		}
 		public final boolean isHostile() {
 			return HOSTILETYPES.contains(this);
 		}
 		public final boolean hasAtmosphere() {
 			return ATMOSPHERETYPES.contains(this);
 		}
 
 		public static final EnumSet<planetType> ALLTYPES = EnumSet.allOf(planetType.class);
 	}
	public planetType type;
	
	private Star star;
	private int diameter;
	private String name;
	private Vector3f location;
	private int centerofdiam;
	private BlockTerrainControl terrcontrol;
	private Node planetNode;
	private Node terrainNode;
	private Vector3f amountRotation;
	private Quaternion originalRotation;
	private Vector3f originalTerrainTranslation;
	private CelestialPortal portal;
	private List<PlanetCorner> cornerList;
	private Box atmospherebox;
	private Geometry atmospheregeom;
	private Material atmospheremat;
	private Node starNode;
	private Vector3f amountRevolution;
	private CollisionShape terrainCollision;
	private RigidBodyControl terrainRigidBody;
	private Vector3f originalPlanetTranslation;
	private Vector3f previousPlanetTranslation;
	private Quaternion previousPlanetRotation;

	/**
	 * Create a new Planet
	 * @param star Parent Star
	 * @param diameter Diameter of the planet (in 16 block chunks) MUST be odd number
	 * @param location Center point of planet
	 * @param name Name of the planet
	 */
	public Planet(Star star, int diameter, Vector3f location, String name)
	{
		this.star = star;
		this.diameter = diameter;
		this.location = location;
		this.centerofdiam = (int)Math.ceil((float)diameter/2);
		this.portal = star.getSolarSystem().getSector().getGalaxy().getPortal();
		this.amountRotation = new Vector3f(0f, 0.01f, 0f);
		this.amountRevolution = new Vector3f(0f, 0.0001f, 0f);
		this.name = name;
		this.atmosphereSizeFactor = 1.2f;

		if(diameter % 2 == 0)
		{
			System.err.println("Planet Diameter MUST be odd number!");
			return;
		}

		this.generatePlanet();
	}

	private void generatePlanet() {
		starNode = new Node();
		planetNode = new Node();
		starNode.attachChild(planetNode);
		terrainNode = new Node();
		planetNode.attachChild(terrainNode);
		
		/* NODES */

		this.originalRotation = planetNode.getWorldRotation().clone(); 

		terrainNode.move(((centerofdiam*16)-8)*-3,((centerofdiam*16)-8)*-3,((centerofdiam*16)-8)*-3);
		planetNode.move(location);
		starNode.move(star.getStarNode().getWorldTranslation());
		this.originalTerrainTranslation = terrainNode.getWorldTranslation().clone();
		this.originalPlanetTranslation = planetNode.getWorldTranslation().clone();

		star.getStarNode().attachChild(starNode);
		
		/* PLANET TYPE DETERMINATION */
		
		if(this.starNode.getWorldTranslation().distance(this.planetNode.getWorldTranslation()) >= 3500F && 
				this.starNode.getWorldTranslation().distance(this.planetNode.getWorldTranslation()) <= 4000F) {
			this.type = planetType.HABITABLE;
		} else if(this.starNode.getWorldTranslation().distance(this.planetNode.getWorldTranslation()) <= 3500F && 
				this.starNode.getWorldTranslation().distance(this.planetNode.getWorldTranslation()) >= 2500F) {
			this.type = planetType.INNER;
		} else if(this.starNode.getWorldTranslation().distance(this.planetNode.getWorldTranslation()) <= 2500F) {
			this.type = planetType.INFERNO;
		} else if(this.starNode.getWorldTranslation().distance(this.planetNode.getWorldTranslation()) <= 5000F && 
				this.starNode.getWorldTranslation().distance(this.planetNode.getWorldTranslation()) >= 4000F) {
			this.type = planetType.OUTER;
		} else if(this.starNode.getWorldTranslation().distance(this.planetNode.getWorldTranslation()) >= 5000F) {
			this.type = planetType.FRIGID;
		}

		terrcontrol = new BlockTerrainControl(portal.csettings, new Vector3Int(diameter, diameter, diameter));
		terrcontrol.addChunkListener(this);

		for(int i=0; i<diameter; i++) //y
		{    
			for(int j=0; j<diameter; j++) //x
			{
				for(int k=0; k<diameter; k++) //z
				{
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
				makeTreeAt(new Vector3Int(x1, y1, z1), terrcontrol);
			}
		}
		terrainNode.addControl(terrcontrol);

		/* LIGHTING */
		planetNode.setShadowMode(ShadowMode.CastAndReceive);

		/* CORNERS */
		this.cornerList = new ArrayList<PlanetCorner>();

		PlanetCorner c1 = new PlanetCorner(TOP,NORTH,EAST);
		planetNode.attachChild(c1);
		c1.move(((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*3);
		this.cornerList.add(c1);

		PlanetCorner c2 = new PlanetCorner(TOP,EAST,SOUTH);
		planetNode.attachChild(c2);
		c2.move(((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*-3);
		this.cornerList.add(c2);

		PlanetCorner c3 = new PlanetCorner(TOP,SOUTH,WEST);
		planetNode.attachChild(c3);
		c3.move(((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*-3);
		this.cornerList.add(c3);

		PlanetCorner c4 = new PlanetCorner(TOP,WEST,NORTH);
		planetNode.attachChild(c4);
		c4.move(((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*3);
		this.cornerList.add(c4);

		PlanetCorner c5 = new PlanetCorner(BOTTOM,NORTH,EAST);
		planetNode.attachChild(c5);
		c5.move(((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*3);
		this.cornerList.add(c5);

		PlanetCorner c6 = new PlanetCorner(BOTTOM,EAST,SOUTH);
		planetNode.attachChild(c6);
		c6.move(((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*-3);
		this.cornerList.add(c6);

		PlanetCorner c7 = new PlanetCorner(BOTTOM,SOUTH,WEST);
		planetNode.attachChild(c7);
		c7.move(((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*-3);
		this.cornerList.add(c7);

		PlanetCorner c8 = new PlanetCorner(BOTTOM,WEST,NORTH);
		planetNode.attachChild(c8);
		c8.move(((centerofdiam*16)-8)*3, ((centerofdiam*16)-8)*-3, ((centerofdiam*16)-8)*3);
		this.cornerList.add(c8);
		
		/* CORNER BEDROCK */
		Vector3Int c1Int = Vector3Int.convert3f(c1.getWorldTranslation());
		makeCubeAt(c1Int.getX(), c1Int.getY(), c1Int.getZ(), BlocksEnum.SUBSTRATUS, terrcontrol);
		
		Vector3Int c2Int = Vector3Int.convert3f(c2.getWorldTranslation());
		makeCubeAt(c2Int.getX(), c2Int.getY(), c2Int.getZ(), BlocksEnum.SUBSTRATUS, terrcontrol);

		Vector3Int c3Int = Vector3Int.convert3f(c3.getWorldTranslation());
		makeCubeAt(c3Int.getX(), c3Int.getY(), c3Int.getZ(), BlocksEnum.SUBSTRATUS, terrcontrol);
		
		Vector3Int c4Int = Vector3Int.convert3f(c4.getWorldTranslation());
		makeCubeAt(c4Int.getX(), c4Int.getY(), c4Int.getZ(), BlocksEnum.SUBSTRATUS, terrcontrol);

		Vector3Int c5Int = Vector3Int.convert3f(c5.getWorldTranslation());
		makeCubeAt(c5Int.getX(), c5Int.getY(), c5Int.getZ(), BlocksEnum.SUBSTRATUS, terrcontrol);

		Vector3Int c6Int = Vector3Int.convert3f(c6.getWorldTranslation());
		makeCubeAt(c6Int.getX(), c6Int.getY(), c6Int.getZ(), BlocksEnum.SUBSTRATUS, terrcontrol);

		Vector3Int c7Int = Vector3Int.convert3f(c7.getWorldTranslation());
		makeCubeAt(c7Int.getX(), c7Int.getY(), c7Int.getZ(), BlocksEnum.SUBSTRATUS, terrcontrol);

		Vector3Int c8Int = Vector3Int.convert3f(c8.getWorldTranslation());
		makeCubeAt(c8Int.getX(), c8Int.getY(), c8Int.getZ(), BlocksEnum.SUBSTRATUS, terrcontrol);
		
		System.out.println(c1.getWorldTranslation());
		System.out.println(c1Int.toString());
		
		/* ATMOSPHERE */
		if(this.type.hasAtmosphere()) {
			this.atmospherebox = new Box(this.diameter*16*3*atmosphereSizeFactor, this.diameter*16*3*atmosphereSizeFactor, this.diameter*16*3*atmosphereSizeFactor);
			this.atmospheregeom = new Geometry("Atmosphere", this.atmospherebox);
			this.atmospheremat = new Material(portal.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			
			if(this.type.equals(planetType.HABITABLE))
				this.atmospheremat.setColor("Color", new ColorRGBA(0.3f, 0.5f, 1, 0.75f));
			else if(this.type.equals(planetType.INNER))
				this.atmospheremat.setColor("Color", new ColorRGBA(0.68f, 0.4f, 0.09f, 0.75f));
			else
				this.atmospheremat.setColor("Color", new ColorRGBA(0.25f, 0.38f, 0.98f, 0.75f));
			this.atmospheregeom.setMaterial(this.atmospheremat);
	
			this.atmospheremat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			this.atmospheremat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
			this.atmospheregeom.setQueueBucket(Bucket.Transparent);
			this.atmospheregeom.setShadowMode(ShadowMode.Receive);
	
			this.planetNode.attachChild(this.atmospheregeom);
		}

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
						if(this.type.equals(planetType.HABITABLE))
							makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.GRASS, blockTerrain);
					}
					else
					{
						if(this.type.hasAtmosphere()) {
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
						} else {
							//TODO update with better detail
							if(this.type.equals(planetType.INFERNO)) {
								makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.DARKSTONE, blockTerrain);
							}
							else if(this.type.equals(planetType.FRIGID)) {
								//Block_Ice && Block_BlackStone
								Random randomGenerator = new Random();
								for (int idx = 1; idx <= 2; ++idx){
									int rInt = randomGenerator.nextInt(2);
									if(rInt == 1){
										makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.ICE, blockTerrain);
									} else {
										makeCubeAt(locx+i,locy+j,locz+k, BlocksEnum.DARKSTONE, blockTerrain);
									}
								}
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
			case TOP:
				values[0] = c1;
				values[1] = c2;
				values[2] = c3;
				values[3] = c4;
				break;
			case NORTH:
				values[0] = c1;
				values[1] = c2;
				values[2] = c4;
				values[3] = c3;
				break;
			case EAST:
				values[0] = c2;
				values[1] = c1;
				values[2] = c3;
				values[3] = c4;
				break;
			case SOUTH:
				values[0] = c2;
				values[1] = c1;
				values[2] = c3;
				values[3] = c4;
				break;
			case WEST:
				values[0] = c1;
				values[1] = c2;
				values[2] = c4;
				values[3] = c3;
				break;
			case BOTTOM:
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
		bcc.getOptimizedGeometry_Opaque().setShadowMode(ShadowMode.CastAndReceive);
		bcc.getOptimizedGeometry_Transparent().setShadowMode(ShadowMode.Receive);
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
	public Star getStar() {
		return this.star;
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

	public float getDiameterAsFloat(boolean includeAtmosphere)
	{
		if(includeAtmosphere)
			return this.diameter*16*3*atmosphereSizeFactor;
		else
			return this.diameter*16*3;
	}

	public float getRadiusAsFloat(boolean includeAtmosphere)
	{
		if(includeAtmosphere)
			return (this.centerofdiam*16-8)*3*atmosphereSizeFactor;
		else
			return (this.centerofdiam*16-8)*3;
	}

	public Vector3f getSpawnLocation(int face)
	{
		return new Vector3f(
				this.planetNode.getWorldTranslation().getX()+1.5f,
				this.planetNode.getWorldTranslation().getY() + this.diameter*16*3+3f,
				this.planetNode.getWorldTranslation().getZ()+1.5f);
	}

	public void rotate() {
		this.previousPlanetRotation = planetNode.getLocalRotation().clone();
		this.previousPlanetTranslation = planetNode.getWorldTranslation().clone();
		starNode.rotate(this.amountRevolution.getX()*FastMath.DEG_TO_RAD, this.amountRevolution.getY()*FastMath.DEG_TO_RAD, this.amountRevolution.getZ()*FastMath.DEG_TO_RAD);
		planetNode.rotate(this.amountRotation.getX()*FastMath.DEG_TO_RAD, this.amountRotation.getY()*FastMath.DEG_TO_RAD, this.amountRotation.getZ()*FastMath.DEG_TO_RAD);
		updateCollision();
	}

	public void updateCollision()
	{
		/*this.terrcontrol.getChunk(new Vector3Int(0,0,0)).needsMeshUpdate = true;
		this.terrcontrol.updateSpatial();		*/
		for(int x = 0; x<this.diameter; x++)
		{
			for(int y = 0; y<this.diameter; y++)
			{
				for(int z = 0; z<this.diameter; z++)
				{
					BlockChunkControl chunk = this.terrcontrol.getChunks()[x][y][z];
					if(chunk.getOptimizedGeometry_Opaque() != null)
					{
						if(chunk.getOptimizedGeometry_Opaque().getControl(RigidBodyControl.class) != null)
						{
							RigidBodyControl r = chunk.getOptimizedGeometry_Opaque().getControl(RigidBodyControl.class);
							chunk.getOptimizedGeometry_Opaque().removeControl(r);
							portal.getBulletAppState().getPhysicsSpace().remove(r);
							chunk.getOptimizedGeometry_Opaque().addControl(r);
							portal.getBulletAppState().getPhysicsSpace().add(r);
						}
					}
					if(chunk.getOptimizedGeometry_Transparent() != null)
					{
						if(chunk.getOptimizedGeometry_Transparent().getControl(RigidBodyControl.class) != null)
						{
							RigidBodyControl r = chunk.getOptimizedGeometry_Transparent().getControl(RigidBodyControl.class);
							chunk.getOptimizedGeometry_Transparent().removeControl(r);
							portal.getBulletAppState().getPhysicsSpace().remove(r);
							chunk.getOptimizedGeometry_Transparent().addControl(r);
							portal.getBulletAppState().getPhysicsSpace().add(r);
						}
					}
				}
			}
		}
	}
	
	private Vector3f getUpdatedChunkLocation(int x, int y, int z)
	{
		Vector3f terrainloc = this.getCurrentTerrainTranslation().clone();
		float scalarx = (x*16*3)+(8*3);
		float scalary = (y*16*3)+(8*3);
		float scalarz = (z*16*3)+(8*3);
		return terrainloc.add(scalarx, scalary, scalarz);
	}

	public Vector3f getCurrentPlanetTranslation() {
		return planetNode.getWorldTranslation();
	}

	public Vector3f getCurrentTerrainTranslation() {
		return terrainNode.getWorldTranslation();
	}

	public Quaternion getRotation()
	{
		return planetNode.getWorldRotation();
	}

	public Vector3f getOriginalTerrainTranslation() {
		return this.originalTerrainTranslation;
	}

	public Vector3f getOriginalPlanetTranslation() {
		return this.originalPlanetTranslation;
	}
	
	public Vector3f getPreviousPlanetTranslation()
	{
		return this.previousPlanetTranslation;
	}
	
	public Quaternion getPreviousPlanetRotation()
	{
		return this.previousPlanetRotation;
	}

	public Spatial getStarNode() {
		return starNode;
	}
}
