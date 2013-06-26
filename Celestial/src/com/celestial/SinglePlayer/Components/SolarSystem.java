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
	private String name;
	private Map<Vector3f,Planet> planetMap;
	private List<Planet> planetList;

	public SolarSystem(Sector sector, String name)
	{
		this.sector = sector;
		this.name = name;
		this.generate();
	}

	private void generate() {
		this.planetList = new ArrayList<Planet>();
		this.planetMap = new HashMap<Vector3f,Planet>();
		
		this.star = new Star(this);
		
		Planet p = new Planet(star, 1, new Vector3f(1000, -2000, 3200), name);
		
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
	
	public List<Planet> getPlanets() {
		return this.planetList;
	}
	
	public String getName() {
		return name;
	}
}
