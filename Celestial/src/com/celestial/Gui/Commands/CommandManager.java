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

public class CommandManager
{
    
    private Celestial parent;
    private Gui gui;
    private Console console;
    
    private WireProcessor wP;
    
    private ArrayList<String> commands = new ArrayList<String>();
    
    public CommandManager(Celestial parent, Gui gui, Console console)
    {
	this.parent = parent;
	this.gui = gui;
	this.console = console;
	
	registerCommand("help");
	registerCommand("generateEntire");
	registerCommand("noclip");
	registerCommand("show");
    }
    
    public void registerCommand(String command)
    {
	if (!this.commands.contains(command))
	{
	    this.commands.add(command);
	}
    }
    
    public void onCommand(final ConsoleExecuteCommandEvent commandInfo)
    {
	String command = commandInfo.getCommand();
	String[] args = commandInfo.getArguments();
	
	if (!commands.contains(command))
	{
	    this.console.outputError("Invalid command.");
	}
	else
	{
	    if (command.equalsIgnoreCase("help"))
	    {
		StringBuilder sb = new StringBuilder();
		for (String com : commands)
		{
		    sb.append(com + " ");
		}
		this.console.output("Commands: " + sb.toString());
	    }
	    else if (command.equalsIgnoreCase("generateEntire"))
	    {
		if (args.length == 0)
		{
		    this.console.outputError("Invalid command.");
		    this.console.outputError("generateEntire [bool]");
		}
		else if (args.length == 1)
		    if (parent.getPortal() instanceof SPPortal)
		    {
			try
			{
			    SPPortal.generateEntire = Boolean
				    .parseBoolean(args[0]);
			}
			catch (Exception e)
			{
			    this.console.outputError("Invalid command.");
			    this.console.outputError("generateEntire [bool]");
			}
		    }
		    else
		    {
			this.console.outputError("Unsupported command.");
		    }
		else
		{
		    this.console.outputError("Invalid command.");
		    this.console.outputError("generateEntire [bool]");
		}
	    }
	    else if (command.equalsIgnoreCase("noclip"))
	    {
		if (args.length == 0)
		{
		    if (this.parent.portal instanceof SPPortal)
		    {
			if (parent.portal.player.getBulletAppState()
				.isEnabled())
			{
			    parent.portal.player.getBulletAppState()
				    .setEnabled(false);
			    parent.portal.setCamSpeed(100);
			    parent.portal.player.setVisibleToClient(true);
			    this.console.output("Noclip On");
			}
			else
			{
			    parent.portal.player.getBulletAppState()
				    .setEnabled(true);
			    parent.portal.player.setLocation(new Vector3f(
				    parent.portal.cam.getLocation().getX(),
				    parent.portal.cam.getLocation().getY()
					    - parent.portal.getCamHeight(),
				    parent.portal.cam.getLocation().getZ()));
			    parent.portal.player.setVisibleToClient(false);
			    this.console.output("Noclip Off");
			}
		    }
		    else
		    {
			this.console.outputError("Unsupported command.");
		    }
		}
		else
		{
		    this.console.outputError("Invalid command.");
		    this.console.outputError("generateEntire [bool]");
		}
	    }
	    else if (command.equalsIgnoreCase("show"))
	    {
		if (args.length == 0)
		{
		    this.console.outputError("Invalid command.");
		    this.console.outputError("show <var>");
		}
		else if (args[0].equalsIgnoreCase("renderstats"))
		{
		    String[] labels = parent.app.getRenderer().getStatistics()
			    .getLabels();
		    int[] stats = new int[labels.length];
		    parent.app.getRenderer().getStatistics().getData(stats);
		    for (int i = 0; i < parent.app.getRenderer()
			    .getStatistics().getLabels().length; i++)
		    {
			this.console.output(labels[i] + ": " + stats[i]);
		    }
		}
		else if (args[0].equalsIgnoreCase("wireframe"))
		{
		    if (wP == null)
			wP = new WireProcessor(parent.getAssetManager());
		    if (!parent.getViewPort().getProcessors().contains(wP))
			parent.getViewPort().addProcessor(wP);
		    else
			parent.getViewPort().removeProcessor(wP);
		}
		else if (args[0].equalsIgnoreCase("planetface"))
		{
		    if (this.parent.portal instanceof SPPortal)
		    {
			this.console
				.output(""
					+ parent.portal.player
						.getCurrentFaceOfPlanet(parent.portal.galaxy
							.getPlanet(
								new SectorCoord(
									0, 0, 0),
								0, 0)));
			this.console
				.output(""
					+ parent.portal.player
						.getPlanet()
						.getPlanetNode()
						.getWorldTranslation()
						.distance(
							parent.portal.player
								.getPlanet()
								.getStar()
								.getStarNode()
								.getWorldTranslation()));
		    }
		    else
		    {
			this.console.outputError("Unsupported command.");
		    }
		}
	    }
	}
    }
    
}
