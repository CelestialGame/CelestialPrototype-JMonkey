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

public abstract class UnaryOperator extends Basis {

    Basis basis;
    public UnaryOperator(Basis basis) {
        this.basis = basis;
    }

    protected float get(float x, float y, float z) {
        return operate(basis.get(x, y, z));
    }
    protected float get(float x, float y) {
        return operate(basis.get(x, y));
    }

    protected abstract float operate(float val);
}
