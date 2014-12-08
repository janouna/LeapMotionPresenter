package edu.wpi.cs.lmp.objects;

import org.w3c.dom.Element;

import edu.wpi.cs.lmp.scenes.LeapScene;

public interface IObject {
	void startMove();
	void endMove();
	void resize(double percentChange);
	void setX(double x);
	void setY(double y);

	void onScreenTap();
	void onCounterCircle();

	boolean inBounds(LeapScene parent, double x, double y);
	
	Element toXML();
}
