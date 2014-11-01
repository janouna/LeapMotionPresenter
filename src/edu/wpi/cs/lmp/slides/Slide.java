package edu.wpi.cs.lmp.slides;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.lmp.objects.IObject;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

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
		// Do stuff here
	}
	
	public void removeObject(IObject removeObject) {
		// Remove object here
	}
	
	public IObject getAt(Point position) {
		// Return obj at
		// TODO Implement
		return null;
	}

}
