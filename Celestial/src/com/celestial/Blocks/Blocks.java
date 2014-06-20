/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.Blocks;

import com.cubes.Block.Face;
import com.cubes.BlockChunkControl;
import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.Vector3Int;

/**
 *
 * @author kevint
 */
public class Blocks {
	
    public static void init() {
    	BlockManager.getInstance().register(BlocksEnum.GRASS.getBClass(), new BlockSkin(new BlockSkin_TextureLocation[]{
    		    //We specify the 3 textures we need:
    		    //Grass, Earth-Grass-Transition and Earth
    		    new BlockSkin_TextureLocation(0, 0),
    		    new BlockSkin_TextureLocation(1, 0),
    		    new BlockSkin_TextureLocation(2, 0),
    		}, false){
    		 
    		    @Override
    		    //The number that's being returned specified the index
    		    //of the texture in the previous declared TextureLocation array
    		    protected int getTextureLocationIndex(BlockChunkControl chunk, Vector3Int blockLocation, Face face){
    		        if(chunk.isBlockOnSurface(blockLocation)){
    		            switch(face){
    		                case Top:
    		                    return 0;
    		 
    		                case Bottom:
    		                    return 2;
    		                    
    		                case Left:
    		                	return 1;
    		                case Right:
    		                	return 1;
    		                case Front:
    		                	return 1;
    		                case Back:
    		                	return 1;
    		            }
    		            return 1;
    		        }
    		        return 2;
    		    }
    		});
        BlockManager.getInstance().register(BlocksEnum.DIRT.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(2, 0), false));
        BlockManager.getInstance().register(BlocksEnum.LEAVES.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(7, 0), false));
        BlockManager.getInstance().register(BlocksEnum.WOOD.getBClass(), new BlockSkin(new BlockSkin_TextureLocation[]{
        	    new BlockSkin_TextureLocation(4, 0),
        	    new BlockSkin_TextureLocation(4, 0),
        	    new BlockSkin_TextureLocation(3, 0),
        	    new BlockSkin_TextureLocation(3, 0),
        	    new BlockSkin_TextureLocation(3, 0),
        	    new BlockSkin_TextureLocation(3, 0)
        	}, false));
        BlockManager.getInstance().register(BlocksEnum.BIRCHWOOD.getBClass(), new BlockSkin(new BlockSkin_TextureLocation[]{
        	    new BlockSkin_TextureLocation(6, 0),
        	    new BlockSkin_TextureLocation(6, 0),
        	    new BlockSkin_TextureLocation(5, 0),
        	    new BlockSkin_TextureLocation(5, 0),
        	    new BlockSkin_TextureLocation(5, 0),
        	    new BlockSkin_TextureLocation(5, 0)
        	}, false));
        BlockManager.getInstance().register(BlocksEnum.STONE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(4, 1), false));
        BlockManager.getInstance().register(BlocksEnum.COBBLE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(9, 0), false));
        BlockManager.getInstance().register(BlocksEnum.TORCH.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(0, 2), false));
        
        BlockManager.getInstance().register(BlocksEnum.COAL_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(10, 1), false));
        BlockManager.getInstance().register(BlocksEnum.COPPER_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(9, 1), false));
        BlockManager.getInstance().register(BlocksEnum.RAW_DIAMOND.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(8, 1), false));
        BlockManager.getInstance().register(BlocksEnum.TIN_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(7, 1), false));
        BlockManager.getInstance().register(BlocksEnum.GOLD_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(6, 1), false));
        BlockManager.getInstance().register(BlocksEnum.IRON_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(5, 1), false));
        BlockManager.getInstance().register(BlocksEnum.SUBSTRATUS.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(9, 0), false));
        BlockManager.getInstance().register(BlocksEnum.DARKSTONE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(3, 1), false));
        BlockManager.getInstance().register(BlocksEnum.ICE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(0, 1), true));
    }
    
}
