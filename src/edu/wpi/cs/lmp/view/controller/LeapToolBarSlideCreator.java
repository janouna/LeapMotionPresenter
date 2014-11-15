package edu.wpi.cs.lmp.view.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.slides.SlideManager;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarSlideCreator extends LeapToolBarSelectedHandler {
	
	private LeapToolBarGroup container;

	public LeapToolBarSlideCreator(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container) {
		super(mousedButton, mousedBar);
		this.container = container;
	}
	
	@Override
	public void action(MouseEvent event) {
		// Add slide
		SlideManager.getInstance().addSlide();
		container.removeMenuAll();
	}

}
