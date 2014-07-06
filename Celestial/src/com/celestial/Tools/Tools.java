package com.celestial.Tools;

public enum Tools {
	
	MARKER(Marker.class, "Marker", 1, null),
	STICK(Stick.class, "Stick", 2, null),
	COPPER_INGOT(Copper_Ingot.class, "Copper Ingot", 3, null);
	
	private Class<? extends Tool> toolClass;
	private String name;
	private int id;
	private String iconpath;

	private Tools(Class<? extends Tool> toolClass, String name, int id, String iconpath) {
		this.toolClass = toolClass;
		this.iconpath = iconpath;
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getIconPath() {
		return this.iconpath;
	}
	
	public int getID() {
		return id;
	}
	public Class<? extends Tool> getToolClass() {
		return this.toolClass;
	}
}
