package edu.wpi.cs.lmp.objects;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.wpi.cs.lmp.scenes.LeapScene;

public interface IObject {
	void startMove();
	void endMove();
	void resize(double percentChangeWidth, double percentChangeHeight);
	void setX(double x);
	void setY(double y);
	void rotate(double angle);

	void onScreenTap();
	void onCounterCircle();
	
	void radialMenuActions(int action);
	
	void copyTo(File to);

	boolean inBounds(LeapScene parent, double x, double y);
	
	Element toXML(Document doc);
}
