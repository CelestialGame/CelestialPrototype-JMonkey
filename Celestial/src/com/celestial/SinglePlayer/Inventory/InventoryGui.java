package com.celestial.SinglePlayer.Inventory;

import java.util.List;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

/**
 * Inventory GUI stuffs
 *
 * @author kevint.
 *         Created Jun 6, 2013.
 */
public class InventoryGui {

	CelestialPortal parent;
	
	private Picture hotbarsel;
	Node hotbaricons = new Node();
	
	public InventoryGui(CelestialPortal parent) {
		this.parent = parent;
		
		initHotBar();
		
	}
	
	public void initHotBar() {
		Picture pic = new Picture("HotBar");
		pic.setImage(this.parent.getAssetManager(), "/assets/textures/inventory/hotbar.png", true);
		pic.setWidth(342);
		pic.setHeight(38);
		pic.setPosition(0, this.parent.getSettings().getHeight() - 63);
		this.parent.getGuiNode().attachChild(pic);
		
		this.hotbarsel = new Picture("HotBarSelection");
		this.hotbarsel.setImage(this.parent.getAssetManager(), "/assets/textures/inventory/selected.png", true);
		this.hotbarsel.setWidth(38);
		this.hotbarsel.setHeight(38);
		this.hotbarsel.setPosition(0, this.parent.getSettings().getHeight() - 63);
		this.parent.getGuiNode().attachChild(this.hotbarsel);
		
		this.parent.getGuiNode().attachChild(this.hotbaricons);
	}
	
	public void updateHotBar() {
		this.parent.getGuiNode().detachChild(this.hotbaricons);
		this.hotbaricons.detachAllChildren();
		List<InventorySlot> slots = this.parent.getInventoryManager().getAllHotSlots();
		for(InventorySlot slot : slots) {
			if(slot.getItem() != null) {
				Picture pic = new Picture("HotBarIcon");
				pic.setImage(this.parent.getAssetManager(), slot.getItem().getIcon(), true);
				pic.setWidth(38);
				pic.setHeight(38);
				pic.setPosition((slots.indexOf(slot))*38, this.parent.getSettings().getHeight() - 63);
				this.hotbaricons.attachChild(pic);
			}
		}
		this.parent.getGuiNode().attachChild(this.hotbaricons);
	}
	
	public void setHotBarSelection(int slot) {
		switch(slot)
		{
		case 0:
			this.hotbarsel.setPosition(0, this.parent.getSettings().getHeight() - 63);
			break;
		case 1:
			this.hotbarsel.setPosition(39, this.parent.getSettings().getHeight() - 63);
			break;
		case 2:
			this.hotbarsel.setPosition(77, this.parent.getSettings().getHeight() - 63);
			break;
		case 3:
			this.hotbarsel.setPosition(115, this.parent.getSettings().getHeight() - 63);
			break;
		case 4:
			this.hotbarsel.setPosition(153, this.parent.getSettings().getHeight() - 63);
			break;
		case 5:
			this.hotbarsel.setPosition(191, this.parent.getSettings().getHeight() - 63);
			break;
		case 6:
			this.hotbarsel.setPosition(229, this.parent.getSettings().getHeight() - 63);
			break;
		case 7:
			this.hotbarsel.setPosition(267, this.parent.getSettings().getHeight() - 63);
			break;
		case 8:
			this.hotbarsel.setPosition(305, this.parent.getSettings().getHeight() - 63);
			break;
		default:
			this.hotbarsel.setPosition(0, this.parent.getSettings().getHeight() - 63);
			break;
		}
	}
} 
