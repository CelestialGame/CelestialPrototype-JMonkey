/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes.render;

import com.cubes.Block;
import com.cubes.BlockChunkControl;
import com.cubes.Vector3Int;
import com.cubes.Block.Face;

/**
 *
 * @author Carl
 */
public interface BlockChunk_MeshMerger{
    
    public abstract boolean shouldFaceBeAdded(BlockChunkControl chunk, Vector3Int location, Block.Face face);
}
