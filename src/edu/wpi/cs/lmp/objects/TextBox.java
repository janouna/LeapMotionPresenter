package edu.wpi.cs.lmp.objects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.text.Text;
import edu.wpi.cs.lmp.leap.HandStateObservable;

public class TextBox extends Text implements IObject {
	
	private final DoubleProperty textWidth;
	private final DoubleProperty textHeight;
	
	public TextBox() {
		//TODO: Text is an interesting problem with bounds perhaps layout container?
		textWidth = new SimpleDoubleProperty();
		textHeight = new SimpleDoubleProperty();
		/* Resize will need to accound to for font sizing
		textWidth.bind(this.);
		this.fitHeightProperty().bind(textHeight);
		*/ 
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
		final double xPos = this.getX();
		final double yPos = this.getY();
		final double width = textWidth.doubleValue();
		final double height = textHeight.doubleValue();
		
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
