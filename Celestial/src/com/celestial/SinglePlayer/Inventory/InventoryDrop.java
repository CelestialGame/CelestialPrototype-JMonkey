package com.celestial.SinglePlayer.Inventory;

import com.celestial.Celestial;
import com.celestial.SinglePlayer.SPPortal;
import com.celestial.SinglePlayer.Components.Planet;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * Inventory Drop Items
 *
 * @author kevint.
 *         Created Jun 7, 2013.
 */
public class InventoryDrop{
	
	private Box itemdropshape;
	private Geometry itemdrop;
	
	private InventoryItem item;
	private RigidBodyControl rigidBodyControl;
	
	private Node itemdropnode;
	
	private Planet planet;
	
	public InventoryDrop(InventoryItem item, Vector3f location, Planet planet) {
		Material mat = new Material(Celestial.portal.getAssetManager(),  // Create new material and...
			    "Common/MatDefs/Light/Lighting.j3md");
		mat.setColor("Diffuse", new ColorRGBA(0.5f,0.5f,0.5f,0.3f));
		mat.setColor("Ambient", new ColorRGBA(0.5f,0.5f,0.5f,0.3f));
		mat.setTexture("DiffuseMap", Celestial.portal.getAssetManager().loadTexture(item.getIcon()));
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		this.itemdropshape = new Box(0.3f,0.3f,0.3f);
		this.itemdrop = new Geometry("DropItem", this.itemdropshape);
		this.itemdrop.setMaterial(mat);
		this.itemdrop.getMesh().scaleTextureCoordinates(new Vector2f(1f,1f));
		
		this.itemdrop.setLocalTranslation(location);
		
		this.itemdrop.setQueueBucket(Bucket.Transparent);
		this.itemdrop.setShadowMode(ShadowMode.Cast);
		
		this.rigidBodyControl = new RigidBodyControl();
		this.rigidBodyControl.setMass(1);
		//this.rigidBodyControl.removeCollideWithGroup(Celestial.portal.player.getCollisionGroup());
        this.itemdrop.addControl(rigidBodyControl);
        //Celestial.portal.player.removeCollideWithGroup(this.rigidBodyControl.getCollisionGroup());
        Celestial.portal.getPhysics().getPhysicsSpace().add(this.itemdrop);
        
        this.itemdropnode = new Node();
        this.itemdropnode.attachChild(itemdrop);
        
        this.planet = planet;
        
        this.item = item;
	}
	
	public Box getShape() {
		return this.itemdropshape;
	}
	
	public Geometry getGeometry() {
		return this.itemdrop;
	}
	
	public InventoryItem getItem() {
		return this.item;
	}
	public Node getNode() {
		return this.itemdropnode;
	}
	public RigidBodyControl getCollisionBox() {
		return this.rigidBodyControl;
	}
	public InventorySlot getSlot(InventoryManager invmanager) {
		return new InventorySlot(getItem(), 1, invmanager);
	}
	public Planet getPlanet() {
		return this.planet;
	}
	
	public int getCurrentFaceOfPlanet(Planet planet)
	{		
		Vector3f P1 = planet.getOriginalPlanetTranslation();

		Vector3f itemP = null;
		if(SPPortal.self.getPhysics().isEnabled())
			itemP = this.itemdrop.getWorldTranslation();
		else
			return -1;

		Vector3f rot1P = planet.getStarNode().getLocalRotation().inverse().mult(itemP);
		Vector3f transP = rot1P.subtract(P1);
		Vector3f rot2P = planet.getPlanetNode().getLocalRotation().inverse().mult(transP);

		float x = rot2P.x;
		float y = rot2P.y;
		float z = rot2P.z;

		if( Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z) ) {
			if( y < 0 ) {
				//System.out.println("Bottom");
				return Planet.BOTTOM;
			} else {
				//System.out.println("Top");
				return Planet.TOP;
			}
		} else if( Math.abs(x) > Math.abs(z) ) {
			if( x < 0 ) {
				//System.out.println("West");
				return Planet.WEST;
			} else {
				//System.out.println("East");
				return Planet.EAST;
			}
		} else if( Math.abs(z) > Math.abs(x) ) {
			if( z < 0 ) {
				//System.out.println("North");
				return Planet.NORTH;
			} else {
				//System.out.println("South");
				return Planet.SOUTH;
			}
		} else {
			return -1;
		}
	}
	
}
