package edu.wpi.cs.lmp.objects;

public interface IObject {
	void startMove();
	void endMove();
	void resize(double percentChange);
}
