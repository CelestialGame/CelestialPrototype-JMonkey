/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.jme3.math.Vector3f;

/**
 *
 * @author Carl
 */
public class Block{
	
	/**
     * 0, 0, 0
     */
    public static final Vector3f FRONT_BOTTOM_LEFT = new Vector3f(0, 0, 0);
    /**
     * 1, 0, 0
     */
    public static final Vector3f FRONT_BOTTOM_RIGHT = new Vector3f(1, 0, 0);
    /**
     * 0, 0, 1
     */
    public static final Vector3f REAR_BOTTOM_LEFT = new Vector3f(0, 0, 1);
    /**
     * 1, 0, 1
     */
    public static final Vector3f REAR_BOTTOM_RIGHT = new Vector3f(1, 0, 1);
    /**
     * 0, 1, 0
     */
    public static final Vector3f FRONT_TOP_LEFT = new Vector3f(0, 1, 0);
    /**
     * 1, 1, 0
     */
    public static final Vector3f FRONT_TOP_RIGHT = new Vector3f(1, 1, 0);
    /**
     * 0, 1, 1
     */
    public static final Vector3f REAR_TOP_LEFT = new Vector3f(0, 1, 1);
    /**
     * 1, 1, 1
     */
    public static final Vector3f REAR_TOP_RIGHT = new Vector3f(1, 1, 1);

    public Block(){
        type = BlockManager.getInstance().getType(getClass());
    }
    public static enum Face{
    	Top(new Vector3i(0, 1, 0)), 
        Bottom(new Vector3i(0, -1, 0)), 
        Left(new Vector3i(-1, 0, 0)), 
        Right(new Vector3i(1, 0, 0)), 
        Front(new Vector3i(0, 0, 1)), 
        Back(new Vector3i(0, 0, -1));
    	
    	private final Vector3i offset;
        private final List<Float> normals;
    	Face(Vector3i offset) {
            this.offset = offset;
            normals = Collections.unmodifiableList(new ArrayList<Float>(Arrays.asList((float) offset.getX(), (float) offset.getY(), (float) offset.getZ())));
        }
        
        public Vector3i getOffsetVector() {
            return offset;
        }

        public Vector3i getNeighbor(Vector3i point) {
            return point.add(this.offset);
        }
        
        public Vector3i getNeightborLocal(Vector3i point) {
            return point.addLocal(this.offset);
        }

        public List<Float> getNormals() {
            return normals;
        }
    };
    private BlockType type;

    public BlockType getType(){
        return type;
    }
}
