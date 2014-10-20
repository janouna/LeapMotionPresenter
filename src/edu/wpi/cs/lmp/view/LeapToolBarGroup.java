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

		// Instantiate main bar
		mainBar = new LeapToolBar(new String[] { "File", "Add", "Present" });
		addBar = new LeapToolBar(new String[] { "Slide", "Text", "Image",
				"Video" });
		this.getChildren().add(mainBar);
		// Set main bar button controls
		mainBar.getButton("File").setOnMouseEntered(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						instance.getChildren().remove(addBar);
					}
				});
		
		mainBar.getButton("File").setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.getY() > mainBar.getButton("File").getLayoutY() + mainBar.getButton("File").getHeight()) {
					instance.getChildren().add(addBar);
				}
			}
			
		});

		mainBar.getButton("Add").setOnMouseEntered(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						// Record the mouse enter of the add submenu
					}
				});

		mainBar.getButton("Present").setOnMouseEntered(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						// Record rthe mouse enter of the present submenu
					}
				});
	}

}
