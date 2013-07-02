/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.SinglePlayer.Input;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.celestial.Blocks.BlocksEnum;
import com.celestial.Gui.Gui;
import com.celestial.SinglePlayer.Components.SectorCoord;
import com.celestial.SinglePlayer.Events.PlayerEvents;
import com.celestial.SinglePlayer.Inventory.InventoryItem;
import com.celestial.World.Picker;
import com.cubes.Block;
import com.cubes.BlockManager;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
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
		inputManager.addMapping("Respawn", new KeyTrigger(KeyInput.KEY_R));
		inputManager.addMapping("FindFace", new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping("Inventory", new KeyTrigger(KeyInput.KEY_E));
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
		inputManager.addListener(actionListener, "Respawn");
		inputManager.addListener(actionListener, "FindFace");
		inputManager.addListener(actionListener, "Inventory");
	}

	private ActionListener actionListener = new ActionListener() {

		private Vector3f lastabs;
		private Vector3Int last;
		private boolean inventoryopen;

		public void onAction(String binding, boolean keyPressed, float tpf) {

			if (binding.equals("Block_Del") && !keyPressed) 
			{
				Object[] values = Picker.getCurrentPointedBlock(false, parent, cam);
				if(values == null || values[0] == null || values[1] == null) //Check to see if they clicked the sky...
				{
					return;
				}
				Vector3Int blockLocation = (Vector3Int) values[1];
				Vector3f blockAbsLocation = (Vector3f) values[0];
				if(blockLocation != null)
				{
					float dist = parent.player.getLocation().distance(blockAbsLocation);
					InventoryItem item;
					if(!this.inventoryopen) {
						if(!parent.getBulletAppState().isEnabled()) //Are they flying?
						{
							BlockTerrainControl chunk = parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getTerrControl();
							if(chunk != null && blockLocation != null && chunk.getBlock(blockLocation) != null) {
								PlayerEvents.PlayerDeleteBlockEvent(parent.player, parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0), blockAbsLocation, blockLocation);	
								Class<? extends Block> block = BlockManager.getClass(chunk.getBlock(blockLocation).getType());
								item = parent.getInventoryManager().getItembyBlock(BlocksEnum.getBlockByClass(block));
								if(item != null) {
									parent.getInventoryManager().dropItem(item, blockAbsLocation);
								}
								chunk.removeBlock(blockLocation); //Remove the Block
								PlayerEvents.PlayerDeleteBlockEvent(parent.player, parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0), blockAbsLocation, blockLocation);
							}
	
						}
						else
						{
							if(dist <= 15F) //Is the block nearby?
							{
								BlockTerrainControl chunk = parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getTerrControl();
								if(chunk != null && blockLocation != null && chunk.getBlock(blockLocation) != null) {
									PlayerEvents.PlayerDeleteBlockEvent(parent.player, parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0), blockAbsLocation, blockLocation);	
									Class<? extends Block> block = BlockManager.getClass(chunk.getBlock(blockLocation).getType());
									item = parent.getInventoryManager().getItembyBlock(BlocksEnum.getBlockByClass(block));
									if(item != null) {
										parent.getInventoryManager().dropItem(item, blockAbsLocation);
									}
									chunk.removeBlock(blockLocation); //Remove the Block
								}
							}
						}
					}
				}
			}
			if (binding.equals("Block_Pick") && !keyPressed) 
			{
				Object[] values = Picker.getCurrentPointedBlock(false, parent, cam);
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
				Object[] values = Picker.getCurrentPointedBlock(true, parent, cam);
				if(values == null || values[0] == null || values[1] == null) //Check to see if they clicked the sky...
				{
					return;
				}
				Vector3Int blockLocation = (Vector3Int) values[1];
				Vector3f blockAbsLocation = (Vector3f) values[0];
				if(blockLocation != null){
					float dist = parent.player.getLocation().distance(blockAbsLocation);
					if(!parent.getBulletAppState().isEnabled()) //Are they flying?
					{
						BlockTerrainControl chunk = parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getTerrControl();
						if(chunk != null && blockLocation != null && parent.getInventoryManager().getSelectedHotSlot().getItem() != null) {
							PlayerEvents.PlayerAddBlockEvent(parent.player, parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0), blockAbsLocation, blockLocation);
							chunk.setBlock(blockLocation, parent.getInventoryManager().getSelectedHotSlot().getItem().getBlock().getBClass()); //Add the Block
							if(chunk.getBlock(blockLocation) != null)
								parent.getInventoryManager().getSelectedHotSlot().updateContents(true);
						}
					}
					else
					{
						if(dist <= 15F) //Is the block nearby?
						{
							BlockTerrainControl chunk = parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0).getTerrControl();
							if(chunk != null && blockLocation != null && parent.getInventoryManager().getSelectedHotSlot().getItem() != null) {
								chunk.setBlock(blockLocation, parent.getInventoryManager().getSelectedHotSlot().getItem().getBlock().getBClass()); //Add the Block
								PlayerEvents.PlayerAddBlockEvent(parent.player, parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0), blockAbsLocation, blockLocation);
								chunk.setBlock(blockLocation, parent.getInventoryManager().getSelectedHotSlot().getItem().getBlock().getBClass()); //Add the Block
								if(chunk.getBlock(blockLocation) != null)
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
				PlayerEvents.PlayerJumpEvent(parent.player);
			}
			else if(binding.equals("NoClip") && !keyPressed) {
				if(parent.getBulletAppState().isEnabled()) {
					parent.getBulletAppState().setEnabled(false);
					parent.setCamSpeed(100);
					parent.player.setVisibleToClient(true);
				} else {
					parent.getBulletAppState().setEnabled(true);
					parent.player.setLocation(new Vector3f(cam.getLocation().getX(), cam.getLocation().getY()-parent.getCamHeight(), cam.getLocation().getZ()));
					PlayerEvents.PlayerMoveEvent(parent.player, new Vector3f(cam.getLocation().getX(), cam.getLocation().getY()-parent.getCamHeight(), cam.getLocation().getZ()));
					parent.player.setVisibleToClient(false);
				}
			}
			else if(binding.equals("ESC"))
			{
				parent.app.stop();
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
				parent.getInventoryManager().setSelectedHotSlot(0);
			}
			else if(binding.equals("Slot2") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(1);
			}
			else if(binding.equals("Slot3") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(2);
			}
			else if(binding.equals("Slot4") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(3);
			}
			else if(binding.equals("Slot5") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(4);
			}
			else if(binding.equals("Slot6") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(5);
			}
			else if(binding.equals("Slot7") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(6);
			}
			else if(binding.equals("Slot8") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(7);
			}
			else if(binding.equals("Slot9") && !keyPressed) {
				parent.getInventoryManager().setSelectedHotSlot(8);
			}
			else if(binding.equals("Respawn") && !keyPressed)
			{

				parent.player.spawnPlayer(parent.player.getSystem().getPlanet(0), 0);
				PlayerEvents.PlayerMoveEvent(parent.player, parent.player.getSpawnLocation(parent.player.getSystem().getPlanet(0), 0));
				parent.cam.setLocation(new Vector3f(parent.player.getLocation().getX(), parent.player.getLocation().getY()+parent.getCamHeight(), parent.player.getLocation().getZ()));
			}
			else if(binding.equals("FindFace") && !keyPressed)
			{
				System.out.println(parent.player.getCurrentFaceOfPlanet(parent.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0)));
				System.out.println(parent.player.getPlanet().getPlanetNode().getWorldTranslation().distance(parent.player.getPlanet().getStar().getStarNode().getWorldTranslation()));
			}
			else if(binding.equals("Inventory") && !keyPressed) {
				if(!this.inventoryopen) {
					parent.getGui().showPopup(Gui.INVENTORY);
					this.inventoryopen = true;
				} else {
					parent.getGui().closePopup(Gui.INVENTORY);
					this.inventoryopen = false;
				}
			}
		}
	};


}
