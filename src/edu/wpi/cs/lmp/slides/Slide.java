package edu.wpi.cs.lmp.slides;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import edu.wpi.cs.lmp.objects.IObject;

public class Slide extends Parent {
	
	private List<IObject> children;
	private ImageView background;
	
	public Slide() {
		children = new ArrayList<IObject>();
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
	
	public List<IObject> getAt(Point position) {
		List<IObject> atList = new LinkedList<IObject>();
		for(IObject i: atList){
			// TODO Fix Node cast
			Node j = (Node) i;
			if(j.contains(new Point2D(position.getX(), position.getY()))){
				atList.add(i);
			}
		}
		
		return atList;
	}

}
