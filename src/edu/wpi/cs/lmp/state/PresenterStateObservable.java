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
