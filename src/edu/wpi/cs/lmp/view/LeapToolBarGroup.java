package edu.wpi.cs.lmp.view;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.lmp.objects.ObjectType;
import edu.wpi.cs.lmp.view.controller.LeapToolBarExitMenuHandler;
import edu.wpi.cs.lmp.view.controller.LeapToolBarExitProgramHandler;
import edu.wpi.cs.lmp.view.controller.LeapToolBarObjectCreator;
import edu.wpi.cs.lmp.view.controller.LeapToolBarSlideCreator;
import edu.wpi.cs.lmp.view.controller.LeapToolBarSubMenuHandler;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class LeapToolBarGroup extends VBox {

	private final LeapToolBarGroup instance;

	private final LeapToolBar mainBar;

	private final LeapToolBar fileBar;
	private final LeapToolBar addBar;
	public LeapToolBarGroup() {
		super();
		instance = this;

		// Instantiate main bar and behavior
		mainBar = new LeapToolBar(new String[] { "File", "Add", "Present", "ExitMenu" }, 0,
				true);
		mainBar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (mainBar.isHidden()) {
					mainBar.transitionIn();
				} else {
					instance.removeMenuAbove(mainBar.getMenuLevel());
					mainBar.unselectAllButton();
				}
			}
		});

		// Instantiate other bars
		addBar = new LeapToolBar(new String[] { "Slide", "Text", "Image",
				"Video" }, 1, false);
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
		
		// Set add bar button controls
		addBar.getButton("Image").setOnMouseExited(new LeapToolBarObjectCreator(addBar.getButton("Image"), addBar, this, ObjectType.IMAGE, true));
		addBar.getButton("Video").setOnMouseExited(new LeapToolBarObjectCreator(addBar.getButton("Video"), addBar, this, ObjectType.VIDEO, true));
		addBar.getButton("Text").setOnMouseExited(new LeapToolBarObjectCreator(addBar.getButton("Text"), addBar, this, ObjectType.TEXT, false));
		addBar.getButton("Slide").setOnMouseExited(new LeapToolBarSlideCreator(addBar.getButton("Slide"), addBar, this));

		fileBar.getButton("Exit").setOnMouseExited(new LeapToolBarExitProgramHandler(fileBar.getButton("Exit"), fileBar));
	}

	public LeapToolBar getMenuAt(int level) {
		final ObservableList<Node> children = this.getChildren();
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
		final ObservableList<Node> children = this.getChildren();
		final List<LeapToolBar> toRemove = new ArrayList<LeapToolBar>();
		for (int i = 0; i < children.size(); i++) {
			LeapToolBar currentBar = (LeapToolBar) children.get(i);
			if (currentBar.getMenuLevel() > level) {
				toRemove.add(currentBar);
				currentBar.unselectAllButton();
			}
		}
		this.getChildren().removeAll(toRemove);
	}
	
	public void removeMenuAll() {
		this.removeMenuAbove(0);
		this.removeMenuAt(0);
	}
}
