package edu.wpi.cs.lmp.objects;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;
import edu.wpi.cs.lmp.leap.HandStateObservable;

public class Image extends ImageView implements IObject {
	
	private DoubleProperty imgWidth;
	private DoubleProperty imgHeight;
	
	private DoubleProperty imgX;
	private DoubleProperty imgY;
	
	private final Image instance;
	
	public Image() {
		// TODO Pollice is default image for testing
		super("file:gary.JPG");
		instance = this;
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				imgWidth = new SimpleDoubleProperty(instance.getImage().getWidth());
				imgHeight = new SimpleDoubleProperty(instance.getImage().getHeight());
				imgX = new SimpleDoubleProperty(instance.getX());
				imgY = new SimpleDoubleProperty(instance.getY());
				instance.fitWidthProperty().bind(imgWidth);
				instance.fitHeightProperty().bind(imgHeight);
			}
			
		});
	}

	@Override
	public void resize(double percentageChange) {
		imgWidth.set((imgWidth.doubleValue()*percentageChange)/100);
		imgHeight.set((imgHeight.doubleValue()*percentageChange)/100);
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
	public boolean inBounds(double x, double y) {
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

}
