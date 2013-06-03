/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.Blocks;

import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

import com.cubes.Vector3Int;
import com.cubes.Block.Face;
import com.cubes.BlockChunkControl;

/**
 *
 * @author kevint
 */
public class Blocks {
    
    public static void init() {
    	BlockManager.register(Block_Grass.class, new BlockSkin(new BlockSkin_TextureLocation[]{
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
        BlockManager.register(Block_Dirt.class, new BlockSkin(new BlockSkin_TextureLocation(2, 0), false));
        BlockManager.register(Block_Leaves.class, new BlockSkin(new BlockSkin_TextureLocation(7, 0), false));
        BlockManager.register(Block_Wood.class, new BlockSkin(new BlockSkin_TextureLocation[]{
        	    new BlockSkin_TextureLocation(4, 0),
        	    new BlockSkin_TextureLocation(4, 0),
        	    new BlockSkin_TextureLocation(3, 0),
        	    new BlockSkin_TextureLocation(3, 0),
        	    new BlockSkin_TextureLocation(3, 0),
        	    new BlockSkin_TextureLocation(3, 0)
        	}, false));
        BlockManager.register(Block_BirchWood.class, new BlockSkin(new BlockSkin_TextureLocation[]{
        	    new BlockSkin_TextureLocation(6, 0),
        	    new BlockSkin_TextureLocation(6, 0),
        	    new BlockSkin_TextureLocation(5, 0),
        	    new BlockSkin_TextureLocation(5, 0),
        	    new BlockSkin_TextureLocation(5, 0),
        	    new BlockSkin_TextureLocation(5, 0)
        	}, false));
        BlockManager.register(Block_Stone.class, new BlockSkin(new BlockSkin_TextureLocation(3, 1), false));
        BlockManager.register(Block_Cobble.class, new BlockSkin(new BlockSkin_TextureLocation(9, 0), false));
        BlockManager.register(Block_Torch.class, new BlockSkin(new BlockSkin_TextureLocation(0, 2), false));
    }
    
}
