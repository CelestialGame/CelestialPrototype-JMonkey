package com.celestial.SinglePlayer.Components;

import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Physics.Listener;
import com.jme3.bullet.BulletAppState;

public class Space
{
    
    private CelestialPortal portal;
    private BulletAppState bulletAppState;
    
    public Space(CelestialPortal portal)
    {
	this.portal = portal;
	this.bulletAppState = new BulletAppState();
	portal.getParent().getStateManager().attach(bulletAppState);
	this.bulletAppState.getPhysicsSpace().addCollisionListener(
		new Listener());
    }
    
    public BulletAppState getBulletAppState()
    {
	return bulletAppState;
    }
    
}
