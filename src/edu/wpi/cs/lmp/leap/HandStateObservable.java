/*******************************************************************************
* This file is part of James Anouna and Johnny Hernandez's MQP.
* Leap Motion Presenter
* Advised by Professor Gary Pollice
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* James Anouna
* Johnny Hernandez
*******************************************************************************/
package edu.wpi.cs.lmp.leap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HandStateObservable {
	private static final HandStateObservable INSTANCE = new HandStateObservable();
	private final ObjectProperty<HandState> handState = new SimpleObjectProperty<>();
	private final SimpleDoubleProperty handX = new SimpleDoubleProperty();
	private final SimpleDoubleProperty handY = new SimpleDoubleProperty();

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
