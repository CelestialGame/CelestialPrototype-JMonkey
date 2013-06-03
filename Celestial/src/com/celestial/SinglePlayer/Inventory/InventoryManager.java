package com.celestial.SinglePlayer.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
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

	List<InventoryItem> items;
	FixedHashMap<InventoryItem, Integer> slots;
	
	
	
	public InventoryManager() {
		items = new ArrayList<InventoryItem>();
		
		slots = new FixedHashMap<InventoryItem, Integer>(10);
		
	}
	
	public boolean putintoSlot(InventoryItem item, int slot) throws InventoryException {
		if(items.contains(item)) {
			Object o = slots.put(item, slot);
			if(o != null) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new InventoryException("ItemNotRegistered");
		}
	}
	
	public void registerItem(InventoryItem item) throws InventoryException {
		if(!items.contains(item)) {
			items.add(item);
		}
	}
}
