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
		LeapScene scene;
		try {
			scene = scenes.get(currentScene.intValue());
		} catch(IndexOutOfBoundsException e) {
			scene = null;
		}
		
		return scene;
	}

	/**
	 * Sets the current scene
	 * @param i The index of the new current scene
	 * @return The new current scene
	 */
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

	/**
	 * Adds a new empty scene
	 */
	public void addScene() {
		scenes.add(new LeapScene());
	}

	/**
	 * Adds a new empty scene
	 * @param i The index of the new scene in the scene list
	 */
	public void addScene(int i) {
		scenes.add(i, new LeapScene());
	}

	/**
	 * Removes the given scene
	 * @param i The index of the scene to remove
	 */
	public void removeScene(int i) {
		scenes.remove(i);
	}
	
	/**
	 * Removes all scenes
	 */
	public void removeAllScenes() {
		scenes.clear();
		currentScene.set(0);
	}

	/**
	 * Moves a scene to another location in the scene list
	 * @param from The original location of the scene
	 * @param to The new location of the scene
	 */
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
	
	/**
	 * Sets the root Pane object
	 * @param root The root Pane object
	 */
	public void addRoot(Pane root) {
		this.root = root;
	}
	
	public Pane getRoot() {
		return root;
	}
	
	/**
	 * Adds a RadialOptionsMenu to the scene
	 * @param radial The RadialOptionsMenu to add
	 */
	public void addRadial(RadialOptionsMenu radial) {
		if (!root.getChildren().contains(radial)) {
			root.getChildren().add(radial);
		}
	}

}
