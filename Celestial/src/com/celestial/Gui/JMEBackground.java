package com.celestial.Gui;

import java.awt.Canvas;

import com.celestial.Celestial;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.util.SkyFactory;

/**
 * TODO Put here a description of what this class does.
 *
 * @author kevint.
 *         Created Jun 8, 2013.
 */
public class JMEBackground extends SimpleApplication{
	
	public static Application app;
	public static Canvas canvas;
	static JMEBackground jmebg;
	private static JmeCanvasContext context;
	
	public JMEBackground() {
		
	}
	
	public static void createNewCanvas() {
		AppSettings settings = new AppSettings(true);
		settings.setWidth(Celestial.width);
		settings.setHeight(Celestial.height);
		settings.setTitle(Celestial.title);

		app = jmebg;
		
		app.setSettings(settings);
		app.createCanvas();
		
		jmebg.setDisplayFps(false);
		jmebg.setDisplayStatView(false);

		context = (JmeCanvasContext) app.getContext();
		canvas = context.getCanvas();
		canvas.setSize(settings.getWidth(), settings.getHeight());
		settings.setEmulateMouse(false);
		
	}

	@Override
	public void simpleInitApp() {
		
		getInputManager().setCursorVisible(true);
		getInputManager().endInput();
		
		cam.setLocation(new Vector3f(261, 0, 233));
		cam.setRotation(new Quaternion(-0.017417872f, 0.9273262f, -0.0435002f, -0.371309f));

		inputManager.clearMappings();
		
		Node StarNode = new Node();
		
		Box starsphere = new Box(5,5,5);
		Geometry StarGeometry = new Geometry("Star", starsphere);
		Material mat = new Material(assetManager,  // Create new material and...
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", new ColorRGBA(247f, 214f, 81f, 0f));
		mat.setColor("GlowColor", new ColorRGBA(247f, 214f, 81f, 0f));
		StarGeometry.setMaterial(mat);
		StarNode.attachChild(StarGeometry);
		
		StarNode.setQueueBucket(Bucket.Opaque);
		
		PointLight light = new PointLight();
		light.setPosition(new Vector3f(0,0,0));
		light.setColor(ColorRGBA.White);
		light.setRadius(50f);
		
		StarNode.addLight(light);
		StarNode.move(light.getPosition());
		
		Spatial sky = SkyFactory.createSky(this.assetManager, "assets/textures/nightsky.jpg", true);
		sky.rotate(270*FastMath.DEG_TO_RAD,0,0);
		this.rootNode.attachChild(sky);
		
		flyCam.setMoveSpeed(100);
		
		rootNode.attachChild(StarNode);
		
		initAudio();
		
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		//update
	}
	
	public void initAudio() {
		/* nature sound - keeps playing in a loop. */
	    AudioNode audio_ambient = new AudioNode(this.assetManager, "assets/sounds/mainmenu/70.MurderintheRuins.ogg", true);
	    audio_ambient.setLooping(true);  // activate continuous playing
	    audio_ambient.setPositional(false);
	    //audio_ambient.setLocalTranslation(Vector3f.ZERO.clone());
	    audio_ambient.setVolume(3);
	    this.rootNode.attachChild(audio_ambient);
	    audio_ambient.play(); // play continuously!
	}
	
}
