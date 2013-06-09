package com.celestial;

import java.awt.Canvas;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.celestial.Gui.Gui;
import com.celestial.MultiPlayer.MPPortal;
import com.celestial.SinglePlayer.SPPortal;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.util.JmeFormatter;

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

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JPopupMenu.setDefaultLightWeightPopupEnabled(false);
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				} catch(Exception e) {
					System.out.println("Error setting Java LAF: " + e);
				}
				Gui frame = new Gui(cel);
				frame.initGui();
				frame.setVisible(true);
				Celestial.gui = frame;
			}
		});
	}

	public static Canvas canvas;

	public static Gui gui;

	public static final int SINGLEPLAYER = 0;
	public static final int MULTIPLAYER = 1;

	
	private static int type;

	public static CelestialPortal portal;

	public Celestial() {
	}

	public static void createNewCanvas(int type){
		AppSettings settings = new AppSettings(true);
		settings.setWidth(Celestial.width);
		settings.setHeight(Celestial.height);
		settings.setTitle(Celestial.title);

		app = cel;
		
		Celestial.type = type;

		app.setPauseOnLostFocus(false);
		app.setSettings(settings);
		app.createCanvas();

		context = (JmeCanvasContext) app.getContext();
		canvas = context.getCanvas();
		canvas.setSize(settings.getWidth(), settings.getHeight());
	}

	@Override
	public void simpleInitApp() {
		if(type == Celestial.SINGLEPLAYER)
		{
			
			Celestial.portal = new SPPortal(this, rootNode, guiNode, cam, flyCam, viewPort, assetManager, inputManager, settings, app);
		}
		else
		{
			Celestial.portal = new MPPortal(this);
		}
		startGame();
	}

	public void startGame()
	{    	
		if(portal != null)
			Celestial.portal.startGame();
	}

	@Override
	public void simpleUpdate(float tpf) {
		Celestial.portal.simpleUpdate(tpf);
	}

	@Override
	public void simpleRender(RenderManager rm) {
		Celestial.portal.simpleRender(rm);
	}
}
