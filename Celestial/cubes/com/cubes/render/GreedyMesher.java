package com.cubes.render;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Mikola Lysenko - https://github.com/mikolalysenko/greedy-mesher
 * 
 * Adaptation to Java by Robert O'Leary - https://github.com/roboleary/GreedyMesh
 * Minor updates to allow Texturing by Nick Minkler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


import com.celestial.SinglePlayer.Components.Planet.Planet;
import com.cubes.Block;
import com.cubes.BlockChunkControl;
import com.cubes.BlockManager;
import com.cubes.Vector3i;
import com.cubes.Block.Face;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

import java.util.ArrayList;

public class GreedyMesher extends VoxelMesher {

	@Override
    public Mesh generateMesh(BlockChunkControl terrain, int chunkSize, boolean isTransparent) {
		this.terrain = terrain;
		
        ArrayList<Vector3f> verts = new ArrayList<Vector3f>();
        ArrayList<Vector2f> textCoords = new ArrayList<Vector2f>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        ArrayList<Float> normals = new ArrayList<Float>();

        Vector3i tmpI = new Vector3i();
        
        int i, j, k, l, h, w, u, v, n, r, s, t;
 
        Face face = null;
        final int[] x = new int[] {0, 0, 0};
        final int[] q = new int[] {0, 0, 0};
        int[] du = new int[] {0, 0, 0};
        int[] dv = new int[] {0, 0, 0};  
        
        final byte[] mask = new byte[chunkSize * chunkSize];
        // First pass is for front face, second pass is for back face
        for (boolean backFace = true, b = false; b != backFace; backFace = backFace && b, b = !b) {
            // Loop over all 3 dimensions
            for (int d = 0; d < 3; d++) {
                u = (d + 1) % 3;
                v = (d + 2) % 3;
                
                x[0] = 0; x[1] = 0; x[2] = 0;
                q[0] = 0; q[1] = 0; q[2] = 0; q[d] = 1;
                
                // Keep track of what face we are computing
                if (d ==0) {
                    face = backFace ? Face.Right : Face.Left;
                } else if (d == 1) {
                    face = backFace ? Face.Top : Face.Bottom;
                } else if (d == 2) {
                    face = backFace ? Face.Front : Face.Back;
                }
                // Loop over the entire voxel volume to generate the mask
                for (x[d] = 0; x[d] < chunkSize; x[d]++) {
                    n = 0;
                    for (x[v] = 0; x[v] < chunkSize; x[v]++) {
                        for (x[u] = 0; x[u] < chunkSize; x[u]++) {
                            tmpI.set(x[0], x[1], x[2]);
                            try {
                            	mask[n++] = terrain.isFaceVisible(tmpI, face) ? terrain.getBlock(tmpI).getType() : 0;
                            } catch(NullPointerException e) {
                            	;
                            }
                        }
                    }
                    
                    n = 0;
                    for (j = 0; j < chunkSize; j++) {
                        for (i = 0; i < chunkSize;) {
                            // Loop until we find a start point
                            if (mask[n] != 0) {
                                // Find the width of this mask section, w == current width
                                for (w = 1; w + i < chunkSize && mask[n + w] != 0 && mask[n + w] == mask[n]; w++) { }
                                
                                boolean done = false;
                                // find the height of the mask section, h == current height
                                for (h = 1; j + h < chunkSize; h++) {
                                    // make sure a whoel row of each height matches the width
                                    for (k = 0; k < w; k++) {
                                        if (mask[n + k + h * chunkSize] == 0 || mask[n + k + h * chunkSize] != mask[n]) {
                                            done = true;
                                            break;
                                        }
                                    }
                                    if (done) break;
                                }
                                x[u] = i;
                                x[v] = j;
                                du[0] = 0; du[1] = 0; du[2] = 0; du[u] = w;
                                dv[0] = 0; dv[1] = 0; dv[2] = 0; dv[v] = h;
                                if (!backFace) {
                                    r = x[0];
                                    s = x[1];
                                    t = x[2];
                                } else {
                                    r = x[0] + q[0];
                                    s = x[1] + q[1];
                                    t = x[2] + q[2];
                                }
                                Vector3f vec0 = new Vector3f(r, s, t);
                                Vector3f vec1 = new Vector3f(r + du[0], s + du[1], t + du[2]);
                                Vector3f vec2 = new Vector3f(r + dv[0], s + dv[1], t + dv[2]);
                                Vector3f vec3 = new Vector3f(r + du[0] + dv[0], s + du[1] + dv[1], t + du[2] + dv[2]);
                                
                                // Each face has a specific order of vertices otherwise the textures rotate incorrectly
                                // width/height are flipped when dealing with left/right/bottom face due to how rotation of dimensions works, and what order the greedy mesher merges them
                                switch (face) {
                                    case Top:
                                        writeQuad(verts, indices, normals, vec1, vec3, vec0, vec2, face);
                                        writeTextureCoords(textCoords, terrain, tmpI.set(x[0], x[1], x[2]), face, h, w, BlockManager.getInstance().getType(mask[n]).getSkin());
                                        break;
                                    case Bottom:
                                        writeQuad(verts, indices, normals, vec3, vec1, vec2, vec0, face);
                                        writeTextureCoords(textCoords, terrain, tmpI.set(x[0], x[1], x[2]), face, h, w, BlockManager.getInstance().getType(mask[n]).getSkin());
                                        break;
                                    case Left:
                                        writeQuad(verts, indices, normals, vec0, vec2, vec1, vec3, face);
                                        writeTextureCoords(textCoords, terrain, tmpI.set(x[0], x[1], x[2]), face, h, w, BlockManager.getInstance().getType(mask[n]).getSkin());
                                        break;
                                    case Right:
                                        //TODO: figure out why right face overwrites transparency
                                        writeQuad(verts, indices, normals, vec2, vec0, vec3, vec1, face);
                                        writeTextureCoords(textCoords, terrain, tmpI.set(x[0], x[1], x[2]), face, h, w, BlockManager.getInstance().getType(mask[n]).getSkin());
                                        break;
                                    case Front:
                                        writeQuad(verts, indices, normals, vec0, vec1, vec2, vec3, face);
                                        writeTextureCoords(textCoords, terrain, tmpI.set(x[0], x[1], x[2]), face, w, h, BlockManager.getInstance().getType(mask[n]).getSkin());
                                        break;
                                    case Back:
                                        writeQuad(verts, indices, normals, vec1, vec0, vec3, vec2, face);
                                        writeTextureCoords(textCoords, terrain, tmpI.set(x[0], x[1], x[2]), face, w, h, BlockManager.getInstance().getType(mask[n]).getSkin());
                                        break;
                                }
                                
                                // Clear the mask
                                for (l = 0; l < h; ++l) {
                                    for (k = 0; k < w; ++k) {
                                        mask[n + k + l * chunkSize] = 0;
                                    }
                                }
                                // increment i and n
                                i += w;
                                n += w;
                            } else {
                                i++;
                                n++;
                            }
                        }
                    }
                }
            }
        }
        if (indices.isEmpty()) {
            //return new NaiveMesher().generateMesh(terrain, chunkSize, isTransparent);
        	return null;
        }
        return genMesh(verts, textCoords, indices, normals);
    }
    
}
