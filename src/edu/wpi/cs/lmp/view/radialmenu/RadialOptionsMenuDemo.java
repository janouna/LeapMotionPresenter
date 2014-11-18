/**
 * Copyright 2013 (C) Mr LoNee - (Laurent NICOLAS) - www.mrlonee.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package edu.wpi.cs.lmp.view.radialmenu;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import com.leapmotion.leap.Controller;
// import com.sun.glass.ui.Cursor;



import edu.wpi.cs.lmp.leap.MouseController;

public class RadialOptionsMenuDemo extends Application {
	
	Controller c;
	MouseController mouseController;

	public static void main(final String[] args) {
		launch(args);
	}

	private Group container;
	private RadialOptionsMenu radialMenu;

	@Override
	public void start(final Stage primaryStage) {
		container = new Group();
		final Scene scene = new Scene(container);
		scene.setFill(Color.LIGHTBLUE);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setWidth(1920);
		primaryStage.setHeight(1080);
		primaryStage.centerOnScreen();
		primaryStage.setTitle("Radial Options Menu Demo");
		primaryStage.show();

		// 60
		final double itemInnerRadius = 100;
		// 95
		final double itemRadius = 180;
		final double centerClosedRadius = 28;
		final double centerOpenedRadius = 40;

		final String[] menus = new String[] { "DOWLOADS", "SYNOPSIS", "VIDEO",
				"PHOTO", "GAME", "CAST & CREW" };

		radialMenu = new RadialOptionsMenu(this, menus, itemInnerRadius, itemRadius,
				centerClosedRadius, centerOpenedRadius);

		radialMenu.setTranslateX(1920/2);
		radialMenu.setTranslateY(1080/2);
		container.getChildren().addAll(radialMenu);
		// Mouse controlling
		
		c = new Controller();
		mouseController = new MouseController(Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
		c.addListener(mouseController);
		
		Image handCursor = new Image("file:hand_cursor.png");
		
		scene.setCursor(new ImageCursor(handCursor));

		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(final MouseEvent event) {
				if (event.isSecondaryButtonDown()) {
					radialMenu.setTranslateX(event.getX());
					radialMenu.setTranslateY(event.getY());
				}
			}
		});

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(final KeyEvent event) {
				System.out.println(event);
				if (event.getCode() == KeyCode.F5) {
					RadialOptionsMenuDemo.this.takeSnapshot(scene);
				}
			}
		});

	}

	int snapshotCounter = 0;

	private void takeSnapshot(final Scene scene) {
		// Take snapshot of the scene
		final WritableImage writableImage = scene.snapshot(null);

		// Write snapshot to file system as a .png image
		final File outFile = new File("snapshot/radialmenu-snapshot-"
				+ snapshotCounter + ".png");
		outFile.getParentFile().mkdirs();
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png",
					outFile);
		} catch (final IOException ex) {
			System.out.println(ex.getMessage());
		}

		snapshotCounter++;
	}

}
