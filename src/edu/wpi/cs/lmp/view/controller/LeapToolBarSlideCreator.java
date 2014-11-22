package edu.wpi.cs.lmp.view.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarSlideCreator extends LeapToolBarSelectedHandler {
	
	private final LeapToolBarGroup container;

	public LeapToolBarSlideCreator(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container) {
		super(mousedButton, mousedBar);
		this.container = container;
		setSelection = false;
	}
	
	@Override
	public void action(MouseEvent event) {
		// Add slide
		LeapSceneManager.getInstance().addScene();
		container.removeMenuAll();
	}

}
