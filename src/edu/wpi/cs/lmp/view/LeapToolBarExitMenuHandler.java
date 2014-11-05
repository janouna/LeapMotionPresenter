package edu.wpi.cs.lmp.view;

import javafx.scene.control.ToggleButton;

public class LeapToolBarExitMenuHandler extends LeapToolBarSelectedHandler {
	
	private LeapToolBarGroup container;

	public LeapToolBarExitMenuHandler(ToggleButton mousedNode,
			LeapToolBarGroup container, LeapToolBar mousedBar) {
		super(mousedNode, mousedBar);
		this.container = container;
	}

	@Override
	public void action() {
		// Remove higher level sub menus
		container.removeMenuAbove(mousedBar.getMenuLevel());
		// Instantiate the desired submenu
		container.removeMenuAt(mousedBar.getMenuLevel());
	}
}
