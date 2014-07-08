package jme3tools.noise;

import com.jme3.math.FastMath;

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
public class Ridge extends UnaryOperator {
    protected float offset;

    public Ridge() {
        super(new Perlin(System.nanoTime()));
        this.offset = 1.0f;
    }
    public Ridge(Basis basis) {
        super(basis);
        this.offset = 1.0f;
    }
    public Ridge(Basis basis, float offset) {
        super(basis);
        this.offset = offset;
    }
    
    @Override
    protected float operate(float val) {
        val = FastMath.abs(val);
        val = offset - val;
        val = val * val;
        return val;
    }

}
