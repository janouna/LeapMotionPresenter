package edu.wpi.cs.lmp.view.controller;

import java.io.File;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import edu.wpi.cs.lmp.file.FileSaver;
import edu.wpi.cs.lmp.view.LeapToolBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapToolBarSaveProject extends LeapToolBarSelectedHandler {

	private final LeapToolBarGroup container;

	public LeapToolBarSaveProject(ToggleButton mousedButton,
			LeapToolBar mousedBar, LeapToolBarGroup container) {
		super(mousedButton, mousedBar);
		setSelection = false;
		this.container = container;
	}

	@Override
	public void action(MouseEvent event) {
		// Choose file directory
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Project As...");
		final FileChooser.ExtensionFilter extLMP = new FileChooser.ExtensionFilter(
				"LeapMotion Presentation (*.lmp)", "*.lmp", "*.LMP");
		fileChooser.getExtensionFilters().add(extLMP);
		final File file = fileChooser.showSaveDialog(container.getScene()
				.getWindow());
		// Save it
		if (file != null) {
			FileSaver.savePresentation(file);
		}
		container.removeMenuAll();
	}

}
