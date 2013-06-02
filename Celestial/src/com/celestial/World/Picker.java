/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.World;

import com.celestial.Celestial;
import com.cubes.BlockNavigator;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.List;

/**
 *
 * @author kevint
 */
public class Picker {
    
    private static CollisionResults getRayCastingResults(Node node, Celestial parent, Camera cam){
        Vector3f origin = cam.getWorldCoordinates(new Vector2f((parent.getSettings().getWidth() / 2), (parent.getSettings().getHeight() / 2)), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(new Vector2f((parent.getSettings().getWidth() / 2), (parent.getSettings().getHeight() / 2)), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();
        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        node.collideWith(ray, results);
        return results;
    }
    public static Vector3Int getCurrentPointedBlockLocation(boolean getNeighborLocation, Celestial parent, Camera cam){
        for(Node worldNode : parent.WorldSides) {
            for(BlockTerrainControl chunk : parent.sides) {
                CollisionResults results = getRayCastingResults(worldNode, parent, cam);
                if(results.size() > 0){
                    Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
                    return BlockNavigator.getPointedBlockLocation(chunk, collisionContactPoint, getNeighborLocation);
                }
                return null;
            }
        }
        return null;
    }
    
}
