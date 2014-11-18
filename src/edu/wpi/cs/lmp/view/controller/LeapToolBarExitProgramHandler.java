package edu.wpi.cs.lmp.view.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.view.LeapToolBar;

public class LeapToolBarExitProgramHandler extends LeapToolBarSelectedHandler {

	public LeapToolBarExitProgramHandler(ToggleButton mousedButton, LeapToolBar mousedBar) {
		super(mousedButton, mousedBar);
	}

	@Override
	public void action(MouseEvent event) {
		System.exit(0);
	}
}
