package edu.wpi.cs.lmp.objects;

import edu.wpi.cs.lmp.leap.HandStateObservable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.text.Text;

public class TextBox extends Text implements IObject {
	
	private DoubleProperty textWidth;
	private DoubleProperty textHeight;
	
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
		double xPos = this.getX();
		double yPos = this.getY();
		double width = textWidth.doubleValue();
		double height = textHeight.doubleValue();
		
		// return (x > xPos-(width/2) && x < xPos+(width/2)) && (y > yPos-(height/2) && y < yPos+(height/2));
		return (x > xPos && x < xPos+(width)) && (y > yPos && y < yPos+(height));
	}

}
