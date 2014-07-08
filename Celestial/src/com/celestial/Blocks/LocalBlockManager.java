/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.celestial.Blocks;

import com.cubes.Block.Face;
import com.cubes.BlockChunkControl;
import com.cubes.BlockManager;
import com.cubes.BlockSkin;
import com.cubes.BlockSkin_TextureLocation;
import com.cubes.Vector3i;

/**
 *
 * @author kevint
 */
public class LocalBlockManager {

	public static void init() {
		BlockManager.getInstance().register(GameBlock.GRASS.getBClass(), new BlockSkin(new BlockSkin_TextureLocation[]{
				//We specify the 3 textures we need:
				//Grass, Earth-Grass-Transition and Earth
				new BlockSkin_TextureLocation(0, 0),
				new BlockSkin_TextureLocation(1, 0),
				new BlockSkin_TextureLocation(2, 0),
		}, false){

			@Override
			//The number that's being returned specified the index
			//of the texture in the previous declared TextureLocation array
			protected int getTextureLocationIndex(BlockChunkControl chunk, Vector3i blockLocation, Face face){
				if(chunk.isBlockOnSurface(blockLocation)){
					switch(face){
					case Top:
						return 0;

					case Bottom:
						return 2;

					case Left:
						return 1;
					case Right:
						return 1;
					case Front:
						return 1;
					case Back:
						return 1;
					}
					return 1;
				}
				return 2;
			}
		});
		BlockManager.getInstance().register(GameBlock.DIRT.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(2, 0), false));
		BlockManager.getInstance().register(GameBlock.LEAVES.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(7, 0), true));
		BlockManager.getInstance().register(GameBlock.WOOD.getBClass(), new BlockSkin(new BlockSkin_TextureLocation[]{
				new BlockSkin_TextureLocation(4, 0),
				new BlockSkin_TextureLocation(4, 0),
				new BlockSkin_TextureLocation(3, 0),
				new BlockSkin_TextureLocation(3, 0),
				new BlockSkin_TextureLocation(3, 0),
				new BlockSkin_TextureLocation(3, 0)
		}, false));
		BlockManager.getInstance().register(GameBlock.BIRCHWOOD.getBClass(), new BlockSkin(new BlockSkin_TextureLocation[]{
				new BlockSkin_TextureLocation(6, 0),
				new BlockSkin_TextureLocation(6, 0),
				new BlockSkin_TextureLocation(5, 0),
				new BlockSkin_TextureLocation(5, 0),
				new BlockSkin_TextureLocation(5, 0),
				new BlockSkin_TextureLocation(5, 0)
		}, false));
		BlockManager.getInstance().register(GameBlock.STONE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(4, 1), false));
		BlockManager.getInstance().register(GameBlock.COBBLE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(9, 0), false));
		BlockManager.getInstance().register(GameBlock.TORCH.getBClass(), new BlockSkin(new BlockSkin_TextureLocation[]{
				new BlockSkin_TextureLocation(1, 2),
				new BlockSkin_TextureLocation(1, 2),
				new BlockSkin_TextureLocation(0, 2),
				new BlockSkin_TextureLocation(0, 2),
				new BlockSkin_TextureLocation(0, 2),
				new BlockSkin_TextureLocation(0, 2),
		}, true));

		BlockManager.getInstance().register(GameBlock.COAL_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(10, 1), false));
		BlockManager.getInstance().register(GameBlock.COPPER_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(9, 1), false));
		BlockManager.getInstance().register(GameBlock.RAW_DIAMOND.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(8, 1), false));
		BlockManager.getInstance().register(GameBlock.TIN_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(7, 1), false));
		BlockManager.getInstance().register(GameBlock.GOLD_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(6, 1), false));
		BlockManager.getInstance().register(GameBlock.IRON_ORE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(5, 1), false));
		BlockManager.getInstance().register(GameBlock.SUBSTRATUS.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(10, 0), false));
		BlockManager.getInstance().register(GameBlock.DARKSTONE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(3, 1), false));
		BlockManager.getInstance().register(GameBlock.ICE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation(0, 1), true));
		BlockManager.getInstance().register(GameBlock.WORKBENCH.getBClass(), new BlockSkin(new BlockSkin_TextureLocation[]{
				new BlockSkin_TextureLocation(4, 3),
				new BlockSkin_TextureLocation(8, 0),
				new BlockSkin_TextureLocation(3, 3),
				new BlockSkin_TextureLocation(3, 3),
				new BlockSkin_TextureLocation(3, 3),
				new BlockSkin_TextureLocation(3, 3)
		}, true));
		BlockManager.getInstance().register(GameBlock.FURNACE.getBClass(), new BlockSkin(new BlockSkin_TextureLocation[]{
				new BlockSkin_TextureLocation(1, 3),
				new BlockSkin_TextureLocation(0, 3),
				new BlockSkin_TextureLocation(2, 3)
		}, false){
			@Override
			//The number that's being returned specified the index
			//of the texture in the previous declared TextureLocation array
			protected int getTextureLocationIndex(BlockChunkControl chunk, Vector3i blockLocation, Face face){
				for(Vector3i loc : chunk.getTerrain().getDynamicBlocks().keySet()) {
					if(loc.equals(blockLocation))
						if(chunk.getTerrain().getDynamicBlocks().get(loc) instanceof Block_Furnace) {
							if(((Block_Furnace) chunk.getTerrain().getDynamicBlocks().get(loc)).inUse)
								switch(face){
								case Top:
									return 1;

								case Bottom:
									return 1;

								case Left:
									return 1;
								case Right:
									return 1;
								case Front:
									return 2;
								case Back:
									return 1;
								}
						}
					break;
				}
				switch(face){
				case Top:
					return 1;

				case Bottom:
					return 1;

				case Left:
					return 1;
				case Right:
					return 1;
				case Front:
					return 0;
				case Back:
					return 1;
				}
				return 1;
			}
		});
	}

}
