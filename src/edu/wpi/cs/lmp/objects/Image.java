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
package edu.wpi.cs.lmp.objects;

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
import edu.wpi.cs.lmp.scenes.LeapScene;

/**
 * The image object in the Leap Motion Presenter.
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class Image extends ImageView implements IObject {

	private DoubleProperty imgWidth;
	private DoubleProperty imgHeight;

	private DoubleProperty imgX;
	private DoubleProperty imgY;
	
	private DoubleProperty grabbedAtX;
	private DoubleProperty grabbedAtY;
	
	private DoubleProperty imgAngle;
	
	private String path;

	private final Image instance;

	public Image() {
		this("file:gary.JPG");
	}
	
	/**
	 * Creates an Image IObject
	 * @param path The path to the image file, local or relative
	 */
	public Image(String path) {
		super("file:" + path);
		this.path = path;
		instance = this;
		imgWidth = new SimpleDoubleProperty();
		imgHeight = new SimpleDoubleProperty();
		imgX = new SimpleDoubleProperty();
		imgY = new SimpleDoubleProperty();
		grabbedAtX = new SimpleDoubleProperty();
		grabbedAtY = new SimpleDoubleProperty();
		imgAngle = new SimpleDoubleProperty();
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				imgWidth.set(instance.getImage().getWidth());
				imgHeight.set(instance.getImage().getHeight());
				imgX.set(instance.getX());
				imgY.set(instance.getY());
				imgAngle.set(instance.getRotate());
				instance.fitWidthProperty().bind(imgWidth);
				instance.fitHeightProperty().bind(imgHeight);
				instance.rotateProperty().bind(imgAngle);
			}

		});
	}
	
	/**
	 * Creates an Image IObject
	 * @param path The path to the image file, local or relative
	 * @param x The x coordinate of the image
	 * @param y The y coordinate of the image
	 * @param width The width of the image
	 * @param height The height of the image
	 * @param angle The rotation angle of the image
	 */
	public Image(String path, final double x, final double y, final double width, final double height, final double angle) {
		super("file:" + path);
		this.path = path;
		instance = this;
		imgWidth = new SimpleDoubleProperty();
		imgHeight = new SimpleDoubleProperty();
		imgX = new SimpleDoubleProperty();
		imgY = new SimpleDoubleProperty();
		grabbedAtX = new SimpleDoubleProperty();
		grabbedAtY = new SimpleDoubleProperty();
		imgAngle = new SimpleDoubleProperty();
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				imgWidth.set(width);
				imgHeight.set(height);
				imgX.set(x);
				imgY.set(y);
				imgAngle.set(angle);
				instance.translateXProperty().bind(instance.imgX);
				instance.translateYProperty().bind(instance.imgY);
				instance.translateXProperty().unbind();
				instance.translateYProperty().unbind();
				instance.fitWidthProperty().bind(imgWidth);
				instance.fitHeightProperty().bind(imgHeight);
				instance.rotateProperty().bind(imgAngle);
			}

		});
	}
	
	public double getImgWidth() {
		return imgWidth.doubleValue();
	}
	
	/**
	 * Sets the width of the image
	 * @param imgWidth The width of the image
	 */
	public void setImgWidth(double imgWidth) {
		this.imgWidth.set(imgWidth);
	}
	
	public double getImgHeight() {
		return imgHeight.doubleValue();
	}

	/**
	 * Sets of the height of the image
	 * @param imgHeight The height of the image
	 */
	public void setImgHeight(double imgHeight) {
		this.imgHeight.set(imgHeight);
	}

	public double getImgX() {
		return imgX.doubleValue();
	}
	
	/**
	 * Sets the X position of the image
	 * @param imgX The X position of the image
	 */
	public void setImgX(double imgX) {
		this.imgX.set(imgX);
	}

	public double getImgY() {
		return imgY.doubleValue();
	}
	
	/**
	 * Sets the Y position of the image
	 * @param imgY The Y position of the image
	 */
	public void setImgY(double imgY) {
		this.imgY.set(imgY);
	}

	public String getPath() {
		return path;
	}
	
	/**
	 * Sets the path of the image
	 * @param path The relative or absolute path to the image
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public void resize(double percentageChangeWidth, double percentageChangeHeight) {
		// This is to keep aspect ratio
		imgWidth.set((imgWidth.doubleValue()*percentageChangeWidth)/100);
		imgHeight.set((imgHeight.doubleValue()*percentageChangeWidth)/100);
	}

	@Override
	public void startMove() {
		instance.layoutXProperty().set(-instance.getX());
		instance.layoutYProperty().set(-instance.getY());
		grabbedAtX.bind(HandStateObservable.getInstance().getObservableX());
		grabbedAtY.bind(HandStateObservable.getInstance().getObservableY());
		this.translateXProperty().bind(grabbedAtX.add(imgX.doubleValue() - grabbedAtX.doubleValue()));
		this.translateYProperty().bind(grabbedAtY.add(imgY.doubleValue() - grabbedAtY.doubleValue()));
		imgX.bind(this.translateXProperty());
		imgY.bind(this.translateYProperty());
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
		grabbedAtX.unbind();
		grabbedAtY.unbind();
		imgX.unbind();
		imgY.unbind();
	}
	
	@Override
	public void rotate(double angle) {
		imgAngle.set(angle * -1);
	}

	@Override
	public boolean inBounds(LeapScene parent, double x, double y) {
		final double xPos = imgX.doubleValue();
		final double yPos = imgY.doubleValue();
		final double width = imgWidth.doubleValue();
		final double height = imgHeight.doubleValue();

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public Element toXML(Document doc) {
		final Element img = doc.createElement("Image");
		// Source field + copying
		final File file = new File(path);
		final Element src = doc.createElement("src");
		src.appendChild(doc.createTextNode("Assets/" + file.getName()));
		img.appendChild(src);
		// Position fields
		final Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode(String.valueOf(imgX.doubleValue())));
		img.appendChild(x);
		final Element y = doc.createElement("y");
		y.appendChild(doc.createTextNode(String.valueOf(imgY.doubleValue())));
		img.appendChild(y);
		// Size fields
		final Element width = doc.createElement("width");
		width.appendChild(doc.createTextNode(String.valueOf(imgWidth.doubleValue())));
		img.appendChild(width);
		final Element height = doc.createElement("height");
		height.appendChild(doc.createTextNode(String.valueOf(imgHeight.doubleValue())));
		img.appendChild(height);
		final Element angle = doc.createElement("angle");
		angle.appendChild(doc.createTextNode(String.valueOf(imgAngle.doubleValue())));
		img.appendChild(angle);
		
		return img;
	}
	
	/**
	 * Creates an image from an XML file
	 * @param e The top-level XML element
	 * @param directory The location of the XML file
	 * @return The image
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
