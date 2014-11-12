package edu.wpi.cs.lmp.view;

import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public abstract class LeapToolBarSelectedHandler implements
		EventHandler<MouseEvent> {

	protected ToggleButton mousedButton;
	protected LeapToolBar mousedBar;
	protected boolean setSelection = true;

	public LeapToolBarSelectedHandler(ToggleButton mousedButton,
			LeapToolBar mousedBar) {
		this.mousedButton = mousedButton;
		this.mousedBar = mousedBar;
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (event.getY() > mousedButton.getLayoutY()
					+ mousedButton.getHeight()) {
				if (!mousedBar.isHidden()) {
					action(event);
					mousedBar.unselectAllButton();
					mousedButton.setSelected(setSelection);
				}
			}
		}
	}

	public void action(MouseEvent event) {
		// Carry this action out
	}
}
