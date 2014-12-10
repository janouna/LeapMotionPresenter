package edu.wpi.cs.lmp.view.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import edu.wpi.cs.lmp.file.FileOpener;
import edu.wpi.cs.lmp.file.FileSaver;
import edu.wpi.cs.lmp.objects.Image;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarOpenProject extends LeapToolBarSelectedHandler {

	private final LeapToolBarGroup container;
	
	public LeapToolBarOpenProject(ToggleButton mousedButton, LeapToolBar mousedBar, LeapToolBarGroup container) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.container = container;
	}

	@Override
	public void action(MouseEvent event) {
		FileOpener.openPresentation();
		container.removeMenuAll();
	}

}
