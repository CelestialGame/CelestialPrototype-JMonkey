package com.celestial.SinglePlayer.Inventory;

import java.awt.image.BufferedImage;

import com.celestial.Blocks.GameBlock;
import com.celestial.util.InventoryException;
import com.jme3.asset.AssetNotFoundException;

public class InventoryRegister
{
    
    public static void RegisterBlocks(InventoryManager IM)
    {
	for(GameBlock block : GameBlock.values())
	{
	    if(block.getID() < 0 || block.getIconPath() == null)
		continue;
	    try
	    {
		BufferedImage icon = block.getIcon();
		if(icon == null)
		{
		    RegBlankIconBlock(block, IM);
		    continue;
		}
		IM.registerItem(new InventoryItem(block), block.getID());
	    }
	    catch(InventoryException e)
	    {
		if(e.getMessage().equalsIgnoreCase("AlreadyRegistered"))
		{
		    // pass...w/e bro
		}
		else
		{
		    // crap this must be serious
		    e.printStackTrace();
		}
	    }
	    catch(AssetNotFoundException e)
	    {
		RegBlankIconBlock(block, IM);
		continue;
	    }
	}
    }
    
    private static void RegBlankIconBlock(GameBlock block, InventoryManager IM)
    {
	String path = "/assets/textures/inventory/icons/grass.png";
	try
	{
	    IM.registerItem(new InventoryItem(block), block.getID());
	}
	catch(InventoryException e)
	{
	    e.printStackTrace();
	}
    }
    
}
