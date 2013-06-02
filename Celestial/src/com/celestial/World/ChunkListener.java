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
        for(Node side : parent.WorldSides) {
            Geometry optimizedGeometry = bcc.getOptimizedGeometry_Opaque();
            RigidBodyControl rigidBodyControl = optimizedGeometry.getControl(RigidBodyControl.class);
            if(rigidBodyControl == null){
                rigidBodyControl = new RigidBodyControl(0);
                optimizedGeometry.addControl(rigidBodyControl);
                parent.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
            }
            rigidBodyControl.setCollisionShape(new MeshCollisionShape(optimizedGeometry.getMesh()));
        }
    }
    
}
