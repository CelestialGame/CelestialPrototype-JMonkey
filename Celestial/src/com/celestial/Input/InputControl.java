/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.Input;

import com.celestial.Celestial;
import com.celestial.Blocks.*;
import com.celestial.Gui.Gui;
import com.celestial.World.Picker;
import com.cubes.BlockChunkControl;
import com.cubes.BlockTerrainControl;
import com.cubes.BlockType;
import com.cubes.Vector3Int;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author kevint
 */
public class InputControl {

	Celestial parent;

	Camera cam;
	
	public static boolean statson = false;

	public InputControl(Celestial parent, Camera cam, InputManager inputManager) {
		this.parent = parent;
		this.cam = cam;

		inputManager.addMapping("Block_Del", // trigger 1: spacebar
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
		inputManager.addListener(actionListener, "Block_Del");
		inputManager.addMapping("Block_Add", // trigger 1: spacebar
				new MouseButtonTrigger(MouseInput.BUTTON_RIGHT)); // trigger 2: left-button click
		inputManager.addListener(actionListener, "Block_Add");

		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("NoClip", new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addMapping("ESC", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping("SeeStats", new KeyTrigger(KeyInput.KEY_F3));
		inputManager.addListener(actionListener, "Left");
		inputManager.addListener(actionListener, "Right");
		inputManager.addListener(actionListener, "Up");
		inputManager.addListener(actionListener, "Down");
		inputManager.addListener(actionListener, "Jump");
		inputManager.addListener(actionListener, "NoClip");
		inputManager.addListener(actionListener, "ESC");
		inputManager.addListener(actionListener, "SeeStats");
	}

	private ActionListener actionListener = new ActionListener() {

		public void onAction(String binding, boolean keyPressed, float tpf) {

			if (binding.equals("Block_Del") && !keyPressed) 
			{
				Object[] values = Picker.getCurrentPointedBlockLocation(false, parent, cam);
				if(values == null || values[0] == null || values[1] == null) //Check to see if they clicked the sky...
				{
					return;
				}
				Vector3Int blockLocation = (Vector3Int) values[1];
				Vector3f blockAbsLocation = (Vector3f) values[0];
				if(blockLocation != null)
				{
					float dist = parent.player.getPhysicsLocation().distance(blockAbsLocation);
					if(!parent.bulletAppState.isEnabled()) //Are they flying?
					{
						for(BlockTerrainControl chunk : parent.sides) 
						{
							if(chunk != null && blockLocation != null)
								chunk.removeBlock(blockLocation); //Remove the Block
						}
					}
					else
					{
						if(dist <= 15F) //Is the block nearby?
						{
							for(BlockTerrainControl chunk : parent.sides) 
							{
								if(chunk != null && blockLocation != null)
									chunk.removeBlock(blockLocation); //Remove the Block
							}
						}
					}
				}
			}

			if (binding.equals("Block_Add") && !keyPressed) {
				Object[] values = Picker.getCurrentPointedBlockLocation(true, parent, cam);
				if(values == null || values[0] == null || values[1] == null) //Check to see if they clicked the sky...
				{
					return;
				}
				Vector3Int blockLocation = (Vector3Int) values[1];
				Vector3f blockAbsLocation = (Vector3f) values[0];
				if(blockLocation != null){
					float dist = parent.player.getPhysicsLocation().distance(blockAbsLocation);
					if(!parent.bulletAppState.isEnabled()) //Are they flying?
					{
						for(BlockTerrainControl chunk : parent.sides) 
						{
							if(chunk != null && blockLocation != null)
								chunk.setBlock(blockLocation, Block_Dirt.class); //Add the Block
						}
					}
					else
					{
						if(dist <= 15F) //Is the block nearby?
						{
							for(BlockTerrainControl chunk : parent.sides) 
							{
								if(chunk != null && blockLocation != null)
									chunk.setBlock(blockLocation, Block_Stone.class); //Add the Block
							}
						}
					}
				}
			}
			else if (binding.equals("Left")) {
				parent.left = keyPressed;
			} else if (binding.equals("Right")) {
				parent.right = keyPressed;
			} else if (binding.equals("Up")) {
				parent.up = keyPressed;
			} else if (binding.equals("Down")) {
				parent.down = keyPressed;
			} else if (binding.equals("Jump")) {
				parent.player.jump();
			}
			else if(binding.equals("NoClip") && !keyPressed) {
				if(parent.bulletAppState.isEnabled()) {
					parent.bulletAppState.setEnabled(false);
					parent.setCamSpeed(100);
				} else {
					parent.bulletAppState.setEnabled(true);
					parent.player.setPhysicsLocation(new Vector3f(cam.getLocation().getX(), cam.getLocation().getY()-Celestial.camHeight, cam.getLocation().getZ()));
				}
			}
			else if(binding.equals("ESC"))
			{
				Celestial.app.stop();
				Celestial.gui.gamePane.remove(Celestial.canvas);
				Celestial.gui.changeCard(Gui.MAIN);
			}
			else if(binding.equals("SeeStats") && !keyPressed) {
				if(statson) {
					Celestial.self.setDisplayFps(false);
			        Celestial.self.setDisplayStatView(false);
			        statson = false;
				} else {
					Celestial.self.setDisplayFps(true);
			        Celestial.self.setDisplayStatView(true);
			        statson = true;
				}
			}
		}
	};

	public void renderBlockBorder()
	{
		// NOT DONE -- It'll turn the entire thing blue if you do use this.... xD
		/*BlockChunkControl block = Picker.getCurrentPointedBlock(false, parent, cam);
		if(block != null) {
			block.getOptimizedGeometry_Opaque().getMaterial().setColor("Color", new ColorRGBA(0f,0f,0.5f, 1f));
		}*/

	}


}
