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
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Gui implements ScreenController {

	private Nifty nifty;
	private Celestial parent;
	
	private FlyByCamera flyCam;
	private InputManager inputManager;

	public Gui(Celestial parent, AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort, FlyByCamera flyCam)
	{
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);

		this.flyCam = flyCam;
		this.inputManager = inputManager;
		
		// disable the fly cam
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
		
		this.parent = parent;
		
		nifty = niftyDisplay.getNifty();
        nifty.fromXml("assets/gui/Gui.xml", "start", this);
        
        guiViewPort.addProcessor(niftyDisplay);
	}

	@Override
	public void bind(Nifty arg0, Screen arg1) {}

	@Override
	public void onEndScreen() {
	}

	@Override
	public void onStartScreen() {
	}
	
	public void actionPerformed(String id)
	{
		if(this.nifty.getCurrentScreen().getScreenId().equals("start"))
			switch(Integer.parseInt(id))
			{
			case 1: //SP
				this.startGame(Celestial.SINGLEPLAYER);
				break;
			case 2: //MP
				this.startGame(Celestial.MULTIPLAYER);
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
		else if(this.nifty.getCurrentScreen().getScreenId().equals("hud"))
			switch(Integer.parseInt(id))
			{
			case 1:
				break;
			default:
				break;
			}
	}
	
	private void startGame(final int type) {
		nifty.gotoScreen("hud");
		parent.startGame(type);
	}

	public void hoverPerformed(String id)
	{
		if(this.nifty.getCurrentScreen().getScreenId().equals("start"))
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
		else if(this.nifty.getCurrentScreen().getScreenId().equals("hud"))
			switch(Integer.parseInt(id))
			{
			case 1:
				break;
			default:
				break;
			}
	}
	
	public void disable() {
		nifty.exit();
	}
	
	public Nifty getNifty() {
		return nifty;
	}
	
	public void disableControl() {
		flyCam.setEnabled(false);
		flyCam.setDragToRotate(true);
		inputManager.setCursorVisible(true);
	}
	
	public void enableControl() {
		flyCam.setEnabled(true);
		flyCam.setDragToRotate(false);
		inputManager.setCursorVisible(false);
	}
	
	public void setHotBarSelection(int slot) {
		/*for(int i = 0; i<=10; i++)
		{
			
		}*/
	}
}
