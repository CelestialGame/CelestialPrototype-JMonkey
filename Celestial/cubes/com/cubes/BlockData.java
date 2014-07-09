package com.cubes;

import com.celestial.SinglePlayer.Components.Planet.Planet;
import com.celestial.SinglePlayer.Components.Planet.PlanetFace;
import com.jme3.math.Vector3f;

public class BlockData 
{

	private byte blockType;
	private Vector3i location;
	private BlockChunkControl chunkControl;
	
	public BlockData(byte blockType, Vector3i location, BlockChunkControl chunkControl)
	{
		this.blockType = blockType;
		this.location = location;
		this.chunkControl = chunkControl;
	}
	
	public void setBlockType(byte blockType)
	{
		this.blockType = blockType;
	}

	public byte getBlockType()
	{
		return blockType;
	}
	
	public boolean getIsOnSurface()
	{
		PlanetFace currFace = this.getCurrentFaceOfPlanet();
		switch(currFace)
		{
		case TOP:
			return (this.chunkControl.getNeighborBlock_Local(getLocation(), Block.Face.Top) == null);
		case BOTTOM:
			return (this.chunkControl.getNeighborBlock_Local(getLocation(), Block.Face.Bottom) == null);
		case NORTH:
			return (this.chunkControl.getNeighborBlock_Local(getLocation(), Block.Face.Front) == null);
		case SOUTH:
			return (this.chunkControl.getNeighborBlock_Local(getLocation(), Block.Face.Back) == null);
		case EAST:
			return (this.chunkControl.getNeighborBlock_Local(getLocation(), Block.Face.Right) == null);
		case WEST:
			return (this.chunkControl.getNeighborBlock_Local(getLocation(), Block.Face.Left) == null);
		default:
			return false;
		}
	}
	
	public Vector3i getLocation()
	{
		return this.location;
	}
	
	public PlanetFace getCurrentFaceOfPlanet()
	{
		Planet planet = this.chunkControl.getTerrain().getBlockChunkManager().getPlanet();
		float x = getLocation().getX() - ((planet.getDiameter()*Planet.CHUNK_SIZE)/2);
		float y = getLocation().getY() - ((planet.getDiameter()*Planet.CHUNK_SIZE)/2);
		float z = getLocation().getZ() - ((planet.getDiameter()*Planet.CHUNK_SIZE)/2);
		
		if( Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z) ) {
			if( y < 0 ) {
				return PlanetFace.BOTTOM;
			} else {
				return PlanetFace.TOP;
			}
		} else if( Math.abs(x) > Math.abs(z) ) {
			if( x < 0 ) {
				return PlanetFace.WEST;
			} else {
				return PlanetFace.EAST;
			}
		} else if( Math.abs(z) > Math.abs(x) ) {
			if( z < 0 ) {
				return PlanetFace.NORTH;
			} else {
				return PlanetFace.SOUTH;
			}
		} else {
			return PlanetFace.UNKNOWN;
		}
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 32;
    	int result = 1;
    	
    	result = prime * result + this.getBlockType();
    	result = prime * result + this.getLocation().hashCode();
    	
    	return result;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other == null)
			return false;
		if(!(other instanceof BlockData))
			return false;
		if(((BlockData)other).getBlockType() != this.getBlockType())
			return false;
		if(((BlockData)other).getLocation() != this.getLocation())
			return false;
		
		return true;
	}
	
}
