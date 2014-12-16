package edu.wpi.cs.lmp.state;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PresenterStateObservable {
	private static final PresenterStateObservable INSTANCE = new PresenterStateObservable();
	private final ObjectProperty<PresenterState> presenterState = new SimpleObjectProperty<>();

	private PresenterStateObservable(){
		presenterState.set(PresenterState.CREATING);
	}
	
	public static PresenterStateObservable getInstance() {
		return INSTANCE;
	}
	
	public void set(PresenterState state) {
		presenterState.set(state);
	}
	
	public PresenterState get() {
		return presenterState.get();
	}
	
	public ObjectProperty<PresenterState> getPresenterState() {
		return presenterState;
	}
}
