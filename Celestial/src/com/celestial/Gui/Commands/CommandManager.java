package com.celestial.Gui.Commands;

import java.util.ArrayList;

import com.celestial.Celestial;
import com.celestial.Gui.Gui;
import com.celestial.SinglePlayer.SPPortal;

import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;

public class CommandManager {
	
	private Celestial parent;
	private Gui gui;
	private Console console;
	
	private ArrayList<String> commands = new ArrayList<String>();
	
	public CommandManager(Celestial parent, Gui gui, Console console) {
		this.parent = parent;
		this.gui = gui;
		this.console = console;
		
		registerCommand("help");
		registerCommand("generateEntire");
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
					if(parent.getPortal() instanceof SPPortal) {
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
			}
		}
	}

}
