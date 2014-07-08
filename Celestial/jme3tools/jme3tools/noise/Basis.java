package jme3tools.noise;

/**
*
* @author dpwhitt
*/
/*
 * All credit to @dpwhittaker for coding these into Java and providing
 * his code on the JME3 forums
 * 
 * Only modifications were to switch from Simplex (patented) to
 * Perlin (only copyrighted) :D
 * 
 */

public abstract class Basis {
    protected abstract float get(float x, float y, float z);
    protected abstract float get(float x, float y);
    protected int fastfloor(float x) {
        return x > 0 ? (int) x : (int) x - 1;
    }

}
