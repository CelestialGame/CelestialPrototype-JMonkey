package com.celestial.Blocks;

import com.celestial.Gui.Gui.PopupType;
import com.celestial.SinglePlayer.SPPortal;

public class Block_Furnace extends com.cubes.Block implements DynamicBlock{
	
	boolean inUse = false;
	
	public Block_Furnace() {;}
	
	public boolean isInUse() {
		return inUse;
	}
	
	public void turnOn() {
		inUse = true;
	}
	public void turnOff() {
		inUse = false;
	}

	@Override
	public void actionSelected() {
		SPPortal.self.getGui().showPopup(PopupType.FURNACE);
		if(isInUse())
			turnOff();
		else
			turnOn();
		
	}
}
