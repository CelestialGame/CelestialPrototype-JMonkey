/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.World;

import com.celestial.CelestialPortal;
import com.cubes.BlockChunkControl;
import com.cubes.BlockNavigator;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

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
		BlockTerrainControl terrcontrol = parent.planets.get(0).getTerrControl();
		CollisionResults results = getRayCastingResults(terrNode, parent, cam);
		if(results.size() > 0){

			Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
			if(collisionContactPoint == null)
				return null;
			values[0] = collisionContactPoint;
			//collisionContactPoint = smartRound(collisionContactPoint, true);
			Quaternion q = parent.planets.get(0).getRotation().inverse();
			Vector3f rotatedContactPoint = q.mult(collisionContactPoint);
			Vector3f rotatedTranslation = q.mult(terrNode.getWorldTranslation());
			Vector3f originalTranslation = parent.planets.get(0).getOriginalTranslation();
			System.out.println(collisionContactPoint);
			rotatedContactPoint = smartRound(rotatedContactPoint, false);
			Vector3f relContactPoint = new Vector3f(
					rotatedContactPoint.getX() - originalTranslation.getX(),
					rotatedContactPoint.getY() - originalTranslation.getY(),
					rotatedContactPoint.getZ() - originalTranslation.getZ());
			if(getNeighborLocation)
				relContactPoint = smartRound(relContactPoint, false);

			Vector3Int blockPoint = BlockNavigator.getPointedBlockLocation(terrcontrol, relContactPoint, getNeighborLocation);
			if(blockPoint == null)
				return null;
			values[1] = blockPoint;

			return values;
		}
		return null;
	}
	private static Vector3f smartRound(Vector3f vec, boolean floor) {

		float x = vec.x;
		float y = vec.y;
		float z = vec.z;

		float xd = x - (int)x;
		float yd = y - (int)y;
		float zd = z - (int)z;

		Vector3f newvec = vec;
		if(!floor)
		{
			if((xd > yd && xd > zd))
			{
				newvec = new Vector3f((float) Math.ceil(x), y, z);
			}
			else if((yd > xd && yd > zd))
			{
				newvec = new Vector3f(x, (float) Math.ceil(y), z);
			}
			else if((zd > xd && zd > yd))
			{
				newvec = new Vector3f(x, y, (float) Math.ceil(z));
			}

			if(xd == 0 || yd == 0 || zd == 0)
			{
				newvec = vec;
			}
		}
		else
		{
			if((xd < yd && xd < zd))
			{
				newvec = new Vector3f((float) Math.floor(x), y, z);
			}
			else if((yd < xd && yd < zd))
			{
				newvec = new Vector3f(x, (float) Math.floor(y), z);
			}
			else if((zd < xd && zd < yd))
			{
				newvec = new Vector3f(x, y, (float) Math.floor(z));
			}

			if(xd == 0 || yd == 0 || zd == 0)
			{
				newvec = vec;
			}
		}


		return newvec;
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
