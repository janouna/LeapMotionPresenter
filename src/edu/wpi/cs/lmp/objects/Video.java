package edu.wpi.cs.lmp.objects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.MediaView;

public class Video extends MediaView implements IObject {
	
	private DoubleProperty vidWidth;
	private DoubleProperty vidHeight;
	
	public Video () {
		vidWidth = new SimpleDoubleProperty(this.getFitWidth());
		vidHeight = new SimpleDoubleProperty(this.getFitHeight());
		this.fitWidthProperty().bind(vidWidth);
		this.fitHeightProperty().bind(vidHeight);
	}

	@Override
	public void startMove() {
		// TODO Implement startMove()
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
	}

	@Override
	public void resize(double percentageChange) {
		vidWidth.set((vidWidth.doubleValue()*percentageChange)/100);
		vidHeight.set((vidHeight.doubleValue()*percentageChange)/100);
	}

}
