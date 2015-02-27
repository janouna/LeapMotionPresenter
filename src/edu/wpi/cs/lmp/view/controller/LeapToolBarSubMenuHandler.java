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

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

/**
 * Creates submenus for the main tool bar
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class LeapToolBarSubMenuHandler extends LeapToolBarSelectedHandler {

	private final LeapToolBarGroup container;
	private final LeapToolBar newMenu;

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
