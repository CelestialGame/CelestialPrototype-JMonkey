package jme3tools.noise;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import java.util.Random;

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

public class BasicNoise implements Noise {
    protected Transform inputTransform;
    protected float outputScale;
    protected float outputOffset;
    protected int octaves;
    protected float lacunarity;
    protected float gain;
    protected boolean multiFractal;
    protected Basis basis;
    protected Random rand;

    public BasicNoise() {
        inputTransform = Transform.IDENTITY;
        outputScale = 1.0f;
        outputOffset = 0.0f;
        octaves = 5;
        lacunarity = 2.0f;
        gain = 0.5f;
        multiFractal = false;
        basis = new Perlin(System.nanoTime());
        rand = new Random(System.nanoTime());
    }
    public BasicNoise(Transform inputTransform, float outputScale, float outputOffset,
            long seed, int octaves, float lacunarity, float gain, boolean multiFractal,
            Basis basis) {
        this.inputTransform = inputTransform;
        this.outputScale = outputScale;
        this.outputOffset = outputOffset;
        this.octaves = octaves;
        this.lacunarity = lacunarity;
        this.gain = gain;
        this.multiFractal = multiFractal;
        this.basis = basis;

        if (this.basis == null)
            this.basis = new Perlin(seed);
        rand = new Random(seed);
    }

    public float noise(float x, float y, float z) {
        Vector3f pos = new Vector3f();
        getInputTransform().transformVector(new Vector3f(x, y, z), pos);

        float sum = 0;
        float amplitude = (getOctaves() == 1 ? 1.0f : 0.5f);
        float frequency = 1.0f;
        float prev = 1.0f;

        for (int i = 0; i < getOctaves(); i++)
        {
            float n = getBasis().get(pos.x * frequency, pos.y * frequency, pos.z * frequency);
            sum += n * amplitude * prev;
            if (isMultiFractal()) prev = n;
            frequency *= getLacunarity();
            amplitude *= getGain();
        }

        return sum * getOutputScale() + getOutputOffset();
    }

    public float noise(float x, float y) {
        Vector3f pos = new Vector3f();
        getInputTransform().transformVector(new Vector3f(x, y, 1), pos);

        float sum = 0;
        float amplitude = (getOctaves() == 1 ? 1.0f : 0.5f);
        float frequency = 1.0f;
        float prev = 1.0f;

        for (int i = 0; i < getOctaves(); i++)
        {
            float n = getBasis().get(pos.x * frequency, pos.y * frequency);
            sum += n * amplitude * prev;
            if (isMultiFractal()) prev = n;
            frequency *= getLacunarity();
            amplitude *= getGain();
        }

        return sum * getOutputScale() + getOutputOffset();

    }

    /**
     * @return the inputTransform
     */
    public Transform getInputTransform() {
        return inputTransform;
    }

    /**
     * @param inputTransform the inputTransform to set
     */
    public void setInputTransform(Transform inputTransform) {
        this.inputTransform = inputTransform;
    }

    /**
     * @return the outputScale
     */
    public float getOutputScale() {
        return outputScale;
    }

    /**
     * @param outputScale the outputScale to set
     */
    public void setOutputScale(float outputScale) {
        this.outputScale = outputScale;
    }

    /**
     * @return the outputOffset
     */
    public float getOutputOffset() {
        return outputOffset;
    }

    /**
     * @param outputOffset the outputOffset to set
     */
    public void setOutputOffset(float outputOffset) {
        this.outputOffset = outputOffset;
    }

    /**
     * @return the octaves
     */
    public int getOctaves() {
        return octaves;
    }

    /**
     * @param octaves the octaves to set
     */
    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    /**
     * @return the lacunarity
     */
    public float getLacunarity() {
        return lacunarity;
    }

    /**
     * @param lacunarity the lacunarity to set
     */
    public void setLacunarity(float lacunarity) {
        this.lacunarity = lacunarity;
    }

    /**
     * @return the gain
     */
    public float getGain() {
        return gain;
    }

    /**
     * @param gain the gain to set
     */
    public void setGain(float gain) {
        this.gain = gain;
    }

    /**
     * @return the multiFractal
     */
    public boolean isMultiFractal() {
        return multiFractal;
    }

    /**
     * @param multiFractal the multiFractal to set
     */
    public void setMultiFractal(boolean multiFractal) {
        this.multiFractal = multiFractal;
    }

    /**
     * @return the basis
     */
    public Basis getBasis() {
        return basis;
    }

    /**
     * @param basis the basis to set
     */
    public void setBasis(Basis basis) {
        this.basis = basis;
    }
}

