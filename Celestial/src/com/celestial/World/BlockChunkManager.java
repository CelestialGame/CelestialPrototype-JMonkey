package com.celestial.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.celestial.SinglePlayer.SPPortal;
import com.celestial.SinglePlayer.Components.Planet;
import com.celestial.World.Chunks.ChunkThreads;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.jme3.math.Vector3f;

public class BlockChunkManager {

	public static class PreGeneratedChunk {
		private BlockTerrainControl terrainControl;
		private int x;
		private int y;
		private int z;
		private Planet planet;
		public PreGeneratedChunk(BlockTerrainControl terrainControl, int locx, int locy, int locz, Planet planet) {
			this.terrainControl = terrainControl;
			x = locx;
			y = locy;
			z = locz;
			this.planet = planet;
		}
		public void generate() {
			new ChunkThreads.GenerateChunkThread(terrainControl, (x*Planet.CHUNK_SIZE), (y*Planet.CHUNK_SIZE), (z*Planet.CHUNK_SIZE), this).generateChunks();
		}
		public Vector3Int getLocation() {
			return new Vector3Int(x,y,z);
		}
		public Planet getPlanet() {
			return planet;
		}
	}



	Planet planet;
	BlockTerrainControl terrControl;
	private ExecutorService preGeneratedChunkService;
	private Future<List<PreGeneratedChunk>> preGeneratedChunkFutureTask;

	private List<PreGeneratedChunk> preGenChunks = new ArrayList<PreGeneratedChunk>();

	public BlockChunkManager(BlockTerrainControl terrControl, Planet planet) {
		this.planet = planet;
		this.terrControl = terrControl;
	}

	public void preGenerateChunks() 
	{
		this.preGeneratedChunkService = Executors.newFixedThreadPool(1);        
		this.preGeneratedChunkFutureTask = this.preGeneratedChunkService.submit(new ChunkThreads.PreGenChunksThread(planet.getDiameter(), this.terrControl, preGenChunks, planet));
	}

	public void checkChunks(Vector3f camLocation, Vector3f planetLocation) {
		if(!this.preGeneratedChunkService.isShutdown())
		{
			if(this.preGeneratedChunkFutureTask.isDone())
			{
				List<PreGeneratedChunk> chunkList = null;

				try {
					chunkList = this.preGeneratedChunkFutureTask.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

				this.preGeneratedChunkService.shutdown();

				if(chunkList != null)
					this.preGenChunks = chunkList;
				else
				{
					System.out.println("preGenChunk List was null!\nAt line: "+Thread.currentThread().getStackTrace()[2].getLineNumber()+"\nIn BlockChunkManager.");
					SPPortal.self.stopGame();
				}

			}
		}
		if(!this.preGenChunks.isEmpty()) {
			Iterator<PreGeneratedChunk> iterator = this.preGenChunks.iterator();
			while (iterator.hasNext()) {
				PreGeneratedChunk preGenChunk = (PreGeneratedChunk) iterator.next();
				float distance = camLocation.distance(Vector3Int.convert3Int(preGenChunk.getLocation()).add(planetLocation));
				if(distance < Planet.VIEW_DISTANCE) {
					preGenChunk.generate();
					iterator.remove();
				}
			}
		}
		for (int x = 0; x < this.planet.getDiameter(); x++)
			for (int y = 0; y < this.planet.getDiameter(); y++)
				for(int z = 0; z < this.planet.getDiameter(); z++) {
					float distance = camLocation.distance(new Vector3f(x,y,z).add(planetLocation));
					if(distance > Planet.VIEW_DISTANCE) {
						this.terrControl.getChunks()[x][y][z].unloadChunk();
					} else {
						this.terrControl.getChunks()[x][y][z].loadChunk();
					}
				}
	}

}
