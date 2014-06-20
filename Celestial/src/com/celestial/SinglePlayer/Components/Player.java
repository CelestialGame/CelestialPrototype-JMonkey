/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.SPPortal;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

public class Player extends BetterCharacterControl{

	private CelestialPortal portal;
	private Galaxy galaxy;
	private Sector sector;
	private SolarSystem system;
	private Camera cam;
	private Planet planet;
	private Spatial playerSpatial;
	private Node playerNode;
	private String name;
	private Vector3f translationUpdate = null;
	private BulletAppState bulletAppState;

	public Player(CelestialPortal portal, String name)
	{
		super(1.2f, 5.8f, 1f);	
		this.portal = portal;
		playerSpatial = portal.getAssetManager().loadModel("assets/models/player/simpleplayer.mesh.xml");
		playerSpatial.scale(0.5f, 1.5f, 1);
		playerSpatial.scale(0.5f);
		playerNode = (Node) playerSpatial;
		playerNode.addControl(this);
		this.setJumpForce(new Vector3f(0, 15f, 0));
		this.setPhysicsDamping(1f);
		portal.getRootNode().attachChild(playerNode);
		this.cam = portal.cam;
		this.name = name;
		this.translationUpdate = null;
		this.setApplyPhysicsLocal(true);
		this.setBulletAppState(portal.galaxy.getSpace().getBulletAppState());
	}

	public void setVisibleToClient(boolean visible)
	{
		if(visible)
			this.playerSpatial.setCullHint(CullHint.Never);
		else
			this.playerSpatial.setCullHint(CullHint.Always);
	}

	public void setLocation(Vector3f location)
	{
		this.setPhysicsLocation(location);
	}

	public Vector3f getLocation()
	{
		return this.location;
	}

	public Quaternion getRotation()
	{
		return this.rotation;
	}

	public Vector3f getLocalUp()
	{
		return this.localUp;
	}

	public Vector3f getLocalLeft()
	{
		return this.localLeft;
	}

	public Vector3f getLocalForward()
	{
		return this.localForward;
	}

	@Override 
	public void update(float tpf)
	{
		super.update(tpf);

		if(getPlanet() != null)
		{

			Vector3f translation = getLocation();
			

			if(this.translationUpdate != null)
			{
				translation.addLocal(translationUpdate);	
				this.translationUpdate = null;
			}
			translation.addLocal(walkDirection);
			
			this.setLocation(translation);
		}
	}

	@Override 
	public void physicsTick(PhysicsSpace space, float tpf)
	{
		super.physicsTick(space, tpf);
	}

	public void spawnPlayer(Planet planet, int face)
	{
		this.setLocation(planet.getSpawnLocation(face));
	}

	public Vector3f getSpawnLocation(Planet planet, int face)
	{
		return planet.getSpawnLocation(face);
	}

	public void setGalaxy(Galaxy galaxy)
	{
		this.galaxy = galaxy;
	}

	public void setSector(Sector sector)
	{
		this.sector = sector;
	}

	public void setSystem(SolarSystem system)
	{
		this.system = system;
	}

	public void setPlanet(Planet planet)
	{
		if(planet != null)
		{
			this.setBulletAppState(planet.getBulletAppState());
			planet.getPlanetNode().attachChild(this.getNode());
			planet.getPlanetNode().attachChild(this.playerSpatial);
		}
		else
		{
			if(this.planet != null)
			{
				this.setBulletAppState(portal.galaxy.getSpace().getBulletAppState());
			}
		}
		this.planet = planet;
	}

	public Galaxy getGalaxy()
	{
		return galaxy;
	}

	public Sector getSector()
	{
		return sector;
	}

	public SolarSystem getSystem()
	{
		return system;
	}

	public Planet getPlanet()
	{
		return planet;
	}

	/**
	 * Used if we ever switch back from BetterCharacterControl. Currently this is handled by BetterCharacterControl.
	 * @param face
	 */
	public void rotatePlayer(int face) {
		Quaternion q = new Quaternion();
		int x=0,y=0,z=0;
		if(face == Planet.TOP) {
			x=0;y=0;z=0;
			this.spatial.rotateUpTo(new Vector3f(0, 1, 0));
		} else if (face == Planet.BOTTOM) {
			x=0;y=180;z=0;
			this.spatial.rotateUpTo(new Vector3f(0, -1, 0));
		} else if (face == Planet.NORTH) {
			x=0;y=0;z=90;
			this.spatial.rotateUpTo(new Vector3f(0, 0, 1));
		} else if (face == Planet.SOUTH) {
			x=0;y=0;z=270;
			this.spatial.rotateUpTo(new Vector3f(0, 0, -1));
		} else if (face == Planet.EAST) {
			x=90;y=0;z=0;
			this.spatial.rotateUpTo(new Vector3f(1, 0, 0));
		} else if (face == Planet.WEST) {
			x=270;y=0;z=0;
			this.spatial.rotateUpTo(new Vector3f(-1, 0, 0));
		} else {
			return;
		}
		q.fromAngles(x*FastMath.DEG_TO_RAD, y*FastMath.DEG_TO_RAD, z*FastMath.DEG_TO_RAD);
		this.spatial.setLocalRotation(q);
	}

	public Planet getClosestPlanet()
	{
		float closestdist = -1;
		Planet closestplanet = null;
		for(Planet planet : getSystem().getPlanets()) {
			float distance = getLocation().distance(planet.getPlanetNode().getWorldTranslation());
			if(distance < closestdist || closestdist == -1)
			{
				closestdist = distance;
				closestplanet = planet;
			}
		}
		return closestplanet;
	}

	public float getDistanceFromPlanet(Planet planet)
	{
		if(planet != null)
		{
			return getLocation().distance(planet.getPlanetNode().getWorldTranslation());
		}
		return -1;
	}

	public float getDistanceFromPlanet(Planet planet, Vector3f location)
	{
		if(planet != null)
		{
			return location.distance(planet.getPlanetNode().getWorldTranslation());
		}
		return -1;
	}

	public boolean isInAtmosphere(Planet planet)
	{
		Vector3f difference = this.getLocation().subtract(planet.getCurrentPlanetTranslation());
		if(
				difference.getX() > -1 * (planet.getAtmosphereDiameter()/2) 
				&& difference.getX() < (planet.getAtmosphereDiameter()/2)
				&& difference.getY() > -1 * (planet.getAtmosphereDiameter()/2) 
				&& difference.getY() < (planet.getAtmosphereDiameter()/2)
				&& difference.getZ() > -1 * (planet.getAtmosphereDiameter()/2) 
				&& difference.getZ() < (planet.getAtmosphereDiameter()/2)
				)
			return true;
		else
			return false;
	}

	public boolean isWithinLoadingDistance(Planet planet)
	{
		if(this.getDistanceFromPlanet(planet)
				<= planet.getRadiusAsFloat(true)+200)
			return true;
		return false;
	}

	public boolean isWithinLoadingDistance(Planet planet, Vector3f location)
	{
		if(this.getDistanceFromPlanet(planet, location)
				<= planet.getRadiusAsFloat(true)+200)
			return true;
		return false;
	}

	public int getCurrentFaceOfPlanet(Planet planet)
	{		
		Vector3f P1 = planet.getOriginalPlanetTranslation();

		Vector3f playerP = null;
		if(this.getBulletAppState().isEnabled())
			playerP = getLocation();
		else
			playerP = portal.getCam().getLocation();

		Vector3f rot1P = planet.getStarNode().getLocalRotation().inverse().mult(playerP);
		Vector3f transP = rot1P.subtract(P1);
		Vector3f rot2P = planet.getPlanetNode().getLocalRotation().inverse().mult(transP);

		float x = rot2P.x;
		float y = rot2P.y;
		float z = rot2P.z;
		//System.out.println("x "+x+" y "+y+" z "+z);

		if( Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z) ) {
			if( y < 0 ) {
				//System.out.println("Bottom");
				return Planet.BOTTOM;
			} else {
				//System.out.println("Top");
				return Planet.TOP;
			}
		} else if( Math.abs(x) > Math.abs(z) ) {
			if( x < 0 ) {
				//System.out.println("West");
				return Planet.WEST;
			} else {
				//System.out.println("East");
				return Planet.EAST;
			}
		} else if( Math.abs(z) > Math.abs(x) ) {
			if( z < 0 ) {
				//System.out.println("North");
				return Planet.NORTH;
			} else {
				//System.out.println("South");
				return Planet.SOUTH;
			}
		} else {
			return -1;
		}
	}

	public boolean isInSpace()
	{
		return this.getPlanet() == null;
	}

	public CelestialPortal getPortal() {
		return portal;
	}

	public PhysicsRigidBody getCollisionBox() {
		return this.rigidBody;
	}

	public Node getNode() {
		return this.playerNode;
	}

	public Spatial getSpatial() {
		return this.playerSpatial;
	}

	public String getName() {
		return this.name;
	}

	public BulletAppState getBulletAppState()
	{
		return this.bulletAppState;
	}
	
	public void setBulletAppState(BulletAppState state)
	{
		if(this.bulletAppState != null)
		{
			this.bulletAppState.getPhysicsSpace().remove(this);
		}
		this.bulletAppState = state;
		this.bulletAppState.getPhysicsSpace().add(this);
	}
	
	public void setTranslationUpdate(Vector3f translation) {
		this.translationUpdate  = translation;
	}
}
