/**
@author	Mitch Talmadge
Date Created:
	Jun 3, 2013
*/

package com.celestial.Blocks;

import com.cubes.Block;

public enum BlocksEnum {
	/** CLASSES **/
	
	GRASS(Block_Grass.class),
	DIRT(Block_Dirt.class),
	LEAVES(Block_Leaves.class),
	WOOD(Block_Wood.class),
	BIRCHWOOD(Block_BirchWood.class),
	STONE(Block_Stone.class),
	COBBLE(Block_Cobble.class),
	TORCH(Block_Torch.class);
	
	/** END CLASSES **/
	
	private Class<? extends Block> block;
	public Class<? extends Block> getBlock()
	{
		return block;
	}
	public void setBlock(Class<? extends Block> block)
	{
		this.block = block;
	}
	private BlocksEnum(Class<? extends Block> blockClass)
	{
		this.block = blockClass;
	}
}
