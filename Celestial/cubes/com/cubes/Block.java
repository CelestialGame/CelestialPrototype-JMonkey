/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Carl
 */
public class Block{

    public Block(){
        type = BlockManager.getInstance().getType(getClass());
    }
    public static enum Face{
    	Top(new Vector3Int(0, 1, 0)), 
        Bottom(new Vector3Int(0, -1, 0)), 
        Left(new Vector3Int(-1, 0, 0)), 
        Right(new Vector3Int(1, 0, 0)), 
        Front(new Vector3Int(0, 0, 1)), 
        Back(new Vector3Int(0, 0, -1));
    	
    	private final Vector3Int offset;
        private final List<Float> normals;
    	Face(Vector3Int offset) {
            this.offset = offset;
            normals = Collections.unmodifiableList(new ArrayList<Float>(Arrays.asList((float) offset.getX(), (float) offset.getY(), (float) offset.getZ())));
        }
        
        public Vector3Int getOffsetVector() {
            return offset;
        }

        public Vector3Int getNeighbor(Vector3Int point) {
            return point.add(this.offset);
        }
        
        public Vector3Int getNeightborLocal(Vector3Int point) {
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
