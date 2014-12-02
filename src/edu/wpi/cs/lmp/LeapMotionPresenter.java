package edu.wpi.cs.lmp;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import com.leapmotion.leap.Controller;

import edu.wpi.cs.lmp.leap.HandStateController;
import edu.wpi.cs.lmp.leap.MouseController;
import edu.wpi.cs.lmp.scenes.LeapScene;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;
import edu.wpi.cs.lmp.view.LeapSceneBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;

public class LeapMotionPresenter extends Application {

	private Controller c;
	private MouseController mouseController;
	private HandStateController handController;
	private LeapScene leapScene;

	private LeapMotionPresenter instance;
	private Pane root;
	

	@Override
	public void start(Stage stage) {
		// Background setting
		instance = this;
		root = new Pane();
		root.setId("background");

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
		final LeapToolBarGroup topBar = new LeapToolBarGroup();
		root.getChildren().add(topBar);
		
		// Leap UI slides bar
		final LeapSceneBar bottomBar = new LeapSceneBar();
		root.getChildren().add(bottomBar);

		// Scene building
		final Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(),
				Screen.getPrimary().getBounds().getHeight());

		// Hand cursor placement, this should be its own object that chnages
		// icon based on state (Open palm, closed palm, finger pointed, etc)
		final javafx.scene.image.Image handCursor = new javafx.scene.image.Image("file:hand_cursor.png");
		scene.setCursor(new ImageCursor(handCursor));
		
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
