/**
@author	Mitch Talmadge
Date Created:
	Jun 3, 2013
 */

package com.celestial.Blocks;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.cubes.Block;
import com.cubes.BlockManager;
import com.cubes.BlockType;

public enum GameBlock
{
    /** CLASSES **/
    
    GRASS(Block_Grass.class, "Grass", 3,
	    "assets/textures/inventory/icons/Grass.png"), DIRT(
	    Block_Dirt.class, "Dirt", 1,
	    "assets/textures/inventory/icons/Dirt.png"), LEAVES(
	    Block_Leaves.class, "Leaves", 7,
	    "assets/textures/inventory/icons/Leaves.png"), WOOD(
	    Block_Wood.class, "Oak Wood", 5,
	    "assets/textures/inventory/icons/OakWood.png"), BIRCHWOOD(
	    Block_BirchWood.class, "Birch Wood", 6,
	    "assets/textures/inventory/icons/BirchWood.png"), STONE(
	    Block_Stone.class, "Stone", 2,
	    "assets/textures/inventory/icons/Stone.png"), COBBLE(
	    Block_Cobble.class, "Cobblestone", 4,
	    "assets/textures/inventory/icons/CobbleStone.png"), TORCH(
	    Block_Torch.class, "Torch", -1,
	    "assets/textures/inventory/icons/Torch.png"), COAL_ORE(
	    Block_CoalOre.class, "Coal Ore", 8,
	    "assets/textures/inventory/icons/CoalOre.png"), IRON_ORE(
	    Block_IronOre.class, "Iron Ore", 9,
	    "assets/textures/inventory/icons/IronOre.png"), COPPER_ORE(
	    Block_CopperOre.class, "Copper Ore", 10,
	    "assets/textures/inventory/icons/CopperOre.png"), TIN_ORE(
	    Block_TinOre.class, "Tin Ore", 11,
	    "assets/textures/inventory/icons/TinOre.png"), RAW_DIAMOND(
	    Block_RawDiamond.class, "Raw Diamond", 12,
	    "assets/textures/inventory/icons/RawDiamond.png"), GOLD_ORE(
	    Block_GoldOre.class, "Gold Ore", 13,
	    "assets/textures/inventory/icons/GoldOre.png"), SUBSTRATUS(
	    Block_Substratus.class, "Substratus", 14,
	    "assets/textures/inventory/icons/Substratus.png"), DARKSTONE(
	    Block_DarkStone.class, "Dark Stone", 13,
	    "assets/textures/inventory/icons/DarkStone.png"), ICE(
	    Block_Ice.class, "Ice", 13,
	    "assets/textures/inventory/icons/Ice.png"), WORKBENCH(
	    Block_WorkBench.class, "Workbench", 16,
	    "assets/textures/inventory/icons/WorkBench.png", true), FURNACE(
	    Block_Furnace.class, "Furnace", 15,
	    "assets/textures/inventory/icons/Furnace.png", true);
    
    /** END CLASSES **/
    
    private Class<? extends Block> blockclass;
    private String name;
    private int id;
    private String iconpath;
    private boolean isDynamic;
    
    public Class<? extends Block> getBClass()
    {
	return blockclass;
    }
    
    public String getName()
    {
	return name;
    }
    
    public int getID()
    {
	return id;
    }
    
    public String getIconPath()
    {
	return iconpath;
    }
    
    public BufferedImage getIcon()
    {
	try
	{
	    if (iconpath == null)
	    {
		return ImageIO.read(this.getClass().getResourceAsStream(
			"assets/textures/inventory/icons/blank.png"));
	    }
	    return ImageIO.read(this.getClass().getResourceAsStream(iconpath));
	}
	catch (IOException e)
	{
	    return null;
	}
	catch (IllegalArgumentException e)
	{
	    return null;
	}
    }
    
    public void setBlock(Class<? extends Block> block, String name, int id,
	    String iconpath)
    {
	this.blockclass = block;
	this.name = name;
	this.id = id;
	this.iconpath = iconpath;
    }
    
    private GameBlock(Class<? extends Block> blockClass, String name, int id,
	    String iconpath)
    {
	this.blockclass = blockClass;
	this.name = name;
	this.id = id;
	this.iconpath = iconpath;
    }
    
    private GameBlock(Class<? extends Block> blockClass, String name, int id,
	    String iconpath, boolean dynamic)
    {
	this.blockclass = blockClass;
	this.name = name;
	this.id = id;
	this.iconpath = iconpath;
	this.isDynamic = dynamic;
    }
    
    public static GameBlock getBlockByClass(Class<? extends Block> blockClass)
    {
	for (GameBlock b : values())
	{
	    if (b.getBClass().equals(blockClass))
		return b;
	}
	return null;
    }
    
    public static GameBlock getBlockByType(BlockType type)
    {
	for (GameBlock b : values())
	{
	    if (BlockManager.getInstance().getType(b.getBClass()).equals(type))
		return b;
	}
	return null;
    }
    
    public boolean isDynamic()
    {
	return isDynamic;
    }
}
