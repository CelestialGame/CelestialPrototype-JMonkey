/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import com.jme3.math.Vector3f;

/**
 *
 * @author Carl
 */
public class BlockNavigator{
    
    public static Vector3i getNeighborBlockLocalLocation(Vector3i location, Block.Face face){
        Vector3i neighborLocation = getNeighborBlockLocation_Relative(face);
        neighborLocation.addLocal(location);
        return neighborLocation;
    }
    
    public static Vector3i getNeighborBlockLocation_Relative(Block.Face face){
        Vector3i neighborLocation = new Vector3i();
        switch(face){
            case Top:
                neighborLocation.set(0, 1, 0);
                break;
            
            case Bottom:
                neighborLocation.set(0, -1, 0);
                break;
            
            case Left:
                neighborLocation.set(-1, 0, 0);
                break;
            
            case Right:
                neighborLocation.set(1, 0, 0);
                break;
            
            case Front:
                neighborLocation.set(0, 0, 1);
                break;
            
            case Back:
                neighborLocation.set(0, 0, -1);
                break;
        }
        return neighborLocation;
    }
    
    public static Vector3i getPointedBlockLocation(BlockTerrainControl blockTerrain, Vector3f collisionContactPoint, boolean getNeighborLocation){
        Vector3f collisionLocation = Util.compensateFloatRoundingErrors(collisionContactPoint);
        Vector3i blockLocation = new Vector3i(
                (int) (collisionLocation.getX() / blockTerrain.getSettings().getBlockSize()),
                (int) (collisionLocation.getY() / blockTerrain.getSettings().getBlockSize()),
                (int) (collisionLocation.getZ() / blockTerrain.getSettings().getBlockSize()));
        if((blockTerrain.getBlock(blockLocation) != null) == getNeighborLocation){
            if((collisionLocation.getX() % blockTerrain.getSettings().getBlockSize()) == 0) {
                blockLocation.subtractLocal(1, 0, 0);
            }
            else if((collisionLocation.getY() % blockTerrain.getSettings().getBlockSize()) == 0){
                blockLocation.subtractLocal(0, 1, 0);
            }
            else if((collisionLocation.getZ() % blockTerrain.getSettings().getBlockSize()) == 0){
                blockLocation.subtractLocal(0, 0, 1);
            }
        }
        return blockLocation;
    }
}
