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

			Quaternion q1 = parent.planets.get(0).getPlanetNode().getLocalRotation().inverse();

			Vector3f translatedContactPoint = collisionContactPoint.subtract(parent.planets.get(0).getWantedLocation());
			Vector3f rotatedContactPoint = q1.mult(translatedContactPoint);
			Vector3f rotatedTranslation = q1.mult(terrNode.getWorldTranslation().subtract(parent.planets.get(0).getWantedLocation()));//q.mult(terrNode.getWorldTranslation());	

			System.out.println(rotatedContactPoint);
			rotatedContactPoint = smartRound(rotatedContactPoint);
			System.out.println(rotatedContactPoint);
			rotatedTranslation = round(rotatedTranslation);

			System.out.println("Sub: "+collisionContactPoint.subtract(terrNode.getWorldTranslation()));
			System.out.println("Q: "+q1+" - Rotated: "+rotatedTranslation);

			Vector3f relContactPoint = rotatedContactPoint.subtract(rotatedTranslation);

			Vector3Int blockPoint = BlockNavigator.getPointedBlockLocation(terrcontrol, relContactPoint, getNeighborLocation);
			if(blockPoint == null)
				return null;
			values[1] = blockPoint;

			return values;
		}
		return null;
	}
	private static Vector3f round(Vector3f vec) {
		return new Vector3f(
				StrictMath.round(vec.x),
				StrictMath.round(vec.y),
				StrictMath.round(vec.z));
	}
	private static Vector3f smartRound(Vector3f vec) {

		float x = vec.x;
		float y = vec.y;
		float z = vec.z;

		String xs = new String(x+"");
		String ys = new String(y+"");
		String zs = new String(z+"");
		System.out.println(xs+" "+ys+" "+zs);
		float xd = Float.parseFloat("0."+xs.substring(xs.indexOf(".")+1, xs.length()));
		float yd = Float.parseFloat("0."+ys.substring(ys.indexOf(".")+1, ys.length()));
		float zd = Float.parseFloat("0."+zs.substring(zs.indexOf(".")+1, zs.length()));

		System.out.println(xd+" "+yd+" "+zd);
		
		Vector3f newvec = vec.clone();
		int greatest = 0, smallest = 0;

		if((xd > yd && xd > zd))
		{
			greatest=0;
		}
		else if((yd > xd && yd > zd))
		{
			greatest=1;
		}
		else if((zd > xd && zd > yd))
		{
			greatest=2;
		}
		
		if((xd < yd && xd < zd))
		{
			smallest=0;
		}
		else if((yd < xd && yd < zd))
		{
			smallest=1;
		}
		else if((zd < xd && zd < yd))
		{
			smallest=2;
		}
		
		if(greatest == 0 && smallest == 1) //xy
		{
			int ground = StrictMath.round(x);
			int sround = StrictMath.round(y);
			
			if(ground % 3 == 0 && sround % 3 == 0)
				if(Math.abs(x-ground) < Math.abs(y-sround))
					newvec = new Vector3f(ground, y, z);
				else
					newvec = new Vector3f(x, sround, z);
			else if(ground % 3 == 0)
				newvec = new Vector3f(ground, y, z);
			else
				newvec = new Vector3f(x, sround, z);
		}
		else if(greatest == 0 && smallest == 2) //xz
		{
			int ground = StrictMath.round(x);
			int sround = StrictMath.round(z);
			
			if(ground % 3 == 0 && sround % 3 == 0)
				if(Math.abs(x-ground) < Math.abs(z-sround))
					newvec = new Vector3f(ground, y, z);
				else
					newvec = new Vector3f(x, y, sround);
			else if(ground % 3 == 0)
				newvec = new Vector3f(ground, y, z);
			else
				newvec = new Vector3f(x, y, sround);
		}
		else if(greatest == 1 && smallest == 0) //yx
		{
			int ground = StrictMath.round(y);
			int sround = StrictMath.round(x);
			
			if(ground % 3 == 0 && sround % 3 == 0)
				if(Math.abs(y-ground) < Math.abs(x-sround))
					newvec = new Vector3f(x, ground, z);
				else
					newvec = new Vector3f(sround, y, z);
			else if(ground % 3 == 0)
				newvec = new Vector3f(x, ground, z);
			else
				newvec = new Vector3f(sround, y, z);
		}
		else if(greatest == 1 && smallest == 2) //yz
		{
			int ground = StrictMath.round(y);
			int sround = StrictMath.round(z);
			
			if(ground % 3 == 0 && sround % 3 == 0)
				if(Math.abs(y-ground) < Math.abs(z-sround))
					newvec = new Vector3f(x, ground, z);
				else
					newvec = new Vector3f(x, y, sround);
			else if(ground % 3 == 0)
				newvec = new Vector3f(x, ground, z);
			else
				newvec = new Vector3f(x, y, sround);
		}
		else if(greatest == 2 && smallest == 0) //zx
		{
			int ground = StrictMath.round(z);
			int sround = StrictMath.round(x);
			
			if(ground % 3 == 0 && sround % 3 == 0)
				if(Math.abs(z-ground) < Math.abs(x-sround))
					newvec = new Vector3f(x, y, ground);
				else
					newvec = new Vector3f(sround, y, z);
			else if(ground % 3 == 0)
				newvec = new Vector3f(x, y, ground);
			else
				newvec = new Vector3f(sround, y, z);
		}
		else if(greatest == 2 && smallest == 1) //zy
		{
			int ground = StrictMath.round(z);
			int sround = StrictMath.round(y);
			
			if(ground % 3 == 0 && sround % 3 == 0)
				if(Math.abs(z-ground) < Math.abs(y-sround))
					newvec = new Vector3f(x, y, ground);
				else
					newvec = new Vector3f(x, sround, z);
			else if(ground % 3 == 0)
				newvec = new Vector3f(x, y, ground);
			else
				newvec = new Vector3f(x, sround, z);
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
