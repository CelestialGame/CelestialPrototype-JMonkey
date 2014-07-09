/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.SinglePlayer.Components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Components.Planet.Planet;

public class Galaxy {

	private CelestialPortal portal;
	private Map<SectorCoord,Sector> sectorMap;
	private int diameter;
	private int depth;
	private int maxSystemsPerSector;
	private int centerOfDiameter;
	private int centerOfDepth;
	private Space space;

	/**
	 * Creates a galaxy. One galaxy per game may be used.
	 * 
	 * @param portal The portal being used
	 * @param diameter The diameter of the galaxy (in sectors) MUST be odd number
	 * @param depth The depth of the galaxy (in sectors) MUST be odd number
	 * @param maxSystemsPerSector The maximum amount of systems per sector
	 */
	public Galaxy(CelestialPortal portal, int diameter, int depth, int maxSystemsPerSector)
	{
		this.portal = portal;
		this.sectorMap = new HashMap<SectorCoord,Sector>();
		this.diameter = diameter;
		this.depth = depth;
		this.maxSystemsPerSector = maxSystemsPerSector;
		this.centerOfDiameter = (int)Math.ceil((float)diameter/2);
		this.centerOfDepth = (int)Math.ceil((float)depth/2);
		this.space = new Space(portal);
		
		if(diameter % 2 == 0)
		{
			System.err.println("Galaxy Diameter MUST be odd number!");
			return;
		}
		
		if(depth % 2 == 0)
		{
			System.err.println("Galaxy Depth MUST be odd number!");
			return;
		}

		this.generate();
	}
	
	private void generate() {
		for(int i = 0; i<diameter; i++)
		{
			for(int j = 0; j<diameter; j++)
			{
				for(int k = 0; k<depth; k++)
				{
					//TODO random name generation
					this.createSectorAt(i-(this.centerOfDiameter-1), j-(this.centerOfDiameter-1), k-(this.centerOfDepth-1), "Alpha");
				}
			}
		}
	}

	private void createSectorAt(int x, int y, int z, String name) 
	{
		this.createSectorAt(new SectorCoord(x,y,z), name);
	}
	
	private void createSectorAt(SectorCoord coord, String name)
	{
		if(this.sectorMap.get(coord) == null)
		{
			int amountOfSystems = 1 + (int)Math.random() * this.maxSystemsPerSector;
			Sector sector = new Sector(this, amountOfSystems, name);
			this.sectorMap.put(coord, sector);
		}
	}
	
	public Sector getSectorAt(int x, int y, int z)
	{
		return this.getSectorAt(new SectorCoord(x,y,z));
	}
	
	public Sector getSectorAt(SectorCoord coord)
	{
		return this.sectorMap.get(coord);
	}
	
	public ArrayList<Sector> getSectors()
	{
		return new ArrayList<Sector>(this.sectorMap.values());
	}
	
	public Planet getPlanet(SectorCoord coord, int index, int index2)
	{
		if(this.getSectorAt(coord) != null)
			return this.getSectorAt(coord).getSolarSystem(index).getPlanet(index2);
		else
		{
			return null;
		}
	}

	public CelestialPortal getPortal() {
		return portal;
	}
	
	public Space getSpace()
	{
		return space;
	}
	
}
