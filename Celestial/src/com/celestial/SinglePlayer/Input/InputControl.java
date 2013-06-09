/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.SinglePlayer.Input;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.celestial.Blocks.*;
import com.celestial.Gui.Gui;
import com.celestial.Gui.MainMenu;
import com.celestial.SinglePlayer.Inventory.InventoryItem;
import com.celestial.World.Picker;
import com.cubes.Block;
import com.cubes.BlockChunkControl;
import com.cubes.BlockManager;
import com.cubes.BlockSkin;
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

	CelestialPortal parent;

	Camera cam;

	public static boolean statson = false;

	public InputControl(CelestialPortal parent, Camera cam, InputManager inputManager) {
		this.parent = parent;
		this.cam = cam;

		inputManager.addMapping("Block_Del",
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(actionListener, "Block_Del");
		inputManager.addMapping("Block_Add",
				new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addListener(actionListener, "Block_Add");
		inputManager.addMapping("Block_Pick",
				new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
		inputManager.addListener(actionListener, "Block_Pick");
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("NoClip", new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addMapping("ESC", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping("SeeStats", new KeyTrigger(KeyInput.KEY_F3));
		inputManager.addMapping("Slot1", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("Slot2", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping("Slot3", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping("Slot4", new KeyTrigger(KeyInput.KEY_4));
		inputManager.addMapping("Slot5", new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping("Slot6", new KeyTrigger(KeyInput.KEY_6));
		inputManager.addMapping("Slot7", new KeyTrigger(KeyInput.KEY_7));
		inputManager.addMapping("Slot8", new KeyTrigger(KeyInput.KEY_8));
		inputManager.addMapping("Slot9", new KeyTrigger(KeyInput.KEY_9));
		inputManager.addListener(actionListener, "Left");
		inputManager.addListener(actionListener, "Right");
		inputManager.addListener(actionListener, "Up");
		inputManager.addListener(actionListener, "Down");
		inputManager.addListener(actionListener, "Jump");
		inputManager.addListener(actionListener, "NoClip");
		inputManager.addListener(actionListener, "ESC");
		inputManager.addListener(actionListener, "SeeStats");
		inputManager.addListener(actionListener, "Slot1");
		inputManager.addListener(actionListener, "Slot2");
		inputManager.addListener(actionListener, "Slot3");
		inputManager.addListener(actionListener, "Slot4");
		inputManager.addListener(actionListener, "Slot5");
		inputManager.addListener(actionListener, "Slot6");
		inputManager.addListener(actionListener, "Slot7");
		inputManager.addListener(actionListener, "Slot8");
		inputManager.addListener(actionListener, "Slot9");
	}

	private ActionListener actionListener = new ActionListener() {

		private Vector3f lastabs;
		private Vector3Int last;

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
					float dist = parent.getPlayer().getPhysicsLocation().distance(blockAbsLocation);
					InventoryItem item;
					if(!parent.getBulletAppState().isEnabled()) //Are they flying?
					{
						BlockTerrainControl chunk = parent.planets.get(0).getTerrControl();
						if(chunk != null && blockLocation != null) {
							/*Class<? extends Block> block = BlockManager.getClass(chunk.getBlock(blockLocation).getType());
							item = parent.getInventoryManager().getItembyBlock(block);
							if(item != null) {
								parent.getInventoryManager().dropItem(item, blockAbsLocation);
							}*/
							chunk.removeBlock(blockLocation); //Remove the Block
						}
					
					}
					else
					{
						if(dist <= 15F) //Is the block nearby?
						{
							BlockTerrainControl chunk = parent.planets.get(0).getTerrControl();
							if(chunk != null && blockLocation != null) {
								Class<? extends Block> block = BlockManager.getClass(chunk.getBlock(blockLocation).getType());
								item = parent.getInventoryManager().getItembyBlock(block);
								if(item != null) {
									parent.getInventoryManager().dropItem(item, blockAbsLocation);
								}
								chunk.removeBlock(blockLocation); //Remove the Block
							}
						}
					}
				}
			}
			if (binding.equals("Block_Pick") && !keyPressed) 
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
					if(last == null)
					{
						this.last = new Vector3Int(0,0,0);
						this.lastabs = new Vector3f(0,0,0);
					}
					System.out.println("Loc -- Rel: "+ blockLocation +"Abs: "+ blockAbsLocation);
					System.out.println("Distance from last: "+lastabs.distance(blockAbsLocation)+" ("+lastabs.distance(blockAbsLocation)/3+")");
					this.last = blockLocation;
					this.lastabs = blockAbsLocation;
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
					if(!parent.getBulletAppState().isEnabled()) //Are they flying?
					{
						BlockTerrainControl chunk = parent.planets.get(0).getTerrControl();
						if(chunk != null && blockLocation != null && parent.getInventoryManager().getSelectedHotSlot().getItem() != null) {
							chunk.setBlock(blockLocation, parent.getInventoryManager().getSelectedHotSlot().getItem().getBlock()); //Add the Block
							parent.getInventoryManager().getSelectedHotSlot().updateContents(true);
						}
					}
					else
					{
						if(dist <= 15F) //Is the block nearby?
						{
							BlockTerrainControl chunk = parent.planets.get(0).getTerrControl();
							if(chunk != null && blockLocation != null && parent.getInventoryManager().getSelectedHotSlot().getItem() != null) {
								chunk.setBlock(blockLocation, parent.getInventoryManager().getSelectedHotSlot().getItem().getBlock()); //Add the Block
								parent.getInventoryManager().getSelectedHotSlot().updateContents(true);
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
				if(parent.getBulletAppState().isEnabled()) {
					parent.getBulletAppState().setEnabled(false);
					parent.setCamSpeed(100);
				} else {
					parent.getBulletAppState().setEnabled(true);
					parent.player.setPhysicsLocation(new Vector3f(cam.getLocation().getX(), cam.getLocation().getY()-parent.getCamHeight(), cam.getLocation().getZ()));
				}
			}
			else if(binding.equals("ESC"))
			{
				parent.app.stop();
				Celestial.gui.topcardPane.remove(Celestial.canvas);
				Celestial.gui.changeCard(MainMenu.MAIN);
			}
			else if(binding.equals("SeeStats") && !keyPressed) {
				if(statson) {
					Celestial.toggleStats(false);
					parent.getGuiNode().detachChild(parent.InvText);
					statson = false;
				} else {
					Celestial.toggleStats(true);
					parent.getGuiNode().attachChild(parent.InvText);
					statson = true;
				}
			}
			else if(binding.equals("Slot1") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(1);
			}
			else if(binding.equals("Slot2") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(2);
			}
			else if(binding.equals("Slot3") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(3);
			}
			else if(binding.equals("Slot4") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(4);
			}
			else if(binding.equals("Slot5") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(5);
			}
			else if(binding.equals("Slot6") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(6);
			}
			else if(binding.equals("Slot7") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(7);
			}
			else if(binding.equals("Slot8") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(8);
			}
			else if(binding.equals("Slot9") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(9);
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
