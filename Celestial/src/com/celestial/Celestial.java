package com.celestial;

import com.celestial.Blocks.*;
import com.celestial.Input.InputControl;
import com.celestial.WorldCreation.PlanetGenerator;
import com.cubes.*;
import com.cubes.test.CubesTestAssets;
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
import com.jme3.util.SkyFactory;
import java.util.*;

/**
 * test
 * @author kevint
 */
public class Celestial extends SimpleApplication{

    public static void main(String[] args) {
        Celestial app = new Celestial();
        app.start();
    }
    
    public List<BlockTerrainControl> sides = new ArrayList<BlockTerrainControl>();
    
    public List<Node> WorldSides = new ArrayList<Node>();
    public Node worldNode;
    
    BitmapText posText;
    public BulletAppState bulletAppState;
    
    public Vector3f walkDirection = new Vector3f();
    public boolean left = false, right = false, up = false, down = false;
    public CharacterControl player; 
    
    public Celestial() {
        
        this.settings = new AppSettings(true);
        this.settings.setTitle("Celestial");
       
    }
    @SuppressWarnings("deprecation")
	@Override
    public void simpleInitApp() {
        
        /** Set up Physics */
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
        
        CubesTestAssets.registerBlocks();
        Blocks.init();
        
        
        worldNode = new Node();
        sides.add(new PlanetGenerator(this).makePlanetSide(0,0,0,worldNode));
        WorldSides.add(worldNode);
        
        InputControl inputControl = new InputControl(this, cam, inputManager);
        
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
    }

    @Override
    public void simpleUpdate(float tpf) {
        Vector3f location = cam.getLocation();
        posText.setText("X: "+location.x + " Y: "+location.y+" Z: "+location.z);
        if(bulletAppState.isEnabled()) {
            Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
            Vector3f camLeft = cam.getLeft().clone().multLocal(0.2f);
            walkDirection.set(0, 0, 0);
            if (left)  { walkDirection.addLocal(camLeft); }
            if (right) { walkDirection.addLocal(camLeft.negate()); }
            if (up)    { walkDirection.addLocal(camDir); }
            if (down)  { walkDirection.addLocal(camDir.negate()); }
            player.setWalkDirection(walkDirection);
            cam.setLocation(player.getPhysicsLocation());
        } else {
            //pass
        }
        bulletAppState.update(tpf);
        if(player.getPhysicsLocation().getY() <= -150 || cam.getLocation().getY() <= -150) {
            player.setPhysicsLocation(new Vector3f(0, 50, 0));
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
    
}
