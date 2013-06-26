/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components;

import com.celestial.CelestialPortal;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

@SuppressWarnings("deprecation")
public class Player extends CharacterControl{

	private CapsuleCollisionShape capsuleShape;
	private CelestialPortal portal;
	private Galaxy galaxy;
	private Sector sector;
	private SolarSystem system;

	public Player(CelestialPortal portal, CapsuleCollisionShape capsuleShape)
	{
		super(capsuleShape, 0.05f);	
		this.capsuleShape = capsuleShape;
		this.portal = portal;
		setJumpSpeed(20);
		setFallSpeed(30);
		setGravity(50);
		setPhysicsLocation(portal.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getSpawnLocation());
		//setCollisionGroup(COLLISION_GROUP_01);
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

	public Planet getClosestPlanet()
	{
		for(Planet planet : getSystem().getPlanets()) {
			float distance = this.getPhysicsLocation().distance(planet.getPlanetNode().getWorldTranslation());

			float factor = (((planet.centerofdiam*16)-8)*3)*7;
			if(distance <= factor) {
				return planet;
			}
		}
		return null;
	}

	public int getCurrentFaceOfPlanet(Planet planet)
	{		
		Vector3f P1 = planet.getPlanetNode().getWorldTranslation();

		Vector3f playerP = null;
		if(portal.getPhysics().isEnabled())
			playerP = this.getPhysicsLocation();
		else
			playerP = portal.getCam().getLocation();

		Vector3f transP = playerP.subtract(P1);
		Vector3f rotP = planet.getPlanetNode().getLocalRotation().inverse().mult(transP);

		float x,y,z;

		x = rotP.x;
		y = rotP.y;
		z = rotP.z;

		if( Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z) ) {
			// on top or bottom, ie: it’s farther up then it is to either side.
			// at x == y or z == y you’d be at the 1:1 slope point (45 degrees)
			if( y < 0 ) {
				return Planet.BOTTOM;
			} else {
				return Planet.TOP;
			}
		} else if( Math.abs(x) > Math.abs(z) ) {
			if( x < 0 ) {
				return Planet.WEST;
			} else {
				return Planet.EAST;
			}
		} else if( Math.abs(z) > Math.abs(x) ) {
			if( z < 0 ) {
				return Planet.NORTH;
			} else {
				return Planet.SOUTH;
			}
		} else {
			return -1;
		}
	}
}
