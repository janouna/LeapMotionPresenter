package edu.wpi.cs.lmp.view.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarExitMenuHandler extends LeapToolBarSelectedHandler {
	
	private final LeapToolBarGroup container;

	public LeapToolBarExitMenuHandler(ToggleButton mousedNode,
			LeapToolBarGroup container, LeapToolBar mousedBar) {
		super(mousedNode, mousedBar);
		setSelection = false;
		this.container = container;
	}

	@Override
	public void action(MouseEvent event) {
		// Remove higher level sub menus
		container.removeMenuAbove(mousedBar.getMenuLevel());
		// Instantiate the desired submenu
		container.removeMenuAt(mousedBar.getMenuLevel());
	}
}
