package edu.wpi.cs.lmp.view;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class LeapToolBarGroup extends VBox {

	private LeapToolBarGroup instance;

	private int activeToolBars;
	private LeapToolBar mainBar;

	private LeapToolBar fileBar;
	private LeapToolBar addBar;
	private LeapToolBar presentBar;

	public LeapToolBarGroup() {
		super();
		activeToolBars = 0;

		instance = this;

		// Instantiate main bar and behavior
		mainBar = new LeapToolBar(new String[] { "File", "Add", "Present" }, 0, false);
		mainBar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (mainBar.isHidden()) {
					mainBar.transitionIn();
				}
			}
		});

		// Instantiate other bars
		addBar = new LeapToolBar(new String[] { "Slide", "Text", "Image",
				"Video" }, 1, false);
		fileBar = new LeapToolBar(new String[] {"Open", "Save", "Save As", "Exit"}, 1, false);
		this.getChildren().add(mainBar);
		
		// Set main bar button controls
		mainBar.getButton("File").setOnMouseExited(
				new LeapToolBarSubMenuHandler(mainBar.getButton("File"),
						this, mainBar, fileBar));
		mainBar.getButton("Add").setOnMouseExited(
				new LeapToolBarSubMenuHandler(mainBar.getButton("Add"),
						this, mainBar, addBar));
		
	}

}
