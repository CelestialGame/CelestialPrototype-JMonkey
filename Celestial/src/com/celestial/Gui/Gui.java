/**
@author	Mitch Talmadge
Date Created:
	Jun 9, 2013
*/

package com.celestial.Gui;

import com.celestial.Celestial;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.font.BitmapFont;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Gui implements ScreenController {

	private Nifty nifty;
	private Celestial parent;

	public Gui(Celestial parent, AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort, FlyByCamera flyCam)
	{
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
		
		this.parent = parent;
		
		nifty = niftyDisplay.getNifty();
        nifty.fromXml("assets/gui/Gui.xml", "start", this);

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
	}

	@Override
	public void bind(Nifty arg0, Screen arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub
		
	}
	
	public void actionPerformed(String id)
	{
		switch(Integer.parseInt(id))
		{
		case 1: //SP
			parent.startGame(Celestial.SINGLEPLAYER);
			break;
		case 2: //MP
			parent.startGame(Celestial.MULTIPLAYER);
			break;
		case 3: //Options
			break;
		case 4: //Credits
			break;
		case 5: //Quit
			Celestial.app.stop();
			break;
		default:
			break;
		}
	}
	
	public void hoverPerformed(String id)
	{
		switch(Integer.parseInt(id))
		{
		case 1: //SP
			break;
		case 2: //MP
			break;
		case 3: //Options
			break;
		case 4: //Credits
			break;
		case 5: //Quit
			break;
		default:
			break;
		}
	}
	
}
