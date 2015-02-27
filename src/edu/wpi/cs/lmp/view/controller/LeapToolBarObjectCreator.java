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
import edu.wpi.cs.lmp.fileChooser.FileChooserFactory;
import edu.wpi.cs.lmp.objects.IObject;
import edu.wpi.cs.lmp.objects.ObjectFactory;
import edu.wpi.cs.lmp.objects.ObjectType;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

/**
 * Communicates between the main tool bar and the rest of the Presentation application.
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class LeapToolBarObjectCreator extends LeapToolBarSelectedHandler {

	private final ObjectType object;
	private final LeapToolBarGroup container;
	private final boolean useFileChooser;

	public LeapToolBarObjectCreator(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container,
			ObjectType object, Boolean useFileChooser) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.object = object;
		this.container = container;
		this.useFileChooser = useFileChooser;
	}

	@Override
	public void action(MouseEvent event) {
		// Instantiate file chooser window and capture URI of file
		if (useFileChooser) {
			final File file = FileChooserFactory.getInstance()
					.makeFileChooser(object)
					.showOpenDialog(container.getScene().getWindow());

			// Object instantiation
			if (file != null) {
				final IObject newObject = ObjectFactory.getInstance()
						.createObject(object, file.toString());
				newObject.setX(event.getScreenX());
				newObject.setY(event.getScreenY());
				LeapSceneManager.getInstance().getCurrentScene()
						.addObject(newObject);
			}
		} else {
			final IObject newObject = ObjectFactory.getInstance()
					.createObject(object);
			newObject.setX(event.getScreenX());
			newObject.setY(event.getScreenY());
			LeapSceneManager.getInstance().getCurrentScene()
					.addObject(newObject);
		}
		container.removeMenuAll();
	}

}
