package com.celestial.SinglePlayer.Inventory;

import com.cubes.Block;

/**
 * TODO Put here a description of what this class does.
 *
 * @author kevint.
 *         Created Jun 2, 2013.
 */
public class InventoryItem {

	Block block;
	String name;
	
	public InventoryItem(Block block, String name) {
		this.block = block;
		this.name = name;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public String getName() {
		return name;
	}
}
