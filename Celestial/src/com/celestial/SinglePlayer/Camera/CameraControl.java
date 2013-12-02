package com.celestial.SinglePlayer.Camera;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Events.PlayerEvents;
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
		mat.fromAngleNormalAxis(rotationSpeed * value * tpf, axis);

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
		if(this.parent.getBulletAppState().isEnabled()) {
			//rotate cam
			
			
			//walk direction
			
			Vector3f camDir = this.cam.getDirection().clone().multLocal(20);
			Vector3f camLeft = this.cam.getLeft().clone().multLocal(20);
			this.parent.walkDirection.set(0, 0, 0);
			if (this.parent.left)
				this.parent.walkDirection.addLocal(camLeft); 
			if (this.parent.right) 
				this.parent.walkDirection.addLocal(camLeft.negate());
			if (this.parent.up)    
				this.parent.walkDirection.addLocal(camDir);
			if (this.parent.down)  
				this.parent.walkDirection.addLocal(camDir.negate());

			this.parent.walkDirection.y = 0;

			if(this.parent.up || this.parent.down || this.parent.right || this.parent.left)
				PlayerEvents.PlayerMoveEvent(parent.player, parent.player.getLocation().add(this.parent.walkDirection));

			this.parent.player.setWalkDirection(this.parent.walkDirection);
			this.cam.setLocation(new Vector3f(this.parent.player.getLocation().getX(), this.parent.player.getLocation().getY()+parent.getCamHeight(), this.parent.player.getLocation().getZ()));
		}

		this.parent.getParent().getListener().setLocation(this.cam.getLocation());
		this.parent.getParent().getListener().setRotation(this.cam.getRotation());
	}
}
