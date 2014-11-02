package edu.wpi.cs.lmp.objects;

public interface IObject {
	public void startMove();
	public void endMove();
	public void resize(double percentChange);
}
