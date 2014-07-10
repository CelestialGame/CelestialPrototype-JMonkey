package com.celestial.SinglePlayer.Components.Planet;

import java.util.EnumSet;

public enum PlanetType {
	
	HABITABLE, 
	INNER, 
	OUTER, 
	INFERNO, 
	FRIGID, 
	MOON;
	
	public static final EnumSet<PlanetType> PLANETTYPES = EnumSet.range(HABITABLE, FRIGID);
	public static final EnumSet<PlanetType> HOSTILETYPES = EnumSet.range(INNER, MOON);
	public static final EnumSet<PlanetType> ATMOSPHERETYPES = EnumSet.range(HABITABLE, OUTER);

	public final boolean isPlanetType() {
		return PLANETTYPES.contains(this);
	}
	public final boolean isHostile() {
		return HOSTILETYPES.contains(this);
	}
	public final boolean hasAtmosphere() {
		return ATMOSPHERETYPES.contains(this);
	}

	public static final EnumSet<PlanetType> ALLTYPES = EnumSet.allOf(PlanetType.class);
	
}
