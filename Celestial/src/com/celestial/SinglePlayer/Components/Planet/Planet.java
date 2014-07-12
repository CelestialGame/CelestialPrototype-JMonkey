/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components.Planet;

import java.util.Random;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Components.Star;
import com.celestial.SinglePlayer.Physics.Listener;
import com.celestial.World.BlockChunkManager;
import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3i;
import com.cubes.render.GreedyMesher;
import com.cubes.render.NaiveMesher;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

public class Planet
{
    public float atmosphereSizeFactor;
    
    public PlanetType type;
    
    public static int CHUNK_SIZE = 16;
    public static int VIEW_DISTANCE = 128;
    
    private Star star;
    private int diameter;
    private String name;
    private Vector3f location;
    private int centerofdiam;
    private BlockTerrainControl terrainControl;
    private Node planetNode;
    private Node terrainNode;
    public Vector3f amountRotation;
    private Quaternion originalRotation;
    private Vector3f originalTerrainTranslation;
    private CelestialPortal portal;
    private Box atmospherebox;
    private Geometry atmospheregeom;
    private Material atmospheremat;
    private Node starNode;
    public Vector3f amountRevolution;
    private Vector3f originalPlanetTranslation;
    private Vector3f previousPlanetTranslation;
    private Quaternion previousPlanetRotation;
    private Quaternion previousStarNodeRotation;
    private BulletAppState bulletAppState;
    private long seed;
    
    private DirectionalLight directionalLight;
    
    private DirectionalLightShadowRenderer dlsr;
    
    /**
     * Create a new Planet
     * 
     * @param star
     *            Parent Star
     * @param diameter
     *            Diameter of the planet (in 16 block chunks) MUST be odd number
     * @param location
     *            Center point of planet
     * @param name
     *            Name of the planet
     */
    public Planet(Star star, int diameter, Vector3f location, String name)
    {
	this(star, diameter, location, name, new Random().nextLong());
    }
    
    public Planet(Star star, int diameter, Vector3f location, String name,
	    long seed)
    {
	this.star = star;
	this.diameter = diameter;
	this.location = location;
	this.centerofdiam = (int) Math.ceil((float) diameter / 2);
	this.portal = star.getSolarSystem().getSector().getGalaxy().getPortal();
	this.amountRotation = new Vector3f(0f, 0.000f, 0f);
	this.amountRevolution = new Vector3f(0f, 0.000f, 0f);
	this.name = name;
	this.atmosphereSizeFactor = 1.001f;
	this.bulletAppState = new BulletAppState();
	this.portal.getParent().getStateManager().attach(bulletAppState);
	this.bulletAppState.getPhysicsSpace().addCollisionListener(
		new Listener());
	this.seed = seed;
	
	if(diameter % 2 == 0)
	{
	    System.err.println("Planet Diameter MUST be odd number!");
	    return;
	}
	
	this.generatePlanet();
    }
    
    private void generatePlanet()
    {
	
	/* NODES */
	starNode = new Node();
	planetNode = new Node();
	starNode.attachChild(planetNode);
	terrainNode = new Node();
	planetNode.attachChild(terrainNode);
	
	this.originalRotation = planetNode.getWorldRotation().clone();
	
	terrainNode.move(((centerofdiam * CHUNK_SIZE) - (CHUNK_SIZE / 2)) * -3,
		((centerofdiam * CHUNK_SIZE) - (CHUNK_SIZE / 2)) * -3,
		((centerofdiam * CHUNK_SIZE) - (CHUNK_SIZE / 2)) * -3);
	planetNode.move(location);
	starNode.move(star.getStarNode().getWorldTranslation());
	this.originalTerrainTranslation = terrainNode.getWorldTranslation()
		.clone();
	this.originalPlanetTranslation = planetNode.getWorldTranslation()
		.clone();
	
	star.getStarNode().attachChild(starNode);
	
	/* LIGHTING */
	initLighting();
	
	/* PLANET TYPE DETERMINATION */
	
	if(this.starNode.getWorldTranslation().distance(
		this.planetNode.getWorldTranslation()) >= 3500F
		&& this.starNode.getWorldTranslation().distance(
			this.planetNode.getWorldTranslation()) <= 4000F)
	{
	    this.type = PlanetType.HABITABLE;
	}
	else if(this.starNode.getWorldTranslation().distance(
		this.planetNode.getWorldTranslation()) <= 3500F
		&& this.starNode.getWorldTranslation().distance(
			this.planetNode.getWorldTranslation()) >= 2500F)
	{
	    this.type = PlanetType.INNER;
	}
	else if(this.starNode.getWorldTranslation().distance(
		this.planetNode.getWorldTranslation()) <= 2500F)
	{
	    this.type = PlanetType.INFERNO;
	}
	else if(this.starNode.getWorldTranslation().distance(
		this.planetNode.getWorldTranslation()) <= 5000F
		&& this.starNode.getWorldTranslation().distance(
			this.planetNode.getWorldTranslation()) >= 4000F)
	{
	    this.type = PlanetType.OUTER;
	}
	else if(this.starNode.getWorldTranslation().distance(
		this.planetNode.getWorldTranslation()) >= 5000F)
	{
	    this.type = PlanetType.FRIGID;
	}
	
	/* TERRAIN CONTROL & CHUNK LISTENER */
	
	terrainControl = new BlockTerrainControl(portal.csettings,
		new Vector3i(diameter, diameter, diameter), new GreedyMesher());
	terrainControl.addChunkListener(new BlockChunkListener()
	{
	    @Override
	    public void onSpatialUpdated(BlockChunkControl blockChunk)
	    {
		Geometry optimizedGeometry = blockChunk
			.getOptimizedGeometry_Opaque();
		Geometry transparentGeometry = blockChunk
			.getOptimizedGeometry_Transparent();
		RigidBodyControl optimizedRigidBodyControl = optimizedGeometry
			.getControl(RigidBodyControl.class);
		RigidBodyControl transparentRigidBodyControl = transparentGeometry
			.getControl(RigidBodyControl.class);
		if(optimizedRigidBodyControl != null)
		{
		    bulletAppState.getPhysicsSpace().remove(
			    optimizedRigidBodyControl);
		    optimizedGeometry.removeControl(RigidBodyControl.class);
		}
		if(transparentRigidBodyControl != null)
		{
		    bulletAppState.getPhysicsSpace().remove(
			    transparentRigidBodyControl);
		    transparentGeometry.removeControl(RigidBodyControl.class);
		}
		
		if(optimizedGeometry.getTriangleCount() > 0)
		{
		    optimizedRigidBodyControl = new RigidBodyControl(0);
		    optimizedGeometry.addControl(optimizedRigidBodyControl);
		    bulletAppState.getPhysicsSpace().add(
			    optimizedRigidBodyControl);
		    optimizedRigidBodyControl
			    .setCollisionShape(new MeshCollisionShape(
				    optimizedGeometry.getMesh()));
		}
		
		if(transparentGeometry.getTriangleCount() > 0)
		{
		    transparentRigidBodyControl = new RigidBodyControl(0);
		    transparentGeometry.addControl(transparentRigidBodyControl);
		    bulletAppState.getPhysicsSpace().add(
			    transparentRigidBodyControl);
		    transparentRigidBodyControl
			    .setCollisionShape(new MeshCollisionShape(
				    transparentGeometry.getMesh()));
		}
	    }
	});
	
	/* TERRAIN CONTROL CHUNK GENERATION */
	
	terrainControl.setBlockChunkManager(new BlockChunkManager(
		terrainControl, this));
	
	terrainNode.addControl(terrainControl);
	
	/* LIGHTING */
	terrainNode.setShadowMode(ShadowMode.CastAndReceive);
	
	/* ATMOSPHERE */
	if(this.type.hasAtmosphere())
	{
	    this.atmospherebox = new Box(this.diameter * CHUNK_SIZE * 3
		    * atmosphereSizeFactor, this.diameter * CHUNK_SIZE * 3
		    * atmosphereSizeFactor, this.diameter * CHUNK_SIZE * 3
		    * atmosphereSizeFactor);
	    this.atmospheregeom = new Geometry("Atmosphere", this.atmospherebox);
	    this.atmospheremat = new Material(portal.getAssetManager(),
		    "Common/MatDefs/Misc/Unshaded.j3md");
	    
	    if(this.type.equals(PlanetType.HABITABLE))
		this.atmospheremat.setColor("Color", new ColorRGBA(0.3f, 0.5f,
			1, 0.75f));
	    else if(this.type.equals(PlanetType.INNER))
		this.atmospheremat.setColor("Color", new ColorRGBA(0.68f, 0.4f,
			0.09f, 0.75f));
	    else
		this.atmospheremat.setColor("Color", new ColorRGBA(0.25f,
			0.38f, 0.98f, 0.75f));
	    this.atmospheregeom.setMaterial(this.atmospheremat);
	    
	    this.atmospheremat.getAdditionalRenderState().setBlendMode(
		    BlendMode.Alpha);
	    this.atmospheremat.getAdditionalRenderState().setFaceCullMode(
		    FaceCullMode.Off);
	    this.atmospheregeom.setQueueBucket(Bucket.Transparent);
	    this.atmospheregeom.setShadowMode(ShadowMode.Receive);
	    
	    this.planetNode.attachChild(this.atmospheregeom);
	}
	
    }
    
    private void initLighting()
    {
	this.directionalLight = new DirectionalLight();
	this.directionalLight.setColor(ColorRGBA.White);
	this.directionalLight.setDirection(this.getCurrentPlanetTranslation()
		.subtract(this.starNode.getWorldTranslation()).normalize());
	this.starNode.addLight(this.directionalLight);
	
	this.dlsr = new DirectionalLightShadowRenderer(
		this.portal.getAssetManager(), 2048, 3);
	this.dlsr.setShadowIntensity(0.4f);
	this.dlsr.setLight(directionalLight);
	// this.dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
	this.portal.getViewPort().addProcessor(this.dlsr);
    }
    
    public void update(PlanetUpdate update)
    {
	switch(update)
	{
	    case ROTATION:
		this.rotate();
		//this.directionalLight.setDirection(this.getCurrentPlanetTranslation()
		//	.subtract(this.starNode.getWorldTranslation()).normalize());
		break;
	    case CHUNKLOADER:
		this.updateChunks();
		break;
	    default:
		return;
	}
    }
    
    public float getAtmosphereDiameter()
    {
	try
	{
	    return this.atmospherebox.getXExtent() * 2;
	}
	catch(NullPointerException e)
	{
	    // The silly planet may not have an atmosphere, so calculate one
	    return this.diameter * CHUNK_SIZE * 3 * atmosphereSizeFactor;
	}
    }
    
    private void updateChunks()
    {
	terrainControl.getBlockChunkManager().checkChunks();
    }
    
    public BulletAppState getBulletAppState()
    {
	return this.bulletAppState;
    }
    
    public BlockTerrainControl getTerrControl()
    {
	return terrainControl;
    }
    
    public Star getStar()
    {
	return this.star;
    }
    
    public Node getPlanetNode()
    {
	return planetNode;
    }
    
    public Node getTerrainNode()
    {
	return terrainNode;
    }
    
    public int getDiameter()
    {
	return diameter;
    }
    
    public String getName()
    {
	return name;
    }
    
    public float getDiameterAsFloat(boolean includeAtmosphere)
    {
	if(includeAtmosphere)
	    return this.diameter * CHUNK_SIZE * 3 * atmosphereSizeFactor;
	else
	    return this.diameter * CHUNK_SIZE * 3;
    }
    
    public float getRadiusAsFloat(boolean includeAtmosphere)
    {
	if(includeAtmosphere)
	    return (this.centerofdiam * CHUNK_SIZE - (CHUNK_SIZE / 2)) * 3
		    * atmosphereSizeFactor;
	else
	    return (this.centerofdiam * CHUNK_SIZE - (CHUNK_SIZE / 2)) * 3;
    }
    
    public Vector3f getSpawnLocation(int face)
    {
	return new Vector3f(
		this.planetNode.getWorldTranslation().getX() + 1.5f,
		this.planetNode.getWorldTranslation().getY()
			+ (this.diameter * CHUNK_SIZE * 3) / 2 + 1.5f,
		this.planetNode.getWorldTranslation().getZ() + 1.5f);
    }
    
    private void rotate()
    {
	this.previousPlanetRotation = planetNode.getWorldRotation().clone();
	this.previousPlanetTranslation = planetNode.getWorldTranslation()
		.clone();
	this.previousStarNodeRotation = starNode.getWorldRotation().clone();
	starNode.rotate(this.amountRevolution.getX() * FastMath.DEG_TO_RAD,
		this.amountRevolution.getY() * FastMath.DEG_TO_RAD,
		this.amountRevolution.getZ() * FastMath.DEG_TO_RAD);
	planetNode.rotate(this.amountRotation.getX() * FastMath.DEG_TO_RAD,
		this.amountRotation.getY() * FastMath.DEG_TO_RAD,
		this.amountRotation.getZ() * FastMath.DEG_TO_RAD);
    }
    
    public Vector3f getUpVector()
    {
	float x = this.planetNode.getLocalRotation().getX();
	float y = this.planetNode.getLocalRotation().getY();
	float z = this.planetNode.getLocalRotation().getZ();
	float w = this.planetNode.getLocalRotation().getW();
	
	return new Vector3f(2 * (x * y - w * z), 1 - 2 * (x * x + z * z),
		2 * (y * z + w * x));
    }
    
    public Vector3f getForwardVector()
    {
	float x = this.planetNode.getLocalRotation().getX();
	float y = this.planetNode.getLocalRotation().getY();
	float z = this.planetNode.getLocalRotation().getZ();
	float w = this.planetNode.getLocalRotation().getW();
	
	return new Vector3f(2 * (x * z + w * y), 2 * (y * x - w * x),
		1 - 2 * (x * x + y * y));
    }
    
    public Vector3f getLeftVector()
    {
	float x = this.planetNode.getLocalRotation().getX();
	float y = this.planetNode.getLocalRotation().getY();
	float z = this.planetNode.getLocalRotation().getZ();
	float w = this.planetNode.getLocalRotation().getW();
	
	return new Vector3f((1 - 2 * (y * y + z * z)), 2 * (x * y + w * z),
		2 * (x * z - w * y));
    }
    
    public Vector3f getCurrentPlanetTranslation()
    {
	return planetNode.getWorldTranslation();
    }
    
    public Vector3f getCurrentTerrainTranslation()
    {
	return terrainNode.getWorldTranslation();
    }
    
    public Vector3f getOriginalTerrainTranslation()
    {
	return this.originalTerrainTranslation;
    }
    
    public Vector3f getOriginalPlanetTranslation()
    {
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
    
    public Spatial getStarNode()
    {
	return starNode;
    }
    
    public Quaternion getPreviousStarNodeRotation()
    {
	return this.previousStarNodeRotation;
    }
    
    public Quaternion getCurrentPlanetRotation()
    {
	return this.planetNode.getWorldRotation();
    }
    
    public Quaternion getCurrentStarNodeRotation()
    {
	return this.starNode.getWorldRotation();
    }
    
    public PlanetType getType()
    {
	if(this.type == null)
	    return null;
	return this.type;
    }
    
    public long getSeed()
    {
	return this.seed;
    }
}
