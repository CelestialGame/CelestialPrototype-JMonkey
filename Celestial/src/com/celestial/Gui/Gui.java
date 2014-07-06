/**
@author	Mitch Talmadge
Date Created:
	Jun 9, 2013
 */

package com.celestial.Gui;

import com.celestial.Celestial;
import com.celestial.Blocks.GameBlock;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Gui implements ScreenController {

	private Nifty nifty;
	private Celestial parent;

	private FlyByCamera flyCam;
	private InputManager inputManager;
	private AssetManager assetManager;
	private Picture invselected;
	private int width;
	private int height;
	private Picture invhotslot0;
	private Picture invhotslot1;
	private Picture invhotslot2;
	private Picture invhotslot3;
	private Picture invhotslot4;
	private Picture invhotslot5;
	private Picture invhotslot6;
	private Picture invhotslot7;
	private Picture invhotslot8;
	private Picture invhotslot9;
	private Element inventoryPopup;
	
	private Element workbenchPopup;
	private Element furnacePopup;
	private Element buildmenuPopup;
	
	private int gameToStart;
	private Node StarNode;
	private Geometry StarGeometry;
	private PointLight light;
	
	public static enum PopupType {
		INVENTORY, WORKBENCH, FURNACE, BUILD;
	}
	
	
	public Gui(Celestial parent, AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort, FlyByCamera flyCam)
	{
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
				inputManager,
				audioRenderer,
				guiViewPort);

		this.width = Celestial.canvas.getWidth();
		this.height = Celestial.canvas.getHeight();

		this.flyCam = flyCam;
		this.inputManager = inputManager;
		this.assetManager = assetManager;
		
		// disable the fly cam
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
		
		this.parent = parent;
		
		nifty = niftyDisplay.getNifty();
        nifty.fromXml("assets/gui/Gui.xml", "start", this);
		// disable the fly cam
		flyCam.setEnabled(false);
		flyCam.setDragToRotate(true);
		inputManager.setCursorVisible(true);

		guiViewPort.addProcessor(niftyDisplay);

		/* IMAGES */
		invselected = new Picture("invselected");
		invselected.setImage(assetManager, "assets/gui/images/invselected.png", true);
		invselected.setWidth(50);
		invselected.setHeight(50);

		invhotslot0 = new Picture("invhotslot0");        
		invhotslot1 = new Picture("invhotslot1");        
		invhotslot2 = new Picture("invhotslot2");        
		invhotslot3 = new Picture("invhotslot3");        
		invhotslot4 = new Picture("invhotslot4");        
		invhotslot5 = new Picture("invhotslot5");        
		invhotslot6 = new Picture("invhotslot6");        
		invhotslot7 = new Picture("invhotslot7");     
		invhotslot8 = new Picture("invhotslot8");
		invhotslot9 = new Picture("invhotslot9");
		
		//Make cute star for main menu
		
		this.StarNode = new Node();
		Box starbox = new Box(.5f,.5f,.5f);
		this.StarGeometry = new Geometry("Star", starbox);
		Material mat = new Material(this.assetManager,  // Create new material and...
			    "Common/MatDefs/Light/Lighting.j3md");
		mat.setColor("Diffuse", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("Ambient", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("Specular", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("GlowColor", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setFloat("Shininess", 5f);
		mat.setBoolean("UseMaterialColors",true);
		this.StarGeometry.setMaterial(mat);
		this.StarNode.attachChild(this.StarGeometry);
		this.StarNode.setQueueBucket(Bucket.Opaque);
		this.light = new PointLight();
		light.setPosition(this.StarNode.getWorldTranslation());
        light.setColor(ColorRGBA.White);
		this.StarNode.addLight(light);
	}

	@Override
	public void bind(Nifty arg0, Screen arg1) {}

	@Override
	public void onEndScreen() {
		if(this.nifty.getCurrentScreen().getScreenId().equals("start"))
		{
			parent.getGuiNode().detachChild(this.StarNode);
		}
	}

	@Override
	public void onStartScreen() {
		if(this.nifty.getCurrentScreen().getScreenId().equals("hud"))
		{
			/* INIT HUD */
			parent.getGuiNode().attachChild(invselected);
			parent.getGuiNode().attachChild(this.invhotslot0);
			parent.getGuiNode().attachChild(this.invhotslot1);
			parent.getGuiNode().attachChild(this.invhotslot2);
			parent.getGuiNode().attachChild(this.invhotslot3);
			parent.getGuiNode().attachChild(this.invhotslot4);
			parent.getGuiNode().attachChild(this.invhotslot5);
			parent.getGuiNode().attachChild(this.invhotslot6);
			parent.getGuiNode().attachChild(this.invhotslot7);
			parent.getGuiNode().attachChild(this.invhotslot8);
			parent.getGuiNode().attachChild(this.invhotslot9);

			for(int i=0; i<10; i++)
			{
				Picture p = null;
				p = (Picture) parent.getGuiNode().getChild("invhotslot"+i);
				p.setCullHint(CullHint.Always);
				p.setWidth(40);
				p.setHeight(40);
				p.setPosition(this.nifty.getCurrentScreen().findElementByName("hotslot"+i).getX(), 5);
			}
			this.setHotBarSelection(0);
			
			inventoryPopup = this.nifty.createPopup("Inventory");
			furnacePopup = this.nifty.createPopup("Furnace");
			workbenchPopup = this.nifty.createPopup("Workbench");
			buildmenuPopup = this.nifty.createPopup("BuildMenu");
			
		}
		else if(this.nifty.getCurrentScreen().getScreenId().equals("loadgame"))
		{
			parent.startGame(gameToStart);
			nifty.gotoScreen("hud");
		}
		else if(this.nifty.getCurrentScreen().getScreenId().equals("start"))
		{
			parent.getGuiNode().attachChild(this.StarNode);
		}
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
		nifty.gotoScreen("loadgame");
		this.gameToStart = type;
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
		if(this.nifty.getCurrentScreen().findElementByName("hotslot"+slot) != null)
			invselected.setPosition(this.nifty.getCurrentScreen().findElementByName("hotslot"+slot).getX()-5, 0);
	}

	public void setHotBarIcon(int slot, GameBlock block)
	{
		Picture p = null;
		if(parent.getGuiNode().getChild("invhotslot"+slot) != null)
			p = (Picture) parent.getGuiNode().getChild("invhotslot"+slot);
		else
			return;
		if(block == null)
			p.setCullHint(CullHint.Always);
		else
		{
			p.setCullHint(CullHint.Never);
			try
			{
				p.setImage(assetManager, block.getIconPath(), true);
			}
			catch(AssetNotFoundException e)
			{
				p.setImage(assetManager, "assets/textures/inventory/icons/blank.png", true);
			}
		}
	}
	
	public void showPopup(PopupType type)
	{
		switch(type)
		{
		case INVENTORY:
			nifty.showPopup(nifty.getCurrentScreen(), inventoryPopup.getId(), null);
			this.disableControl();
			break;
		case FURNACE:
			nifty.showPopup(nifty.getCurrentScreen(), furnacePopup.getId(), null);
			this.disableControl();
			break;
		case WORKBENCH:
			nifty.showPopup(nifty.getCurrentScreen(), workbenchPopup.getId(), null);
			this.disableControl();
			break;
		case BUILD:
			nifty.showPopup(nifty.getCurrentScreen(), buildmenuPopup.getId(), null);
			this.disableControl();
			break;
		default:
			return;
		}
	}
	
	public void closePopup(PopupType type)
	{
		switch(type)
		{
		case INVENTORY:
			nifty.closePopup(inventoryPopup.getId());
			this.enableControl();
			break;
		case FURNACE:
			nifty.closePopup(furnacePopup.getId());
			this.enableControl();
			break;
		case WORKBENCH:
			nifty.closePopup(workbenchPopup.getId());
			this.enableControl();
			break;
		case BUILD:
			nifty.closePopup(buildmenuPopup.getId());
			this.enableControl();
			break;
		default:
			return;
		}
	}
	
	public void closePopup() {
		try {
			nifty.closePopup(nifty.getCurrentScreen().getTopMostPopup().getId());
			this.enableControl();
		} catch(Exception e) {
			;
		}
	}
}
