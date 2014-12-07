package edu.wpi.cs.lmp.view.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.file.FileSaver;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarSaveProject extends LeapToolBarSelectedHandler{
	
	private final LeapToolBarGroup container;

	public LeapToolBarSaveProject(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.container = container;
	}

	@Override
	public void action(MouseEvent event) {
		FileSaver.savePresentation();
		container.removeMenuAll();
	}

}
