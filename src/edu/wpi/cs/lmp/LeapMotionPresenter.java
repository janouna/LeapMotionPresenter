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
package edu.wpi.cs.lmp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.controlsfx.control.Notifications;

import com.leapmotion.leap.Controller;

import edu.wpi.cs.lmp.leap.HandState;
import edu.wpi.cs.lmp.leap.HandStateController;
import edu.wpi.cs.lmp.leap.HandStateObservable;
import edu.wpi.cs.lmp.leap.MouseController;
import edu.wpi.cs.lmp.scenes.LeapScene;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;
import edu.wpi.cs.lmp.state.PresenterState;
import edu.wpi.cs.lmp.state.PresenterStateObservable;
import edu.wpi.cs.lmp.view.LeapSceneBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapMotionPresenter extends Application {

	private Controller c;
	private MouseController mouseController;
	private HandStateController handController;
	private LeapScene leapScene;

	private LeapMotionPresenter instance;
	private Pane root;
	private LeapToolBarGroup topBar;
	private LeapSceneBar bottomBar;
	private Scene scene;

	@Override
	public void start(Stage stage) {
		// Background setting
		instance = this;
		root = new Pane();
		root.setId("background");
		LeapSceneManager.getInstance().addRoot(root);

		// Binding leap controls to the mouse
		c = new Controller();
		mouseController = new MouseController(Screen.getPrimary().getBounds().getWidth(),
				Screen.getPrimary().getBounds().getHeight());
		handController = new HandStateController();
		c.addListener(mouseController);
		c.addListener(handController);

		leapScene = LeapSceneManager.getInstance().getCurrentScene();
		root.getChildren().add(leapScene);

		// Leap UI toolbar
		topBar = new LeapToolBarGroup();
		root.getChildren().add(topBar);

		// Leap UI slides bar
		bottomBar = new LeapSceneBar();
		root.getChildren().add(bottomBar);

		// Scene building
		scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(),
				Screen.getPrimary().getBounds().getHeight());
		
		// Setting cursor
		scene.setCursor(new ImageCursor(new Image("file:icons/hand_cursor_open.png")));
		
		// Hand cursor placement, this should be its own object that chnages
		// icon based on state (Open palm, closed palm, finger pointed, etc)
		changeHandObserver();

		stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean oldVal, Boolean newVal) {
				if (!newVal) {
					c.removeListener(handController);
					c.removeListener(mouseController);
				} else {
					c.addListener(handController);
					c.addListener(mouseController);
				}
			}
		});
		
		/*
		final String content_Url = "<iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/C0DPdy98e4c\" frameborder=\"0\" allowfullscreen></iframe>";

		final WebView webView = new WebView();
		final WebEngine webEngine = webView.getEngine();
		webEngine.loadContent(content_Url);
		webView.setPrefHeight(315);
		webView.setPrefWidth(560);

		// root.getChildren().add(webView);
*/
	     
	    changeSceneObserver();
	    changePresentObserver();
	    
	    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE && PresenterStateObservable.getInstance().get() == PresenterState.PRESENTING) {
					PresenterStateObservable.getInstance().set(PresenterState.CREATING);
				}
			}
		});
		scene.getStylesheets().add("file:stylesheet.css");
		stage.setTitle("Leap Motion Presenter");
		stage.setScene(scene);
		stage.setFullScreen(true);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.setResizable(false);
		stage.show();

	}
	
	private void changeSceneObserver() {
		LeapSceneManager.getInstance().getCurrentSceneProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				System.out.println("SLIDE CHANGING");
				// Get information of current slide
				LeapScene currentScene = instance.getSlide();
				final int position = instance.getRoot().getChildren().indexOf(currentScene);

				// Remove, replace, and readd the current slide with the new slide
				instance.getRoot().getChildren().remove(position);
				currentScene = LeapSceneManager.getInstance().getCurrentScene();
				instance.setSlide(currentScene);
				instance.getRoot().getChildren().add(position, currentScene);
			}

		});
	}
	
	private void changeHandObserver(){
		HandStateObservable.getInstance().getHandState().addListener(new ChangeListener<HandState>() {
			@Override
			public void changed(ObservableValue<? extends HandState> observable,
					HandState oldValue, final HandState newValue) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						switch(newValue) {
						case OPEN:
							scene.setCursor(new ImageCursor(new Image("file:icons/hand_cursor_open.png")));
							break;
						case CLOSED:
							scene.setCursor(new ImageCursor(new Image("file:icons/hand_cursor_closed.png")));
							break;
						case GONE:
							scene.setCursor(Cursor.NONE);
							break;
						case POINTING:
							scene.setCursor(new ImageCursor(new Image("file:icons/hand_cursor_point1.png")));
							break;
						default:
							break;
						}
					}
				});
			}
		});
	}
	
	private void changePresentObserver() {
		PresenterStateObservable.getInstance().getPresenterState().addListener(new ChangeListener<PresenterState>() {

			@Override
			public void changed(ObservableValue<? extends PresenterState> observable,
					PresenterState oldValue, PresenterState newValue) {
				switch(newValue) {
				case PRESENTING:
					Notifications.create()
						.title("PRESENTING")
						.position(Pos.CENTER)
						.hideCloseButton()
						.hideAfter(Duration.seconds(2))
						.text("You are now presenting!")
						.showInformation();
					break;
				case CREATING:
					Notifications.create()
					.title("EXITING PRESENTATION")
					.position(Pos.CENTER)
					.hideCloseButton()
					.hideAfter(Duration.seconds(2))
					.text("You finished your presentation!")
					.showInformation();
					break;
				default:
					break;
				}
			}
			
		});
	}

	public LeapScene getSlide() {
		return leapScene;
	}

	public void setSlide(LeapScene scene) {
		this.leapScene = scene;
	}

	public Pane getRoot() {
		return root;
	}

	public void setRoot(Pane root) {
		this.root = root;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
