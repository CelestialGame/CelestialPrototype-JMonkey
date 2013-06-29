/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Events.PlayerEvents;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

@SuppressWarnings("deprecation")
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

	public Player(CelestialPortal portal, String name)
	{
		super(1.2f, 5.8f, 1f);	
		this.portal = portal;
		portal.getBulletAppState().getPhysicsSpace().add(this);
		playerSpatial = portal.getAssetManager().loadModel("assets/models/player/simpleplayer.mesh.xml");
		//playerSpatial.scale(0.0275f);
		playerSpatial.scale(0.5f, 1.5f, 1);
		playerSpatial.scale(0.5f);
		playerNode = (Node) playerSpatial;
		playerNode.addControl(this);
		this.setJumpForce(new Vector3f(0, 8f, 0));
		this.setPhysicsDamping(0.9f);
		portal.getRootNode().attachChild(playerNode);
		this.cam = portal.cam;
		this.name = name;
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
		this.warp(location);
	}

	public Vector3f getLocation()
	{
		return spatial.getWorldTranslation();
	}

	public void spawnPlayer(Planet planet, int face)
	{
		this.setLocation(planet.getSpawnLocation(face));
		this.rotatePlayer(face);
		PlayerEvents.PlayerMoveEvent(this, planet.getSpawnLocation(face));
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
		//do box check thing... This will require more than just a check of distance, 
		//I'ma have to use normals and crap. Leave this here till I get the time to do it
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
		if(portal.getPhysics().isEnabled())
			playerP = getLocation();
		else
			playerP = portal.getCam().getLocation();

		Vector3f rot1P = planet.getStarNode().getLocalRotation().inverse().mult(playerP);
		Vector3f transP = rot1P.subtract(P1);
		Vector3f rot2P = planet.getPlanetNode().getLocalRotation().inverse().mult(transP);

		float x = rot2P.x;
		float y = rot2P.y;
		float z = rot2P.z;

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
}
