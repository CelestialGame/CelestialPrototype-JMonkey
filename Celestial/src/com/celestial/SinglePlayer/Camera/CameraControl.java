package com.celestial.SinglePlayer.Camera;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Components.Planet;
import com.jme3.input.InputManager;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * Camera tid bits
 *
 * @author kevint.
 *         Created Dec 1, 2013.
 */
public class CameraControl {
	CelestialPortal parent;

	Camera cam;
	Vector3f walkDirection;

	public CameraControl(CelestialPortal parent, Camera cam, InputManager inputManager) {
		this.parent = parent;
		this.cam = cam;
	}
	
	public void init() {
		this.parent.walkDirection = new Vector3f();
		this.parent.up = this.parent.down = this.parent.left = this.parent.right = false;
	}

	public void onAnalog(String name, float value, float tpf) {
		if (name.equals("MOUSE_AXIS_LEFT")) {
			rotateCamera(value, tpf, this.parent.getCam().getUp());
		} else if (name.equals("MOUSE_AXIS_RIGHT")) {
			rotateCamera(-value, tpf, this.parent.getCam().getUp());
		} else if (name.equals("MOUSE_AXIS_UP")) {
			rotateCamera(-value, tpf, this.parent.getCam().getLeft());
		} else if (name.equals("MOUSE_AXIS_DOWN")) {
			rotateCamera(value, tpf, this.parent.getCam().getLeft());
		}
	}

	protected void rotateCamera(float value, float tpf, Vector3f axis) {
		float rotationSpeed = 1000;

		Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis(value, axis);

		Vector3f upDir = this.parent.getCam().getUp();
		Vector3f leftDir = this.parent.getCam().getLeft();
		Vector3f dir = this.parent.getCam().getDirection();

		mat.mult(upDir, upDir);
		mat.mult(leftDir, leftDir);
		mat.mult(dir, dir);

		Quaternion q = new Quaternion();
		q.fromAxes(leftDir, upDir, dir);
		q.normalizeLocal();

		this.parent.getCam().setAxes(q);

		dir = this.parent.getCam().getDirection();
		dir = dir.mult(20.0f);
		Vector3f invDir = new Vector3f(-dir.x, -dir.y, -dir.z);
		Vector3f centre = new Vector3f(0,0,0);
		Vector3f position = invDir.add(centre);
		this.parent.getCam().setLocation(position);
	}
	
	public void updateCamera(float tpf) {
		if(this.parent.player.getBulletAppState().isEnabled()) {
			//this.cam.setAxes(this.parent.player.getLocalLeft(), this.parent.player.getLocalUp(), this.parent.player.getLocalForward());
			
			Vector3f camDir = this.cam.getDirection().clone().divide(16);
			Vector3f camLeft = this.cam.getLeft().clone().divide(16);
			this.parent.walkDirection.set(0, 0, 0);
			if (this.parent.left)
				this.parent.walkDirection.addLocal(camLeft); 
			if (this.parent.right) 
				this.parent.walkDirection.addLocal(camLeft.negate());
			if (this.parent.up)    
				this.parent.walkDirection.addLocal(camDir);
			if (this.parent.down)  
				this.parent.walkDirection.addLocal(camDir.negate());

			if(this.parent.player.getPlanet() != null)
			{
				switch(this.parent.player.getCurrentFaceOfPlanet(this.parent.player.getPlanet()))
				{
				case Planet.TOP:
				case Planet.BOTTOM:
					this.parent.walkDirection.y = 0;
					break;
				case Planet.EAST:
				case Planet.WEST:
					this.parent.walkDirection.x = 0;
					break;
				case Planet.NORTH:
				case Planet.SOUTH:
					this.parent.walkDirection.z = 0;
					break;
				default:
					break;
				}
			}

			this.parent.player.setWalkDirection(this.parent.walkDirection);
			this.cam.setLocation(new Vector3f(this.parent.player.getLocation().getX(), this.parent.player.getLocation().getY(), this.parent.player.getLocation().getZ()));
			this.cam.setLocation(cam.getLocation().add(this.parent.player.getLocalUp().mult(this.parent.getCamHeight())));
		}

		this.parent.getParent().getListener().setLocation(this.cam.getLocation());
		this.parent.getParent().getListener().setRotation(this.cam.getRotation());
	}
}
