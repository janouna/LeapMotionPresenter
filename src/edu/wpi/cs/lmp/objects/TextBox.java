package edu.wpi.cs.lmp.objects;

import java.io.File;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.wpi.cs.lmp.leap.HandStateObservable;
import edu.wpi.cs.lmp.scenes.LeapScene;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;
import edu.wpi.cs.lmp.state.PresenterState;
import edu.wpi.cs.lmp.state.PresenterStateObservable;
import edu.wpi.cs.lmp.view.radialmenu.RadialMenuFactory;
import edu.wpi.cs.lmp.view.radialmenu.RadialOptionsMenu;

public class TextBox extends Text implements IObject {

	private static final float EXTRA_BOUNDS = 1.1f;

	private final DoubleProperty textX;
	private final DoubleProperty textY;
	
	private DoubleProperty grabbedAtX;
	private DoubleProperty grabbedAtY;
	
	private FontPosture posture;
	private FontWeight weight;

	private final TextBox instance;

	public TextBox() {
		// TODO: Text is an interesting problem with bounds perhaps layout
		// container?
		super();
		
		promptUserText("New Text Element");

		instance = this;
		
		weight = FontWeight.NORMAL;
		posture = FontPosture.REGULAR;
		
		grabbedAtX = new SimpleDoubleProperty();
		grabbedAtY = new SimpleDoubleProperty();

		instance.setFont(new Font(45));

		textX = new SimpleDoubleProperty(instance.getX());
		textY = new SimpleDoubleProperty(instance.getY());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// textWidth = new SimpleDoubleProperty(instance.getScaleX());
				// textHeight = new SimpleDoubleProperty(instance.getScaleY());
				textX.set(instance.getX());
				textY.set(instance.getY());
				// instance.scaleXProperty().bind(textWidth);
				// instance.scaleYProperty().bind(textHeight);
			}

		});
	}

	public TextBox(String text, final double x, final double y,
			final double fontSize) {
		// TODO: Text is an interesting problem with bounds perhaps layout
		// container?
		super(text);

		instance = this;
		
		//TODO: Fix this
		weight = FontWeight.NORMAL;
		posture = FontPosture.REGULAR;
		
		instance.setFont(new Font(fontSize));

		textX = new SimpleDoubleProperty(x);
		textY = new SimpleDoubleProperty(y);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// textWidth = new SimpleDoubleProperty(instance.getScaleX());
				// textHeight = new SimpleDoubleProperty(instance.getScaleY());
				instance.translateXProperty().bind(instance.textX);
				instance.translateYProperty().bind(instance.textY);
				instance.translateXProperty().unbind();
				instance.translateYProperty().unbind();
				// instance.scaleXProperty().bind(textWidth);
				// instance.scaleYProperty().bind(textHeight);
			}

		});
	}

	@Override
	public void startMove() {
		instance.layoutXProperty().set(-instance.getX());
		instance.layoutYProperty().set(-instance.getY());
		grabbedAtX.bind(HandStateObservable.getInstance().getObservableX());
		grabbedAtY.bind(HandStateObservable.getInstance().getObservableY());
		this.translateXProperty().bind(
				grabbedAtX.add(textX.doubleValue() - grabbedAtX.doubleValue()));
		this.translateYProperty().bind(
				grabbedAtY.add(textY.doubleValue() - grabbedAtY.doubleValue()));
		textX.bind(this.translateXProperty());
		textY.bind(this.translateYProperty());
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
		grabbedAtX.unbind();
		grabbedAtY.unbind();
		textX.unbind();
		textY.unbind();
	}

	@Override
	public void resize(double percentageChangeWidth,
			double percentageChangeHeight) {
		// textWidth.set((textWidth.doubleValue()*percentageChange)/100);
		// textHeight.set((textHeight.doubleValue()*percentageChange)/100);
		final Double currentSize = this.getFont().getSize();
		final Font newFont = new Font(this.getFont().getName(),
				(currentSize * percentageChangeWidth) / 100);
		this.setFont(newFont);
	}

	@Override
	public void rotate(double angle) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean inBounds(LeapScene parent, double x, double y) {
		final double xPos = textX.doubleValue();
		double yPos = textY.doubleValue();

		final double width = this.getLayoutBounds().getWidth() * EXTRA_BOUNDS;
		final double height = this.getLayoutBounds().getHeight() * EXTRA_BOUNDS;

		// Y as at the bottom left for text
		yPos -= this.getLayoutBounds().getHeight()
				- (height - this.getLayoutBounds().getHeight()) / 2;

		// return (x > xPos-(width/2) && x < xPos+(width/2)) && (y >
		// yPos-(height/2) && y < yPos+(height/2));

		// parent.drawBounds(xPos, yPos, width, height);

		return (x > xPos && x < xPos + (width))
				&& (y > yPos && y < yPos + (height));
	}

	@Override
	public void onScreenTap() {
		if (PresenterStateObservable.getInstance().get() == PresenterState.CREATING) {
			promptUserText("Edit Text Element");
		} else {
			// We are presenting make no changes
		}
	}

	@Override
	public void onCounterCircle() {
		RadialOptionsMenu radialMenu = RadialMenuFactory.getInstance()
				.createRadialMenu(this, ObjectType.TEXT);
		LeapSceneManager.getInstance().addRadial(radialMenu);
	}

	@Override
	public void radialMenuActions(int action) {
		switch (action) {
		case 0: // Font
			break;
		case 1: // Color
			break;
		case 2: // Bold menu item
			if (weight == FontWeight.BOLD) {
				weight = FontWeight.NORMAL;
			} else {
				weight = FontWeight.BOLD;
			}
			this.setFont(Font.font(this.getFont().getName(), weight, posture, this.getFont().getSize()));
			break;
		case 3: // Italics
			if (posture == FontPosture.ITALIC) {
				posture = FontPosture.REGULAR;
			} else {
				posture = FontPosture.ITALIC;
			}
			this.setFont(Font.font(this.getFont().getName(), weight, posture, this.getFont().getSize()));
			break;
		case 4: // Underline
			break;
		default:
			break;
		}
	}

	@Override
	public Element toXML(Document doc) {
		final Element txt = doc.createElement("Text");
		// Source field
		final Element src = doc.createElement("string");
		src.appendChild(doc.createTextNode(this.getText()));
		txt.appendChild(src);
		// Position fields
		final Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode(String.valueOf(textX.doubleValue())));
		txt.appendChild(x);
		final Element y = doc.createElement("y");
		y.appendChild(doc.createTextNode(String.valueOf(textY.doubleValue())));
		txt.appendChild(y);
		// "Other" fields
		final Element fontSize = doc.createElement("fontSize");
		fontSize.appendChild(doc.createTextNode(String.valueOf(this.getFont()
				.getSize())));
		txt.appendChild(fontSize);

		final Element fontFamily = doc.createElement("fontFamily");
		fontFamily.appendChild(doc.createTextNode(this.getFont().getFamily()));
		txt.appendChild(fontFamily);

		final Element fontColor = doc.createElement("fontColor");
		fontColor.appendChild(doc.createTextNode(this.getFill().toString()));
		txt.appendChild(fontColor);

		return txt;
	}

	public static TextBox fromXML(Element e, File directory) {
		final String text = e.getElementsByTagName("string").item(0)
				.getTextContent();

		final double x = (Double.parseDouble(e.getElementsByTagName("x")
				.item(0).getTextContent()));

		final double y = (Double.parseDouble(e.getElementsByTagName("y")
				.item(0).getTextContent()));

		final double fontSize = (Double.parseDouble(e
				.getElementsByTagName("fontSize").item(0).getTextContent()));

		return new TextBox(text, x, y, fontSize);
	}

	public void copyTo(File to) {
		// No need to copy
	}
	
	private void promptUserText(String promptText) {
		Optional<String> userText = Dialogs.create()
				.styleClass(Dialog.STYLE_CLASS_CROSS_PLATFORM)
				.title(promptText)
				.showTextInput(this.getText());
		this.setText(userText.get());
	}

}
