/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer.Components;

import java.util.Vector;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Components.Planet.Planet;
import com.celestial.SinglePlayer.Components.Planet.PlanetFace;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

public class Player extends BetterCharacterControl {

	private CelestialPortal portal;
	private Galaxy galaxy;
	private Sector sector;
	private SolarSystem system;
	private Camera cam;
	private Planet planet;
	private Spatial playerSpatial;
	private Node playerNode;
	private String name;
	private Vector3f translationUpdate = null;
	private BulletAppState bulletAppState;
	private static float mass = 50f;
	private final static float jump = 10f;

	public Player(CelestialPortal portal, String name) {
		//super(new CapsuleCollisionShape((portal.csettings.getBlockSize() / 2), portal.csettings.getBlockSize() * 2f),
		//		0.05f);
		super(2f, 150f, mass);
		this.portal = portal;
		
		playerSpatial = portal.getAssetManager().loadModel("assets/models/player/simpleplayer.blend");
		playerSpatial.scale(0.5f, 1.5f, 1);
		playerSpatial.scale(0.5f);
		//this.setSpatial(portal.getAssetManager().loadModel("assets/models/player/simpleplayer.blend"));
		playerNode = (Node) playerSpatial;
		playerNode.setName("Player");
		playerNode.addControl(this);
		this.getNode().setName("Player");
		this.getNode().addControl(this);
		//this.setJumpSpeed(25f);
		//this.setFallSpeed(20f);
		portal.getRootNode().attachChild(this.getNode());
		this.cam = portal.cam;
		this.name = name;
		this.translationUpdate = null;
		this.setApplyPhysicsLocal(true);
		this.setBulletAppState(portal.galaxy.getSpace().getBulletAppState());
	}

	public void setVisibleToClient(boolean visible) {
		if (visible)
			this.playerSpatial.setCullHint(CullHint.Never);
		else
			this.playerSpatial.setCullHint(CullHint.Always);
	}

	public void setLocation(Vector3f location) {
		this.setPhysicsLocation(location);
	}

	public Vector3f getLocation()
	{
		//return this.getPhysicsLocation();
		return this.getSpatialTranslation();
	}

	public Quaternion getRotation() {
		return this.cam.getRotation();
	}

	public Vector3f getLocalUp() {
		return this.cam.getUp();
	}

	public Vector3f getLocalLeft() {
		return this.cam.getLeft();
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (getPlanet() != null) {

			Vector3f translation = getLocation();

			if (this.translationUpdate != null) {
				translation.addLocal(translationUpdate);
				this.translationUpdate = null;
			}
			translation.addLocal(walkDirection);

			this.setLocation(translation);
		}
	}

	public void spawnPlayer(Planet planet, int face) {
		this.setLocation(planet.getSpawnLocation(face));
	}

	public Vector3f getSpawnLocation(Planet planet, int face) {
		return planet.getSpawnLocation(face);
	}

	public void setGalaxy(Galaxy galaxy) {
		this.galaxy = galaxy;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public void setSystem(SolarSystem system) {
		this.system = system;
	}

	public void setPlanet(Planet planet) {
		if (planet != null) {
			this.setBulletAppState(planet.getBulletAppState());
			planet.getPlanetNode().attachChild(this.getNode());
		} else {
			if (this.planet != null) {
				this.setBulletAppState(portal.galaxy.getSpace().getBulletAppState());
			}
		}
		this.planet = planet;
	}

	public Galaxy getGalaxy() {
		return galaxy;
	}

	public Sector getSector() {
		return sector;
	}

	public SolarSystem getSystem() {
		return system;
	}

	public Planet getPlanet() {
		return planet;
	}

	/**
	 * Used if we ever switch back from BetterCharacterControl. Currently this
	 * is handled by BetterCharacterControl.
	 * 
	 * @param face
	 */
	public void rotatePlayer(PlanetFace face) {
		Quaternion q = new Quaternion();
		int x = 0, y = 0, z = 0;
		switch (face) {
		case TOP:
			x = 0;
			y = 0;
			z = 0;
			this.spatial.rotateUpTo(new Vector3f(0, 1, 0));
			break;
		case BOTTOM:
			x = 0;
			y = 180;
			z = 0;
			this.spatial.rotateUpTo(new Vector3f(0, -1, 0));
			break;
		case NORTH:
			x = 0;
			y = 0;
			z = 90;
			this.spatial.rotateUpTo(new Vector3f(0, 0, 1));
			break;
		case SOUTH:
			x = 0;
			y = 0;
			z = 270;
			this.spatial.rotateUpTo(new Vector3f(0, 0, -1));
			break;
		case EAST:
			x = 90;
			y = 0;
			z = 0;
			this.spatial.rotateUpTo(new Vector3f(1, 0, 0));
			break;
		case WEST:
			x = 270;
			y = 0;
			z = 0;
			this.spatial.rotateUpTo(new Vector3f(-1, 0, 0));
			break;
		default:
			return;
		}
		q.fromAngles(x * FastMath.DEG_TO_RAD, y * FastMath.DEG_TO_RAD, z * FastMath.DEG_TO_RAD);
		this.spatial.setLocalRotation(q);
	}

	public Planet getClosestPlanet() {
		float closestdist = -1;
		Planet closestplanet = null;
		for (Planet planet : getSystem().getPlanets()) {
			float distance = getLocation().distance(planet.getPlanetNode().getWorldTranslation());
			if (distance < closestdist || closestdist == -1) {
				closestdist = distance;
				closestplanet = planet;
			}
		}
		return closestplanet;
	}

	public float getDistanceFromPlanet(Planet planet) {
		if (planet != null) {
			return getLocation().distance(planet.getPlanetNode().getWorldTranslation());
		}
		return -1;
	}

	public float getDistanceFromPlanet(Planet planet, Vector3f location) {
		if (planet != null) {
			return location.distance(planet.getPlanetNode().getWorldTranslation());
		}
		return -1;
	}

	public boolean isInAtmosphere(Planet planet) {
		Vector3f difference = this.getLocation().subtract(planet.getCurrentPlanetTranslation());
		if (difference.getX() > -1 * (planet.getAtmosphereDiameter() / 2)
				&& difference.getX() < (planet.getAtmosphereDiameter() / 2)
				&& difference.getY() > -1 * (planet.getAtmosphereDiameter() / 2)
				&& difference.getY() < (planet.getAtmosphereDiameter() / 2)
				&& difference.getZ() > -1 * (planet.getAtmosphereDiameter() / 2)
				&& difference.getZ() < (planet.getAtmosphereDiameter() / 2))
			return true;
		else
			return false;
	}

	public boolean isWithinLoadingDistance(Planet planet) {
		if (this.getDistanceFromPlanet(planet) <= planet.getRadiusAsFloat(true) + 200)
			return true;
		return false;
	}

	public boolean isWithinLoadingDistance(Planet planet, Vector3f location) {
		if (this.getDistanceFromPlanet(planet, location) <= planet.getRadiusAsFloat(true) + 200)
			return true;
		return false;
	}

	public PlanetFace getCurrentFaceOfPlanet(Planet planet) {
		Vector3f P1 = planet.getOriginalPlanetTranslation();

		Vector3f playerP = null;
		if (this.getBulletAppState().isEnabled())
			playerP = getLocation();
		else
			playerP = portal.getCam().getLocation();

		Vector3f rot1P = planet.getStarNode().getLocalRotation().inverse().mult(playerP);
		Vector3f transP = rot1P.subtract(P1);
		Vector3f rot2P = planet.getPlanetNode().getLocalRotation().inverse().mult(transP);

		float x = rot2P.x;
		float y = rot2P.y;
		float z = rot2P.z;

		if (Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z)) {
			if (y < 0) {
				return PlanetFace.BOTTOM;
			} else {
				return PlanetFace.TOP;
			}
		} else if (Math.abs(x) > Math.abs(z)) {
			if (x < 0) {
				return PlanetFace.WEST;
			} else {
				return PlanetFace.EAST;
			}
		} else if (Math.abs(z) > Math.abs(x)) {
			if (z < 0) {
				return PlanetFace.NORTH;
			} else {
				return PlanetFace.SOUTH;
			}
		} else {
			return PlanetFace.UNKNOWN;
		}
	}

	public boolean isInSpace() {
		return this.getPlanet() == null;
	}

	public CelestialPortal getPortal() {
		return portal;
	}

	public Node getNode() {
		return this.playerNode;
	}

	public Spatial getSpatial() {
		return this.playerSpatial;
	}

	public String getName() {
		return this.name;
	}

	public BulletAppState getBulletAppState() {
		return this.bulletAppState;
	}

	public void setBulletAppState(BulletAppState state) {
		if (this.bulletAppState != null) {
			this.bulletAppState.getPhysicsSpace().remove(this);
		}
		this.bulletAppState = state;
		this.bulletAppState.getPhysicsSpace().add(this);
		this.bulletAppState.getPhysicsSpace().addAll(this.getNode());
	}

	public void setTranslationUpdate(Vector3f translation) {
		this.translationUpdate = translation;
	}
	
	public float getMass() {
		return Player.mass;
	}
	public float getJumpMagnitude() {
		return jump;
	}

}
