package edu.wpi.cs.lmp.leap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HandStateObservable {
	public final HandStateObservable INSTANCE = new HandStateObservable();
	private ObjectProperty<HandState> handState = new SimpleObjectProperty<>();
	
	private HandStateObservable(){
		handState.set(HandState.GONE);
	}
	
	public void setHandState(HandState state){
		handState.set(state);
	}
	
	public HandState getHandState() {
		return handState.get();
	}
}
