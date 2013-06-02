/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.Blocks;

import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;

/**
 *
 * @author kevint
 */
public class Blocks {
    
    public static void init() {
        BlockManager.register(Block_BetterGrass.class, new BlockSkin(new BlockSkin_TextureLocation(0, 0), false));
        BlockManager.register(Block_Dirt.class, new BlockSkin(new BlockSkin_TextureLocation(2, 0), false));
    }
    
}
