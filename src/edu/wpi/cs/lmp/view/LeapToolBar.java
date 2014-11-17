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

public class LeapToolBar extends HBox {
	
	// Size of the button based on percentage of the screen height
	private static final double BUTTON_HEIGHT = 0.15;
	private static final double BUTTON_WIDTH = 0.30;
	// Number of seconds for slide in/out animations for the toolbar
	private static final int ANIMATION_TIME = 1;

	private LeapToolBar instance;
	private HBox buttonBar;
	private TranslateTransition animationIn;
	private TranslateTransition animationOut;
	private boolean isAnimating;
	private boolean isHidden;
	private int menuLevel;

	public LeapToolBar(String[] menuNames, int menuLevel, boolean isHidden) {
		super();

		this.menuLevel = menuLevel;
		this.getStyleClass().add("leap-tool-bar");
		this.isHidden = isHidden;
		
		this.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
		
		if (this.isHidden) {
			this.setOpacity(0);
		}

		instance = this;

//		animationIn = TranslateTransitionBuilder.create()
//				.duration(new Duration(ANIMATION_TIME * 1000)).node(this).fromY(-2).toY(0)
//				.autoReverse(true).interpolator(Interpolator.EASE_OUT).build();
//
//		animationIn.onFinishedProperty().set(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent arg0) {
//				isAnimating = false;
//				instance.isHidden = false;
//			}
//		});
//
//		animationOut = TranslateTransitionBuilder.create()
//				.duration(new Duration(ANIMATION_TIME * 1000)).node(this).fromY(0).toY(-100)
//				.autoReverse(true).interpolator(Interpolator.EASE_OUT).build();
//
//		animationOut.onFinishedProperty().set(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent arg0) {
//				isAnimating = false;
//				instance.isHidden = true;
//				instance.setOpacity(0);
//			}
//		});
		
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
			}
		});

		buttonBar = new HBox();
		buttonBar.getStyleClass().setAll("segmented-button-bar");

		for (int i = 0; i < menuNames.length; i++) {
			final ToggleButton newButton = new ToggleButton(menuNames[i]);
			newButton.setPrefHeight(Screen.getPrimary().getBounds().getHeight()*BUTTON_HEIGHT);
			newButton.setPrefWidth(Screen.getPrimary().getBounds().getHeight()*BUTTON_WIDTH);
			newButton.setOnMouseMoved(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					double percentSelected = (event.getY() * 100)
							/ (newButton.getLayoutY() + newButton.getHeight());
					newButton.setStyle("-selected-percent: "
							+ "linear-gradient(to bottom, -light-orange "
							+ (percentSelected) + "%, -dark-black "
							+ (percentSelected) + "%);");
				}
			});
			/*
			if (i == 0) {
				newButton.getStyleClass().addAll("first");
			} else if (i == menuNames.length - 1) {
				newButton.getStyleClass().addAll("last", "capsule");
			}
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

	}

	public void transitionIn() {
		if (!isAnimating) {
			instance.setOpacity(1);
			animationIn.play();
			isAnimating = true;
		}
	}

	public void transitionOut() {
		if (!isAnimating) {
			animationOut.play();
			isAnimating = true;
		}
	}

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

	public List<ToggleButton> getAllButton() {
		List<ToggleButton> buttons = new ArrayList<ToggleButton>();
		for (int i = 0; i < buttonBar.getChildren().size(); i++) {
			buttons.add((ToggleButton) buttonBar.getChildren().get(i));
		}
		return buttons;
	}
	
	public void unselectAllButton() {
		for (int i = 0; i < buttonBar.getChildren().size(); i++) {
			ToggleButton thisButton = (ToggleButton) buttonBar.getChildren()
					.get(i);
			thisButton.setSelected(false);
		}

	}

	public boolean isHidden() {
		return isHidden;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

}
