package com.celestial.SinglePlayer.Components.Empire;

import java.util.ArrayList;

import com.celestial.SinglePlayer.Inventory.InventorySlot;
import com.cubes.Vector3Int;

public abstract class Building {

	Empire owner;
	int level = 1;
	Vector3Int location;
	Vector3Int size;
	
	ArrayList<InventorySlot> resourcesReq = new ArrayList<InventorySlot>();
	
	public Building(Empire emp, Vector3Int location, Vector3Int size) {
		owner = emp;
		this.location = location;
		this.size = size;
		this.resourcesReq = setResourcesReq();
	}
	public Building(Empire emp, Vector3Int location, Vector3Int size, int level) {
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
	public Vector3Int[] getLocationSquared() {
		return new Vector3Int[]{location, size};
	}
}
