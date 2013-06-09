package com.celestial.SinglePlayer.Inventory;

import com.celestial.Celestial;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;

/**
 * Inventory Drop Items
 *
 * @author kevint.
 *         Created Jun 7, 2013.
 */
public class InventoryDrop{
	
	Box itemdropshape;
	Geometry itemdrop;
	
	InventoryItem item;
	private RigidBodyControl rigidBodyControl;
	
	public InventoryDrop(InventoryItem item, Vector3f location) {
		/** Use this if there is light present **/
		/*Material mat = new Material(Celestial.self.getAssetManager(),  // Create new material and...
			    "Common/MatDefs/Light/Lighting.j3md");
		mat.setColor("Diffuse", new ColorRGBA(0,0,0,0.3f));
		mat.setColor("Ambient", new ColorRGBA(0,0,0,0.3f));
		mat.setTexture("DiffuseMap", Celestial.self.getAssetManager().loadTexture(item.getIcon()));
		mat.setBoolean("UseAlpha",true);*/
		
		/** if theres no light, use this **/
		Material mat = new Material(Celestial.portal.getAssetManager(),  // Create new material and...
				"Common/MatDefs/Misc/Unshaded.j3md");
		//mat.setColor("Color", new ColorRGBA(0,0,0,1f));
		mat.setTexture("ColorMap", Celestial.portal.getAssetManager().loadTexture(item.getIcon()));
		
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		this.itemdropshape = new Box(0.3f,0.3f,0.3f);
		this.itemdrop = new Geometry("DropItem", this.itemdropshape);
		this.itemdrop.setMaterial(mat);
		this.itemdrop.getMesh().scaleTextureCoordinates(new Vector2f(0.6f,0.6f));
		
		this.itemdrop.setLocalTranslation(location);
		
		this.itemdrop.setQueueBucket(Bucket.Translucent); 
		
		this.rigidBodyControl = new RigidBodyControl();
		this.rigidBodyControl.removeCollideWithGroup(Celestial.portal.player.getCollisionGroup());
        this.itemdrop.addControl(rigidBodyControl);
        Celestial.portal.player.removeCollideWithGroup(this.rigidBodyControl.getCollisionGroup());
        Celestial.portal.getPhysics().getPhysicsSpace().add(this.itemdrop);
        
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
	public RigidBodyControl getCollisionBox() {
		return this.rigidBodyControl;
	}
	
}
