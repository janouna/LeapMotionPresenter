package edu.wpi.cs.lmp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import com.leapmotion.leap.Controller;

import edu.wpi.cs.lmp.leap.HandState;
import edu.wpi.cs.lmp.leap.HandStateController;
import edu.wpi.cs.lmp.leap.HandStateObservable;
import edu.wpi.cs.lmp.leap.MouseController;
import edu.wpi.cs.lmp.slides.Slide;
import edu.wpi.cs.lmp.view.LeapSlideBar;
import edu.wpi.cs.lmp.view.LeapToolBarGroup;
import edu.wpi.cs.lmp.objects.Image;

public class LeapMotionPresenter extends Application {

	Controller c;
	MouseController mouseController;
	HandStateController handController;

	@Override
	public void start(Stage stage) {
		// Background setting
		StackPane root = new StackPane();
		root.setId("background");

		// Binding leap controls to the mouse
		c = new Controller();
		mouseController = new MouseController(Screen.getPrimary()
				.getVisualBounds().getWidth(), Screen.getPrimary()
				.getVisualBounds().getHeight());
		handController = new HandStateController();
		c.addListener(mouseController);
		c.addListener(handController);
		
		Slide slide = new Slide();
		StackPane.setAlignment(slide, Pos.TOP_LEFT);
		root.getChildren().add(slide);

		// Leap UI toolbar
		final LeapToolBarGroup topBar = new LeapToolBarGroup();
		root.getChildren().add(topBar);
		StackPane.setAlignment(topBar, Pos.TOP_CENTER);

		// Leap UI slides bar
		final LeapSlideBar bottomBar = new LeapSlideBar();
		root.getChildren().add(bottomBar);
		StackPane.setAlignment(bottomBar, Pos.BOTTOM_CENTER);

		// Scene building
		Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds()
				.getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

		// Hand cursor placement, this should be its own object that chnages
		// icon based on state (Open palm, closed palm, finger pointed, etc)
		javafx.scene.image.Image handCursor = new javafx.scene.image.Image("file:hand_cursor.png");
		scene.setCursor(new ImageCursor(handCursor));

		slide.addObject(new Image());
		Image otherImage = new Image();
		otherImage.setX(400);
		slide.addObject(otherImage);

		scene.getStylesheets().add("file:stylesheet.css");
		stage.setScene(scene);
		stage.setFullScreen(true);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.setResizable(false);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
