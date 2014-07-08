package com.celestial.SinglePlayer.Components.Empire;

import java.util.ArrayList;

import com.celestial.SinglePlayer.Inventory.InventorySlot;
import com.cubes.Vector3i;

public abstract class Building {

	Empire owner;
	int level = 1;
	Vector3i location;
	Vector3i size;
	
	ArrayList<InventorySlot> resourcesReq = new ArrayList<InventorySlot>();
	
	public Building(Empire emp, Vector3i location, Vector3i size) {
		owner = emp;
		this.location = location;
		this.size = size;
		this.resourcesReq = setResourcesReq();
	}
	public Building(Empire emp, Vector3i location, Vector3i size, int level) {
		owner = emp;
		this.location = location;
		this.size = size;
		this.level = level;
		this.resourcesReq = setResourcesReq();
	}
	
	public abstract void onUpdate();
	public abstract ArrayList<InventorySlot> setResourcesReq();
	
	public ArrayList<InventorySlot> getResourcesReq() {
		return this.resourcesReq;
	}
	public Empire getEmpire() {
		return owner;
	}
	public int getLevel() {
		return level;
	}
	public Vector3i[] getLocationSquared() {
		return new Vector3i[]{location, size};
	}
}
