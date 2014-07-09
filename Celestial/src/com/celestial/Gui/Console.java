package com.celestial.Gui;

import com.celestial.Celestial;
import com.celestial.CelestialPortal;
import com.celestial.Gui.Commands.CommandManager;
import com.jme3.input.InputManager;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Console implements ScreenController, KeyInputHandler{

	private Celestial parent;
	private Gui guiManager;
	private CommandManager commandManager;
	
	private Nifty nifty;
	private Screen screen;
	private Element consolePopup;
	private boolean consoleVisible = false;
	private boolean allowConsoleToggle = true;
	private boolean firstConsoleShow = true;
	
	public Console(Celestial parent, Gui gui) {
		this.parent = parent;
		this.guiManager = gui;
	}

	@Override
	public void bind(Nifty arg0, Screen arg1) {
		nifty = arg0;
		screen= arg1;
		screen.addKeyboardInputHandler(new DefaultInputMapping(), this);
		consolePopup = nifty.createPopup("consolePopup");
		this.commandManager = new CommandManager(parent, guiManager, consolePopup.findNiftyControl("console", de.lessvoid.nifty.controls.Console.class));
	}

	@Override
	public void onEndScreen() {}

	@Override
	public void onStartScreen() {}

	@Override
	public boolean keyEvent(final NiftyInputEvent inputEvent) {
		if (inputEvent == NiftyInputEvent.ConsoleToggle) {
			toggleConsole();
			return true;
		} else {
			return false;
		}
	}

	private void toggleConsole() {
		if (allowConsoleToggle) {
			allowConsoleToggle = false;
			if (consoleVisible) {
				closeConsole();
				guiManager.enableControl();
				guiManager.enableInput();
			} else {
				openConsole();
				guiManager.disableControl();
				guiManager.disableInput();
			}
		}
	}

	private void openConsole() {
		String id = consolePopup != null ? consolePopup.getId() : null;
		if (id == null) {
			return;
		}
		nifty.showPopup(screen, id, consolePopup.findElementByName("console#textInput"));
		screen.processAddAndRemoveLayerElements();

		if (firstConsoleShow) {
			firstConsoleShow = false;
			de.lessvoid.nifty.controls.Console console = screen.findNiftyControl("console", de.lessvoid.nifty.controls.Console.class);
			if (console != null) {
				console.output("      ___           ___     \n"+
							   "     /\\__\\         /\\__\\    \n"+
							   "    /:/  /        /:/  /    \n"+
							   "   /:/  /        /:/  /     \n"+
							   "  /:/  /  ___   /:/  /  ___ \n"+
							   " /:/__/  /\\__\\ /:/__/  /\\__\\\n"+
							   " \\:\\  \\ /:/  / \\:\\  \\ /:/  /\n"+
							   "  \\:\\  /:/  /   \\:\\  /:/  / \n"+
							   "   \\:\\/:/  /     \\:\\/:/  /  \n"+
							   "    \\::/  /       \\::/  /   \n"+
							   "     \\/__/         \\/__/    ");
				console.output("Celestial Console\nVersion: 0.1a");
			}
		}

		consoleVisible = true;
		allowConsoleToggle = true;
	}

	private void closeConsole() {
		String id = consolePopup != null ? consolePopup.getId() : null;
		if (id == null) {
			return;
		}
		nifty.closePopup(id, new EndNotify() {
			@Override
			public void perform() {
				consoleVisible = false;
				allowConsoleToggle = true;
			}
		});
	}

	@NiftyEventSubscriber(id = "console")
	public void onConsoleCommand(final String id, final ConsoleExecuteCommandEvent command) {
		de.lessvoid.nifty.controls.Console console = consolePopup.findNiftyControl("console", de.lessvoid.nifty.controls.Console.class);
		if (console != null) {
			if ("exit".equals(command.getCommand())) {
				this.parent.stop();
			} else {
				this.commandManager.onCommand(command);
			}
		}
		
	}

}
