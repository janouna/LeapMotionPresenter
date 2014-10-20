package edu.wpi.cs.lmp.view;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class LeapToolBar extends ToolBar {
	
	private LeapToolBar instance;
	private HBox buttonBar;
	private TranslateTransition animationIn;
	private TranslateTransition animationOut;
	
	public LeapToolBar(String[] menuNames) {
		super();
		
		instance = this;
		
        animationIn = TranslateTransitionBuilder
                .create()
                .duration(new Duration(1 * 1000))
                .node(this)
                .fromY(-2)
                .toY(0)
                .autoReverse(true)
                .interpolator(Interpolator.EASE_OUT)
                .build();
        
        animationOut = TranslateTransitionBuilder
                .create()
                .duration(new Duration(1 * 1000))
                .node(this)
                .fromY(0)
                .toY(-100)
                .autoReverse(true)
                .interpolator(Interpolator.EASE_OUT)
                .build();
 
        buttonBar = new HBox();
        buttonBar.getStyleClass().setAll("segmented-button-bar");
        /*
        Button sampleButton = new Button("Tasks");
        sampleButton.getStyleClass().addAll("first");
        Button sampleButton2 = new Button("Administrator");
        Button sampleButton3 = new Button("Search");
        Button sampleButton4 = new Button("Line");
        Button sampleButton5 = new Button("Process");
        Button sampleButton4half = new Button("testing");
        sampleButton5.getStyleClass().addAll("last", "capsule");
        */
        for(int i=0; i < menuNames.length; i++) {
        	Button newButton = new Button(menuNames[i]);
        	if (i == 0) {
        		newButton.getStyleClass().addAll("first");
        	} else if (i == menuNames.length - 1) {
        		newButton.getStyleClass().addAll("last", "capsule");
        	}
        	buttonBar.getChildren().addAll(newButton);
        }
        // buttonBar.getChildren().addAll(sampleButton, sampleButton2, sampleButton3, sampleButton4, sampleButton4half, sampleButton5);
        this.getItems().addAll(buttonBar);
        
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
		animationIn.play();
	}
	
	public void transitionOut() {
		animationOut.play();
	}
	
	public Button getButton(String name) {
		for (int i=0; i < buttonBar.getChildren().size(); i++) {
			Button thisButton = (Button) buttonBar.getChildren().get(i);
			if (thisButton.getText() == name) {
				return thisButton;
			}
		}
		
		return null;
	}

}
