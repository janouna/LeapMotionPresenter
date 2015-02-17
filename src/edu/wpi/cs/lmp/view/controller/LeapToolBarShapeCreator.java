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
import edu.wpi.cs.lmp.objects.IObject;
import edu.wpi.cs.lmp.objects.shapes.ShapeFactory;
import edu.wpi.cs.lmp.objects.shapes.ShapeType;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarShapeCreator extends LeapToolBarSelectedHandler {

	private final LeapToolBarGroup container;
	private final ShapeType shape;

	public LeapToolBarShapeCreator(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container, ShapeType shape) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.container = container;
		this.shape = shape;
	}

	@Override
	public void action(MouseEvent event) {
		final IObject newShape = ShapeFactory.getInstance().createShape(shape);
		newShape.setX(event.getScreenX());
		newShape.setY(event.getScreenY());
		LeapSceneManager.getInstance().getCurrentScene().addObject(newShape);
		System.out.println(newShape);
		container.removeMenuAll();
	}

}
