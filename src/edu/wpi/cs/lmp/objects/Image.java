package edu.wpi.cs.lmp.objects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;

public class Image extends ImageView implements IObject {
	
	private DoubleProperty imgWidth;
	private DoubleProperty imgHeight;
	
	public Image() {
		// TODO Pollice is default image for testing
		super("file:gary.JPG");
		imgWidth = new SimpleDoubleProperty(this.getFitWidth());
		imgHeight = new SimpleDoubleProperty(this.getFitHeight());
		this.fitWidthProperty().bind(imgWidth);
		this.fitHeightProperty().bind(imgHeight);
	}

	@Override
	public void resize(double percentageChange) {
		imgWidth.set((imgWidth.doubleValue()*percentageChange)/100);
		imgHeight.set((imgHeight.doubleValue()*percentageChange)/100);
	}

	@Override
	public void startMove() {
		// TODO Implement startMove()
		/*
		this.translateXProperty().bind(palmX);
		this.translateYProperty().bind(palmY);
		*/
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
	}

}
