package edu.wpi.cs.lmp.view;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class LeapToolBar extends ToolBar {

	private LeapToolBar instance;
	private HBox buttonBar;
	private TranslateTransition animationIn;
	private TranslateTransition animationOut;
	private boolean isAnimating;
	private boolean isHidden;
	private int menuLevel;

	public LeapToolBar(String[] menuNames, int menuLevel) {
		super();
		
		this.menuLevel = menuLevel;

		instance = this;

		animationIn = TranslateTransitionBuilder.create()
				.duration(new Duration(1 * 1000)).node(this).fromY(-2).toY(0)
				.autoReverse(true).interpolator(Interpolator.EASE_OUT).build();
		
		animationIn.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				isAnimating = false;
				isHidden = false;
			}
		});

		animationOut = TranslateTransitionBuilder.create()
				.duration(new Duration(1 * 1000)).node(this).fromY(0).toY(-100)
				.autoReverse(true).interpolator(Interpolator.EASE_OUT).build();
		
		animationOut.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				isAnimating = false;
				isHidden = true;
				instance.setOpacity(0);
			}
		});

		buttonBar = new HBox();
		buttonBar.getStyleClass().setAll("segmented-button-bar");

		for (int i = 0; i < menuNames.length; i++) {
			ToggleButton newButton = new ToggleButton(menuNames[i]);
			if (i == 0) {
				newButton.getStyleClass().addAll("first");
			} else if (i == menuNames.length - 1) {
				newButton.getStyleClass().addAll("last", "capsule");
			}
			buttonBar.getChildren().addAll(newButton);
		}
		this.getItems().addAll(buttonBar);

		// Set items to be centered and animation elements
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				animationIn.setFromY(-80);
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
			ToggleButton thisButton = (ToggleButton) buttonBar.getChildren().get(i);
			if (thisButton.getText() == name) {
				return thisButton;
			}
		}

		return null;
	}
	
	public List<ToggleButton> getAllButton() {
		List<ToggleButton> buttons = new ArrayList<ToggleButton>();
		for (int i=0; i < buttonBar.getChildren().size(); i++) {
			buttons.add((ToggleButton) buttonBar.getChildren().get(i));
		}
		return buttons;
	}
	
	public boolean isHidden() {
		return isHidden;
	}
	
	public int getMenuLevel() {
		return menuLevel;
	}

}
