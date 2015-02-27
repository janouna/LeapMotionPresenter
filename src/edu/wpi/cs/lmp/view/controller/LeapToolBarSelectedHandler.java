/*******************************************************************************
 * This file is part of James Anouna and Johnny Hernandez's MQP.
 * Leap Motion Presenter
 * Advised by Professor Gary Pollice
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * James Anouna
 * Johnny Hernandez
 *******************************************************************************/
package edu.wpi.cs.lmp.view.controller;

import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.view.LeapToolBar;

/**
 * Base class for the tool bar menu button handlers. Responsible for checking if
 * the menu item is selected by moving cursor downwards on the button for
 * selection.
 * 
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public abstract class LeapToolBarSelectedHandler implements
		EventHandler<MouseEvent> {

	protected ToggleButton mousedButton;
	protected LeapToolBar mousedBar;
	protected boolean setSelection = true;

	protected LeapToolBarSelectedHandler(ToggleButton mousedButton,
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

	/**
	 * This method will be called when a ToggleButton has been selected by
	 * moving downwards on the button (pulling down on a button). All actions
	 * that need to be performed on selection should be done in this method.
	 * 
	 * @param event
	 *            Carries the information of a MouseEvent when the button was
	 *            selected.
	 */
	public abstract void action(MouseEvent event);
}
