package com.celestial.SinglePlayer.Inventory;

public class InventorySlot {
	
	InventoryItem item;
	int contents;
	private InventoryManager invmanager;
	
	public InventorySlot(InventoryItem item, int contents, InventoryManager inventoryManager) {
		this.item = item;
		this.contents = contents;
		this.invmanager = inventoryManager;
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
	
	public void setItem(InventoryItem item, int contents, int hotslot) {
		this.item = item;
		this.contents = contents;
		if(item != null)
			this.invmanager.getInvGui().setHotBarIcon(hotslot, item.getBlock());
	}
	
	public void modifyNumberContents(boolean decrease, int inc) {
		if(decrease)
			this.contents -= inc;
		else
			this.contents += inc;
	}
	
	public void updateContents(boolean decrease) {
		if(this.getNumberContents() > 0) {
			if(decrease)
				this.modifyNumberContents(true, 1);
			else
				this.modifyNumberContents(false, 1);
		}
	}

}
