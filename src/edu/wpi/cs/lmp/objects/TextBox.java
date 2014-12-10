package edu.wpi.cs.lmp.objects;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import edu.wpi.cs.lmp.leap.HandStateObservable;
import edu.wpi.cs.lmp.scenes.LeapScene;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextBox extends Text implements IObject {

	private static final float EXTRA_BOUNDS = 1.1f;

	private DoubleProperty textX;
	private DoubleProperty textY;

	private TextBox instance;

	public TextBox() {
		//TODO: Text is an interesting problem with bounds perhaps layout container?
		super("TESTING TESTING");

		instance = this;

		instance.setFont(new Font(45));
		
		textX = new SimpleDoubleProperty(instance.getX());
		textY = new SimpleDoubleProperty(instance.getY());
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				//textWidth = new SimpleDoubleProperty(instance.getScaleX());
				//textHeight = new SimpleDoubleProperty(instance.getScaleY());
				textX.set(instance.getX());
				textY.set(instance.getY());
				//instance.scaleXProperty().bind(textWidth);
				//instance.scaleYProperty().bind(textHeight);
			}

		});
	}

	@Override
	public void startMove() {		
		instance.layoutXProperty().set(-instance.getX());
		instance.layoutYProperty().set(-instance.getY());
		this.translateXProperty().bind(HandStateObservable.getInstance().getObservableX());
		this.translateYProperty().bind(HandStateObservable.getInstance().getObservableY());
		textX.bind(HandStateObservable.getInstance().getObservableX());
		textY.bind(HandStateObservable.getInstance().getObservableY());
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
		textX.unbind();
		textY.unbind();
	}

	@Override
	public void resize(double percentageChange) {
		//textWidth.set((textWidth.doubleValue()*percentageChange)/100);
		//textHeight.set((textHeight.doubleValue()*percentageChange)/100);
		Double currentSize = this.getFont().getSize();
		Font newFont = new Font(this.getFont().getName(), (currentSize*percentageChange)/100);
		this.setFont(newFont);
	}

	@Override
	public boolean inBounds(LeapScene parent, double x, double y) {
		double xPos = textX.doubleValue();
		double yPos = textY.doubleValue();

		double width = this.getLayoutBounds().getWidth() * EXTRA_BOUNDS;
		double height = this.getLayoutBounds().getHeight() * EXTRA_BOUNDS;

		// Y as at the bottom left for text
		yPos -= this.getLayoutBounds().getHeight() - (height - this.getLayoutBounds().getHeight())/2;


		// return (x > xPos-(width/2) && x < xPos+(width/2)) && (y > yPos-(height/2) && y < yPos+(height/2));
		
		// parent.drawBounds(xPos, yPos, width, height);

		return (x > xPos && x < xPos+(width)) && (y > yPos && y < yPos+(height));
	}

	@Override
	public void onScreenTap() {
		String newText = JOptionTextAreaPane.showInputDialog("Enter Text");
		
		
		//String newText = JOptionPane.showInputDialog("Enter Text", this.getText());
		if(newText != null)
			this.setText(newText);
	}

	@Override
	public void onCounterCircle() {
		// TODO Auto-generated method stub

	}

	@Override
	public Element toXML(Document doc) {
		Element txt = doc.createElement("Text");
		// Source field
		Element src = doc.createElement("string");
		src.appendChild(doc.createTextNode(this.getText()));
		txt.appendChild(src);
		// Position fields
		Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode(String.valueOf(textX.doubleValue())));
		txt.appendChild(x);
		Element y = doc.createElement("y");
		y.appendChild(doc.createTextNode(String.valueOf(textY.doubleValue())));
		txt.appendChild(y);
		// "Other" fields
		Element fontSize = doc.createElement("fontSize");
		fontSize.appendChild(doc.createTextNode(String.valueOf(this.getFont().getSize())));
		txt.appendChild(fontSize);
		
		Element fontFamily = doc.createElement("fontFamily");
		fontFamily.appendChild(doc.createTextNode(this.getFont().getFamily()));
		txt.appendChild(fontFamily);
		
		Element fontColor = doc.createElement("fontColor");
		fontColor.appendChild(doc.createTextNode(this.getFill().toString()));
		txt.appendChild(fontColor);
		
		return txt;
	}
	
	public static TextBox fromXML(Node n) {
		Node d = n.getFirstChild();
		
		TextBox t = new TextBox();
		t.setText(d.getFirstChild().getNodeValue());
		
		d = d.getNextSibling();
		t.textX.setValue(Double.parseDouble(d.getFirstChild().getNodeValue()));
		d = d.getNextSibling();
		t.textY.setValue(Double.parseDouble(d.getFirstChild().getNodeValue()));
		d = d.getNextSibling();
		Font newFont = new Font(t.getFont().getName(), Double.parseDouble(d.getFirstChild().getNodeValue()));
		t.setFont(newFont);
		
		return t;
	}

}
