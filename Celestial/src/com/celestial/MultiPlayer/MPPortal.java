/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.MultiPlayer;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.renderer.RenderManager;

@SuppressWarnings("deprecation")
public class MPPortal extends CelestialPortal {

	private Celestial parent;
	
	public MPPortal(Celestial parent)
	{
		this.parent = parent;
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CharacterControl getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void simpleRender(RenderManager rm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getCamHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
