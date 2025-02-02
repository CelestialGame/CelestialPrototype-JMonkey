/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes.models;

import java.util.HashMap;

import com.cubes.BlockTerrainControl;
import com.cubes.Vector3i;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Carl
 */
public class BlockModel{

    public BlockModel(String modelPath, Class[] blockClasses){
        this.modelPath = modelPath;
        this.blockClasses = blockClasses;
    }
    private String modelPath;
    private Class[] blockClasses;
    private int nextMaterialIndex = 0;
    private HashMap<Material, Class> materialBlocks = new HashMap<Material, Class>();
    
    public void addToBlockTerrain(BlockTerrainControl blockTerrain, Vector3i location, Vector3i size){
        Spatial spatial = blockTerrain.getSettings().getAssetManager().loadModel(modelPath);
        Vector3f bounds = getBounds(spatial);
        Vector3f relativeBlockSize = new Vector3f((bounds.getX() / size.getX()), (bounds.getY() / size.getY()), (bounds.getZ() / size.getZ()));
        Geometry testBlockBox = new Geometry("", new Box(relativeBlockSize.divide(2), relativeBlockSize.getX(), relativeBlockSize.getY(), relativeBlockSize.getZ()));
        Vector3i tmpLocation = new Vector3i();
        for(int x=0;x<size.getX();x++){
            for(int y=0;y<size.getY();y++){
                for(int z=0;z<size.getZ();z++){
                    testBlockBox.setLocalTranslation(
                        (relativeBlockSize.getX() * x) - (bounds.getX() / 2),
                        (relativeBlockSize.getY() * y),
                        (relativeBlockSize.getZ() * z) - (bounds.getZ() / 2)
                    );
                    CollisionResults collisionResults = new CollisionResults();
                    spatial.collideWith(testBlockBox.getWorldBound(), collisionResults);
                    CollisionResult collisionResult = collisionResults.getClosestCollision();
                    if(collisionResult != null){
                        tmpLocation.set(location).addLocal(x, y, z);
                        Class blockClass = getMaterialBlockClass(collisionResult.getGeometry().getMaterial());
                        blockTerrain.setBlock(tmpLocation, blockClass);
                    }
                }
            }
        }
    }
    
    private Class getMaterialBlockClass(Material material){
        Class blockClass = materialBlocks.get(material);
        if(blockClass == null){
            blockClass = blockClasses[nextMaterialIndex];
            if(nextMaterialIndex < (blockClasses.length - 1)){
                nextMaterialIndex++;
            }
            materialBlocks.put(material, blockClass);
        }
        return blockClass;
    }
    
    private static Vector3f getBounds(Spatial spatial){
        if(spatial.getWorldBound() instanceof BoundingBox){
            BoundingBox boundingBox = (BoundingBox) spatial.getWorldBound();
            return new Vector3f(2 * boundingBox.getXExtent(), 2 * boundingBox.getYExtent(), 2 * boundingBox.getZExtent());
        }
        return new Vector3f(0, 0, 0);
    }
}
