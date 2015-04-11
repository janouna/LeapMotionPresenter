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
package edu.wpi.cs.lmp.scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.leapmotion.leap.Controller;

import edu.wpi.cs.lmp.leap.ObjectGestureListener;
import edu.wpi.cs.lmp.objects.IObject;

/**
 * The primary container for all objects in the presentation.  Different scenes hold different sets of objects.
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class LeapScene extends Parent {

	private final List<IObject> children;
	private final ImageView background;
	private ObjectGestureListener resizer;
	private Controller c;

	private boolean objectMoving;

	public LeapScene() {
		c = new Controller();
		objectMoving = false;
		resizer = new ObjectGestureListener();
		c.addListener(resizer);
		children = new ArrayList<IObject>();
		background = new ImageView();
		background.setFitWidth(Screen.getPrimary().getBounds().getWidth());
		background.setFitHeight(Screen.getPrimary().getBounds().getHeight());
		// this.getChildren().add(background);
		// Bind on click
		this.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final List<IObject> clickedOn = getAt(event.getX(),
						event.getY());
				try {
					if (!objectMoving) {
						objectMoving = true;
						clickedOn.get(clickedOn.size()-1).startMove();
					}
				} catch (IndexOutOfBoundsException e) {
					System.out.println("No object found: onPressed()");
				}
			}
		});
		// Unbind on release
		this.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final List<IObject> objects = getObjects();
				for (IObject i : objects) {
					objectMoving = false;
					i.endMove();
				}
			}
		});
		
		this.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final List<IObject> mouseOver = getAt(event.getX(),
						event.getY());
				try {
					resizer.setIObject(mouseOver.get(mouseOver.size() - 1));
				} catch (IndexOutOfBoundsException e) {
					System.out.println("No object found: OnEntered()");
				}
			}
		});
		/*
		// Bind resizer to object the cursor is ontop of
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final List<IObject> mouseOver = getAt(event.getX(),
						event.getY());
				try {
					System.out.println("SWITCH HERE");
					resizer.setIObject(mouseOver.get(mouseOver.size() - 1));
				} catch (IndexOutOfBoundsException e) {
					System.out.println("No object found: OnEntered()");
				}
			}
		});
		// Unbind resizer as cursor is not on anything
		this.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resizer.setIObject(null);
			}
		});
		
		*/
	}

	/**
	 * Creates a new scene
	 * @param bgFile The background image of the scene
	 */
	public LeapScene(String bgFile) {
		children = new ArrayList<IObject>();
		background = new ImageView("file:" + bgFile);
	}

	public List<IObject> getObjects() {
		// Return objects here
		return children;
	}

	/**
	 * Adds a new object to the current scene
	 * @param newObject The object to add
	 */
	public void addObject(IObject newObject) {
		children.add(newObject);
		// TODO Fix Node cast
		this.getChildren().add((Node) newObject);
	}

	/**
	 * Removes an object from the current scene
	 * @param removeObject The object to remove
	 */
	public void removeObject(IObject removeObject) {
		children.remove(removeObject);
		this.getChildren().remove(removeObject);
	}

	/**
	 * Gets a list of all IObjects that are present at the given coordinate
	 * @param x The x position
	 * @param y The y position
	 * @return The list of objects
	 */
	public List<IObject> getAt(double x, double y) {
		final List<IObject> atList = new LinkedList<IObject>();
		for (IObject i : children) {
			if (i.inBounds(this, x, y)) {
				atList.add(i);
			}
		}

		return atList;
	}

	/**
	 * For debugging purposes, draws an empty red box with the given dimensions
	 * @param x The X position of the box
	 * @param y The Y position of the box
	 * @param width The width of the box
	 * @param height The height of the box
	 */
	public void drawBounds(double x, double y, double width, double height) {
		final Rectangle bounds = new Rectangle(x, y, width, height);
		bounds.setFill(Color.TRANSPARENT);
		bounds.setStroke(Color.RED);
		bounds.setStrokeWidth(2);
		this.getChildren().add(bounds);
	}

	/**
	 * Stores the scene data in an XML file
	 * @param doc The document to store the data in
	 * @return The document with the scene data stored in it
	 */
	public List<Element> toXML(Document doc) {
		final List<Element> xmlObjs = new ArrayList<Element>();
		for (int i = 0; i < children.size(); i++) {
			xmlObjs.add(children.get(i).toXML(doc));
		}
		return xmlObjs;
	}

	/**
	 * Copies all of the scene's children to a given location
	 * @param to The location to copy to
	 */
	public void copyTo(File to) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).copyTo(to);
		}
	}

}
