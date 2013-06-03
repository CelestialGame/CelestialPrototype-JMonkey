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
import com.celestial.Gui.Gui;
import com.celestial.Input.InputControl;
import com.celestial.SinglePlayer.Inventory.InventoryManager;
import com.celestial.SinglePlayer.Inventory.InventoryRegister;
import com.celestial.WorldCreation.PlanetGenerator;
import com.cubes.BlockTerrainControl;
import com.cubes.CubesSettings;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
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
    
    public List<BlockTerrainControl> sides;
    
    public List<Node> WorldSides;
    public Node worldNode;
    
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
	
	InventoryManager invmanager;
    
    public Celestial() {
    
       
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

        CubesSettings csettings = new CubesSettings(app);
        csettings.setChunkSizeX(16);
        csettings.setChunkSizeY(16);
        csettings.setChunkSizeZ(16);
        
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
    	
        /** Set up Physics **/
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        // Display a line of text with a default font
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText statusText = new BitmapText(guiFont, false);
        statusText.setSize(guiFont.getCharSet().getRenderedSize());
        statusText.setText("Loading world...");
        statusText.setLocalTranslation(300, statusText.getLineHeight(), 0);
        guiNode.attachChild(statusText);
        posText = new BitmapText(guiFont, false);
        posText.setSize(guiFont.getCharSet().getRenderedSize());
        posText.setText("");
        posText.setLocalTranslation(450, statusText.getLineHeight(), 0);
        guiNode.attachChild(posText);
        initCrossHairs();
        
        Blocks.init();
        
        invmanager = new InventoryManager();
        
        InventoryRegister.RegisterBlocks(invmanager);
        
        walkDirection = new Vector3f();
        left = false;
        right = false;
        up = false;
        down = false;
        
        sides = new ArrayList<BlockTerrainControl>();
        WorldSides = new ArrayList<Node>();
        
        worldNode = new Node();
        sides.add(new PlanetGenerator(this).makePlanetSide(0,0,0,worldNode));
        WorldSides.add(worldNode);
        
        inputControl = new InputControl(this, cam, inputManager);
        
        Spatial sky = SkyFactory.createSky(assetManager, "assets/textures/nightsky.jpg", true);
        sky.scale(-1, -1, 1);
        rootNode.attachChild(sky);
        
        
        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(10f, 10f, 10f));
        rootNode.addLight(sun);
        
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 2f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(50);
        player.setPhysicsLocation(new Vector3f(0, 50, 0));
       
        flyCam.setMoveSpeed(100);
        
        statusText.setText("World loaded");
        
        for(Node node : WorldSides) {
            CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(node);
            RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0.0f);
            node.addControl(landscape);
            bulletAppState.getPhysicsSpace().add(node);
            node.setShadowMode(ShadowMode.CastAndReceive);
            rootNode.attachChild(node);
        }
        bulletAppState.getPhysicsSpace().add(player);
        
        Celestial.gui.changeCard(Gui.GAME);
        
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
    	if(InputControl.statson) {
	        Vector3f location = cam.getLocation();
	        posText.setText("X: "+location.x + " Y: "+location.y+" Z: "+location.z);
    	} else {
    		posText.setText("");
    	}
        if(bulletAppState.isEnabled()) {
            Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
            Vector3f camLeft = cam.getLeft().clone().multLocal(0.2f);
            walkDirection.set(0, 0, 0);
            if (left)  { walkDirection.addLocal(camLeft); }
            if (right) { walkDirection.addLocal(camLeft.negate()); }
            if (up)    { walkDirection.addLocal(camDir); }
            if (down)  { walkDirection.addLocal(camDir.negate()); }
            walkDirection.y = 0;
            player.setWalkDirection(walkDirection);
            cam.setLocation(new Vector3f(player.getPhysicsLocation().getX(), player.getPhysicsLocation().getY()+camHeight, player.getPhysicsLocation().getZ()));
            this.inputControl.renderBlockBorder();
        } else {
            //pass
        }
        bulletAppState.update(tpf);
        if(player.getPhysicsLocation().getY() <= -150 || cam.getLocation().getY() <= -150) {
            player.setPhysicsLocation(new Vector3f(0, 50, 0));
            cam.setLocation(new Vector3f(player.getPhysicsLocation().getX(), player.getPhysicsLocation().getY()+camHeight, player.getPhysicsLocation().getZ()));
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {

    	//TODO: add render code
    }
    
    
    
    /** A centred plus sign to help the player aim. */
  protected void initCrossHairs() {
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+"); // crosshairs
    ch.setLocalTranslation( // center
      settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
      settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
    guiNode.attachChild(ch);
  }
    
  
  public void setCamSpeed(float speed)
  {
      this.flyCam.setMoveSpeed(speed);
  }

  public AppSettings getSettings()
  {
      return settings;
  }
  public InventoryManager getInventoryManager(){
	  return this.invmanager;
  }
    
}
