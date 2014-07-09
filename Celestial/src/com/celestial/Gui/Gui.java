/**
@author	Mitch Talmadge
Date Created:
	Jun 9, 2013
 */

package com.celestial.Gui;

import com.celestial.Celestial;
import com.celestial.Blocks.GameBlock;
import com.celestial.SinglePlayer.Inventory.InventorySlot;
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
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Gui implements ScreenController {

	private Nifty nifty;
	private Celestial parent;

	private FlyByCamera flyCam;
	private InputManager inputManager;
	private Picture invselected;
	private int width;
	private int height;
	private Element inventoryPopup;
	
	private Element workbenchPopup;
	private Element furnacePopup;
	private Element buildmenuPopup;
	
	private Console console;
	
	private int gameToStart;
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

		/*invhotslot0 = new Picture("invhotslot0");        
		invhotslot1 = new Picture("invhotslot1");        
		invhotslot2 = new Picture("invhotslot2");        
		invhotslot3 = new Picture("invhotslot3");        
		invhotslot4 = new Picture("invhotslot4");        
		invhotslot5 = new Picture("invhotslot5");        
		invhotslot6 = new Picture("invhotslot6");        
		invhotslot7 = new Picture("invhotslot7");     
		invhotslot8 = new Picture("invhotslot8");
		invhotslot9 = new Picture("invhotslot9");*/
	}

	@Override
	public void bind(Nifty arg0, Screen arg1) {}

	@Override
	public void onEndScreen() {
	}

	@Override
	public void onStartScreen() {
		if(this.nifty.getCurrentScreen().getScreenId().equals("hud"))
		{
			
			    //final Droppable droppable = this.nifty.getCurrentScreen().findNiftyControl("hotslot0drop", Droppable.class);
			/* INIT HUD */
			parent.getGuiNode().attachChild(invselected);
			/*parent.getGuiNode().attachChild(this.invhotslot0);
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
				p.setPosition(this.nifty.getCurrentScreen().findElementByName("hotslot"+i+"pane").getX(), 5);
			}*/
			this.setHotBarSelection(0);
			
			inventoryPopup = this.nifty.createPopup("Inventory");
			furnacePopup = this.nifty.createPopup("Furnace");
			workbenchPopup = this.nifty.createPopup("Workbench");
			buildmenuPopup = this.nifty.createPopup("BuildMenu");
			
			
			this.console = new Console(this.parent, this);
			this.console.bind(nifty, this.nifty.getCurrentScreen());
			
		}
		else if(this.nifty.getCurrentScreen().getScreenId().equals("loadgame"))
		{
			parent.startGame(gameToStart);
			nifty.gotoScreen("hud");
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
	
	public void enableInput() {
		Celestial.getPortal().getInputControl().enable();
	}
	public void disableInput() {
		Celestial.getPortal().getInputControl().disable();
	}

	@NiftyEventSubscriber(pattern = "space-.*")
	public void onInventoryItemMoved(final String id, final DroppableDroppedEvent event) {
		
	}
	@NiftyEventSubscriber(id = "console")
	public void onConsoleCommand(final String id, final ConsoleExecuteCommandEvent command) {
		if(this.console != null) {
			this.console.onConsoleCommand(id, command);
		}
	}
	
	public void setHotBarSelection(int slot) {
		if(this.nifty.getCurrentScreen().findElementByName("hotslot"+slot+"drop") != null)
			invselected.setPosition(this.nifty.getCurrentScreen().findElementByName("hotslot"+slot+"drop").getX()-5, 0);
	}

	public void setHotBarIcon(int pos, final InventorySlot slot)
	{
		Element dragSlot = this.nifty.getCurrentScreen().findElementByName("hotslot"+pos+"drop");
		String imagePath;
		if(slot.getItem().getBlock() == null)
			imagePath = slot.getItem().getTool().getIconPath();
		else
			imagePath = slot.getItem().getBlock().getIconPath();
		if(imagePath == null || imagePath.equals("")) {
			imagePath = "assets/textures/inventory/icons/blank.png";
		}
		if(this.nifty.getCurrentScreen().findElementByName("hotslot"+pos) != null) {
			NiftyImage img;
			try {
				img = this.nifty.getRenderEngine().createImage(this.nifty.getCurrentScreen(), imagePath, false);
			} catch (AssetNotFoundException e) {
				imagePath = "assets/textures/inventory/icons/blank.png";
				img = this.nifty.getRenderEngine().createImage(this.nifty.getCurrentScreen(), imagePath, false);
			}
			Element drop = this.nifty.getCurrentScreen().findElementByName("hotslot"+pos);
			drop.getRenderer(ImageRenderer.class).setImage(img);
			this.nifty.getCurrentScreen().findElementByName("hotslot"+pos+"contents").getRenderer(TextRenderer.class).setText(""+slot.getNumberContents());
			return;
		}
		DraggableBuilder dB = new DraggableBuilder("hotslot"+pos);
		dB.childLayoutCenter();
		ImageBuilder ib = new ImageBuilder("hotslot"+pos+"img");
		ib.filename(imagePath);
		ib.childLayoutCenter();
		ib.text(new TextBuilder("hotslot"+pos+"contents"){{
			font("assets/fonts/visitor.fnt");
			text(""+slot.getNumberContents());
		}});
		
		dB.image(ib);
		try {
			dB.build(nifty, this.nifty.getCurrentScreen(), dragSlot);
		} catch (AssetNotFoundException e) {
			ib.filename("assets/textures/inventory/icons/blank.png");
			dB.build(nifty, this.nifty.getCurrentScreen(), dragSlot);
		}
		
		/*Picture p = null;
		if(parent.getGuiNode().getChild("invhotslot"+pos) != null)
			p = (Picture) parent.getGuiNode().getChild("invhotslot"+pos);
		else
			return;
		if(slot.getItem() == null)
			p.setCullHint(CullHint.Always);
		else
		{
			p.setCullHint(CullHint.Never);
			try
			{
				p.setImage(assetManager, slot.getItem().getBlock().getIconPath(), true);
			}
			catch(AssetNotFoundException e)
			{
				p.setImage(assetManager, "assets/textures/inventory/icons/grass.png", true);
			}
			catch(NullPointerException e)
			{
				p.setImage(assetManager, slot.getItem().getTool().getIconPath(), true);
			}
		}*/
	}
	private void syncHotBars(boolean popupClosing) {
		if(popupClosing)
			syncHotSpacetoBar();
		else
			syncHotBartoSpace();
	}
	private void syncHotSpacetoBar() {
		for(int i=0; i<9; i++) {
			Element hotbarDropSlot = this.nifty.getScreen("hud").findElementByName("hotslot"+i+"drop");
			Element hotspaceDrag = inventoryPopup.findElementByName("hotspace-"+i);
			if(hotspaceDrag != null) {
				NiftyImage img = inventoryPopup.findElementByName("hotspace-"+i+"img").getRenderer(ImageRenderer.class).getImage();
				final String txt  = inventoryPopup.findElementByName("hotspace-"+i+"contents").getRenderer(TextRenderer.class).getOriginalText();
			
				if(this.nifty.getScreen("hud").findElementByName("hotslot"+i) == null) {
					DraggableBuilder dB = new DraggableBuilder("hotslot"+i);
					dB.childLayoutCenter();
					ImageBuilder ib = new ImageBuilder("hotslot"+i+"img");
					ib.childLayoutCenter();
					ib.x("40px");
					ib.y("40px");
					ib.height("100%");
					ib.width("100%");
					ib.text(new TextBuilder("hotslot"+i+"contents"){{
						font("assets/fonts/visitor.fnt");
						text(txt);
					}});
					dB.image(ib);
					dB.build(nifty, this.nifty.getCurrentScreen(), hotbarDropSlot);
					inventoryPopup.findElementByName("hotspace-"+i+"img").getRenderer(ImageRenderer.class).setImage(img);
					continue;
				}
				this.nifty.getCurrentScreen().findElementByName("hotslot"+i+"img").getRenderer(ImageRenderer.class).setImage(img);
				this.nifty.getCurrentScreen().findElementByName("hotslot"+i+"contents").getRenderer(TextRenderer.class).setText(txt);
			}
		}
	}
	private void syncHotBartoSpace() {
		for(int i=0; i<10; i++) {
			
			Element hotspaceDropSlot = inventoryPopup.findElementByName("hotspace"+i);
			Element hotbarDrag = this.nifty.getCurrentScreen().findElementByName("hotslot"+i);
			if(hotbarDrag != null) {
				NiftyImage img = this.nifty.getCurrentScreen().findElementByName("hotslot"+i+"img").getRenderer(ImageRenderer.class).getImage();
				final String txt  = this.nifty.getCurrentScreen().findElementByName("hotslot"+i+"contents").getRenderer(TextRenderer.class).getOriginalText();
				
				if(inventoryPopup.findElementByName("hotspace-"+i) == null) {
					DraggableBuilder dB = new DraggableBuilder("hotspace-"+i);
					dB.childLayoutCenter();
					ImageBuilder ib = new ImageBuilder("hotspace-"+i+"img");
					ib.childLayoutCenter();
					ib.x("40px");
					ib.y("40px");
					ib.height("100%");
					ib.width("100%");
					ib.text(new TextBuilder("hotspace-"+i+"contents"){{
						font("assets/fonts/visitor.fnt");
						text(txt);
					}});
					dB.image(ib);
					dB.build(nifty, this.nifty.getCurrentScreen(), hotspaceDropSlot);
					inventoryPopup.findElementByName("hotspace-"+i+"img").getRenderer(ImageRenderer.class).setImage(img);
					continue;
				}
				inventoryPopup.findElementByName("hotspace-"+i+"img").getRenderer(ImageRenderer.class).setImage(img);
				inventoryPopup.findElementByName("hotspace-"+i+"contents").getRenderer(TextRenderer.class).setText(txt);
			} else {
				System.err.println("No item found in hotbar"+i);
				continue;
			}
		}
	}
	
	public void showPopup(PopupType type)
	{
		switch(type)
		{
		case INVENTORY:
			nifty.showPopup(nifty.getCurrentScreen(), inventoryPopup.getId(), null);
			syncHotBars(false);
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
			syncHotBars(true);
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
