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
public class Sum extends BinaryOperator {
    public Sum(Basis basisA, Basis basisB) {
        super(basisA, basisB);
    }
    
    @Override
    protected float operate(float valA, float valB) {
        return valA + valB;
    }

}
