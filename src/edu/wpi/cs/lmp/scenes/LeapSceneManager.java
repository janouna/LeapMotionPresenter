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
package edu.wpi.cs.lmp.scenes;

import java.io.File;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import edu.wpi.cs.lmp.state.PresenterState;
import edu.wpi.cs.lmp.state.PresenterStateObservable;
import edu.wpi.cs.lmp.view.radialmenu.RadialOptionsMenu;

/**
 * The manager for all the scene objects.  Holds the current scene, and facilitates adding scenes and changing the current scene.
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class LeapSceneManager {

	private static final LeapSceneManager INSTANCE = new LeapSceneManager();
	private File projectDir;
	private final IntegerProperty currentScene;
	private final ListProperty<LeapScene> scenes;
	private Pane root = null;

	private LeapSceneManager() {
		currentScene = new SimpleIntegerProperty(0);
		final ObservableList<LeapScene> observableList = FXCollections
				.observableArrayList();
		scenes = new SimpleListProperty<LeapScene>(observableList);
		projectDir = null;
		scenes.add(new LeapScene());
	}

	public static LeapSceneManager getInstance() {
		return INSTANCE;
	}

	public LeapScene getCurrentScene() {
		return scenes.get(currentScene.intValue());
	}

	public LeapScene setCurrentScene(int i) {
		if (i >= scenes.size()) {
			PresenterStateObservable.getInstance().set(PresenterState.CREATING);
			return null;
		}
		if (i >= 0) {
			currentScene.set(i);
			return getCurrentScene();
		} else {
			return null;
		}
	}

	public List<LeapScene> getAllScenes() {
		return scenes.getValue();
	}

	public int getCurrentSceneNumber() {
		return currentScene.intValue();
	}

	public void addScene() {
		scenes.add(new LeapScene());
	}

	public void addScene(int i) {
		scenes.add(i, new LeapScene());
	}

	public void removeScene(int i) {
		scenes.remove(i);
	}
	
	public void removeAllScenes() {
		scenes.clear();
		currentScene.set(-1);
	}

	public void moveScene(int from, int to) {
		final LeapScene temp = scenes.get(from);
		removeScene(from);
		scenes.add(to, temp);
	}

	public ListProperty<LeapScene> getScenesProperty() {
		return scenes;
	}

	public IntegerProperty getCurrentSceneProperty() {
		return currentScene;
	}
	
	public void setProjectDirectory(File path) {
		this.projectDir = path;
	}
	
	public File getProjectDirectory() {
		return projectDir;
	}
	
	public void addRoot(Pane root) {
		this.root = root;
	}
	
	public Pane getRoot() {
		return root;
	}
	
	public void addRadial(RadialOptionsMenu radial) {
		if (!root.getChildren().contains(radial)) {
			root.getChildren().add(radial);
		}
	}

}
