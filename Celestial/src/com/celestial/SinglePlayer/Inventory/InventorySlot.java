package com.celestial.SinglePlayer.Inventory;

/**
 * TODO Put here a description of what this class does.
 *
 * @author kevint.
 *         Created Jun 3, 2013.
 */
public class InventorySlot {
	
	InventoryItem item;
	int contents;
	
	public InventorySlot(InventoryItem item, int contents) {
		this.item = item;
		this.contents = contents;
	}
	
	public InventoryItem getItem() {
		return this.item;
	}
	
	public int getNumberContents() {
		return this.contents;
	}
	
	public InventorySlot getSlot() {
		return this;
	}
	
	public void setItem(InventoryItem item, int contents) {
		this.item = item;
		this.contents = contents;
	}

}
