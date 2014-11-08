package edu.wpi.cs.lmp.view;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public class LeapToolBarSubMenuHandler extends LeapToolBarSelectedHandler {

	private LeapToolBarGroup container;
	private LeapToolBar newMenu;

	public LeapToolBarSubMenuHandler(ToggleButton mousedNode,
			LeapToolBarGroup container, LeapToolBar mousedBar, LeapToolBar newMenu) {
		super(mousedNode, mousedBar);
		this.container = container;
		this.newMenu = newMenu;
	}

	@Override
	public void action(MouseEvent event) {
		// Remove higher level sub menus
		container.removeMenuAbove(mousedBar.getMenuLevel());
		// Instantiate the desired submenu
		container.getChildren().add(newMenu);
	}

}
