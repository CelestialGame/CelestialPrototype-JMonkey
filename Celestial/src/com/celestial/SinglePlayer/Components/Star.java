/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.SinglePlayer.Components;

import com.celestial.CelestialPortal;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;


public class Star {
	private SolarSystem system;
	private Vector3f location;
	private Node StarNode;
	private PointLight light;
	private Geometry StarGeometry;
	private CelestialPortal portal;
	
	public class LightMap {
		DirectionalLight top;
		DirectionalLight bottom;
		DirectionalLight front;
		DirectionalLight back;
		DirectionalLight left;
		DirectionalLight right;
		
		public LightMap() {
			top = new DirectionalLight();
			top.setDirection(new Vector3f(0f, 1f, 0f).normalizeLocal());
			top.setColor(ColorRGBA.White);
			
			bottom = new DirectionalLight();
			bottom.setDirection(new Vector3f(0f, -1f, 0f).normalizeLocal());
			bottom.setColor(ColorRGBA.White);
			
			front = new DirectionalLight();
			front.setDirection(new Vector3f(0f, 0f, 1f).normalizeLocal());
			front.setColor(ColorRGBA.White);
			
			back = new DirectionalLight();
			back.setDirection(new Vector3f(0f, 0f, -1f).normalizeLocal());
			back.setColor(ColorRGBA.White);
			
			left = new DirectionalLight();
			left.setDirection(new Vector3f(-1f, 0f, -0f).normalizeLocal());
			left.setColor(ColorRGBA.White);
			
			right = new DirectionalLight();
			right.setDirection(new Vector3f(1f, 0f, 0f).normalizeLocal());
			right.setColor(ColorRGBA.White);
		}
		
		public LightMap(DirectionalLight top, DirectionalLight bottom,
				DirectionalLight front, DirectionalLight back,
				DirectionalLight left, DirectionalLight right) {
			this.top = top;
			this.bottom = bottom;
			this.front = front;
			this.back = back;
			this.left = left;
			this.right = right;
		}
		
		public DirectionalLight[] getLights() {
			return new DirectionalLight[]{top,bottom,front,back,left,right};
		}
	}

	/**
	 * Generates a star at given location	
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
	 * @param solarSystem
	 */
	public Star(SolarSystem system)
	{
		this.system = system;
		this.location = new Vector3f(0,0,0);
		this.portal = system.getSector().getGalaxy().getPortal();
		
		this.generateStar();
	}
	
	private void generateStar()
	{
		StarNode = new Node();
		
		Box starbox = new Box(500,500,500);
		this.StarGeometry = new Geometry("Star", starbox);
		Material mat = new Material(portal.getAssetManager(),  // Create new material and...
			    "Common/MatDefs/Light/Lighting.j3md");
		mat.setColor("Diffuse", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("Ambient", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("Specular", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("GlowColor", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setFloat("Shininess", 5f);
		mat.setBoolean("UseMaterialColors",true);
		this.StarGeometry.setMaterial(mat);
		this.StarNode.attachChild(this.StarGeometry);
		portal.getRootNode().attachChild(StarNode);
		
		this.StarNode.setQueueBucket(Bucket.Opaque);
		
		/*this.lightMap = new LightMap();
		
		for(DirectionalLight light : this.lightMap.getLights()) {
			this.StarNode.addLight(light);
		}*/
		this.light = new PointLight();
		light.setPosition(location);
        light.setColor(ColorRGBA.White);
        light.setRadius(65000f);
		this.StarNode.addLight(light);
		
		this.StarNode.move(location);
	}
	
	public Node getStarNode()
	{
		return StarNode;
	}
	
	/*public LightMap getLightMap() {
		return lightMap;
	}*/
	public PointLight getLight() {
		return this.light;
	}
	
	public Vector3f getLocation()
	{
		return location;
	}

	public SolarSystem getSolarSystem() {
		return system;
	}
}
