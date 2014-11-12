package edu.wpi.cs.lmp.view;

import edu.wpi.cs.lmp.objects.IObject;
import edu.wpi.cs.lmp.objects.ObjectFactory;
import edu.wpi.cs.lmp.objects.ObjectType;
import edu.wpi.cs.lmp.slides.SlideManager;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public class LeapToolBarObjectCreator extends LeapToolBarSelectedHandler {
	
	private ObjectType object;
	private LeapToolBarGroup container;

	public LeapToolBarObjectCreator(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container, ObjectType object) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.object = object;
		this.container = container;
	}
	
	@Override
	public void action(MouseEvent event) {
		IObject newObject = ObjectFactory.getInstance().CreateObject(object);
		newObject.setX(event.getScreenX());
		newObject.setY(event.getScreenY());
		SlideManager.getInstance().getCurrentSlide().addObject(newObject);
		container.removeMenuAll();
	}

}
