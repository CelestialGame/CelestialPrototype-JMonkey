package jme3tools.noise;

import java.util.Random;

/**
 * Special Thanks to Ken Perlin
 * @author jda1
 *
 */
/*
 * Credit to @dpwhittaker for coding these into Java and providing
 * his code on the JME3 forums
 * 
 */
public class Perlin extends Basis {

    int p[] = new int[512];

    public Perlin(long seed) {
        Random rand = new Random(seed);
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }
        for (int i = 0; i < 256; i++) {
            int a = rand.nextInt(256);
            int b = rand.nextInt(256);
            int temp = p[a];
            p[a] = p[b];
            p[b] = temp;
        }
        System.arraycopy(p, 0, p, 256, 256);
    }

    @Override
    protected float get(float x, float y, float z) {
        // FIND UNIT CUBE THAT CONTAINS POINT.
        int X = fastfloor(x), Y = fastfloor(y), Z = fastfloor(z);
        // FIND RELATIVE X,Y,Z OF POINT IN CUBE.
        x -= X;
        y -= Y;
        z -= Z;
        // WRAP TO 255X255X255 CUBE.
        X &= 255;
        Y &= 255;
        Z &= 255;
        // COMPUTE FADE CURVES FOR EACH OF X,Y,Z.
        float u = fade(x), v = fade(y), w = fade(z);
        // HASH COORDINATES OF THE 8 CUBE CORNERS
        int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z,
            B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z;

        // AND ADD BLENDED RESULTS FROM 8 CORNERS OF CUBE
        return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z),
                grad(p[BA], x - 1, y, z)),
                lerp(u, grad(p[AB], x, y - 1, z),
                grad(p[BB], x - 1, y - 1, z))),
                lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1),
                grad(p[BA + 1], x - 1, y, z - 1)),
                lerp(u, grad(p[AB + 1], x, y - 1, z - 1),
                grad(p[BB + 1], x - 1, y - 1, z - 1))));
    }

    @Override
    public float get(float x, float y) {
        // FIND UNIT SQUARE THAT CONTAINS POINT.
        int X = fastfloor(x), Y = fastfloor(y);
        // FIND RELATIVE X,Y OF POINT IN SQUARE.
        x -= X;
        y -= Y;

        X &= 255;
        Y &= 255;
        // COMPUTE FADE CURVES FOR EACH OF X,Y,Z.
        float u = fade(x), v = fade(y);
        // HASH COORDINATES OF THE 4 SQUARE CORNERS.
        int A = p[X] + Y, B = p[X + 1] + Y;
        // AND ADD BLENDED RESULTS FROM 4 CORNERS OF SQUARE
        return lerp(v, lerp(u, grad(p[A], x, y),
                grad(p[B], x - 1, y)),
                lerp(u, grad(p[A + 1], x, y - 1),
                grad(p[B + 1], x - 1, y - 1)));
    }

    private static float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    private static float grad(int hash, float x, float y, float z) {
        int h = hash & 15; // CONVERT LO 4 BITS OF HASH CODE
        float u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
                v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    static float grad(int hash, float x, float y) {
        int h = hash & 3; // CONVERT LO 2 BITS OF HASH CODE INTO 4 GRADIENT DIRS.
        float u = h<2 ? x : y;
        return (h&1) == 0 ? u : -u;
    }

    protected int fastfloor(float x) {
        return x > 0 ? (int) x : (int) x - 1;
    }
}
