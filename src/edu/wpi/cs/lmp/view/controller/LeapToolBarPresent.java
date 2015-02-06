package edu.wpi.cs.lmp.view.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.state.PresenterState;
import edu.wpi.cs.lmp.state.PresenterStateObservable;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarPresent extends LeapToolBarSelectedHandler {

	private final LeapToolBarGroup container;

	public LeapToolBarPresent(ToggleButton mousedButton,
			LeapToolBarGroup container, LeapToolBar mousedBar) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.container = container;
	}

	@Override
	public void action(MouseEvent event) {
		PresenterStateObservable.getInstance().set(PresenterState.PRESENTING);
		// Remove higher level sub menus
		container.removeMenuAbove(mousedBar.getMenuLevel());
		// Instantiate the desired submenu
		container.removeMenuAt(mousedBar.getMenuLevel());
	}

}
