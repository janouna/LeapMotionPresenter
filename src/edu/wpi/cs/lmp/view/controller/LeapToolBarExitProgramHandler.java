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

public class LeapToolBarExitProgramHandler extends LeapToolBarSelectedHandler {

	public LeapToolBarExitProgramHandler(ToggleButton mousedButton, LeapToolBar mousedBar) {
		super(mousedButton, mousedBar);
	}

	@Override
	public void action(MouseEvent event) {
		System.exit(0);
	}
}
