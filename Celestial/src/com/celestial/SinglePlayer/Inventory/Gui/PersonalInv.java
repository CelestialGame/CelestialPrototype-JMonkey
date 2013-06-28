package com.celestial.SinglePlayer.Inventory.Gui;

import com.celestial.Celestial;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * TODO Put here a description of what this class does.
 *
 * @author kevint.
 *         Created Jun 28, 2013.
 */
public class PersonalInv implements ScreenController {
	private Nifty nifty;
	private Celestial parent;

	public PersonalInv(Celestial parent, AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort, FlyByCamera flyCam)
	{
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);

		// disable the fly cam
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
		
		this.parent = parent;
		
		nifty = niftyDisplay.getNifty();
        nifty.fromXml("assets/gui/PersonalInv.xml", "start", this);
        /*Element label = nifty.getCurrentScreen().findElementByName("Singleplayer");
        TextRenderer textRenderer = label.getRenderer(TextRenderer.class);
        textRenderer.setText("New Text brosef");*/

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
	}

	@Override
	public void bind(Nifty arg0, Screen arg1) {
		
	}

	@Override
	public void onEndScreen() {}

	@Override
	public void onStartScreen() {}
	
	public void actionPerformed(String id) {
		
	}
}
