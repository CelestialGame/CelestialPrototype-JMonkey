package com.celestial.SinglePlayer.Components.Empire.Buildings;

import java.util.ArrayList;

import com.celestial.Blocks.GameBlock;
import com.celestial.SinglePlayer.Components.Empire.Building;
import com.celestial.SinglePlayer.Components.Empire.Empire;
import com.celestial.SinglePlayer.Inventory.InventoryItem;
import com.celestial.SinglePlayer.Inventory.InventorySlot;
import com.celestial.Tools.Tool;
import com.cubes.Vector3Int;

public class EmpireSquare extends Building{

	public EmpireSquare(Empire emp, Vector3Int location, Vector3Int size) {
		super(emp, location, size);
	}

	@Override
	public void onUpdate() {
		//sit there and wave? Not much to update xD
	}

	@Override
	public ArrayList<InventorySlot> setResourcesReq() {
		ArrayList<InventorySlot> res = new ArrayList<InventorySlot>();
		res.add(new InventorySlot(new InventoryItem(GameBlock.COBBLE), 5));
		res.add(new InventorySlot(new InventoryItem(Tool.COPPER_INGOT), 10));
		return res;
	}

}
