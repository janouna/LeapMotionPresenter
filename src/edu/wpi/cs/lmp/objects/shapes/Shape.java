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
package edu.wpi.cs.lmp.objects.shapes;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.wpi.cs.lmp.leap.HandStateObservable;
import edu.wpi.cs.lmp.objects.IObject;
import edu.wpi.cs.lmp.objects.Image;
import edu.wpi.cs.lmp.scenes.LeapScene;

/**
 * The top level shape class.  Contains everything from Image, but change resize a bit.
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public abstract class Shape extends ImageView implements IObject {

	private final DoubleProperty shapeWidth;
	private final DoubleProperty shapeHeight;

	private final DoubleProperty shapeX;
	private final DoubleProperty shapeY;
	
	private DoubleProperty grabbedAtX;
	private DoubleProperty grabbedAtY;
	
	private final DoubleProperty shapeAngle;
	
	private String path;

	private final Shape instance;

	protected Shape(String path) {
		super("file:" + path);
		this.path = path;
		instance = this;
		shapeWidth = new SimpleDoubleProperty();
		shapeHeight = new SimpleDoubleProperty();
		shapeX = new SimpleDoubleProperty();
		shapeY = new SimpleDoubleProperty();
		grabbedAtX = new SimpleDoubleProperty();
		grabbedAtY = new SimpleDoubleProperty();
		shapeAngle = new SimpleDoubleProperty();
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				shapeWidth.set(instance.getImage().getWidth());
				shapeHeight.set(instance.getImage().getHeight());
				shapeX.set(instance.getX());
				shapeY.set(instance.getY());
				shapeAngle.set(instance.getRotate());
				instance.fitWidthProperty().bind(shapeWidth);
				instance.fitHeightProperty().bind(shapeHeight);
				instance.rotateProperty().bind(shapeAngle);
			}

		});
	}
	
	/**
	 * Creates a new shape
	 * @param path The path to the shape file
	 * @param x The x coordinate of the shape
	 * @param y The y coordinate of the shape
	 * @param width The width of the shape
	 * @param height The height of the shape
	 * @param angle The rotation angle of the shape
	 */
	protected Shape(String path, final double x, final double y, final double width, final double height, final double angle) {
		super("file:" + path);
		this.path = path;
		instance = this;
		shapeWidth = new SimpleDoubleProperty();
		shapeHeight = new SimpleDoubleProperty();
		shapeX = new SimpleDoubleProperty();
		shapeY = new SimpleDoubleProperty();
		grabbedAtX = new SimpleDoubleProperty();
		grabbedAtY = new SimpleDoubleProperty();
		shapeAngle = new SimpleDoubleProperty();
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				shapeWidth.set(width);
				shapeHeight.set(height);
				shapeX.set(x);
				shapeY.set(y);
				shapeAngle.set(angle);
				instance.translateXProperty().bind(instance.shapeX);
				instance.translateYProperty().bind(instance.shapeY);
				instance.translateXProperty().unbind();
				instance.translateYProperty().unbind();
				instance.fitWidthProperty().bind(shapeWidth);
				instance.fitHeightProperty().bind(shapeHeight);
				instance.rotateProperty().bind(shapeAngle);
			}

		});
	}

	public double getShapeWidth() {
		return shapeWidth.doubleValue();
	}

	/**
	 * Sets the width of the shape
	 * @param imgWidth The width of the shape
	 */
	public void setShapeWidth(double imgWidth) {
		this.shapeWidth.set(imgWidth);
	}

	public double getShapeHeight() {
		return shapeHeight.doubleValue();
	}

	/**
	 * Sets the height of the shape
	 * @param imgHeight The height of the shape
	 */
	public void setShapeHeight(double imgHeight) {
		this.shapeHeight.set(imgHeight);
	}

	public double getShapeX() {
		return shapeX.doubleValue();
	}

	/**
	 * Sets the X coordinate of the shape
	 * @param imgX The X coordinate of the shape
	 */
	public void setShapeX(double imgX) {
		this.shapeX.set(imgX);
	}
	
	public double getShapeY() {
		return shapeY.doubleValue();
	}

	/**
	 * Sets the Y coordinate of the shape
	 * @param imgY The Y coordinate of the shape
	 */
	public void setShapeY(double imgY) {
		this.shapeY.set(imgY);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void resize(double percentageChangeWidth, double percentageChangeHeight) {
		// This is to keep aspect ratio
		shapeWidth.set((shapeWidth.doubleValue()*percentageChangeWidth)/100);
		shapeHeight.set((shapeHeight.doubleValue()*percentageChangeHeight)/100);
	}

	@Override
	public void startMove() {
		instance.layoutXProperty().set(-instance.getX());
		instance.layoutYProperty().set(-instance.getY());
		grabbedAtX.bind(HandStateObservable.getInstance().getObservableX());
		grabbedAtY.bind(HandStateObservable.getInstance().getObservableY());
		this.translateXProperty().bind(grabbedAtX.add(shapeX.doubleValue() - grabbedAtX.doubleValue()));
		this.translateYProperty().bind(grabbedAtY.add(shapeY.doubleValue() - grabbedAtY.doubleValue()));
		shapeX.bind(this.translateXProperty());
		shapeY.bind(this.translateYProperty());
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
		grabbedAtX.unbind();
		grabbedAtY.unbind();
		shapeX.unbind();
		shapeY.unbind();
	}
	
	@Override
	public void rotate(double angle) {
		shapeAngle.set(angle * -1);
	}

	@Override
	public boolean inBounds(LeapScene parent, double x, double y) {
		final double xPos = shapeX.doubleValue();
		final double yPos = shapeY.doubleValue();
		final double width = shapeWidth.doubleValue();
		final double height = shapeHeight.doubleValue();

		// return (x > xPos-(width/2) && x < xPos+(width/2)) && (y > yPos-(height/2) && y < yPos+(height/2));

		return (x > xPos && x < xPos+(width)) && (y > yPos && y < yPos+(height));
	}

	@Override
	public void onScreenTap() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCounterCircle() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void radialMenuActions(int action) {
		
	}

	@Override
	public Element toXML(Document doc) {
		final Element img = doc.createElement("Shape");
		// Source field + copying
		final File file = new File(path);
		final Element src = doc.createElement("src");
		src.appendChild(doc.createTextNode("Assets/" + file.getName()));
		img.appendChild(src);
		// Position fields
		final Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode(String.valueOf(shapeX.doubleValue())));
		img.appendChild(x);
		final Element y = doc.createElement("y");
		y.appendChild(doc.createTextNode(String.valueOf(shapeY.doubleValue())));
		img.appendChild(y);
		// Size fields
		final Element width = doc.createElement("width");
		width.appendChild(doc.createTextNode(String.valueOf(shapeWidth.doubleValue())));
		img.appendChild(width);
		final Element height = doc.createElement("height");
		height.appendChild(doc.createTextNode(String.valueOf(shapeHeight.doubleValue())));
		img.appendChild(height);
		final Element angle = doc.createElement("angle");
		angle.appendChild(doc.createTextNode(String.valueOf(shapeAngle.doubleValue())));
		img.appendChild(angle);
		
		return img;
	}

	/**
	 * Creates a shape from an XML file
	 * @param e The top-level XML element
	 * @param directory The location of the XML file
	 * @return The shape
	 */
	public static Image fromXML(Element e, File directory) {
		final String file = e.getElementsByTagName("src").item(0).getTextContent();
		
		final double x = (Double.parseDouble(e.getElementsByTagName("x").item(0).getTextContent()));

		final double y = (Double.parseDouble(e.getElementsByTagName("y").item(0).getTextContent()));

		final double width = (Double.parseDouble(e.getElementsByTagName("width").item(0).getTextContent()));

		final double height = (Double.parseDouble(e.getElementsByTagName("height").item(0).getTextContent()));
		
		final double angle = (Double.parseDouble(e.getElementsByTagName("angle").item(0).getTextContent()));
		
		return new Image(directory.toString()+ "/" + file, x, y, width, height, angle);
		
	}
	
	public void copyTo(File to) {
		final File thisFile = new File(path);
		final File newFile = new File(to.toString() + "/" + thisFile.getName());
		try {
			Files.copy(thisFile.toPath(), newFile.toPath());
		} catch (FileAlreadyExistsException e) {
			// Do nothing, it already exists
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
