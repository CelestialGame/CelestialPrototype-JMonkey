package com.celestial;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.celestial.Blocks.Blocks;
import com.celestial.Blocks.BlocksEnum;
import com.celestial.Gui.Gui;
import com.celestial.Input.InputControl;
import com.celestial.SinglePlayer.Components.Planet;
import com.celestial.SinglePlayer.Inventory.InventoryManager;
import com.celestial.SinglePlayer.Inventory.InventoryRegister;
import com.celestial.util.InventoryException;
import com.cubes.CubesSettings;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.ui.Picture;
import com.jme3.util.JmeFormatter;
import com.jme3.util.SkyFactory;

/**
 * test
 * @author kevint
 */
@SuppressWarnings("deprecation")
public class Celestial extends SimpleApplication{

	public static Application app;
	private static JmeCanvasContext context;
	public static int width;
	public static int height;
	public static String title;
	public static AssetManager assetManage;

	public static void main(String[] args) {
		Celestial.self = new Celestial();
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

		Celestial.self.setDisplayFps(false);
		Celestial.self.setDisplayStatView(false);

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JPopupMenu.setDefaultLightWeightPopupEnabled(false);
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				} catch(Exception e) {
					System.out.println("Error setting Java LAF: " + e);
				}
				Gui frame = new Gui(Celestial.self);
				frame.initGui();
				frame.setVisible(true);
				Celestial.gui = frame;
			}
		});
	}

	BitmapText posText;
	public BulletAppState bulletAppState;

	public Vector3f walkDirection;
	public boolean left = false, right = false, up = false, down = false;
	public CharacterControl player;

	public static float camHeight = 2f;

	public static Canvas canvas;

	private InputControl inputControl;

	public static Gui gui;

	private boolean playingGame; 

	public static Celestial self;
	public static CubesSettings csettings;

	InventoryManager invmanager;
	public List<Planet> planets;
	private float lastRotation;
	public BitmapText InvText;

	public Celestial() {
		Celestial.assetManage = this.assetManager;
	}

	public static void createNewCanvas(){
		AppSettings settings = new AppSettings(true);
		settings.setWidth(Celestial.width);
		settings.setHeight(Celestial.height);
		settings.setTitle(Celestial.title);

		app = Celestial.self;

		app.setPauseOnLostFocus(false);
		app.setSettings(settings);
		app.createCanvas();

		context = (JmeCanvasContext) app.getContext();
		canvas = context.getCanvas();
		canvas.setSize(settings.getWidth(), settings.getHeight());
	}

	@Override
	public void simpleInitApp() {
		startGame();
	}

	public void startGame()
	{    	
		getInputManager().setCursorVisible(true);

		csettings = new CubesSettings(app);
		csettings.setDefaultBlockMaterial("assets/textures/terrain.png");
		csettings.setChunkSizeX(16);
		csettings.setChunkSizeY(16);
		csettings.setChunkSizeZ(16);
		
		/** Set up Physics **/
		this.bulletAppState = new BulletAppState();
		this.stateManager.attach(this.bulletAppState);

		Blocks.init();
		
		this.guiNode.detachAllChildren();
		

		this.invmanager = new InventoryManager();

		InventoryRegister.RegisterBlocks(this.invmanager);
		
		try {
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.DIRT.getID()), -1, 1);
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.STONE.getID()), -1, 2);
			
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.COAL_ORE.getID()), -1, 3);
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.IRON_ORE.getID()), -1, 4);
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.COPPER_ORE.getID()), -1, 5);
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.TIN_ORE.getID()), -1, 6);
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.RAW_DIAMOND.getID()), -1, 7);
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.GOLD_ORE.getID()), -1, 8);
		} catch (InventoryException e) {
			//pass
		}
		
		initCrossHairs();
		initOtherHud();
		
		this.invmanager.setSelectedHotSlot(1);

		this.walkDirection = new Vector3f();
		this.left = false;
		this.right = false;
		this.up = false;
		this.down = false;

		this.planets = new ArrayList<Planet>();

		this.planets.add(new Planet(null, 3, new Vector3f(50,50,50)));

		this.inputControl = new InputControl(this, this.cam, this.inputManager);

		//Spatial sky = SkyFactory.createSky(this.assetManager, "assets/textures/nightsky.jpg", true);
		//sky.scale(-1, -1, 1);
		//this.rootNode.attachChild(sky);


		// You must add a light to make the model visible
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(10f, 10f, 10f));
		this.rootNode.addLight(sun);

		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 2f, 1);
		this.player = new CharacterControl(capsuleShape, 0.05f);
		this.player.setJumpSpeed(20);
		this.player.setFallSpeed(30);
		this.player.setGravity(50);
		this.player.setPhysicsLocation(new Vector3f(2, 24*3+2, 2));

		this.flyCam.setMoveSpeed(100);

		Node node = this.planets.get(0).getNode();
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(node);
		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0.0f);
		node.addControl(landscape);
		this.bulletAppState.getPhysicsSpace().add(node);
		node.setShadowMode(ShadowMode.CastAndReceive);
		this.rootNode.attachChild(node);
		this.bulletAppState.getPhysicsSpace().add(this.player);

		Celestial.gui.changeCard(Gui.GAME);



	}

	@Override
	public void simpleUpdate(float tpf) {
		if(this.invmanager.getSelectedHotSlot().getItem() != null) {
			int number = this.invmanager.getSelectedHotSlot().getNumberContents();
			if(number == -1) 
				this.InvText.setText("Selected Item: " + this.invmanager.getSelectedHotSlot().getItem().getName() + " - Contents: unlimited");
			else 
				this.InvText.setText("Selected Item: " + this.invmanager.getSelectedHotSlot().getItem().getName() + " - Contents: " + number);
		} else 
			this.InvText.setText("Selected Item: none");
		if(InputControl.statson) {
			Vector3f location = this.cam.getLocation();
			this.posText.setText("X: "+location.x + " Y: "+location.y+" Z: "+location.z);
		} else {
			this.posText.setText("");
		}
		if(this.bulletAppState.isEnabled()) {
			Vector3f camDir = this.cam.getDirection().clone().multLocal(0.6f);
			Vector3f camLeft = this.cam.getLeft().clone().multLocal(0.2f);
			this.walkDirection.set(0, 0, 0);
			if (this.left)  { this.walkDirection.addLocal(camLeft); }
			if (this.right) { this.walkDirection.addLocal(camLeft.negate()); }
			if (this.up)    { this.walkDirection.addLocal(camDir); }
			if (this.down)  { this.walkDirection.addLocal(camDir.negate()); }
			this.walkDirection.y = 0;
			if(this.up || this.down) {
				this.walkDirection.x = this.walkDirection.x/2;
				this.walkDirection.z = this.walkDirection.z/2;
			}
			this.player.setWalkDirection(this.walkDirection);
			this.cam.setLocation(new Vector3f(this.player.getPhysicsLocation().getX(), this.player.getPhysicsLocation().getY()+camHeight, this.player.getPhysicsLocation().getZ()));
			this.inputControl.renderBlockBorder();
		} else {
			//pass
		}
		this.bulletAppState.update(tpf);
		if(this.player.getPhysicsLocation().getY() <= -150 || this.cam.getLocation().getY() <= -150) {
			this.player.setPhysicsLocation(new Vector3f(0, 50, 0));
			this.cam.setLocation(new Vector3f(this.player.getPhysicsLocation().getX(), this.player.getPhysicsLocation().getY()+camHeight, this.player.getPhysicsLocation().getZ()));
		}
		if(this.planets.get(0) != null)
		{
			/*if(this.timer.getTimeInSeconds()-this.lastRotation > 0)
			{
				this.lastRotation = this.timer.getTimeInSeconds();
				planets.get(0).getNode().rotate(0.0001f*FastMath.DEG_TO_RAD, 0, 0);
			}*/
		}
		this.invmanager.refreshHotSlots();
		this.invmanager.getInvGui().updateHotBar();
	}

	@Override
	public void simpleRender(RenderManager rm) {

		//TODO: add render code
	}



	/** A centred plus sign to help the player aim. */
	protected void initCrossHairs() {
		this.guiFont = this.assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(this.guiFont, false);
		ch.setSize(this.guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+"); // crosshairs
		ch.setLocalTranslation( // center
				this.settings.getWidth() / 2 - this.guiFont.getCharSet().getRenderedSize() / 3 * 2,
				this.settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		this.guiNode.attachChild(ch);
	}
	
	protected void initOtherHud() {
		this.guiFont = this.assetManager.loadFont("Interface/Fonts/Default.fnt");
		this.posText = new BitmapText(this.guiFont, false);
		this.posText.setSize(this.guiFont.getCharSet().getRenderedSize());
		this.posText.setText("");
		this.posText.setLocalTranslation(450, this.posText.getLineHeight(), 0);
		this.guiNode.attachChild(this.posText);
		
		this.InvText = new BitmapText(this.guiFont, false);
		this.InvText.setSize(this.guiFont.getCharSet().getRenderedSize());
		this.InvText.setLocalTranslation(350, this.settings.getHeight() - this.InvText.getLineHeight(), 0);
		//this.guiNode.attachChild(this.InvText);
		
		
	}


	public void setCamSpeed(float speed)
	{
		this.flyCam.setMoveSpeed(speed);
	}

	public AppSettings getSettings()
	{
		return this.settings;
	}
	public InventoryManager getInventoryManager(){
		return this.invmanager;
	}
	public Node getguiNode() {
		return this.guiNode;
	}
	@Override
	public AssetManager getAssetManager() {
		return this.assetManager;
	}

}
