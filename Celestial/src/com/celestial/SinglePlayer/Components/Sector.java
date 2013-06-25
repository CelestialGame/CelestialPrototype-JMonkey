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


public class Sector {
	private Galaxy galaxy;
	private int amountOfSystems;
	private Map<Vector3f,SolarSystem> systemMap;
	
	//Temporary...
	public List<SolarSystem> systemList;
	
	public Sector(Galaxy galaxy, int amountOfSystems)
	{
		this.galaxy = galaxy;
		this.amountOfSystems = amountOfSystems;
		
		this.generate();
	}

	private void generate() 
	{
		this.systemList = new ArrayList<SolarSystem>();
		
		this.systemMap = new HashMap<Vector3f,SolarSystem>();
		
		for(int i = 0; i < this.amountOfSystems; i++)
		{
			//TODO: Sector size, system location
			SolarSystem system = new SolarSystem(this);
			float randomx = (float) (Math.random() * 100 + 1);
			float randomy = (float) (Math.random() * 100 + 1);
			float randomz = (float) (Math.random() * 100 + 1);
			Vector3f location = new Vector3f(randomx, randomy, randomz);
			
			while(this.systemMap.get(location) != null)
			{
				randomx = (float) (Math.random() * 100 + 1);
				randomy = (float) (Math.random() * 100 + 1);
				randomz = (float) (Math.random() * 100 + 1);
				location = new Vector3f(randomx, randomy, randomz);
			}
			
			this.systemMap.put(location, system);
			this.systemList.add(system);
		}
	}
	
	public SolarSystem getSystemClosestTo(Vector3f location)
	{
		//TODO
		return null;
	}
	
	public SolarSystem getSystem(int index)
	{
		if(this.systemList.get(index) != null)
			return this.systemList.get(index);
		else
		{
			System.out.println("System Null!");
			return null;
		}
	}
}
