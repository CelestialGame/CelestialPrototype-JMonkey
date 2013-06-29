package com.celestial.SinglePlayer.Inventory;

import com.celestial.Celestial;
import com.celestial.Blocks.BlocksEnum;
import com.jme3.asset.AssetNotFoundException;

public class InventoryItem {

	private BlocksEnum block;
	private String name;
	private String icon;
	
	public InventoryItem(BlocksEnum block) {
		this.block = block;
		this.name = block.getName();
		this.icon = block.getIconPath();
		try
		{
			Celestial.app.getAssetManager().loadTexture(icon);
		}
		catch(AssetNotFoundException e)
		{
			this.icon = "assets/textures/inventory/icons/blank.png";
		}
				
	}
	
	public BlocksEnum getBlock() {
		return block;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIcon() {
		return icon;
	}
}
