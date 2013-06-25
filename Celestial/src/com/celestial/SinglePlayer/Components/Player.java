/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.SinglePlayer.Components;

import com.celestial.CelestialPortal;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;

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
		setJumpSpeed(20);
		setFallSpeed(30);
		setGravity(50);
		setPhysicsLocation(portal.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getSpawnLocation());
		setCollisionGroup(COLLISION_GROUP_01);
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
			float distance = this.getPhysicsLocation().distance(planet.getPlanetNode().getLocalTranslation());
			
			float factor = (((planet.centerofdiam*16)-8)*3)*7;
			if(distance <= factor) {
				return planet;
			}
		}
		return null;
	}
	
	public int getCurrentFaceOfPlanet(Planet planet)
	{
		return 0;
	}
}
