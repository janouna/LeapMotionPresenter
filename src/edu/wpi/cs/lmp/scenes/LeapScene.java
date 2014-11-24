package edu.wpi.cs.lmp.scenes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

import com.leapmotion.leap.Controller;

import edu.wpi.cs.lmp.leap.ResizeListener;
import edu.wpi.cs.lmp.objects.IObject;

public class LeapScene extends Parent {

	private final List<IObject> children;
	private final ImageView background;
	private ResizeListener resizer;
	private Controller c;

	public LeapScene() {
		c = new Controller();
		resizer = new ResizeListener();
		c.addListener(resizer);
		children = new ArrayList<IObject>();
		background = new ImageView();
		background.setFitWidth(Screen.getPrimary().getBounds().getWidth());
		background.setFitHeight(Screen.getPrimary().getBounds().getHeight());
		// this.getChildren().add(background);
		// Bind on click
		this.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final List<IObject> clickedOn = getAt(event.getX(),
						event.getY());
				System.out.println("Binding to: " + clickedOn.get(0));
				clickedOn.get(0).startMove();
			}
		});
		// Unbind on release
		this.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final List<IObject> objects = getObjects();
				for (IObject i : objects) {
					i.endMove();
				}
			}
		});
		// Bind resizer to object the cursor is ontop of
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final List<IObject> mouseOver = getAt(event.getX(),
						event.getY());
				resizer.setIObject(mouseOver.get(0));
			}
		});
		// Unbind resizer as cursor is not on anything
		this.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resizer.setIObject(null);
			}
		});
	}

	public LeapScene(String bgFile) {
		children = new ArrayList<IObject>();
		background = new ImageView("file:" + bgFile);
	}

	public List<IObject> getObjects() {
		// Return objects here
		return children;
	}

	public void addObject(IObject newObject) {
		children.add(newObject);
		// TODO Fix Node cast
		this.getChildren().add((Node) newObject);
	}

	public void removeObject(IObject removeObject) {
		children.remove(removeObject);
		this.getChildren().remove(removeObject);
	}

	public List<IObject> getAt(double x, double y) {
		final List<IObject> atList = new LinkedList<IObject>();
		for (IObject i : children) {
			if (i.inBounds(this, x, y)) {
				atList.add(i);
			}
		}

		return atList;
	}

	public void drawBounds(double x, double y, double width, double height) {
		Rectangle bounds = new Rectangle(x, y, width, height);
		bounds.setFill(Color.TRANSPARENT);
		bounds.setStroke(Color.RED);
		bounds.setStrokeWidth(2);
		this.getChildren().add(bounds);
	}

}
