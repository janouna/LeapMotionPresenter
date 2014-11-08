package edu.wpi.cs.lmp.objects;

import javafx.beans.property.DoubleProperty;
import javafx.scene.text.Text;

public class TextBox extends Text implements IObject {
	
	private DoubleProperty textWidth;
	private DoubleProperty textHeight;
	
	public TextBox() {
		/* Resize will need to accound to for font sizing
		textWidth.bind(this.);
		this.fitHeightProperty().bind(textHeight);
		*/ 
	}

	@Override
	public void startMove() {
		// // TODO Implement startMove()

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

}
