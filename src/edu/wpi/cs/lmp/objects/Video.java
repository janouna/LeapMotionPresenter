package edu.wpi.cs.lmp.objects;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.MediaView;

public class Video extends MediaView implements IObject {
	
	private DoubleProperty vidWidth;
	private DoubleProperty vidHeight;

	@Override
	public void startMove() {
		// TODO Auto-generated method stub

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
