package com.celestial.SinglePlayer.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.celestial.Blocks.GameBlock;
import com.celestial.Gui.Gui;
import com.celestial.SinglePlayer.Components.Planet.Planet;
import com.celestial.util.InventoryException;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class InventoryManager
{
    
    CelestialPortal portal;
    
    public HashMap<Integer, InventoryItem> registeredItems;
    
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
    
    private Gui gui;
    
    public static int TAKE = 1;
    public static int GIVE = 2;
    
    public static int EMPTY = -70;
    
    Node dropitemsnode;
    public ArrayList<InventoryDrop> dropitems;
    
    public InventoryManager(CelestialPortal portal)
    {
	this.registeredItems = new HashMap<Integer, InventoryItem>();
	this.extendedinv = new ArrayList<InventorySlot>();
	this.dropitems = new ArrayList<InventoryDrop>();
	
	this.hotslot0 = new InventorySlot(new InventoryItem(GameBlock.FURNACE),
		3, this);
	this.hotslot1 = new InventorySlot(
		new InventoryItem(GameBlock.WORKBENCH), 3, this);
	this.hotslot2 = new InventorySlot(null, EMPTY, this);
	this.hotslot3 = new InventorySlot(null, EMPTY, this);
	this.hotslot4 = new InventorySlot(null, EMPTY, this);
	this.hotslot5 = new InventorySlot(null, EMPTY, this);
	this.hotslot6 = new InventorySlot(null, EMPTY, this);
	this.hotslot7 = new InventorySlot(null, EMPTY, this);
	this.hotslot8 = new InventorySlot(null, EMPTY, this);
	this.hotslot9 = new InventorySlot(null, EMPTY, this);
	
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
	
	this.gui = portal.getGui();
	
	this.dropitemsnode = new Node();
	portal.getRootNode().attachChild(this.dropitemsnode);
	
	this.portal = portal;
	
	// TODO Add extended inv (stuffs not in the hotbar
	
	for(int i = 0; i < 27; i++)
	{
	    this.extendedinv.add(new InventorySlot(null, EMPTY, this));
	}
    }
    
    public void registerItem(InventoryItem item, int id)
	    throws InventoryException
    {
	if(!this.registeredItems.containsValue(item))
	{
	    this.registeredItems.put(id, item);
	}
	else
	{
	    throw new InventoryException("AlreadyRegistered");
	}
    }
    
    public InventoryItem getItembyID(int id)
    {
	return this.registeredItems.get(id);
    }
    
    public InventoryItem getItembyBlock(GameBlock block)
    {
	for(InventoryItem item : this.registeredItems.values())
	{
	    if(item.getBlock().equals(block))
	    {
		return item;
	    }
	}
	System.out.println("Can't find " + block.getName() + " in this.items");
	return null;
    }
    
    public void updateAll()
    {
	refreshHotSlots();
    }
    
    // Extended inv stuffs
    public List<InventorySlot> getExtendedInvSlots()
    {
	return this.extendedinv;
    }
    
    private void setSlot(InventoryItem item, int contents, int slot)
	    throws InventoryException
    {
	if(this.registeredItems.containsValue(item))
	{
	    if(slot <= this.extendedinv.size())
	    {
		this.extendedinv.get(slot).setItem(item, contents, slot);
	    }
	    else
	    {
		throw new InventoryException("OutOfBounds");
	    }
	}
	else
	{
	    throw new InventoryException("ItemNotRegistered");
	}
    }
    
    private int getNextEmptySlot()
    {
	for(int i = 0; i < this.extendedinv.size(); i++)
	{
	    if(this.extendedinv.get(i).getItem() == null)
	    {
		return i;
	    }
	}
	return -1;
    }
    
    // HotSlot Stuffs
    private void setHotSlot(InventoryItem item, int contents, int hotslot)
	    throws InventoryException
    {
	if(this.registeredItems.containsValue(item))
	{
	    if(hotslot <= 9)
	    {
		this.hotslots.get(hotslot).setItem(item, contents, hotslot);
	    }
	    else
	    {
		throw new InventoryException("OutOfBounds");
	    }
	}
	else
	{
	    throw new InventoryException("ItemNotRegistered");
	}
    }
    
    public void refreshHotSlots()
    {
	for(int i = 0; i < this.hotslots.size(); i++)
	{
	    InventorySlot hotslot = this.hotslots.get(i);
	    if(hotslot.getNumberContents() == 0 && hotslot.getItem() != null)
	    {
		hotslot.setItem(null, EMPTY, i);
	    }
	    if(hotslot.getItem() != null)
		getInvGui().setHotBarIcon(i, hotslot);
	}
    }
    
    public void setSelectedHotSlot(int hotslot)
    {
	this.selectedhotslot = this.hotslots.get(hotslot);
	this.getInvGui().setHotBarSelection(hotslot);
    }
    
    private int getNextEmptyHotSlot()
    {
	for(int i = 0; i < this.hotslots.size(); i++)
	{
	    if(this.hotslots.get(i).getItem() == null)
	    {
		return i;
	    }
	}
	return -1;
    }
    
    public InventorySlot getSelectedHotSlot()
    {
	return this.selectedhotslot;
    }
    
    public List<InventorySlot> getAllHotSlots()
    {
	return this.hotslots;
    }
    
    public Gui getInvGui()
    {
	return this.gui;
    }
    
    public List<InventoryDrop> getDropItems()
    {
	return this.dropitems;
    }
    
    private List<InventoryItem> getHotSlotItems()
    {
	List<InventoryItem> returnlist = new ArrayList<InventoryItem>();
	for(InventorySlot slot : this.hotslots)
	{
	    returnlist.add(slot.getItem());
	}
	return returnlist;
    }
    
    // Drop Items
    public void dropItem(InventoryItem item, Vector3f location, Planet planet)
    {
	InventoryDrop drop = new InventoryDrop(item, location, planet);
	this.dropitemsnode.attachChild(drop.getNode());
	this.dropitems.add(drop);
	drop.getNode().setLocalTranslation(location);
    }
    
    public void removeDropItem(InventoryDrop drop, Iterator<InventoryDrop> itr)
    {
	if(this.dropitems.contains(drop))
	{
	    drop.getPlanet().getBulletAppState().getPhysicsSpace()
		    .remove(drop.getCollisionBox().getUserObject());
	    this.dropitemsnode.detachChild(drop.getNode());
	    itr.remove();
	}
    }
    
    public boolean pickupDrop(InventoryDrop drop, int amount)
    {
	if(getHotSlotItems().contains(drop.getItem())
		|| getExtendedInvSlots().contains(drop.getItem()))
	{
	    int index = getHotSlotItems().lastIndexOf(drop.getItem());
	    InventorySlot slot = this.hotslots.get(index);
	    if(slot.getNumberContents() < 64)
	    {
		// existing hotslot items
		slot.modifyContents(1);
		return true;
	    }
	    try
	    {
		int index_ = getExtendedInvSlots().lastIndexOf(drop.getItem());
		InventorySlot slot_ = this.extendedinv.get(index_);
		if(slot_.getNumberContents() < 64)
		{
		    // existing slot items
		    slot_.modifyContents(1);
		    return true;
		}
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
		// might not be in there so it'll throw an outofbounds
	    }
	    // go for next open hotslot
	    if(this.getNextEmptyHotSlot() != -1)
	    {
		try
		{
		    setHotSlot(drop.getItem(), 1, getNextEmptyHotSlot());
		}
		catch(InventoryException exception)
		{
		    exception.printStackTrace();
		}
	    }
	    else
	    {
		// go for next open SLOT
		if(this.getNextEmptySlot() != -1)
		{
		    try
		    {
			setSlot(drop.getItem(), 1, getNextEmptySlot());
		    }
		    catch(InventoryException exception)
		    {
			exception.printStackTrace();
		    }
		}
		else
		    return false;
	    }
	}
	else
	{
	    // find new open hotslot
	    if(this.getNextEmptyHotSlot() != -1)
	    {
		try
		{
		    setHotSlot(drop.getItem(), 1, getNextEmptyHotSlot());
		}
		catch(InventoryException exception)
		{
		    exception.printStackTrace();
		}
	    }
	    else
	    {
		// find open slot
		if(this.getNextEmptySlot() != -1)
		{
		    try
		    {
			setSlot(drop.getItem(), 1, getNextEmptySlot());
		    }
		    catch(InventoryException exception)
		    {
			exception.printStackTrace();
		    }
		}
		else
		    return false;
	    }
	}
	return true;
    }
    
    public void openExtendedInv(boolean yes)
    {
	if(yes)
	{
	    Celestial.gui.getNifty().gotoScreen("extendedinv");
	    Celestial.gui.disableControl();
	}
	else
	{
	    Celestial.gui.getNifty().gotoScreen("hud");
	    Celestial.gui.enableControl();
	}
    }
    
}
