package edu.wpi.cs.lmp.leap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HandStateObservable {
	private static HandStateObservable INSTANCE = new HandStateObservable();
	private ObjectProperty<HandState> handState = new SimpleObjectProperty<>();
	private SimpleDoubleProperty handX = new SimpleDoubleProperty();
	private SimpleDoubleProperty handY = new SimpleDoubleProperty();
	
	private HandStateObservable(){
		handState.set(HandState.GONE);
		handX.set(0);
		handY.set(0);
	}
	
	public static HandStateObservable getInstance() {
		return INSTANCE;
	}
	
	public void set(HandState state){
		handState.set(state);
	}
	
	public HandState get() {
		return handState.get();
	}
	
	public ObjectProperty<HandState> getHandState() {
		return handState;
	}
	
	public void setX(double x) {
		handX.set(x);
	}
	
	public void setY(double y) {
		handY.set(y);
	}
	
	public SimpleDoubleProperty getObservableX() {
		return handX;
	}
	
	public SimpleDoubleProperty getObservableY() {
		return handY;
	}
}
