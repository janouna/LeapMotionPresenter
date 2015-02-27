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

import java.io.File;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import edu.wpi.cs.lmp.file.FileSaver;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

/**
 * The handler for the main toolbar save button.
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class LeapToolBarSaveProject extends LeapToolBarSelectedHandler {

	private final LeapToolBarGroup container;

	public LeapToolBarSaveProject(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.container = container;
	}

	@Override
	public void action(MouseEvent event) {
		// Choose file directory
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Project As...");
		final FileChooser.ExtensionFilter extLMP = new FileChooser.ExtensionFilter(
				"LeapMotion Presentation (*.lmp)", "*.lmp", "*.LMP");
		fileChooser.getExtensionFilters().add(extLMP);
		final File file = fileChooser.showSaveDialog(container.getScene()
				.getWindow());
		// Save it
		if (file != null) {
			FileSaver.savePresentation(file);
		}
		container.removeMenuAll();
	}

}
