package edu.wpi.cs.lmp.view;

import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public abstract class LeapToolBarSelectedHandler implements
		EventHandler<MouseEvent> {
	
	ToggleButton mousedButton;
	LeapToolBar mousedBar;
	
	public LeapToolBarSelectedHandler(ToggleButton mousedButton, LeapToolBar mousedBar) {
		this.mousedButton = mousedButton;
		this.mousedBar = mousedBar;
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (event.getY() > mousedButton.getLayoutY() + mousedButton.getHeight()) {
				action();
				mousedBar.unselectAllButton();
				mousedButton.setSelected(true);
			}
		}
	}
	
	public void action() {
		// Carry this action out
	}
}
