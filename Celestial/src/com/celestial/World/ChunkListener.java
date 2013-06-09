/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.World;

import com.celestial.Celestial;
import com.cubes.BlockChunkControl;
import com.cubes.BlockChunkListener;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 *
 * @author kevint
 */
public class ChunkListener implements BlockChunkListener{
    
	Celestial parent;
	   
	   public ChunkListener(Celestial parent) {
	       this.parent = parent;
	   }
	   
	   public void onSpatialUpdated(BlockChunkControl bcc) {
	       updateCollisionShape(bcc.getOptimizedGeometry_Opaque());
	       updateCollisionShape(bcc.getOptimizedGeometry_Transparent());
	   }
	   private void updateCollisionShape(Geometry chunkGeometry){
	       RigidBodyControl rigidBodyControl = chunkGeometry.getControl(RigidBodyControl.class);
	       if(chunkGeometry.getTriangleCount() > 0){
	           if(rigidBodyControl != null){
	               chunkGeometry.removeControl(rigidBodyControl);
	               parent.bulletAppState.getPhysicsSpace().remove(rigidBodyControl);
	           }
	           rigidBodyControl = new RigidBodyControl(0);
	           chunkGeometry.addControl(rigidBodyControl);
	           parent.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
	       }
	       else{
	           if(rigidBodyControl != null){
	               chunkGeometry.removeControl(rigidBodyControl);
	           }
	       }
	   }
    
}
