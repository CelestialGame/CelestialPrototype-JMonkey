/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.Input;

import com.celestial.Celestial;
import com.celestial.World.Picker;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.cubes.test.blocks.Block_Wood;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.Camera;

/**
 *
 * @author kevint
 */
public class InputControl {
    
    Celestial parent;
    
    Camera cam;
    
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
        inputManager.addListener(actionListener, "Left");
        inputManager.addListener(actionListener, "Right");
        inputManager.addListener(actionListener, "Up");
        inputManager.addListener(actionListener, "Down");
        inputManager.addListener(actionListener, "Jump");
        inputManager.addListener(actionListener, "NoClip");
    }
    
    private ActionListener actionListener = new ActionListener() {
 
    public void onAction(String binding, boolean keyPressed, float tpf) {
        if (binding.equals("Block_Del") && !keyPressed) {
            Vector3Int blockLocation = Picker.getCurrentPointedBlockLocation(false, parent, cam);
              //(The block location is null, if the user looks in the sky or out of the map)
              if(blockLocation != null){
                  //... and remove a block there :)
                  for(BlockTerrainControl chunk : parent.sides) {
                      chunk.removeBlock(blockLocation);
                  }
              }
        }
        if (binding.equals("Block_Add") && !keyPressed) {
            Vector3Int blockLocation = Picker.getCurrentPointedBlockLocation(true, parent, cam);
              //(The block location is null, if the user looks in the sky or out of the map)
              if(blockLocation != null){
                  //... and remove a block there :)
                  for(BlockTerrainControl chunk : parent.sides) {
                      chunk.setBlock(blockLocation, Block_Wood.class);
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
              parent.player.setPhysicsLocation(cam.getLocation());
          }
      }
    }
  };
    
    
}
