package edu.wpi.cs.lmp.objects;

import edu.wpi.cs.lmp.leap.HandStateObservable;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.text.Text;

public class TextBox extends Text implements IObject {
	
	private DoubleProperty textWidth;
	private DoubleProperty textHeight;
	
	private DoubleProperty textX;
	private DoubleProperty textY;
	
	private TextBox instance;
	
	public TextBox() {
		//TODO: Text is an interesting problem with bounds perhaps layout container?
		super("TESTING TESTING");
		// Resize will need to accound to for font sizing
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				textWidth = new SimpleDoubleProperty(instance.getScaleX());
				textHeight = new SimpleDoubleProperty(instance.getScaleY());
				instance.scaleXProperty().bind(textWidth);
				instance.scaleYProperty().bind(textHeight);
			}
			
		});
	}

	@Override
	public void startMove() {
		this.translateXProperty().bind(HandStateObservable.getInstance().getObservableX());
		this.translateYProperty().bind(HandStateObservable.getInstance().getObservableY());
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
	}

	@Override
	public void resize(double percentageChange) {
		textWidth.set((textWidth.doubleValue()*percentageChange)/100);
		textHeight.set((textHeight.doubleValue()*percentageChange)/100);
	}
	
	@Override
	public boolean inBounds(double x, double y) {
		/*
		double xPos = textX.doubleValue();
		double yPos = textY.doubleValue();
		double width = textWidth.doubleValue();
		double height = textHeight.doubleValue();
		*/
		return true;
		// return (x > xPos-(width/2) && x < xPos+(width/2)) && (y > yPos-(height/2) && y < yPos+(height/2));
		//return (x > xPos && x < xPos+(width)) && (y > yPos && y < yPos+(height));
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
