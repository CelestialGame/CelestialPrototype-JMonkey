/**
@author	Mitch Talmadge
Date Created:
	Jun 24, 2013
*/

package com.celestial.SinglePlayer.Components;

import java.util.ArrayList;
import java.util.List;

import com.jme3.scene.Node;

public class PlanetCorner extends Node{
	private List<Integer> sidelist;

	public PlanetCorner(int side1, int side2, int side3)
	{
		this.sidelist = new ArrayList<Integer>();
		this.sidelist.add(side1);
		this.sidelist.add(side2);
		this.sidelist.add(side3);
	}
	
	public List<Integer> getSides()
	{
		return sidelist;
	}
}
