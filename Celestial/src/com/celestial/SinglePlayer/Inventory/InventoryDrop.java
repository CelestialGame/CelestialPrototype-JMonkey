package com.celestial.SinglePlayer.Inventory;

import com.celestial.Celestial;
import com.celestial.SinglePlayer.SPPortal;
import com.cubes.BlockManager;
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
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture.WrapMode;

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
	
	public InventoryDrop(InventoryItem item, Vector3f location) {
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
	
}
