package edu.wpi.cs.lmp;

import RadialFX.MouseController;

import com.leapmotion.leap.Controller;

import edu.wpi.cs.lmp.view.LeapToolBarGroup;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LeapMotionPresenter extends Application {
	
	Controller c;
	MouseController mouseController;

	@Override
	public void start(Stage stage) throws Exception {	
        StackPane root = new StackPane();
        root.setId("background");
        
        c = new Controller();
        mouseController = new MouseController();
        c.addListener(mouseController);
        
        final LeapToolBarGroup topBar = new LeapToolBarGroup();
        
        root.getChildren().add(topBar);
        StackPane.setAlignment(topBar, Pos.TOP_CENTER);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("file:stylesheet.css");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
