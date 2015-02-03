package edu.wpi.cs.lmp.view.controller;

import edu.wpi.cs.lmp.objects.IObject;
import edu.wpi.cs.lmp.objects.shapes.ShapeFactory;
import edu.wpi.cs.lmp.objects.shapes.ShapeType;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

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
		container.removeMenuAll();
	}

}
