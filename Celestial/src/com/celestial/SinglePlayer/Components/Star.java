/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.SPPortal;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowFilter;
import com.jme3.shadow.PointLightShadowRenderer;

public class Star
{
    private SolarSystem system;
    private Vector3f location;
    private Node StarNode;
    private PointLight light;
    private Geometry StarGeometry;
    private CelestialPortal portal;
    
    /**
     * Generates a star at given location
     * 
     * @param solarSystem
     * @param location
     */
    public Star(SolarSystem system, Vector3f location)
    {
	this.system = system;
	this.location = location;
	
	this.generateStar();
    }
    
    /**
     * Generates a star at 0,0,0
     * 
     * @param solarSystem
     */
    public Star(SolarSystem system)
    {
	this.system = system;
	this.location = new Vector3f(0, 0, 0);
	this.portal = system.getSector().getGalaxy().getPortal();
	
	this.generateStar();
    }
    
    private void generateStar()
    {
	StarNode = new Node();
	
	Box starbox = new Box(500, 500, 500);
	
	this.StarGeometry = new Geometry("Star", starbox);
	Material mat = new Material(portal.getAssetManager(), // Create new
							      // material and...
		"Common/MatDefs/Misc/Unshaded.j3md");
	mat.setColor("Color", ColorRGBA.White);
	// mat.setColor("Diffuse", new ColorRGBA(247f, 214f, 81f, 0f));
	// mat.setColor("Ambient", new ColorRGBA(247f, 214f, 81f, 0f));
	// mat.setColor("Specular", new ColorRGBA(247f, 214f, 81f, 0f));
	mat.setColor("GlowColor", new ColorRGBA(247f, 214f, 81f, 0f));
	// mat.setFloat("Shininess", 5f);
	// mat.setBoolean("UseMaterialColors",true);
	this.StarGeometry.setMaterial(mat);
	this.StarNode.attachChild(this.StarGeometry);
	portal.getRootNode().attachChild(StarNode);
	
	this.StarNode.setQueueBucket(Bucket.Opaque);
	
	this.StarNode.move(location);
    }
    
    public Node getStarNode()
    {
	return StarNode;
    }
    
    /*
     * public LightMap getLightMap() { return lightMap; }
     */
    public PointLight getLight()
    {
	return this.light;
    }
    
    public Vector3f getLocation()
    {
	return location;
    }
    
    public SolarSystem getSolarSystem()
    {
	return system;
    }
}
