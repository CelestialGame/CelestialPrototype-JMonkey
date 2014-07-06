package com.celestial.SinglePlayer.Components.Empire;

import java.util.ArrayList;

import com.celestial.SinglePlayer.Components.Planet;
import com.celestial.SinglePlayer.Components.Player;

public class Empire {
	
	Player owner;
	//Image flag;
	String name;
	String motto;
	//TODO implement empire types (Democracy, Republic, Dictatorship, etc)
	Stance defaultStance;
	Age age = Age.NEOLITHIC;
	Planet home;
	
	ArrayList<Empire> alliedEmpires = new ArrayList<Empire>();
	ArrayList<Empire> neutralEmpires = new ArrayList<Empire>();
	ArrayList<Empire> enemyEmpires = new ArrayList<Empire>();
	
	ArrayList<Building> structuresOwned = new ArrayList<Building>();
	
	public enum Stance {
		HOSTILE, AGGRESSIVE, NEUTRAL, DIPLOMATIC, FRIENDLY
	}
	public enum Age {
		NEOLITHIC("Neolithic"), 
		CLASSICAL("Classical"), 
		EXPLORATION("Exploration"), 
		INDUSTRIAL("Industrial"), 
		MODERN("Modern"), 
		SPACE("Space");
		
		String string;
		
		private Age(String name) {
			this.string = name;
		}
		
		public String toString() {
			return this.string;
		}
	}
	
	public Empire(Player owner, String name, String motto, Stance stance) {
		this.owner = owner;
		this.name = name;
		this.motto = motto;
		this.defaultStance = stance;
		this.home = owner.getPlanet();
	}
	
	public Empire(Player owner, String name, String motto, Stance stance, Age age) {
		this.owner = owner;
		this.name = name;
		this.motto = motto;
		this.defaultStance = stance;
		this.home = owner.getPlanet();
		this.age = age;
	}
	
	public void discoveredEmpire(Empire empire) {
		switch(this.defaultStance) {
		case HOSTILE:
			this.enemyEmpires.add(empire);
		case AGGRESSIVE:
			this.neutralEmpires.add(empire);
		case NEUTRAL:
			this.neutralEmpires.add(empire);
		case DIPLOMATIC:
			this.neutralEmpires.add(empire);
		case FRIENDLY:
			this.alliedEmpires.add(empire);
		default:
			this.neutralEmpires.add(empire);
		}
	}
	
	public Player getOwner() {
		return owner;
	}
	public String getName() {
		return name;
	}
	public String getMotto() {
		return motto;
	}
	public Stance getStance() {
		return this.defaultStance;
	}
	public Planet getHome() {
		return this.home;
	}
	public ArrayList<Empire> getAllies() {
		return this.alliedEmpires;
	}
	public ArrayList<Empire> getNeutrals() {
		return this.neutralEmpires;
	}
	public ArrayList<Empire> getEnemies() {
		return this.enemyEmpires;
	}
	public ArrayList<Building> getStructures() {
		return this.structuresOwned;
	}
}
