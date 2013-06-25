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
		return null;
	}
	
	public int getCurrentFaceOfPlanet(Planet planet)
	{
		for(int i = 0; i<=5; i++)
		{
			System.out.println("------------\nFace: "+i);
			PlanetCorner[] corners = planet.getCornersForFace(i);
			
			Vector3f P1 = planet.getPlanetNode().getWorldTranslation();
			Vector3f P2 = corners[0].getWorldTranslation();
			Vector3f P3 = corners[1].getWorldTranslation();
			Vector3f P4 = corners[2].getWorldTranslation();
			Vector3f P5 = corners[3].getWorldTranslation();
			
			Vector3f playerP = null;
			if(portal.getPhysics().isEnabled())
				playerP = this.getPhysicsLocation();
			else
				playerP = portal.getCam().getLocation();
			
			Vector3f s1a = P2.subtract(P1);
			Vector3f s1b = P3.subtract(P1);
			Vector3f s1c = s1a.cross(s1b);
			Vector3f s1n = s1c.normalize();
			float s1d = playerP.dot(s1n);
			
			Vector3f s2a = P3.subtract(P1);
			Vector3f s2b = P4.subtract(P1);
			Vector3f s2c = s2a.cross(s2b);
			Vector3f s2n = s2c.normalize();
			float s2d = playerP.dot(s2n);
			
			Vector3f s3a = P4.subtract(P1);
			Vector3f s3b = P5.subtract(P1);
			Vector3f s3c = s3a.cross(s3b);
			Vector3f s3n = s3c.normalize();
			float s3d = playerP.dot(s3n);
			
			Vector3f s4a = P5.subtract(P1);
			Vector3f s4b = P2.subtract(P1);
			Vector3f s4c = s4a.cross(s4b);
			Vector3f s4n = s4c.normalize();
			float s4d = playerP.dot(s4n);
			
			System.out.println("1: "+s1d+" 2: "+s2d+" 3: "+s3d+" 4: "+s4d);

			/*if(s1d > 0 && s2d > 0 && s3d < 0 && s4d < 0)
			{
				return i;
			}*/
			
		}
		return -1;
	}
}
