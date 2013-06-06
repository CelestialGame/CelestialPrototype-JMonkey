package com.celestial.SinglePlayer.Inventory;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.cubes.Block;

/**
 * TODO Put here a description of what this class does.
 *
 * @author kevint.
 *         Created Jun 2, 2013.
 */
public class InventoryItem {

	Class<? extends Block> block;
	String name;
	String icon;
	
	public InventoryItem(Class<? extends Block> block, String name, String icon) {
		this.block = block;
		this.name = name;
		this.icon = icon;
	}
	
	public Class<? extends Block> getBlock() {
		return block;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIcon() {
		return icon;
	}
}
