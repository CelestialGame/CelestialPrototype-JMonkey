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

public abstract class BinaryOperator extends Basis {

    Basis basisA;
    Basis basisB;
    public BinaryOperator(Basis basisA, Basis basisB) {
        this.basisA = basisA;
        this.basisB = basisB;
    }

    protected float get(float x, float y, float z) {
        return operate(basisA.get(x, y, z), basisB.get(x, y, z));
    }
    protected float get(float x, float y) {
        return operate(basisA.get(x, y), basisB.get(x, y));
    }

    protected abstract float operate(float valA, float valB);
}
