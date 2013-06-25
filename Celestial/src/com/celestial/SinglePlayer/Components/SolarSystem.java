/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.SinglePlayer.Components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.math.Vector3f;


public class SolarSystem {
	private Sector sector;
	private Star star;
	private Map<Vector3f,Planet> planetMap;
	private List<Planet> planetList;

	public SolarSystem(Sector sector)
	{
		this.sector = sector;
		
		this.generate();
	}

	private void generate() {
		this.planetList = new ArrayList<Planet>();
		this.planetMap = new HashMap<Vector3f,Planet>();
		
		this.star = new Star(this);
		
		Planet p = new Planet(star, 3, new Vector3f(100, 100, 100));
		
		this.planetMap.put(p.getWantedLocation(), p);
	
		this.planetList.add(p);
	}
	
	public Star getStar()
	{
		return star;
	}
	
	public Planet getPlanetClosestTo(Vector3f location)
	{
		//TODO
		return null;
	}
	
	public Planet getPlanet(int index)
	{
		return this.planetList.get(index);
	}

	public Sector getSector() {
		return sector;
	}
}
