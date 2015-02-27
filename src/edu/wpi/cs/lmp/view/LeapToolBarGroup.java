/*******************************************************************************
* This file is part of James Anouna and Johnny Hernandez's MQP.
* Leap Motion Presenter
* Advised by Professor Gary Pollice
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* James Anouna
* Johnny Hernandez
*******************************************************************************/
package edu.wpi.cs.lmp.view;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import edu.wpi.cs.lmp.objects.ObjectType;
import edu.wpi.cs.lmp.objects.shapes.ShapeType;
import edu.wpi.cs.lmp.state.PresenterState;
import edu.wpi.cs.lmp.state.PresenterStateObservable;
import edu.wpi.cs.lmp.view.controller.LeapToolBarExitMenuHandler;
import edu.wpi.cs.lmp.view.controller.LeapToolBarExitProgramHandler;
import edu.wpi.cs.lmp.view.controller.LeapToolBarObjectCreator;
import edu.wpi.cs.lmp.view.controller.LeapToolBarOpenProject;
import edu.wpi.cs.lmp.view.controller.LeapToolBarPresent;
import edu.wpi.cs.lmp.view.controller.LeapToolBarSaveProject;
import edu.wpi.cs.lmp.view.controller.LeapToolBarShapeCreator;
import edu.wpi.cs.lmp.view.controller.LeapToolBarSlideCreator;
import edu.wpi.cs.lmp.view.controller.LeapToolBarSubMenuHandler;

/**
 * Contains the multiple toolbar rows for the main tool bar
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class LeapToolBarGroup extends VBox {

	private final LeapToolBarGroup instance;

	private final LeapToolBar mainBar;

	private final LeapToolBar fileBar;
	private final LeapToolBar addBar;
	private final LeapToolBar shapesBar;

	private boolean isActive;

	public LeapToolBarGroup() {
		super();
		instance = this;

		isActive = true;

		// Instantiate main bar and behavior
		mainBar = new LeapToolBar(new String[] { "File", "Add", "Present",
				"ExitMenu" }, 0, true);
		mainBar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (isActive) {
					if (mainBar.isHidden()) {
						mainBar.transitionIn();
					} else {
						instance.removeMenuAbove(mainBar.getMenuLevel());
						mainBar.unselectAllButton();
					}
				}
			}
		});

		// Instantiate other bars
		addBar = new LeapToolBar(new String[] { "Slide", "Text", "Image",
				"Video", "Shapes" }, 1, false);
		addBar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				instance.removeMenuAbove(addBar.getMenuLevel());
			}
		});
		shapesBar = new LeapToolBar(new String[] { "Line", "Arrow", "Circle",
				"Square" }, 2, false);
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
		mainBar.getButton("Present").setOnMouseExited(
				new LeapToolBarPresent(mainBar.getButton("Present"), this,
						mainBar));
		mainBar.getButton("ExitMenu").setOnMouseExited(
				new LeapToolBarExitMenuHandler(mainBar.getButton("ExitMenu"),
						this, mainBar));

		// Set add bar button controls
		addBar.getButton("Image").setOnMouseExited(
				new LeapToolBarObjectCreator(addBar.getButton("Image"), addBar,
						this, ObjectType.IMAGE, true));
		addBar.getButton("Video").setOnMouseExited(
				new LeapToolBarObjectCreator(addBar.getButton("Video"), addBar,
						this, ObjectType.VIDEO, true));
		addBar.getButton("Text").setOnMouseExited(
				new LeapToolBarObjectCreator(addBar.getButton("Text"), addBar,
						this, ObjectType.TEXT, false));
		addBar.getButton("Slide").setOnMouseExited(
				new LeapToolBarSlideCreator(addBar.getButton("Slide"), addBar,
						this));
		addBar.getButton("Shapes").setOnMouseExited(
				new LeapToolBarSubMenuHandler(addBar.getButton("Shapes"), this,
						addBar, shapesBar));

		fileBar.getButton("Open").setOnMouseExited(
				new LeapToolBarOpenProject(fileBar.getButton("Open"), fileBar,
						this));
		fileBar.getButton("Save As").setOnMouseExited(
				new LeapToolBarSaveProject(fileBar.getButton("Save As"),
						fileBar, this));
		fileBar.getButton("Exit").setOnMouseExited(
				new LeapToolBarExitProgramHandler(fileBar.getButton("Exit"),
						fileBar));
		
		shapesBar.getButton("Line").setOnMouseExited(
				new LeapToolBarShapeCreator(shapesBar.getButton("Line"),
						shapesBar, this, ShapeType.LINE));
		shapesBar.getButton("Arrow").setOnMouseExited(
				new LeapToolBarShapeCreator(shapesBar.getButton("Arrow"),
						shapesBar, this, ShapeType.ARROW));
		shapesBar.getButton("Circle").setOnMouseExited(
				new LeapToolBarShapeCreator(shapesBar.getButton("Circle"),
						shapesBar, this, ShapeType.CIRCLE));
		shapesBar.getButton("Square").setOnMouseExited(
				new LeapToolBarShapeCreator(shapesBar.getButton("Square"),
						shapesBar, this, ShapeType.SQUARE));

		createObservers();
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

	private void createObservers() {
		PresenterStateObservable.getInstance().getPresenterState()
				.addListener(new ChangeListener<PresenterState>() {

					@Override
					public void changed(
							ObservableValue<? extends PresenterState> observable,
							PresenterState oldValue, PresenterState newValue) {
						if (newValue == PresenterState.PRESENTING) {
							instance.setDisabled(true);
							isActive = false;
						} else {
							instance.setDisabled(false);
							isActive = true;
						}
					}

				});
	}
}
