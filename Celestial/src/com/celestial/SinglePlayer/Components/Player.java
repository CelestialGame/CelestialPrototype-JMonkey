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
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

@SuppressWarnings("deprecation")
public class Player extends BetterCharacterControl{

	private CelestialPortal portal;
	private Galaxy galaxy;
	private Sector sector;
	private SolarSystem system;
	private Node node;
	private Camera cam;
	private Planet planet;

	public Player(CelestialPortal portal)
	{
		super(1.5f, 6f, 1f);	
		this.portal = portal;
		//setCollisionGroup(COLLISION_GROUP_01);
		this.node = new Node("Player");
		this.node.addControl(this);
		portal.getRootNode().attachChild(node);
		portal.getBulletAppState().getPhysicsSpace().add(this);
		this.cam = portal.cam;
	}
	
	public void setLocation(Vector3f location)
	{
		this.warp(location);
	}
	
	public Vector3f getLocation()
	{
		return this.node.getWorldTranslation();
	}
	
	public void spawnPlayer(Planet planet, int face)
	{
		if(PlayerEvents.PlayerMoveEvent(this, node.getWorldTranslation(), planet.getSpawnLocation(face)))
		{
			this.setLocation(planet.getSpawnLocation(face));
			this.rotatePlayer(face);
		}
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
	
	public Node getNode()
	{
		return node;
	}
	
	public void rotatePlayer(int face) {
		Quaternion q = new Quaternion();
		int x=0,y=0,z=0;
		if(face == Planet.TOP) {
			x=0;y=0;z=0;
		} else if (face == Planet.BOTTOM) {
			x=0;y=180;z=0;
		} else if (face == Planet.NORTH) {
			x=0;y=0;z=90;
		} else if (face == Planet.SOUTH) {
			x=0;y=0;z=270;
		} else if (face == Planet.EAST) {
			x=90;y=0;z=0;
		} else if (face == Planet.WEST) {
			x=270;y=0;z=0;
		} else {
			return;
		}
		q.fromAngles(x*FastMath.DEG_TO_RAD, y*FastMath.DEG_TO_RAD, z*FastMath.DEG_TO_RAD);
		this.node.setLocalRotation(q);
	}

	public Planet getClosestPlanet()
	{
		float closestdist = -1;
		Planet closestplanet = null;
		for(Planet planet : getSystem().getPlanets()) {
			float distance = node.getWorldTranslation().distance(planet.getPlanetNode().getWorldTranslation());
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
			return node.getWorldTranslation().distance(planet.getPlanetNode().getWorldTranslation());
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
		Vector3f P1 = planet.getPlanetNode().getWorldTranslation();

		Vector3f playerP = null;
		if(portal.getPhysics().isEnabled())
			playerP = this.node.getLocalTranslation();
		else
			playerP = portal.getCam().getLocation();

		Vector3f transP = playerP.subtract(P1);
		Vector3f rotP = planet.getPlanetNode().getLocalRotation().inverse().mult(transP);

		float x,y,z;

		x = rotP.x;
		y = rotP.y;
		z = rotP.z;

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
}
