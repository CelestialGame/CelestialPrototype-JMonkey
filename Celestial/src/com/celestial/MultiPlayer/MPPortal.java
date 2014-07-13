/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.MultiPlayer;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.celestial.SinglePlayer.Components.Player;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.InputManager;
import com.jme3.renderer.RenderManager;

@SuppressWarnings("deprecation")
public class MPPortal extends CelestialPortal
{
    
    private Celestial parent;
    
    public MPPortal(Celestial parent)
    {
	this.parent = parent;
	// TODO: Multiplayer...
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
    }
    
    @Override
    public void startGame()
    {
    }
    
    @Override
    public void stopGame()
    {
    }
    
    @Override
    public Player getPlayer()
    {
	return null;
    }
    
    @Override
    public void simpleRender(RenderManager rm)
    {
    }
    
    @Override
    public InputManager getInputManager()
    {
	return null;
    }
    
    @Override
    public Object[] getNiftyUtils()
    {
	return null;
    }
    
    @Override
    public void hideHighlight()
    {
	// TODO Auto-generated method stub
	
    }
    
}
