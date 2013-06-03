package com.celestial.SinglePlayer.Inventory;

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
		try {
			IM.registerItem(new InventoryItem(Block_BirchWood.class, "Birch Wood", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/BirchWood.png"))));
			IM.registerItem(new InventoryItem(Block_Cobble.class, "Cobblestone", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/CobbleStone.png"))));
		} catch (InventoryException exception) {
			//pass
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
}
