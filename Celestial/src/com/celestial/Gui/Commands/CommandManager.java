package com.celestial.Gui.Commands;

import java.util.ArrayList;

import com.celestial.Celestial;
import com.celestial.Gui.Gui;
import com.celestial.SinglePlayer.SPPortal;
import com.celestial.SinglePlayer.Components.SectorCoord;
import com.celestial.util.WireProcessor;
import com.jme3.math.Vector3f;

import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;

public class CommandManager {
	
	private Celestial parent;
	private Gui gui;
	private Console console;
	
	private WireProcessor wP;
	
	private ArrayList<String> commands = new ArrayList<String>();
	
	public CommandManager(Celestial parent, Gui gui, Console console) {
		this.parent = parent;
		this.gui = gui;
		this.console = console;
		
		registerCommand("help");
		registerCommand("generateEntire");
		registerCommand("noclip");
		registerCommand("show");
	}
	
	public void registerCommand(String command) {
		if(!this.commands.contains(command)) {
			this.commands.add(command);
		}
	}
	
	public void onCommand(final ConsoleExecuteCommandEvent commandInfo) {
		String command = commandInfo.getCommand();
		String[] args = commandInfo.getArguments();
		
		if(!commands.contains(command)) {
			this.console.outputError("Invalid command.");
		} else {
			if(command.equalsIgnoreCase("help")) {
				StringBuilder sb = new StringBuilder();
				for(String com : commands) {
					sb.append(com + " ");
				}
				this.console.output("Commands: "+sb.toString());
			} else if (command.equalsIgnoreCase("generateEntire")) {
				if(args.length == 0) {
					this.console.outputError("Invalid command.");
					this.console.outputError("generateEntire [bool]");
				} else if(args.length == 1)
					if(Celestial.getPortal() instanceof SPPortal) {
						try {
							SPPortal.generateEntire = Boolean.parseBoolean(args[0]);
						} catch (Exception e) {
							this.console.outputError("Invalid command.");
							this.console.outputError("generateEntire [bool]");
						}
					} else {
						this.console.outputError("Unsupported command.");
					}
				else {
					this.console.outputError("Invalid command.");
					this.console.outputError("generateEntire [bool]");
				}
			}  else if (command.equalsIgnoreCase("noclip")) {
				if(args.length == 0) {
					if(Celestial.portal instanceof SPPortal) {
						if(Celestial.portal.player.getBulletAppState().isEnabled()) {
							Celestial.portal.player.getBulletAppState().setEnabled(false);
							Celestial.portal.setCamSpeed(100);
							Celestial.portal.player.setVisibleToClient(true);
							this.console.output("Noclip On");
						} else {
							Celestial.portal.player.getBulletAppState().setEnabled(true);
							Celestial.portal.player.setLocation(new Vector3f(Celestial.portal.cam.getLocation().getX(), Celestial.portal.cam.getLocation().getY()-Celestial.portal.getCamHeight(), Celestial.portal.cam.getLocation().getZ()));
							Celestial.portal.player.setVisibleToClient(false);
							this.console.output("Noclip Off");
						}
					} else {
						this.console.outputError("Unsupported command.");
					}
				}
				else {
					this.console.outputError("Invalid command.");
					this.console.outputError("generateEntire [bool]");
				}
			} else if(command.equalsIgnoreCase("show")) {
				if(args.length == 0) {
					this.console.outputError("Invalid command.");
					this.console.outputError("show <var>");
				} else if (args[0].equalsIgnoreCase("renderstats")) {
					String[] labels = Celestial.portal.app.getRenderer().getStatistics().getLabels();
					int[] stats = new int[labels.length];
					Celestial.portal.app.getRenderer().getStatistics().getData(stats);
					for(int i=0;i<Celestial.portal.app.getRenderer().getStatistics().getLabels().length; i++) {
						this.console.output(labels[i] + ": " + stats[i]);
					}
				} else if(args[0].equalsIgnoreCase("wireframe")) {
					if(wP == null)
						wP = new WireProcessor(parent.getAssetManager());
					if(!parent.getViewPort().getProcessors().contains(wP))
						parent.getViewPort().addProcessor(wP);
					else
						parent.getViewPort().removeProcessor(wP);
				} else if(args[0].equalsIgnoreCase("planetface")) {
					if(Celestial.portal instanceof SPPortal) {
						this.console.output(""+Celestial.portal.player.getCurrentFaceOfPlanet(Celestial.portal.galaxy.getPlanet(new SectorCoord(0,0,0), 0, 0)));
						this.console.output(""+Celestial.portal.player.getPlanet().getPlanetNode().getWorldTranslation().distance(Celestial.portal.player.getPlanet().getStar().getStarNode().getWorldTranslation()));
					} else {
						this.console.outputError("Unsupported command.");
					}
				}
			}
		}
	}

}
