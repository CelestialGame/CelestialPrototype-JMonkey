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
			IM.registerItem(new InventoryItem(Block_Dirt.class, "Dirt", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/Dirt.png"))), 1);
			IM.registerItem(new InventoryItem(Block_Cobble.class, "Cobblestone", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/CobbleStone.png"))), 4);
			IM.registerItem(new InventoryItem(Block_BirchWood.class, "Birch Wood", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/BirchWood.png"))),6);
			IM.registerItem(new InventoryItem(Block_Grass.class, "Grass", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/Grass.png"))),3);
			IM.registerItem(new InventoryItem(Block_Leaves.class, "Leaves", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/Leaves.png"))),7);
			IM.registerItem(new InventoryItem(Block_Stone.class, "Stone", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/Stone.png"))),2);
			IM.registerItem(new InventoryItem(Block_Wood.class, "Oak Wood", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/OakWood.png"))),5);
			
			IM.registerItem(new InventoryItem(Block_CoalOre.class, "Coal Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/CoalOre.png"))),8);
			IM.registerItem(new InventoryItem(Block_IronOre.class, "Iron Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/IronOre.png"))),9);
			IM.registerItem(new InventoryItem(Block_CopperOre.class, "Copper Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/CopperOre.png"))),10);
			IM.registerItem(new InventoryItem(Block_TinOre.class, "Tin Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/TinOre.png"))),11);
			IM.registerItem(new InventoryItem(Block_RawDiamond.class, "Raw Diamond", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/RawDiamond.png"))),12);
			IM.registerItem(new InventoryItem(Block_GoldOre.class, "Gold Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream("/assets/textures/inventory/icons/GoldOre.png"))),13);
		} catch (InventoryException exception) {
			//pass
		} catch (IOException exception) {
			//blank icon
			RegBlankIconBlocks(IM);
		} catch (IllegalArgumentException e) {
			//blank icon
			RegBlankIconBlocks(IM);
		}
	}
	
	private static void RegBlankIconBlocks(InventoryManager IM) {
		try {
			String path = "/assets/textures/inventory/icons/blank.png";
			IM.registerItem(new InventoryItem(Block_Dirt.class, "Dirt", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))), 1);
			IM.registerItem(new InventoryItem(Block_Cobble.class, "Cobblestone", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))), 4);
			IM.registerItem(new InventoryItem(Block_BirchWood.class, "Birch Wood", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),6);
			IM.registerItem(new InventoryItem(Block_Grass.class, "Grass", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),3);
			IM.registerItem(new InventoryItem(Block_Leaves.class, "Leaves", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),7);
			IM.registerItem(new InventoryItem(Block_Stone.class, "Stone", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),2);
			IM.registerItem(new InventoryItem(Block_Wood.class, "Oak Wood", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),5);
			
			IM.registerItem(new InventoryItem(Block_CoalOre.class, "Coal Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),8);
			IM.registerItem(new InventoryItem(Block_IronOre.class, "Iron Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),9);
			IM.registerItem(new InventoryItem(Block_CopperOre.class, "Copper Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),10);
			IM.registerItem(new InventoryItem(Block_TinOre.class, "Tin Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),11);
			IM.registerItem(new InventoryItem(Block_RawDiamond.class, "Raw Diamond", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),12);
			IM.registerItem(new InventoryItem(Block_GoldOre.class, "Gold Ore", 
					ImageIO.read(IM.getClass().getResourceAsStream(path))),13);
		} catch (InventoryException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
}
