package com.cubes.render;

import java.util.ArrayList;

import com.cubes.Block;
import com.cubes.Block.Face;
import com.cubes.BlockChunkControl;
import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockType;
import com.cubes.Vector3i;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

public class NaiveMesher extends VoxelMesher {

	@Override
	public Mesh generateMesh(BlockChunkControl terrain, int chunkSize) {
		this.terrain = terrain;
		
		ArrayList<Vector3f> verts = new ArrayList<>();
		ArrayList<Vector2f> textCoords = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();
		ArrayList<Float> normals = new ArrayList<>();
		Vector3f tmpLocation = new Vector3f();
		Vector3i tmpI = new Vector3i();
		for(int x=0;x<terrain.getTerrain().getSettings().getChunkSizeX();x++){
			for(int y=0;y<terrain.getTerrain().getSettings().getChunkSizeY();y++){
				for(int z=0;z<terrain.getTerrain().getSettings().getChunkSizeZ();z++){
					tmpI.setX(x).setY(y).setZ(z);
					try {
						BlockType bt = BlockManager.getInstance().getType(terrain.getBlockData().get(tmpI).getBlockType());
						BlockSkin skin = bt.getSkin();
						Vector3f faceLoc_frontBotLeft = tmpLocation.add(Block.FRONT_BOTTOM_LEFT.mult(terrain.getTerrain().getSettings().getBlockSize()));
						Vector3f faceLoc_frontBotRight = tmpLocation.add(Block.FRONT_BOTTOM_RIGHT.mult(terrain.getTerrain().getSettings().getBlockSize()));
						Vector3f faceLoc_rearBotLeft = tmpLocation.add(Block.REAR_BOTTOM_LEFT.mult(terrain.getTerrain().getSettings().getBlockSize()));
						Vector3f faceLoc_rearBotRight = tmpLocation.add(Block.REAR_BOTTOM_RIGHT.mult(terrain.getTerrain().getSettings().getBlockSize()));
						Vector3f faceLoc_frontTopLeft = tmpLocation.add(Block.FRONT_TOP_LEFT.mult(terrain.getTerrain().getSettings().getBlockSize()));
						Vector3f faceLoc_frontTopRight = tmpLocation.add(Block.FRONT_TOP_RIGHT.mult(terrain.getTerrain().getSettings().getBlockSize()));
						Vector3f faceLoc_rearTopLeft = tmpLocation.add(Block.REAR_TOP_LEFT.mult(terrain.getTerrain().getSettings().getBlockSize()));
						Vector3f faceLoc_rearTopRight = tmpLocation.add(Block.REAR_TOP_RIGHT.mult(terrain.getTerrain().getSettings().getBlockSize()));
						// Loop over all 6 faces and verify which one should be visible as we currently don't cache this value
						for (Face face : Face.values()) {
							if (terrain.isFaceVisible(tmpI, face)) {
								// Write the verts
								switch (face) {
								case Top:
									writeQuad(verts, indices, normals, 
											faceLoc_rearTopLeft, faceLoc_rearTopRight, faceLoc_frontTopLeft, faceLoc_frontTopRight, face);
									break;
								case Bottom:
									writeQuad(verts, indices, normals,
											faceLoc_rearBotRight, faceLoc_rearBotLeft, faceLoc_frontBotRight, faceLoc_frontBotLeft, face);
									break;
								case Left:
									writeQuad(verts, indices, normals, 
											faceLoc_frontBotLeft, faceLoc_rearBotLeft, faceLoc_frontTopLeft, faceLoc_rearTopLeft, face);
									break;
								case Right:
									writeQuad(verts, indices, normals, 
											faceLoc_rearBotRight, faceLoc_frontBotRight, faceLoc_rearTopRight, faceLoc_frontTopRight, face);
									break;
								case Front:
									writeQuad(verts, indices, normals, 
											faceLoc_rearBotLeft, faceLoc_rearBotRight, faceLoc_rearTopLeft, faceLoc_rearTopRight, face);
									break;
								case Back:
									writeQuad(verts, indices, normals, 
											faceLoc_frontBotRight, faceLoc_frontBotLeft, faceLoc_frontTopRight, faceLoc_frontTopLeft, face);
									break;
								default:       
								}
								// Write the texture coords, width and height is always 1 as we render each cube individually
								this.writeTextureCoords(textCoords, terrain, tmpI, face, 1, 1, skin);
							}
						}
					} catch (NullPointerException e) {
						;
					}
				}
			}
		}
		if (indices.isEmpty()) {
			return null;
		}
		// Return the generated mesh from the list of data
		return genMesh(verts, textCoords, indices, normals);
	}

}
