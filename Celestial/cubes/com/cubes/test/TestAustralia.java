package com.cubes.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cubes.BlockTerrainControl;
import com.cubes.Vector3i;
import com.cubes.test.blocks.Block_Grass;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class TestAustralia extends SimpleApplication{

    public static void main(String[] args){
        Logger.getLogger("").setLevel(Level.SEVERE);
        TestAustralia app = new TestAustralia();
        app.start();
    }

    public TestAustralia(){
        settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Cubes Demo - Heightmap (Australia)");
    }

    @Override
    public void simpleInitApp(){
        CubesTestAssets.registerBlocks();
        CubesTestAssets.initializeEnvironment(this);
        CubesTestAssets.initializeWater(this);
        
        BlockTerrainControl blockTerrain = new BlockTerrainControl(CubesTestAssets.getSettings(this), new Vector3i(7, 1, 7));
        blockTerrain.setBlocksFromHeightmap(new Vector3i(0, 1, 0), "Textures/cubes/heightmap_australia.jpg", 10, Block_Grass.class);
        Node terrainNode = new Node();
        terrainNode.addControl(blockTerrain);
        terrainNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(terrainNode);
        
        cam.setLocation(new Vector3f(32.8f, 111, 379.5f));
        cam.lookAtDirection(new Vector3f(0.44f, -0.47f, -0.77f), Vector3f.UNIT_Y);
        cam.setFrustumFar(4000);
        flyCam.setMoveSpeed(300);
    }
}
