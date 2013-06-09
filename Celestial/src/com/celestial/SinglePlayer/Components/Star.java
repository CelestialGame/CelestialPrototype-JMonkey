/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.SinglePlayer.Components;

import java.util.ArrayList;
import java.util.List;

import com.celestial.Celestial;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;


public class Star {
	private Object solarSystem;
	private Vector3f location;
	private Node StarNode;
	private PointLight light;
	private Geometry StarGeometry;

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
		
		Sphere starsphere = new Sphere(50,50,50);
		this.StarGeometry = new Geometry("Star", starsphere);
		Material mat = new Material(Celestial.portal.getAssetManager(),  // Create new material and...
			    "Common/MatDefs/Light/Lighting.j3md");
		mat.setColor("Diffuse", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("Ambient", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("Specular", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("GlowColor", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setFloat("Shininess", 5f);
		mat.setBoolean("UseMaterialColors",true);
		this.StarGeometry.setMaterial(mat);
		this.StarNode.attachChild(this.StarGeometry);
		
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
	
	public PointLight getLight() {
		return light;
	}
	
	public Vector3f getLocation()
	{
		return location;
	}
}
