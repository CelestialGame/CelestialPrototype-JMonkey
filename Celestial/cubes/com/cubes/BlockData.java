package com.cubes;

import com.celestial.Blocks.GameBlock;

public class BlockData 
{

	private byte blockType;
	private boolean isOnSurface;
	
	public BlockData(byte blockType, boolean isOnSurface)
	{
		this.blockType = blockType;
		this.isOnSurface = isOnSurface;
	}
	
	public BlockData(byte blockType)
	{
		this.blockType = blockType;
		this.isOnSurface = false;
	}
	
	public BlockData(boolean isOnSurface)
	{
		this.blockType = BlockManager.getInstance().getType(GameBlock.DIRT.getBClass()).getType();
		this.isOnSurface = isOnSurface;
	}
	
	public BlockData()
	{
		this.blockType = BlockManager.getInstance().getType(GameBlock.DIRT.getBClass()).getType();
		this.isOnSurface = false;
	}
	
	public void setBlockType(byte blockType)
	{
		this.blockType = blockType;
	}

	public byte getBlockType()
	{
		return blockType;
	}
	
	public void setIsOnSurface(boolean isOnSurface)
	{
		this.isOnSurface = isOnSurface;
	}

	public boolean getIsOnSurface()
	{
		return isOnSurface;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 32;
    	int result = 1;
    	
    	result = prime * result + blockType;
    	result = prime * result + (isOnSurface ? 1 : 0);
    	
    	return result;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other == null)
			return false;
		if(!(other instanceof BlockData))
			return false;
		if(((BlockData)other).getBlockType() != this.blockType)
			return false;
		if(((BlockData)other).getIsOnSurface() != this.isOnSurface)
			return false;
		
		return true;
	}
	
}
