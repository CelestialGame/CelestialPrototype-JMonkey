/**
@author	Mitch Talmadge
Date Created:
	Jun 2, 2013
 */

package com.celestial.SinglePlayer;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.celestial.Blocks.Blocks;
import com.celestial.Gui.Gui;
import com.celestial.SinglePlayer.Components.Galaxy;
import com.celestial.SinglePlayer.Components.Planet;
import com.celestial.SinglePlayer.Components.Player;
import com.celestial.SinglePlayer.Components.SectorCoord;
import com.celestial.SinglePlayer.Events.PlayerEvents;
import com.celestial.SinglePlayer.Input.InputControl;
import com.celestial.SinglePlayer.Inventory.InventoryManager;
import com.celestial.SinglePlayer.Inventory.InventoryRegister;
import com.celestial.SinglePlayer.Physics.Listener;
import com.celestial.World.Picker;
import com.cubes.BlockChunkControl;
import com.cubes.CubesSettings;
import com.cubes.Vector3Int;
import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.Timer;
import com.jme3.util.SkyFactory;

public class SPPortal extends CelestialPortal{

	private BitmapFont guiFont;
	private Timer timer;
	private float lastRotation;
	private boolean rotated = false;
	private FilterPostProcessor fpp;
	public static final int SHADOWMAP_SIZE = 1024;
	public static float camHeight = 4.9f;

	private Vector3f normalGravity = new Vector3f(0.0f, -9.81f, 0.0f);
	private Vector3f zeroGravity = new Vector3f(0.0f, 0.0f, 0.0f);
	private Box blockHighlight;
	private Geometry blockHighlightGeom;
	private Material blockHighlightMat;

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
			Timer timer, 
			Gui gui)
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
		this.gui = gui;
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

		/* Create Galaxy */

		this.galaxy = new Galaxy(this, 1, 1, 1);

		this.invmanager = new InventoryManager(this);

		InventoryRegister.RegisterBlocks(this.invmanager);

		/*try {
			this.invmanager.setHotSlot(this.invmanager.items.get(BlocksEnum.LEAVES.getID()), -1, 1);
		} catch (InventoryException e) {
			//pass
		}*/

		initCrossHairs();
		initOtherHud();

		this.invmanager.setSelectedHotSlot(0);

		this.walkDirection = new Vector3f();
		this.left = false;
		this.right = false;
		this.up = false;
		this.down = false;

		this.inputControl = new InputControl(this, this.cam, this.inputManager);

		Spatial sky = SkyFactory.createSky(this.assetManager, "assets/textures/nightsky.jpg", true);
		sky.rotate(270*FastMath.DEG_TO_RAD,0,0);
		this.rootNode.attachChild(sky);

		/** TODO: lighting fix -_- :P **/
		initLighting();


		this.player = new Player(this, "John Doe");
		this.player.setGalaxy(this.galaxy);
		this.player.setSector(this.galaxy.getSectorAt(0,0,0));
		this.player.setSystem(this.player.getSector().getSystem(0));
		this.player.setVisibleToClient(false);

		player.spawnPlayer(player.getSystem().getPlanet(0), 0);

		this.flyCam.setMoveSpeed(25);
		this.cam.setFrustumFar(65000);

		this.rootNode.setShadowMode(ShadowMode.Off);

		this.bulletAppState.getPhysicsSpace().addCollisionListener(new Listener(this));
		//initAudio();
		
		/* BLOCK HIGHLIGHT */		
		blockHighlight = new Box(1.5f,1.5f,1.5f);
		blockHighlightGeom = new Geometry("blockHighlight", this.blockHighlight);
		blockHighlightMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

		blockHighlightMat.setColor("Color", new ColorRGBA(0f, 0f, 0f, 0.25f));
		blockHighlightGeom.setMaterial(blockHighlightMat);

		blockHighlightMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		blockHighlightGeom.setQueueBucket(Bucket.Transparent);

	}

	@Override
	public void simpleUpdate(float tpf) {
		updatePlayer(tpf);
		updateCamera(tpf);
		updateStats(tpf);
		updateLight(tpf);
		updateGravity(tpf);

		if(player.getPlanet() != null)
			this.renderBlockBorders();
		
		if(this.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0) != null)
		{
			if(this.timer.getTimeInSeconds()-this.lastRotation > 0)
			{
				this.lastRotation = this.timer.getTimeInSeconds();
				this.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).rotate();
			}
		}
		this.invmanager.updateAll();
	}

	private void updatePlayer(float tpf) {
		Planet closest = player.getClosestPlanet();
		if(player.isWithinLoadingDistance(closest, player.getLocation())) //They are within range of a planet	
		{		
			if(player.getPlanet() != closest)
			{
				player.setPlanet(closest);
			}
		}
		else //They are no longer within range of any planet (Deep Space)
			player.setPlanet(null);
	}

	public void updateLight(float tpf) {
		/*if(this.player.getClosestAtmosphere() != null) {
			Planet planet = this.player.getClosestAtmosphere();
	        Vector3f lightPos = planet.getStar().getLocation();
	        LightScatteringFilter filter = new LightScatteringFilter(lightPos);
	        if(!this.fpp.getFilterList().contains(filter))
	        	this.fpp.addFilter(filter);
	        if(!this.viewPort.getProcessors().contains(this.fpp))
	        	this.viewPort.addProcessor(this.fpp);
		} else {
			if(this.fpp.getFilterList().contains(LightScatteringFilter.class)) {
				LightScatteringFilter filter = this.fpp.getFilter(LightScatteringFilter.class);
				this.fpp.removeFilter(filter);
			}	
		}*/
	}

	private void updateGravity(float tpf) {
		if(this.player.getPlanet() != null) {
			Planet planet = this.player.getPlanet();
			int FaceOn = this.player.getCurrentFaceOfPlanet(planet);
			if(FaceOn == Planet.TOP) {
				this.player.setGravity(new Vector3f(0, -10f, 0));
			} else if (FaceOn == Planet.BOTTOM) {
				this.player.setGravity(new Vector3f(0, 10f, 0));
			} else if (FaceOn == Planet.NORTH) {
				this.player.setGravity(new Vector3f(0, 0, 10f));
			} else if (FaceOn == Planet.SOUTH) {
				this.player.setGravity(new Vector3f(0, 0, -10f));
			} else if (FaceOn == Planet.EAST) {
				this.player.setGravity(new Vector3f(-10f, 0, 0));
			} else if (FaceOn == Planet.WEST) {
				this.player.setGravity(new Vector3f(10f, 0, 0));
			}
		} else {
			if(player.getGravity() != this.normalGravity)
				this.player.setGravity(normalGravity);
		}
	}

	private void initLighting() {	  
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		this.rootNode.addLight(al);

		this.rootNode.addLight(this.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getStar().getLight());


		//	    PointLightShadowRenderer plsr = new PointLightShadowRenderer(this.assetManager, SHADOWMAP_SIZE);
		//        plsr.setLight(this.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getStar().getLight());
		//        plsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
		//plsr.setFlushQueues(false);
		//plsr.displayFrustum();
		//plsr.displayDebug();
		//this.viewPort.addProcessor(plsr);

		//        PointLightShadowFilter plsf = new PointLightShadowFilter(this.assetManager, SHADOWMAP_SIZE);
		//        plsf.setLight(this.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getStar().getLight());     
		//        plsf.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
		//        plsf.setEnabled(true);

		this.fpp = new FilterPostProcessor(this.assetManager);
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
		this.fpp.addFilter(bloom);
		//fpp.addFilter(plsf);

		this.viewPort.addProcessor(this.fpp);
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
		//TODO modify to work with planet sides
		if(this.bulletAppState.isEnabled()) {
			Vector3f camDir = this.cam.getDirection().clone().multLocal(20);
			Vector3f camLeft = this.cam.getLeft().clone().multLocal(20);
			this.walkDirection.set(0, 0, 0);
			if (this.left)
				this.walkDirection.addLocal(camLeft); 
			if (this.right) 
				this.walkDirection.addLocal(camLeft.negate());
			if (this.up)    
				this.walkDirection.addLocal(camDir);
			if (this.down)  
				this.walkDirection.addLocal(camDir.negate());

			this.walkDirection.y = 0;

			if(this.up || this.down || this.right || this.left)
				PlayerEvents.PlayerMoveEvent(player, player.getLocation().add(this.walkDirection));

			this.player.setWalkDirection(this.walkDirection);
			this.cam.setLocation(new Vector3f(this.player.getLocation().getX(), this.player.getLocation().getY()+camHeight, this.player.getLocation().getZ()));
		}

		this.parent.getListener().setLocation(this.cam.getLocation());
		this.parent.getListener().setRotation(this.cam.getRotation());

	}
	private void renderBlockBorders() {
		Object[] values = Picker.getCurrentPointedBlock(false, this, cam);
		if(values != null)
		{
			BlockChunkControl block = (BlockChunkControl) values[2];
			Vector3Int blockLocation = (Vector3Int) values[1];
			if(block != null) {
				player.getPlanet().getTerrainNode().attachChild(this.blockHighlightGeom);
				this.blockHighlightGeom.setLocalTranslation(blockLocation.getX()*3+1.5f, blockLocation.getY()*3+1.5f, blockLocation.getZ()*3+1.5f);
				
			}
		}
		else
		{	
			this.blockHighlightGeom.removeFromParent();
		}
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
			if(this.player.getPlanet() != null) 
			{
				this.PlanetText.setText("Planet "+this.player.getPlanet().getName());
			} 
			else
				this.PlanetText.setText("Deep Space");
		} else {
			this.posText.setText("");
			this.PlanetText.setText("");
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

		this.PlanetText = new BitmapText(this.guiFont, false);
		this.PlanetText.setSize(this.guiFont.getCharSet().getRenderedSize());
		this.PlanetText.setLocalTranslation(0, this.PlanetText.getLineHeight(), 0);
		this.guiNode.attachChild(this.PlanetText);


	}

	@Override
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public void simpleRender(RenderManager rm) {}

	@Override
	public float getCamHeight() {
		return camHeight;
	}
	
	@Override
	public InputManager getInputManager() {
		return this.inputManager;
	}
	
	public Object[] getNiftyUtils() {
		return new Object[]{
				this,
				this.assetManager,
				this.inputManager,
				this.parent.getAudioRenderer(),
				this.viewPort,
				this.flyCam
				};
	}

}
