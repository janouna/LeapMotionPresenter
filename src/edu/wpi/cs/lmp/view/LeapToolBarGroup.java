package edu.wpi.cs.lmp.view;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
		mainBar = new LeapToolBar(new String[] { "File", "Add", "Present", "ExitMenu" }, 0,
				true);
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
				"Video", "Pollice" }, 1, false);
		fileBar = new LeapToolBar(new String[] { "Open", "Save", "Save As",
				"Exit" }, 1, false);
		this.getChildren().add(mainBar);

		// Set main bar button controls
		mainBar.getButton("File").setOnMouseExited(
				new LeapToolBarSubMenuHandler(mainBar.getButton("File"), this,
						mainBar, fileBar));
		mainBar.getButton("Add").setOnMouseExited(
				new LeapToolBarSubMenuHandler(mainBar.getButton("Add"), this,
						mainBar, addBar));
		mainBar.getButton("ExitMenu").setOnMouseExited(new LeapToolBarExitMenuHandler(mainBar.getButton("ExitMenu"), this, mainBar));

	}

	public LeapToolBar getMenuAt(int level) {
		ObservableList<Node> children = this.getChildren();
		for (int i = 0; i < children.size(); i++) {
			LeapToolBar currentBar = (LeapToolBar) children.get(i);
			if (currentBar.getMenuLevel() == level) {
				return currentBar;
			}
		}

		return null;
	}

	public void removeMenuAt(int level) {
		if (level == 0) {
			mainBar.transitionOut();
			mainBar.unselectAllButton();
		} else {
			getMenuAt(level).unselectAllButton();
			this.getChildren().remove(getMenuAt(level));
		}
	}

	public void removeMenuAbove(int level) {
		ObservableList<Node> children = this.getChildren();
		List<LeapToolBar> toRemove = new ArrayList<LeapToolBar>();
		for (int i = 0; i < children.size(); i++) {
			LeapToolBar currentBar = (LeapToolBar) children.get(i);
			if (currentBar.getMenuLevel() > level) {
				toRemove.add(currentBar);
				currentBar.unselectAllButton();
			}
		}
		this.getChildren().removeAll(toRemove);
	}

}
