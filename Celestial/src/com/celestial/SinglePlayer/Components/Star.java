/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.SinglePlayer.Components;

import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


public class Star {
	private Object solarSystem;
	private Vector3f location;
	private Node StarNode;
	private PointLight light;

	/**
	 * Generates a star at given location	
	 * @param solarSystem
	 * @param location
	 */
	public Star(SolarSystem solarSystem, Vector3f location)
	{
		this.solarSystem = solarSystem;
		this.location = location;
	
		this.generateStar();
	}
	
	/**
	 * Generates a star at 0,0,0
	 * @param solarSystem
	 */
	public Star(SolarSystem solarSystem)
	{
		this.solarSystem = solarSystem;
		this.location = new Vector3f(0,0,0);
		
		this.generateStar();
	}
	
	private void generateStar()
	{
		StarNode = new Node();
		light = new PointLight();
		light.setPosition(location);
		light.setColor(ColorRGBA.White);
		light.setRadius(50000f);
		
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
