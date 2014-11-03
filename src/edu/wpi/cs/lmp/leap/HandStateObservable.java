package edu.wpi.cs.lmp.leap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HandStateObservable {
	private static HandStateObservable INSTANCE = new HandStateObservable();
	private ObjectProperty<HandState> handState = new SimpleObjectProperty<>();
	
	private HandStateObservable(){
		handState.set(HandState.GONE);
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
}
