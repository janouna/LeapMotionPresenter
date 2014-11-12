package edu.wpi.cs.lmp.objects;

public interface IObject {
	void startMove();
	void endMove();
	void resize(double percentChange);
	void setX(double x);
	void setY(double y);
	
	void onScreenTap();
	void onCounterCircle();
	
	boolean inBounds(double x, double y);
}
