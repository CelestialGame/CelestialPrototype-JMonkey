/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
*/

package com.celestial.SinglePlayer;

import java.util.ArrayList;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.celestial.Blocks.Blocks;
import com.celestial.Blocks.BlocksEnum;
import com.celestial.Gui.Gui;
import com.celestial.SinglePlayer.Components.Planet;
import com.celestial.SinglePlayer.Components.Star;
import com.celestial.SinglePlayer.Input.InputControl;
import com.celestial.SinglePlayer.Inventory.InventoryManager;
import com.celestial.SinglePlayer.Inventory.InventoryRegister;
import com.celestial.util.InventoryException;
import com.cubes.CubesSettings;
import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowFilter;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.system.Timer;
import com.jme3.util.SkyFactory;

@SuppressWarnings("deprecation")
public class SPPortal extends CelestialPortal{
	
	private BitmapFont guiFont;
	private Star star;
	private Timer timer;
	private float lastRotation;
	public static final int SHADOWMAP_SIZE = 1024;
	public static float camHeight = 2f;

	public SPPortal(
			Celestial parent, 
			Node rootNode, 
			Node guiNode, 
			Camera cam, 
			FlyByCamera flyCam, 
			ViewPort viewPort, 
			AssetManager assetManager, 
			InputManager inputManager, 
			AppSettings settings, 
			Application app,
			Timer timer)
	{
		this.parent = parent;
		this.rootNode = rootNode;
		this.guiNode = guiNode;
		this.cam = cam;
		this.flyCam = flyCam;
		this.viewPort = viewPort;
		this.assetManager = assetManager;
		this.inputManager = inputManager;
		this.settings = settings;
		this.app = app;
		this.timer = timer;
	}

	@Override
	public void startGame() {

		this.csettings = new CubesSettings(Celestial.app);
		this.csettings.setDefaultBlockMaterial("assets/textures/terrain.png");
		this.csettings.setChunkSizeX(16);
		this.csettings.setChunkSizeY(16);
		this.csettings.setChunkSizeZ(16);

		/** Set up Physics **/
		this.bulletAppState = new BulletAppState();
		this.parent.getStateManager().attach(this.getBulletAppState());

		Blocks.init();

		this.guiNode.detachAllChildren();


		this.invmanager = new InventoryManager(this);

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
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.GRASS.getID()), -1, 9);
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
		this.planets.add(new Planet(null, 3, new Vector3f(5000,10000,-3000)));

		this.inputControl = new InputControl(this, this.cam, this.inputManager);

		Spatial sky = SkyFactory.createSky(this.assetManager, "assets/textures/nightsky.jpg", true);
		sky.rotate(270*FastMath.DEG_TO_RAD,0,0);
		this.rootNode.attachChild(sky);


		// You must add a light to make the model visible
		this.star = new Star(null, new Vector3f(0,0,0));
		this.rootNode.attachChild(this.star.getStarNode());
		
		/** TODO: lighting fix -_- :P **/
		initLighting();

		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 2f, 1);
		this.player = new CharacterControl(capsuleShape, 0.05f);
		this.player.setJumpSpeed(20);
		this.player.setFallSpeed(30);
		this.player.setGravity(50);
		this.player.setPhysicsLocation(this.planets.get(0).getSpawnLocation());
		this.player.getCollisionGroup();
		this.flyCam.setMoveSpeed(100);
		this.cam.setFrustumFar(65000);

		Node terrnode = this.planets.get(0).getTerrainNode();
		Node planetnode = this.planets.get(0).getPlanetNode();
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(terrnode);
		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0.0f);
		terrnode.addControl(landscape);
		this.bulletAppState.getPhysicsSpace().add(terrnode);
		this.rootNode.attachChild(planetnode);
		this.bulletAppState.getPhysicsSpace().add(this.player);
		//Celestial.gui.changeCard(Gui.GAME);
		
		this.rootNode.setShadowMode(ShadowMode.Off);
		terrnode.setShadowMode(ShadowMode.CastAndReceive);
		
		//initAudio();
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		updateCamera(tpf);
		updateStats(tpf);
		
		updateGravity(tpf);
		
		/**
		 * TODO: Rotation
		 */
		
		/*if(this.planets.get(0) != null)
		{
			if(this.timer.getTimeInSeconds()-this.lastRotation > 0)
			{
				this.lastRotation = this.timer.getTimeInSeconds();
				this.planets.get(0).rotate();
			}
		}*/
		this.invmanager.refreshHotSlots();
	}
	
	private void updateGravity(float tpf) {
		
	}
	
	private void initLighting() {	  
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		rootNode.addLight(al);
		
		this.rootNode.addLight(this.star.getLight());
		
	    PointLightShadowRenderer plsr = new PointLightShadowRenderer(this.assetManager, SHADOWMAP_SIZE);
        plsr.setLight(this.star.getLight());
        plsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        //plsr.setFlushQueues(false);
        //plsr.displayFrustum();
        //plsr.displayDebug();
        this.viewPort.addProcessor(plsr);
        
        PointLightShadowFilter plsf = new PointLightShadowFilter(this.assetManager, SHADOWMAP_SIZE);
        plsf.setLight(this.star.getLight());     
        plsf.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        plsf.setEnabled(true);

        FilterPostProcessor fpp = new FilterPostProcessor(this.assetManager);
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bloom);
        fpp.addFilter(plsf);
        
        this.viewPort.addProcessor(fpp);
	}
	
	public void initAudio() {
		/* nature sound - keeps playing in a loop. */
	    AudioNode audio_ambient = new AudioNode(this.assetManager, "assets/sounds/ambient/DST-ArcOfDawn.ogg", false);
	    audio_ambient.setLooping(true);  // activate continuous playing
	    audio_ambient.setPositional(false);
	    //audio_ambient.setLocalTranslation(Vector3f.ZERO.clone());
	    audio_ambient.setVolume(3);
	    this.rootNode.attachChild(audio_ambient);
	    audio_ambient.play(); // play continuously!
	}
	
	public void updateCamera(float tpf) {
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
		this.parent.getListener().setLocation(this.cam.getLocation());
		this.parent.getListener().setRotation(this.cam.getRotation());
		
		/*if(this.player.getPhysicsLocation().getY() <= -150 || this.cam.getLocation().getY() <= -150) {
			this.player.setPhysicsLocation(this.planets.get(0).getSpawnLocation());
			this.cam.setLocation(new Vector3f(this.player.getPhysicsLocation().getX(), this.player.getPhysicsLocation().getY()+camHeight, this.player.getPhysicsLocation().getZ()));
		}*/
	}
	public void updateStats(float tpf) {
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
	}
		
	protected void initCrossHairs() {
		this.guiFont = this.parent.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
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

	@Override
	public CharacterControl getPlayer() {
		return this.player;
	}

	@Override
	public void simpleRender(RenderManager rm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getCamHeight() {
		return camHeight;
	}
	
}
