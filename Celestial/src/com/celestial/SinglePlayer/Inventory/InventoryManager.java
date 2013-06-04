package com.celestial.SinglePlayer.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.celestial.util.FixedHashMap;
import com.celestial.util.InventoryException;

/**
 * TODO Put here a description of what this class does.
 *
 * @author kevint.
 *         Created Jun 2, 2013.
 */
public class InventoryManager {

	public HashMap<Integer, InventoryItem> items;

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
	
	InventorySlot selectedhotslot;

	private InventorySlot hotslot10;
	
	
	public InventoryManager() {
		this.items = new HashMap<Integer, InventoryItem>();
		
		this.hotslot1 = new InventorySlot(null, -2);
		this.hotslot2 = new InventorySlot(null, -2);
		this.hotslot3 = new InventorySlot(null, -2);
		this.hotslot4 = new InventorySlot(null, -2);
		this.hotslot5 = new InventorySlot(null, -2);
		this.hotslot6 = new InventorySlot(null, -2);
		this.hotslot7 = new InventorySlot(null, -2);
		this.hotslot8 = new InventorySlot(null, -2);
		this.hotslot9 = new InventorySlot(null, -2);
		this.hotslot10 = new InventorySlot(null, -2);
		
		this.hotslots = new ArrayList<InventorySlot>();
		this.hotslots.add(this.hotslot1);
		this.hotslots.add(this.hotslot2);
		this.hotslots.add(this.hotslot3);
		this.hotslots.add(this.hotslot4);
		this.hotslots.add(this.hotslot5);
		this.hotslots.add(this.hotslot6);
		this.hotslots.add(this.hotslot7);
		this.hotslots.add(this.hotslot8);
		this.hotslots.add(this.hotslot9);
		this.hotslots.add(this.hotslot10);
		
		this.selectedhotslot = this.hotslot1;
		
		//TODO Add extended inv (stuffs not in the hotbar
		
	}
	

	public void registerItem(InventoryItem item, int id) throws InventoryException {
		if(!this.items.containsValue(item)) {
			this.items.put(id, item);
		}
	}
	public InventoryItem getItembyID(int id) {
		return this.items.get(id);
	}
	
	//HotSlot Stuffs
	public void setHotSlot(InventoryItem item, int contents, int hotslot) throws InventoryException {
		if(this.items.containsValue(item)) {
			this.hotslots.get(hotslot).setItem(item, contents);
		} else {
			throw new InventoryException("ItemNotRegistered");
		}
	}
	
	public void refreshHotSlots() {
		for(InventorySlot hotslot : this.hotslots) {
			if(hotslot.getNumberContents() == 0) {
				hotslot.setItem(null, 0);
			}
		}
	}
	
	public void setSelectedHotSlot(int hotslot) {
		this.selectedhotslot = this.hotslots.get(hotslot);
	}
	
	public InventorySlot getSelectedHotSlot() {
		return this.selectedhotslot;
	}
}
