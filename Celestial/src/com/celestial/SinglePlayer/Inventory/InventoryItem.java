package com.celestial.SinglePlayer.Inventory;

import com.celestial.Celestial;
import com.celestial.Blocks.GameBlock;
import com.celestial.Tools.Tool;
import com.celestial.Tools.Tools;
import com.jme3.asset.AssetNotFoundException;

public class InventoryItem {
	
	public static enum ItemType {
		BLOCK, TOOL;
	}

	private GameBlock block;
	private Tool tool;
	private ItemType type;
	private String name;
	private String icon;
	
	public InventoryItem(GameBlock block) {
		this.block = block;
		this.name = block.getName();
		this.icon = block.getIconPath();
		this.type = ItemType.BLOCK;
		try
		{
			Celestial.app.getAssetManager().loadTexture(icon);
		}
		catch(AssetNotFoundException e)
		{
			this.icon = "assets/textures/inventory/icons/blank.png";
		}
		catch (NullPointerException e) {
			this.icon = "assets/textures/inventory/icons/blank.png";
		}
				
	}
	public InventoryItem(Tools tool) {
		try {
			this.tool = tool.getToolClass().newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		this.name = tool.getName();
		this.icon = tool.getIconPath();
		this.type = ItemType.TOOL;
		try
		{
			Celestial.app.getAssetManager().loadTexture(icon);
		}
		catch(AssetNotFoundException e)
		{
			this.icon = "assets/textures/inventory/icons/blank.png";
		}
		catch (NullPointerException e) {
			this.icon = "assets/textures/inventory/icons/blank.png";
		}
	}
	
	public GameBlock getBlock() {
		return block;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIcon() {
		return icon;
	}
	public Tool getTool() {
		return tool;
	}
	public ItemType getType() {
		return type;
	}
}
