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
import edu.wpi.cs.lmp.file.FileOpener;
import edu.wpi.cs.lmp.fileChooser.FileChooserFactory;
import edu.wpi.cs.lmp.objects.ObjectType;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarOpenProject extends LeapToolBarSelectedHandler {

	private final LeapToolBarGroup container;

	public LeapToolBarOpenProject(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.container = container;
	}

	@Override
	public void action(MouseEvent event) {
		final File file = FileChooserFactory.getInstance()
				.makeFileChooser(ObjectType.PRESENTATION)
				.showOpenDialog(container.getScene().getWindow());
		if (file != null) {
			FileOpener.openPresentation(file);
		}
		container.removeMenuAll();
	}

}
