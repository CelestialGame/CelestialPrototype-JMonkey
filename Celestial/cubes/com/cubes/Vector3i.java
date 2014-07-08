/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cubes;

import com.jme3.math.Vector3f;

/**
 *
 * @author Carl <br>
 * Modified by Kevin Thorne
 * 
 */
public class Vector3i{

    public Vector3i(int x, int y, int z){
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i(){
        
    }
    private int x;
    private int y;
    private int z;

    public int getX(){
        return x;
    }

    public Vector3i setX(int x){
        this.x = x;
        return this;
    }

    public int getY(){
        return y;
    }

    public Vector3i setY(int y){
        this.y = y;
        return this;
    }

    public int getZ(){
        return z;
    }

    public Vector3i setZ(int z){
        this.z = z;
        return this;
    }
    
    public boolean hasNegativeCoordinate(){
        return ((x < 0) || (y < 0) || (z < 0));
    }
    
    public Vector3i set(Vector3i vector3Int){
        return set(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }

    public Vector3i set(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector3i add(Vector3i vector3Int){
        return add(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }
    
    public Vector3i add(int x, int y, int z){
        return new Vector3i(this.x + x, this.y + y, this.z + z);
    }
    
    public Vector3i addLocal(Vector3i vector3Int){
       return addLocal(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }
    
    public Vector3i addLocal(int x, int y, int z){
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vector3i subtract(Vector3i vector3Int){
        return subtract(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }
    
    public Vector3i subtract(int x, int y, int z){
        return new Vector3i(this.x - x, this.y - y, this.z - z);
    }
    
    public Vector3i subtractLocal(Vector3i vector3Int){
        return subtractLocal(vector3Int.getX(), vector3Int.getY(), vector3Int.getZ());
    }
    
    public Vector3i subtractLocal(int x, int y, int z){
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }
    
    public Vector3i negate(){
        return mult(-1);
    }
    
    public Vector3i mult(int factor){
        return mult(factor, factor, factor);
    }
    
    public Vector3i mult(int x, int y, int z){
        return new Vector3i(this.x * x, this.y * y, this.z * z);
    }
    
    public Vector3i negateLocal(){
        return multLocal(-1);
    }
    
    public Vector3i multLocal(int factor){
        return multLocal(factor, factor, factor);
    }
    
    public Vector3i multLocal(int x, int y, int z){
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }
    
    public static Vector3i convert3f(Vector3f vector)
    {
    	return new Vector3i((int)vector.getX(), (int)vector.getY(), (int)vector.getZ());
    }
    public static Vector3f convert3Int(Vector3i vector)
    {
    	return new Vector3f(vector.getX(), vector.getY(), vector.getZ());
    }
    
    @Override
    public Vector3i clone(){
        return new Vector3i(x, y, z);
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Vector3i){
            Vector3i vector3Int = (Vector3i) object;
            return ((x == vector3Int.getX()) && (y == vector3Int.getY()) && (z == vector3Int.getZ()));
        }
        return false;
    }

    @Override
    public String toString(){
        return "[Vector3Int x=" + x + " y=" + y + " z=" + z + "]";
    }
}
