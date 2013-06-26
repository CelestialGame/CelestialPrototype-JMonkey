/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components;

import com.celestial.CelestialPortal;
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

	public Player(CelestialPortal portal)
	{
		super(1.5f, 6f, 1f);	
		this.portal = portal;
		//setCollisionGroup(COLLISION_GROUP_01);
		this.node = new Node("Player");
		this.node.addControl(this);
		this.cam = portal.cam;
		this.node.setLocalTranslation(portal.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getSpawnLocation());
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
	public Node getNode()
	{
		return node;
	}
	@Deprecated
	public void rotatePlayer(float xAngle, float yAngle, float zAngle) {
		//this.cam.setRotation();
		this.node.rotate(xAngle, yAngle, zAngle);
	}
	
	public void rotatePlayer(int face) {
		if(face == Planet.TOP) {
			this.node.rotate(0, 0, 0);
		} else if (face == Planet.BOTTOM) {
			this.node.rotate(0, 180*FastMath.DEG_TO_RAD, 0);
		} else if (face == Planet.NORTH) {
			this.node.rotate(0, 0, 90*FastMath.DEG_TO_RAD);
		} else if (face == Planet.SOUTH) {
			this.node.rotate(0, 0, 270*FastMath.DEG_TO_RAD);
		} else if (face == Planet.EAST) {
			this.node.rotate(90*FastMath.DEG_TO_RAD,0,0);
		} else if (face == Planet.WEST) {
			this.node.rotate(270*FastMath.DEG_TO_RAD,0,0);
		} else {
			return;
		}
	}

	public Planet getClosestPlanet()
	{
		for(Planet planet : getSystem().getPlanets()) {
			float distance = node.getLocalTranslation().distance(planet.getPlanetNode().getWorldTranslation());

			float factor = (((planet.centerofdiam*16)-8)*3)*7;
			if(distance <= factor) {
				if(planet.getName().equals("null")) {
					return null;
				} else {
					return planet;
				}
			}
		}
		return null;
	}
	
	public Planet getClosestAtmosphere()
	{
		for(Planet planet : getSystem().getPlanets()) {
			float distance = node.getLocalTranslation().distance(planet.getPlanetNode().getWorldTranslation());

			float factor = planet.atmospheresizefactor;
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
			playerP = this.node.getLocalTranslation();
		else
			playerP = portal.getCam().getLocation();

		Vector3f transP = playerP.subtract(P1);
		Vector3f rotP = planet.getPlanetNode().getLocalRotation().inverse().mult(transP);

		//System.out.println("nonrotated: "+playerP+" rotated: "+rotP+"\nPlanet: "+P1);

		float x,y,z;

		x = rotP.x;
		y = rotP.y;
		z = rotP.z;

		if( Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z) ) {
			// on top or bottom, ie: it’s farther up then it is to either side.
			// at x == y or z == y you’d be at the 1:1 slope point (45 degrees)
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
