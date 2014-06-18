package com.celestial.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.celestial.SinglePlayer.Components.Planet;
import com.celestial.World.Chunks.ChunkThreads;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3Int;
import com.jme3.math.Vector3f;

public class BlockChunkManager extends Thread {
	
	public static class PreGeneratedChunk {
		private BlockTerrainControl bTC;
		private int x;
		private int y;
		private int z;
		private Planet planet;
		public PreGeneratedChunk(BlockTerrainControl terr, int locx, int locy, int locz, Planet planet) {
			bTC = terr;
			x = locx;
			y = locy;
			z = locz;
			this.planet = planet;
		}
		public void generate() {
			new ChunkThreads.GenerateChunkThread(bTC, (x*Planet.CHUNK_SIZE), (y*Planet.CHUNK_SIZE), (z*Planet.CHUNK_SIZE), this).run();
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
	Vector3f camLocation = new Vector3f(0,0,0);
	Vector3f planetLocation = new Vector3f(0,0,0);
	
	boolean threadRunning = false;
	
	private List<PreGeneratedChunk> preGenChunks = new ArrayList<PreGeneratedChunk>();
	
	public BlockChunkManager(BlockTerrainControl terrControl, Planet planet) {
		this.planet = planet;
		this.terrControl = terrControl;
	}
	
	public void update(Vector3f location, Vector3f planetLocation) {
		camLocation = location;
		this.planetLocation = planetLocation;
	}
	
	public void preGenerateChunks() {
		final ExecutorService service;
        final Future<List<PreGeneratedChunk>>  task;

        service = Executors.newFixedThreadPool(1);        
        task    = service.submit(new ChunkThreads.PreGenChunksThread(planet.getDiameter(), this.terrControl, preGenChunks, planet));

        try 
        {
            final List<PreGeneratedChunk> list;

            list = task.get();
            this.preGenChunks = list;
        }
        catch(final InterruptedException ex){ex.printStackTrace();}
        catch(final ExecutionException ex){ ex.printStackTrace();}

        service.shutdownNow();
		
	}
	
	@Override
	public void start() {
		super.start();
		threadRunning = true;
	}
	@Override
	public void interrupt() {
		super.interrupt();
		threadRunning = false;
	}
	
	@Override
	public void run() {
		while(threadRunning) {
			if(!this.preGenChunks.isEmpty()) {
				Iterator<com.celestial.World.BlockChunkManager.PreGeneratedChunk> iterator = this.preGenChunks.iterator();
				while (iterator.hasNext()) {
					PreGeneratedChunk preGenChunk = (PreGeneratedChunk) iterator.next();
					float distance = camLocation.distance(Vector3Int.convert3Int(preGenChunk.getLocation()).add(this.planetLocation));
		    		if(distance < Planet.VIEW_DISTANCE) {
		    			preGenChunk.generate();
		    			iterator.remove();
		    		}
				}
			}
			for (int x = 0; x < this.terrControl.getChunks().length; x++)
	    	    for (int y = 0; y < this.terrControl.getChunks()[0].length; y++)
	    	    	for(int z = 0; z < this.terrControl.getChunks()[0][0].length; z++) {
	    	    		float distance = camLocation.distance(new Vector3f(x,y,z).add(this.planetLocation));
	    	    		if(distance > Planet.VIEW_DISTANCE) {
	    	    			this.terrControl.getChunks()[x][y][z].unloadChunk();
	    	    		} else {
	    	    			this.terrControl.getChunks()[x][y][z].loadChunk();
	    	    		}
	    	    	}
		}
	}

}
