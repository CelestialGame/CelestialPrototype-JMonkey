package com.celestial.SinglePlayer.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.celestial.CelestialPortal;
import com.celestial.util.InventoryException;
import com.cubes.Block;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class InventoryManager {

	CelestialPortal portal;
	
	public HashMap<Integer, InventoryItem> items;

	InventorySlot hotslot0;
	InventorySlot hotslot1;
	InventorySlot hotslot2;
	InventorySlot hotslot3;
	InventorySlot hotslot4;
	InventorySlot hotslot5;
	InventorySlot hotslot6;
	InventorySlot hotslot7;
	InventorySlot hotslot8;
	InventorySlot hotslot9;
	
	List<InventorySlot> hotslots;
	List<InventorySlot> extendedinv;
	
	InventorySlot selectedhotslot;

	private InventoryGui inventorygui;
	
	public static int TAKE = 1;
	public static int GIVE = 2;
	
	public static int EMPTY = -70;
	
	Node dropitemsnode;
	public ArrayList<InventoryDrop> dropitems;
	
	public InventoryManager(CelestialPortal portal) {
		this.items = new HashMap<Integer, InventoryItem>();
		this.dropitems = new ArrayList<InventoryDrop>();
		
		this.hotslot0 = new InventorySlot(null, -2, this);
		this.hotslot1 = new InventorySlot(null, -2, this);
		this.hotslot2 = new InventorySlot(null, -2, this);
		this.hotslot3 = new InventorySlot(null, -2, this);
		this.hotslot4 = new InventorySlot(null, -2, this);
		this.hotslot5 = new InventorySlot(null, -2, this);
		this.hotslot6 = new InventorySlot(null, -2, this);
		this.hotslot7 = new InventorySlot(null, -2, this);
		this.hotslot8 = new InventorySlot(null, -2, this);
		this.hotslot9 = new InventorySlot(null, -2, this);
		
		this.hotslots = new ArrayList<InventorySlot>();
		this.hotslots.add(this.hotslot0);
		this.hotslots.add(this.hotslot1);
		this.hotslots.add(this.hotslot2);
		this.hotslots.add(this.hotslot3);
		this.hotslots.add(this.hotslot4);
		this.hotslots.add(this.hotslot5);
		this.hotslots.add(this.hotslot6);
		this.hotslots.add(this.hotslot7);
		this.hotslots.add(this.hotslot8);
		this.hotslots.add(this.hotslot9);
		
		this.selectedhotslot = this.hotslot0;
		
		this.inventorygui = new InventoryGui(portal);
		
		this.dropitemsnode = new Node();
		portal.getRootNode().attachChild(this.dropitemsnode);
		
		this.portal = portal;
		
		//TODO Add extended inv (stuffs not in the hotbar
		
	}
	

	public void registerItem(InventoryItem item, int id) throws InventoryException {
		if(!this.items.containsValue(item)) {
			this.items.put(id, item);
		} else {
			throw new InventoryException("AlreadyRegistered");
		}
	}
	public InventoryItem getItembyID(int id) {
		return this.items.get(id);
	}
	public InventoryItem getItembyBlock(Class<? extends Block> block) {
		for(InventoryItem item : this.items.values()) {
			if(item.getBlock().equals(block)) {
				return item;
			}
		}
		System.out.println("Can't find " + block.getSimpleName()+" in this.items");
		return null;
	}
	
	public void updateAll() {
		refreshHotSlots();
	}
	
	//HotSlot Stuffs
	private void setHotSlot(InventoryItem item, int contents, int hotslot) throws InventoryException {
		if(this.items.containsValue(item)) {
			if(hotslot <= 9) {
				this.hotslots.get(hotslot).setItem(item, contents);
			} else {
				throw new InventoryException("OutOfBounds");
			}
		} else {
			throw new InventoryException("ItemNotRegistered");
		}
	}
	
	public void refreshHotSlots() {
		boolean updateHotbar = false;
		for(InventorySlot hotslot : this.hotslots) {
			if(hotslot.getNumberContents() == 0 && hotslot.getItem() != null) {
				hotslot.setItem(null, 0);
				updateHotbar = true;
			}
		}
		if(updateHotbar)
			getInvGui().updateHotBar();
	}
	
	public void setSelectedHotSlot(int hotslot) {
		this.selectedhotslot = this.hotslots.get(hotslot);
		this.inventorygui.setHotBarSelection(hotslot);
	}
	
	private int getNextEmptySlot() {
		for (int i=0;i<this.hotslots.size();i++) {
			if(this.hotslots.get(i).getItem() == null) {
				return i;
			}
		}
		return EMPTY;
	}
	
	public InventorySlot getSelectedHotSlot() {
		return this.selectedhotslot;
	}
	
	public List<InventorySlot> getAllHotSlots() {
		return this.hotslots;
	}
	public InventoryGui getInvGui() {
		return this.inventorygui;
	}
	public List<InventoryDrop> getDropItems() {
		return this.dropitems;
	}
	private List<InventoryItem> getHotSlotItems() {
		List<InventoryItem> returnlist = new ArrayList<InventoryItem>();
		for(InventorySlot slot : this.hotslots) {
			returnlist.add(slot.getItem());
		}
		return returnlist;
	}
	
	// Drop Items
	public void dropItem(InventoryItem item, Vector3f location) {
		InventoryDrop drop = new InventoryDrop(item, location);
		this.dropitemsnode.attachChild(drop.getNode());
		this.dropitems.add(drop);
		drop.getNode().setLocalTranslation(location);
	}
	public void removeDropItem(InventoryDrop drop, Iterator<InventoryDrop> itr) {
		if(this.dropitems.contains(drop)) {
			this.portal.getBulletAppState().getPhysicsSpace().remove(drop.getCollisionBox().getUserObject());
			this.dropitemsnode.detachChild(drop.getNode());
			itr.remove();
		}
	}
	
	public void pickupDrop(InventoryDrop drop, int amount) {
		if(getHotSlotItems().contains(drop.getItem())) {
			int index = getHotSlotItems().lastIndexOf(drop.getItem());
			InventorySlot slot = this.hotslots.get(index);
			if(slot.getNumberContents() < 64) {
				slot.updateContents(false);
				return;
			} else {
				if(this.getNextEmptySlot() != EMPTY) {
					try {
						setHotSlot(drop.getItem(), 1, getNextEmptySlot());
					} catch (InventoryException exception) {
						exception.printStackTrace();
					}
				} else {
					this.extendedinv.add(new InventorySlot(drop.getItem(), 1, this));
				}
			}
		} else {
			if(this.getNextEmptySlot() != EMPTY) {
				try {
					setHotSlot(drop.getItem(), 1, getNextEmptySlot());
				} catch (InventoryException exception) {
					exception.printStackTrace();
				}
			} else {
				this.extendedinv.add(new InventorySlot(drop.getItem(), 1, this));
			}
		}
		this.getInvGui().updateHotBar();
	}
	
}
