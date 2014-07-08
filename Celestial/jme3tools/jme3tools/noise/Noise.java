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
public interface Noise {
    public float noise(float x, float y, float z);
    public float noise(float x, float y);

}
