package com.celestial.SinglePlayer.Inventory;

public class InventorySlot
{
    
    InventoryItem item;
    int contents;
    private InventoryManager invmanager;
    
    public InventorySlot(InventoryItem item, int contents,
	    InventoryManager inventoryManager)
    {
	this.item = item;
	this.contents = contents;
	this.invmanager = inventoryManager;
    }
    
    public InventorySlot(InventoryItem item, int contents)
    {
	this.item = item;
	this.contents = contents;
    }
    
    public InventoryItem getItem()
    {
	return this.item;
    }
    
    public int getNumberContents()
    {
	return this.contents;
    }
    
    public InventorySlot getSlot()
    {
	return this;
    }
    
    public void setItem(InventoryItem item, int contents, int hotslot)
    {
	this.item = item;
	this.contents = contents;
	
	this.invmanager.refreshHotSlots();
    }
    
    public void modifyContents(int inc)
    {
	this.contents = this.contents + inc;
    }
    
    public void setContents(int number)
    {
	this.contents = number;
    }
    
}
