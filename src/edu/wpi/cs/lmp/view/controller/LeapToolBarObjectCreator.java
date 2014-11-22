package edu.wpi.cs.lmp.view.controller;

import java.io.File;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.FileChooser.FileChooserFactory;
import edu.wpi.cs.lmp.objects.IObject;
import edu.wpi.cs.lmp.objects.ObjectFactory;
import edu.wpi.cs.lmp.objects.ObjectType;
import edu.wpi.cs.lmp.slides.SlideManager;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarObjectCreator extends LeapToolBarSelectedHandler {
	
	private final ObjectType object;
	private final LeapToolBarGroup container;

	public LeapToolBarObjectCreator(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container, ObjectType object) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.object = object;
		this.container = container;
	}
	
	@Override
	public void action(MouseEvent event) {
		// Instantiate file chooser window and capture URI of file
		final File file = FileChooserFactory.getInstance().makeFileChooser(object).showOpenDialog(container.getScene().getWindow());
		System.out.println(file);

		// Object instantiation
		final IObject newObject = ObjectFactory.getInstance().createObject(object);
		newObject.setX(event.getScreenX());
		newObject.setY(event.getScreenY());
		SlideManager.getInstance().getCurrentSlide().addObject(newObject);
		container.removeMenuAll();
	}

}
