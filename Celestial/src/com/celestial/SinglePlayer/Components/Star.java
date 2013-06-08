/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.SinglePlayer.Components;

import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


public class Star {
	private Object solarSystem;
	private Vector3f location;
	private Node StarNode;
	private PointLight light;

	public Star(SolarSystem solarSystem, Vector3f location)
	{
		this.solarSystem = solarSystem;
		this.location = location;
	
		this.generateStar();
	}
	
	private void generateStar()
	{
		StarNode = new Node();
		light = new PointLight();
		light.setPosition(new Vector3f(0,0,0));
		
		this.StarNode.addLight(light);
		this.StarNode.move(location);
	}
	
	public Node getStarNode()
	{
		return StarNode;
	}
	
	public PointLight getLight()
	{
		return light;
	}
	
	public Vector3f getLocation()
	{
		return location;
	}
}
