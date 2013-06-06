package com.celestial.SinglePlayer.Inventory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.celestial.Celestial;
import com.celestial.Blocks.*;
import com.celestial.util.InventoryException;
import com.jme3.asset.AssetNotFoundException;

/**
 * TODO Put here a description of what this class does.
 *
 * @author kevint.
 *         Created Jun 2, 2013.
 */
public class InventoryRegister {
	
	
	public static void RegisterBlocks(InventoryManager IM) {
		for(BlocksEnum block : BlocksEnum.values())
		{
			if(block.getID() < 0 || block.getIconPath() == null)
				continue;
			try {
				BufferedImage icon = block.getIcon();
				if(icon == null)
				{
					RegBlankIconBlock(block, IM);
					continue;
				}
				IM.registerItem(new InventoryItem(block.getBClass(), block.getName(), 
						block.getIconPath()), block.getID());
			} catch (InventoryException e) {
				//do nothing
			} catch (AssetNotFoundException e) {
				RegBlankIconBlock(block, IM);
				continue;
			}
		}
	}
	
	private static void RegBlankIconBlock(BlocksEnum block, InventoryManager IM) {
			String path = "/assets/textures/inventory/icons/blank.png";
			try {
				IM.registerItem(new InventoryItem(block.getBClass(), block.getName(), 
						path), block.getID());
			} catch (InventoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
