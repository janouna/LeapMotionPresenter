package edu.wpi.cs.lmp.objects;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import edu.wpi.cs.lmp.leap.HandStateObservable;
import edu.wpi.cs.lmp.scenes.LeapScene;

public class Image extends ImageView implements IObject {

	private DoubleProperty imgWidth;
	private DoubleProperty imgHeight;

	private DoubleProperty imgX;
	private DoubleProperty imgY;
	
	private DoubleProperty imgAngle;
	
	private LeapScene container;
	
	private String path;

	private final Image instance;

	public Image() {
		this("file:gary.JPG");
	}

	public Image(String path) {
		super("file:" + path);
		this.path = path;
		instance = this;
		imgWidth = new SimpleDoubleProperty();
		imgHeight = new SimpleDoubleProperty();
		imgX = new SimpleDoubleProperty();
		imgY = new SimpleDoubleProperty();
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
	
	public Image(String path, final double x, final double y, final double width, final double height, final double angle) {
		super("file:" + path);
		this.path = path;
		instance = this;
		imgWidth = new SimpleDoubleProperty();
		imgHeight = new SimpleDoubleProperty();
		imgX = new SimpleDoubleProperty();
		imgY = new SimpleDoubleProperty();
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

	public void setImgWidth(double imgWidth) {
		this.imgWidth.set(imgWidth);
	}

	public double getImgHeight() {
		return imgHeight.doubleValue();
	}

	public void setImgHeight(double imgHeight) {
		this.imgHeight.set(imgHeight);;
	}

	public double getImgX() {
		return imgX.doubleValue();
	}

	public void setImgX(double imgX) {
		this.imgX.set(imgX);;
	}

	public double getImgY() {
		return imgY.doubleValue();
	}

	public void setImgY(double imgY) {
		this.imgY.set(imgY);;
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
		imgWidth.set((imgWidth.doubleValue()*percentageChangeWidth)/100);
		imgHeight.set((imgHeight.doubleValue()*percentageChangeWidth)/100);
	}

	@Override
	public void startMove() {
		instance.layoutXProperty().set(-instance.getX());
		instance.layoutYProperty().set(-instance.getY());
		this.translateXProperty().bind(HandStateObservable.getInstance().getObservableX());
		this.translateYProperty().bind(HandStateObservable.getInstance().getObservableY());
		imgX.bind(HandStateObservable.getInstance().getObservableX());
		imgY.bind(HandStateObservable.getInstance().getObservableY());
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
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
	public Element toXML(Document doc) {
		Element img = doc.createElement("Image");
		// Source field + copying
		File file = new File(path);
		Element src = doc.createElement("src");
		src.appendChild(doc.createTextNode("Assets/" + file.getName()));
		img.appendChild(src);
		// Position fields
		Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode(String.valueOf(imgX.doubleValue())));
		img.appendChild(x);
		Element y = doc.createElement("y");
		y.appendChild(doc.createTextNode(String.valueOf(imgY.doubleValue())));
		img.appendChild(y);
		// Size fields
		Element width = doc.createElement("width");
		width.appendChild(doc.createTextNode(String.valueOf(imgWidth.doubleValue())));
		img.appendChild(width);
		Element height = doc.createElement("height");
		height.appendChild(doc.createTextNode(String.valueOf(imgHeight.doubleValue())));
		img.appendChild(height);
		Element angle = doc.createElement("angle");
		angle.appendChild(doc.createTextNode(String.valueOf(imgAngle.doubleValue())));
		img.appendChild(angle);
		
		return img;
	}

	public static Image fromXML(Element e, File directory) {
		String file = e.getElementsByTagName("src").item(0).getTextContent();
		
		double x = (Double.parseDouble(e.getElementsByTagName("x").item(0).getTextContent()));

		double y = (Double.parseDouble(e.getElementsByTagName("y").item(0).getTextContent()));

		double width = (Double.parseDouble(e.getElementsByTagName("width").item(0).getTextContent()));

		double height = (Double.parseDouble(e.getElementsByTagName("height").item(0).getTextContent()));
		
		double angle = (Double.parseDouble(e.getElementsByTagName("angle").item(0).getTextContent()));
		
		return new Image(directory.toString()+ "/" + file, x, y, width, height, angle);
		
	}
	
	public void copyTo(File to) {
		File thisFile = new File(path);
		File newFile = new File(to.toString() + "/" + thisFile.getName());
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
