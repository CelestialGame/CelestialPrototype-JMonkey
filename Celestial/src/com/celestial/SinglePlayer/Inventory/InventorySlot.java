package com.celestial.SinglePlayer.Inventory;

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
	
	public void modifyNumberContents(boolean decrease, int inc) {
		if(decrease)
			this.contents = this.contents - inc;
		else
			this.contents = this.contents + inc;
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
