/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import java.util.HashMap;

/**
 *
 * @author Carl
 * <br> Modified by Kevin Thorne
 */
public class BlockManager{
	
	private static BlockManager instance;
    public static BlockManager getInstance() {
        if (instance == null) {
            instance = new BlockManager();
        }
        return instance;
    }
    
    private static HashMap<Class<? extends Block>, BlockType> BLOCK_TYPES = new HashMap<Class<? extends Block>, BlockType>();
    private static BlockType[] TYPES_BLOCKS = new BlockType[256];
    private static byte nextBlockType = 1;
    
    public void register(Class<? extends Block> blockClass, BlockSkin skin){
        BlockType blockType = new BlockType(nextBlockType, skin);
        BLOCK_TYPES.put(blockClass, blockType);
        TYPES_BLOCKS[nextBlockType] = blockType;
        nextBlockType++;
    }
    
    public BlockType getType(Class<? extends Block> blockClass){
        return BLOCK_TYPES.get(blockClass);
    }
    
    public Class<? extends Block> getClass(byte type){
        return Util.getHashKeyByValue(BLOCK_TYPES, getType(type));
    }
    
    public BlockType getType(byte type){
        return TYPES_BLOCKS[type];
    }
}
