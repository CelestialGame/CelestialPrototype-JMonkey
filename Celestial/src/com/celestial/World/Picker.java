/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.World;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.cubes.Block;
import com.cubes.BlockChunkControl;
import com.cubes.BlockNavigator;
import com.cubes.BlockTerrainControl;
import com.cubes.BlockType;
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

	private static CollisionResults getRayCastingResults(Node node, CelestialPortal parent, Camera cam){
		Vector3f origin = cam.getWorldCoordinates(new Vector2f((parent.getSettings().getWidth() / 2), (parent.getSettings().getHeight() / 2)), 0.0f);
		Vector3f direction = cam.getWorldCoordinates(new Vector2f((parent.getSettings().getWidth() / 2), (parent.getSettings().getHeight() / 2)), 0.3f);
		direction.subtractLocal(origin).normalizeLocal();
		Ray ray = new Ray(origin, direction);
		CollisionResults results = new CollisionResults();
		node.collideWith(ray, results);
		return results;
	}
	public static Object[] getCurrentPointedBlockLocation(boolean getNeighborLocation, CelestialPortal parent, Camera cam){
		Object[] values = new Object[2];
		Node terrNode = parent.planets.get(0).getTerrainNode();
		BlockTerrainControl chunk = parent.planets.get(0).getTerrControl();
		CollisionResults results = getRayCastingResults(terrNode, parent, cam);
		if(results.size() > 0){

			Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
			if(collisionContactPoint == null)
				return null;
			values[0] = collisionContactPoint;

			Vector3f relContactPoint = new Vector3f(
					collisionContactPoint.getX() - terrNode.getWorldTranslation().getX(),
					collisionContactPoint.getY() - terrNode.getWorldTranslation().getY(),
					collisionContactPoint.getZ() - terrNode.getWorldTranslation().getZ());
			
			Vector3Int blockPoint = BlockNavigator.getPointedBlockLocation(chunk, relContactPoint, getNeighborLocation);
			if(blockPoint == null)
				return null;
			values[1] = blockPoint;

			return values;
		}
		return null;
	}
	public static BlockChunkControl getCurrentPointedBlock(boolean getNeighborLocation, CelestialPortal parent, Camera cam) {
		Node terrNode = parent.planets.get(0).getTerrainNode();
		BlockTerrainControl chunk = parent.planets.get(0).getTerrControl();
		CollisionResults results = getRayCastingResults(terrNode, parent, cam);
		if(results.size() > 0){
			Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
			return chunk.getChunk(BlockNavigator.getPointedBlockLocation(chunk, collisionContactPoint, getNeighborLocation));
		}
		return null;
	}

}
