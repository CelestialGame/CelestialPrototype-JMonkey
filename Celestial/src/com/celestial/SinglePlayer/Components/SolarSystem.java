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
		
		Planet p = new Planet(star, 3, new Vector3f(1000, 0, 3500), name + " III");
		/*Planet p1 = new Planet(star, 1, new Vector3f(1000, 0, 1000), name + " I");
		Planet p2 = new Planet(star, 1, new Vector3f(1000, 0, 2500), name + " II");
		Planet p3 = new Planet(star, 1, new Vector3f(1000, 0, 5000), name + " IV");*/
		
		this.planetMap.put(p.getOriginalPlanetTranslation(), p);
		/*this.planetMap.put(p1.getOriginalPlanetTranslation(), p1);
		this.planetMap.put(p2.getOriginalPlanetTranslation(), p2);
		this.planetMap.put(p2.getOriginalPlanetTranslation(), p3);*/
	
		this.planetList.add(p);
		/*this.planetList.add(p1);
		this.planetList.add(p2);
		this.planetList.add(p3);*/
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
