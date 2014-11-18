package edu.wpi.cs.lmp.view.controller;

import edu.wpi.cs.lmp.slides.SlideManager;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public class LeapToolBarExitMenuHandler extends LeapToolBarSelectedHandler {
	
	private LeapToolBarGroup container;

	public LeapToolBarExitMenuHandler(ToggleButton mousedNode,
			LeapToolBarGroup container, LeapToolBar mousedBar) {
		super(mousedNode, mousedBar);
		setSelection = false;
		this.container = container;
	}

	@Override
	public void action(MouseEvent event) {
		// Remove higher level sub menus
		container.removeMenuAbove(mousedBar.getMenuLevel());
		// Instantiate the desired submenu
		container.removeMenuAt(mousedBar.getMenuLevel());
		int currentSlide = SlideManager.getInstance().getCurrentSlideNumber();
		if (currentSlide == 0) {
			SlideManager.getInstance().setCurrentSlide(1);
		} else {
			SlideManager.getInstance().setCurrentSlide(0);
		}
	}
}
