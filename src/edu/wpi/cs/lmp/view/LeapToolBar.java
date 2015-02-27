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

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * Main class for a single tool bar.
 * 
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class LeapToolBar extends HBox {

	// Size of the button based on percentage of the screen height
	private static final double BUTTON_HEIGHT = 0.15;
	private static final double BUTTON_WIDTH = 0.30;
	// Number of seconds for slide in/out animations for the toolbar
	private static final float ANIMATION_TIME = 0.5f;

	private final LeapToolBar instance;
	private final HBox buttonBar;
	private final TranslateTransition animationIn;
	private final TranslateTransition animationOut;
	private boolean isAnimating;
	private boolean isHidden;
	private final int menuLevel;

	/**
	 * Creates a LeapToolBar with the supplied parameters
	 * 
	 * @param menuNames
	 *            Array of strings that will be used to create selectable menu
	 *            items
	 * @param menuLevel
	 *            Menu level in a hierarchy. 0 for the top most menu, 1 if
	 *            another menu calls for this menu, 2 if two other menus come
	 *            before it. This is useful when using LeapToolBars in a
	 *            LeapToolBarGroup
	 * @param isHidden
	 *            Boolean that dictates if this object should be
	 *            hidden/displayed on instantiation
	 */
	public LeapToolBar(String[] menuNames, int menuLevel, boolean isHidden) {
		super();

		this.menuLevel = menuLevel;
		this.getStyleClass().add("leap-tool-bar");
		this.isHidden = isHidden;

		this.setPrefWidth(Screen.getPrimary().getBounds().getWidth());

		instance = this;

		animationIn = new TranslateTransition();
		animationIn.setDuration(new Duration(ANIMATION_TIME * 1000));
		animationIn.setNode(this);
		animationIn.setToY(0);
		animationIn.setAutoReverse(true);
		animationIn.setInterpolator(Interpolator.EASE_OUT);
		animationIn.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				isAnimating = false;
				instance.isHidden = false;
				// instance.enableAllButton(true);
			}
		});

		animationOut = new TranslateTransition();
		animationOut.setDuration(new Duration(ANIMATION_TIME * 1000));
		animationOut.setNode(this);
		animationOut.setFromY(0);
		animationOut.setAutoReverse(true);
		animationOut.setInterpolator(Interpolator.EASE_OUT);
		animationOut.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				isAnimating = false;
				instance.isHidden = true;
				instance.setOpacity(0);
				// instance.enableAllButton(false);
			}
		});

		buttonBar = new HBox();
		buttonBar.getStyleClass().setAll("segmented-button-bar");

		for (int i = 0; i < menuNames.length; i++) {
			final ToggleButton newButton = new ToggleButton(menuNames[i]);
			newButton.setPrefHeight(Screen.getPrimary().getBounds().getHeight()
					* BUTTON_HEIGHT);
			newButton.setPrefWidth(Screen.getPrimary().getBounds().getHeight()
					* BUTTON_WIDTH);
			newButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					double percentSelected = (event.getY() * 100)
							/ (newButton.getLayoutY() + newButton.getHeight());
					newButton.setStyle("-selected-percent: "
							+ "linear-gradient(to bottom, -light-orange "
							+ (percentSelected) + "%, -dark-black "
							+ (percentSelected) + "%);");
					newButton.setSelected(false);
				}
			});
			/*
			 * if (i == 0) { newButton.getStyleClass().addAll("first"); } else
			 * if (i == menuNames.length - 1) {
			 * newButton.getStyleClass().addAll("last", "capsule"); }
			 */
			buttonBar.getChildren().addAll(newButton);
		}
		this.getChildren().add(buttonBar);

		// Set items to be centered and animation elements
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				animationIn.setFromY(-instance.getLayoutBounds().getHeight());
				animationOut.setToY(-instance.getLayoutBounds().getHeight());
			}
		});

		if (this.isHidden) {
			this.setOpacity(0);
			// this.enableAllButton(false);
		}

	}

	/**
	 * Causes this bar to transition in to view
	 */
	public void transitionIn() {
		if (!isAnimating) {
			instance.setOpacity(1);
			animationIn.play();
			isAnimating = true;
		}
	}

	/**
	 * Causes this bar to transition out of view
	 */
	public void transitionOut() {
		if (!isAnimating) {
			animationOut.play();
			isAnimating = true;
		}
	}

	/**
	 * Attempts to obtain the ToggleButton for a menu item based on its name.
	 * 
	 * @param name
	 *            String of the menu item to be obtained
	 * @return The ToggleButton object for that menu item. Null if it was not
	 *         found.
	 */
	public ToggleButton getButton(String name) {
		for (int i = 0; i < buttonBar.getChildren().size(); i++) {
			ToggleButton thisButton = (ToggleButton) buttonBar.getChildren()
					.get(i);
			if (thisButton.getText().equals(name)) {
				return thisButton;
			}
		}

		return null;
	}

	/**
	 * Obtains all of the menu items in the LeapToolBar
	 * 
	 * @return A List of ToggleButton objects for the menu items in this tool
	 *         bar.
	 */
	public List<ToggleButton> getAllButton() {
		final List<ToggleButton> buttons = new ArrayList<ToggleButton>();
		for (int i = 0; i < buttonBar.getChildren().size(); i++) {
			buttons.add((ToggleButton) buttonBar.getChildren().get(i));
		}
		return buttons;
	}

	/**
	 * Unselects all of the menu items that may be selected in this tool bar.
	 */
	public void unselectAllButton() {
		for (int i = 0; i < buttonBar.getChildren().size(); i++) {
			ToggleButton thisButton = (ToggleButton) buttonBar.getChildren()
					.get(i);
			thisButton.setSelected(false);
		}

	}

	/**
	 * Changes the isActive property of all menu items in this tool bar.
	 * 
	 * @param enable
	 *            True to enable all of the buttons, False to disable
	 */
	public void enableAllButton(boolean enable) {
		for (int i = 0; i < buttonBar.getChildren().size(); i++) {
			ToggleButton thisButton = (ToggleButton) buttonBar.getChildren()
					.get(i);
			thisButton.setDisable(!enable);
		}
	}

	/**
	 * Is this menu currently hidden
	 * 
	 * @return Boolean that answers the above
	 */
	public boolean isHidden() {
		return isHidden;
	}

	/**
	 * The menu level in the hierarchy when in a LeapToolBarGroup
	 * 
	 * @return An int representing its level
	 */
	public int getMenuLevel() {
		return menuLevel;
	}

}
