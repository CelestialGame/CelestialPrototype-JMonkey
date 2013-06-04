package com.celestial.SinglePlayer.Inventory;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.celestial.Celestial;
import com.celestial.Blocks.*;
import com.celestial.util.InventoryException;

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
						block.getIcon()), block.getID());
			} catch (InventoryException e) {
				//do nothing
			}
		}
	}
	
	private static void RegBlankIconBlock(BlocksEnum block, InventoryManager IM) {
			String path = "/assets/textures/inventory/icons/blank.png";
			try {
				IM.registerItem(new InventoryItem(block.getBClass(), block.getName(), 
						ImageIO.read(IM.getClass().getResourceAsStream(path))), block.getID());
			} catch (InventoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
