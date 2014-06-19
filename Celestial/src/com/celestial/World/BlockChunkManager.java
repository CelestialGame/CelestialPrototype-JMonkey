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
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

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
			new ChunkThreads.GenerateChunkThread(terrainControl, (x*Planet.CHUNK_SIZE), (y*Planet.CHUNK_SIZE), (z*Planet.CHUNK_SIZE), this).start();
		}
		public Vector3Int getLocation() {
			return new Vector3Int(x,y,z);
		}
		public Planet getPlanet() {
			return planet;
		}
	}



	Planet planet;
	BlockTerrainControl terrainControl;
	private ExecutorService preGeneratedChunkService;
	private Future<List<PreGeneratedChunk>> preGeneratedChunkFutureTask;

	private List<PreGeneratedChunk> preGenChunks = new ArrayList<PreGeneratedChunk>();
	private Box blockHighlight;
	private Geometry blockHighlightGeom;
	private Material blockHighlightMat;

	public BlockChunkManager(BlockTerrainControl terrControl, Planet planet) {
		this.planet = planet;
		this.terrainControl = terrControl;
		
		this.blockHighlight = new Box(8*3+0.2f,8*3+0.2f,8*3+0.2f);
		blockHighlight.setLineWidth(10f);
		blockHighlightGeom = new Geometry("blockHighlight", this.blockHighlight);
		blockHighlightMat = new Material(SPPortal.self.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		blockHighlightMat.getAdditionalRenderState().setWireframe(true);
		
		blockHighlightMat.setColor("Color", new ColorRGBA(0f, 0f, 0f, 1f));
		blockHighlightGeom.setMaterial(blockHighlightMat);

		blockHighlightMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		blockHighlightGeom.setQueueBucket(Bucket.Transparent);
		SPPortal.self.getRootNode().attachChild(blockHighlightGeom);
	}

	public void preGenerateChunks() 
	{
		this.preGeneratedChunkService = Executors.newFixedThreadPool(1);        
		this.preGeneratedChunkFutureTask = this.preGeneratedChunkService.submit(new ChunkThreads.PreGenChunksThread(planet.getDiameter(), this.terrainControl, preGenChunks, planet));
	}

	public void checkChunks(Vector3f camLocation) {
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
				Vector3f centerOfChunk = new Vector3f(((preGenChunk.x*Planet.CHUNK_SIZE*3)+(Planet.CHUNK_SIZE/2)*3),((preGenChunk.y*Planet.CHUNK_SIZE*3)+(Planet.CHUNK_SIZE/2)*3),((preGenChunk.z*Planet.CHUNK_SIZE*3)+(Planet.CHUNK_SIZE/2)*3)).add(planet.getOriginalTerrainTranslation());

				Vector3f currentPlanetTranslation = planet.getCurrentPlanetTranslation();
				Vector3f planetToCamera = camLocation.subtract(currentPlanetTranslation);
				Vector3f rotatedCameraTranslation = planet.getOriginalPlanetTranslation().add(planet.getStarNode().getWorldRotation().inverse().mult(planetToCamera));
									
				float distance = rotatedCameraTranslation.distance(centerOfChunk);
				if(distance < Planet.VIEW_DISTANCE) {
					preGenChunk.generate();
					iterator.remove();
				}
			}
		}
		for (int x = 0; x < this.planet.getDiameter(); x++)
			for (int y = 0; y < this.planet.getDiameter(); y++)
				for(int z = 0; z < this.planet.getDiameter(); z++) {
					Vector3f centerOfChunk = new Vector3f(((x*Planet.CHUNK_SIZE*3)+(Planet.CHUNK_SIZE/2)*3),((y*Planet.CHUNK_SIZE*3)+(Planet.CHUNK_SIZE/2)*3),((z*Planet.CHUNK_SIZE*3)+(Planet.CHUNK_SIZE/2)*3)).add(planet.getOriginalTerrainTranslation());

					Vector3f currentPlanetTranslation = planet.getCurrentPlanetTranslation();
					Vector3f planetToCamera = camLocation.subtract(currentPlanetTranslation);
					Vector3f rotatedCameraTranslation = planet.getOriginalPlanetTranslation().add(planet.getStarNode().getWorldRotation().inverse().mult(planetToCamera));
										
					float distance = rotatedCameraTranslation.distance(centerOfChunk);
					if(distance > Planet.VIEW_DISTANCE) 
					{
						this.terrainControl.getChunks()[x][y][z].unloadChunk();
					} else {
						this.terrainControl.getChunks()[x][y][z].loadChunk();
					}
				}
	}

}
