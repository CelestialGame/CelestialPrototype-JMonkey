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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dpwhitt
 */
public class SummedNoise implements Noise {
    protected List<Noise> noiseList;

    public SummedNoise() {
        noiseList = new ArrayList<Noise>();
    }
    public SummedNoise(BasicNoise noiseA, BasicNoise noiseB) {
        noiseList = new ArrayList<Noise>();
        noiseList.add(noiseA);
        noiseList.add(noiseB);
    }

    public void add(Noise noise) {
        noiseList.add(noise);
    }

    public float noise(float x, float y, float z) {
        float ret = 0.0f;
        for (int i = 0; i < noiseList.size(); i++)
            ret += noiseList.get(i).noise(x, y, z);
        return ret;
    }

    public float noise(float x, float y) {
        float ret = 0.0f;
        for (int i = 0; i < noiseList.size(); i++)
            ret += noiseList.get(i).noise(x, y);
        return ret;
    }
}
