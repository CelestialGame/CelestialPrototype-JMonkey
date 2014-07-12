package com.celestial.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.celestial.Celestial;
import com.celestial.SinglePlayer.SPPortal;
import com.celestial.SinglePlayer.Components.Planet.Planet;
import com.celestial.World.Chunks.ChunkGeneratorCallable;
import com.cubes.BlockData;
import com.cubes.BlockTerrainControl;
import com.cubes.Vector3i;
import com.jme3.math.Vector3f;

public class BlockChunkManager
{
    
    private Planet planet;
    BlockTerrainControl terrainControl;
    private HashMap<Vector3i, Future<Map<Vector3i, BlockData>>> generatingChunks = new HashMap<Vector3i, Future<Map<Vector3i, BlockData>>>();
    
    public BlockChunkManager(BlockTerrainControl terrControl, Planet planet)
    {
	this.planet = planet;
	this.terrainControl = terrControl;
	
    }
    
    public void checkChunks()
    {
	for(Iterator<Entry<Vector3i, Future<Map<Vector3i, BlockData>>>> iterator = this.generatingChunks
		.entrySet().iterator(); iterator.hasNext();)
	{
	    Entry<Vector3i, Future<Map<Vector3i, BlockData>>> entry = iterator
		    .next();
	    if(entry.getValue().isDone())
	    {
		Map<Vector3i, BlockData> map = null;
		try
		{
		    map = entry.getValue().get();
		}
		catch(InterruptedException | ExecutionException e)
		{
		    e.printStackTrace();
		}
		
		if(map == null)
		{
		    iterator.remove();
		    continue;
		}
		this.terrainControl.loadChunk(entry.getKey(), map);
		iterator.remove();
	    }
	    else if(entry.getValue().isCancelled())
	    {
		iterator.remove();
		continue;
	    }
	}
	
	for(int x = 0; x < this.getPlanet().getDiameter(); x++)
	    for(int y = 0; y < this.getPlanet().getDiameter(); y++)
		for(int z = 0; z < this.getPlanet().getDiameter(); z++)
		{
		    
		    Vector3f centerOfChunk = new Vector3f(
			    ((x * Planet.CHUNK_SIZE * 3) + (Planet.CHUNK_SIZE / 2) * 3),
			    ((y * Planet.CHUNK_SIZE * 3) + (Planet.CHUNK_SIZE / 2) * 3),
			    ((z * Planet.CHUNK_SIZE * 3) + (Planet.CHUNK_SIZE / 2) * 3))
			    .add(getPlanet().getOriginalTerrainTranslation());
		    
		    Vector3f currentPlanetTranslation = getPlanet()
			    .getCurrentPlanetTranslation();
		    Vector3f planetToCamera = SPPortal.self.getCam().getLocation()
			    .subtract(currentPlanetTranslation);
		    Vector3f rotatedCameraTranslation = getPlanet()
			    .getOriginalPlanetTranslation().add(
				    getPlanet()
					    .getPlanetNode()
					    .getLocalRotation()
					    .inverse()
					    .mult(getPlanet().getStarNode()
						    .getWorldRotation()
						    .inverse()
						    .mult(planetToCamera)));
		    
		    float distance = rotatedCameraTranslation
			    .distance(centerOfChunk);
		    Vector3i location = new Vector3i(x, y, z);
		    if(distance > Planet.VIEW_DISTANCE)
		    {
			if(this.generatingChunks.containsKey(location)
				|| !this.terrainControl.isChunkLoaded(location))
			    continue;
			// this.terrainControl.unLoadChunk(new Vector3i(x,y,z));
		    }
		    else
		    {
			if(this.generatingChunks.containsKey(location)
				|| this.terrainControl.isChunkLoaded(location))
			    continue;
			this.startChunkGeneration(location);
		    }
		}
    }
    
    public void startChunkGeneration(Vector3i chunkLocation)
    {
	try
	{
	    Future<Map<Vector3i, BlockData>> newFuture = Celestial.executor
		    .submit(new ChunkGeneratorCallable(chunkLocation,
			    this.planet.getDiameter(), Planet.CHUNK_SIZE,
			    this.planet.getType(), this.planet.getSeed()));
	    this.generatingChunks.put(chunkLocation, newFuture);
	}
	catch(Exception e)
	{
	    // e.printStackTrace();
	}
    }
    
    /**
     * @return the planet
     */
    public Planet getPlanet()
    {
	return planet;
    }
    
}
