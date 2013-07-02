package com.celestial;

import java.awt.Canvas;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import com.celestial.Gui.Gui;
import com.celestial.SinglePlayer.SPPortal;
import com.celestial.util.FontLocator;
import com.celestial.util.TTFFontLoader;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.util.JmeFormatter;

import de.lessvoid.nifty.Nifty;

/**
 * test
 * @author kevint
 */
public class Celestial extends SimpleApplication{

	public static Application app;
	private static JmeCanvasContext context;
	public static int width;
	public static int height;
	public static String title;
	private static Celestial cel;

	public static void main(String[] args) {
		cel = new Celestial();
		cel.setShowSettings(false);
		
		JmeFormatter formatter = new JmeFormatter();

		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(formatter);

		Logger.getLogger("").removeHandler(Logger.getLogger("").getHandlers()[0]);
		Logger.getLogger("").addHandler(consoleHandler);

		try {
			Thread.sleep(500);
		} catch (InterruptedException ex) {
		}

		Celestial.width = 850;
		Celestial.height = 500;
		Celestial.title = "Celestial";

		cel.setDisplayFps(false);
		cel.setDisplayStatView(false);
		
		createNewCanvas();
		app.start();
	}

	public static Canvas canvas;

	public static Gui gui;

	public static final int SINGLEPLAYER = 0;
	public static final int MULTIPLAYER = 1;

	
	private static int type;

	public static CelestialPortal portal;
	private Nifty nifty;

	public Celestial() {

	}

	public static void createNewCanvas(){
		AppSettings settings = new AppSettings(true);
		settings.setWidth(Celestial.width);
		settings.setHeight(Celestial.height);
		settings.setTitle(Celestial.title);

		app = cel;
		app.setPauseOnLostFocus(false);
		app.setSettings(settings);
		app.createCanvas();
		
		context = (JmeCanvasContext) app.getContext();
		canvas = context.getCanvas();
		canvas.setSize(settings.getWidth(), settings.getHeight());
	}

	@Override
	public void simpleInitApp() {
		
		assetManager.registerLoader(TTFFontLoader.class, "ttf");
		
		gui = new Gui(
				this,
				assetManager,
				inputManager,
				audioRenderer,
				guiViewPort,
				flyCam);
	}

	public void startGame(int type)
	{    	
		if(type == Celestial.SINGLEPLAYER)
		{
			
			Celestial.portal = new SPPortal(
					this, 
					rootNode, 
					guiNode, 
					cam, 
					flyCam, 
					viewPort, 
					assetManager, 
					inputManager, 
					settings, 
					app, 
					timer,
					gui);
		}
		else
		{
			Celestial.portal = null;//new MPPortal(this);
		}
		
		if(portal != null)
		{
			flyCam.setEnabled(true);
			flyCam.setDragToRotate(false);
			inputManager.setCursorVisible(false);
			Celestial.portal.startGame();
		}
	}

	@Override
	public void simpleUpdate(float tpf) {
		if(portal != null)
			Celestial.portal.simpleUpdate(tpf);
	}

	@Override
	public void simpleRender(RenderManager rm) {
		if(portal != null)
			Celestial.portal.simpleRender(rm);
	}
	
	public static void toggleStats(boolean state) {
		cel.setDisplayFps(state);
		cel.setDisplayStatView(state);
	}

	public static CelestialPortal getPortal() {
		return portal;
	}
}
