package edu.wpi.cs.lmp.slides;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.leapmotion.leap.Controller;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import edu.wpi.cs.lmp.leap.ResizeListener;
import edu.wpi.cs.lmp.objects.IObject;
import edu.wpi.cs.lmp.objects.Image;

public class Slide extends Parent {

	private List<IObject> children;
	private ImageView background;
	private ResizeListener resizer;
	private Controller c;

	public Slide() {
		c = new Controller();
		resizer = new ResizeListener();
		c.addListener(resizer);
		children = new ArrayList<IObject>();
		background = new ImageView();
		background
				.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth());
		background.setFitHeight(Screen.getPrimary().getVisualBounds()
				.getHeight());
		// this.getChildren().add(background);
		// Bind on click
		this.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				List<IObject> clickedOn = getAt(event.getX(), event.getY());
				System.out.println("Binding to: " + clickedOn.get(0));
				clickedOn.get(0).startMove();
			}
		});
		// Unbind on release
		this.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				List<IObject> objects = getObjects();
				for (IObject i : objects) {
					i.endMove();
				}
			}
		});
		// Bind resizer to object the cursor is ontop of
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				List<IObject> mouseOver = getAt(event.getX(), event.getY());
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

	public Slide(String bgFile) {
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
		List<IObject> atList = new LinkedList<IObject>();
		for (IObject i : children) {
			if (i.inBounds(x, y)) {
				atList.add(i);
			}
		}

		return atList;
	}

}
