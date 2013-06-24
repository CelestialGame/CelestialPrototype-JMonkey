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
		//TODO: Multiplayer...
	}
	
	@Override
	public void simpleUpdate(float tpf) {}

	@Override
	public void startGame() {}

	@Override
	public CharacterControl getPlayer() {
		return null;
	}

	@Override
	public void simpleRender(RenderManager rm) {}

	@Override
	public float getCamHeight() {
		return 0;
	}

}
