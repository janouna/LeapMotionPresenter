package edu.wpi.cs.lmp.scenes;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LeapSceneManager {

	private static final LeapSceneManager INSTANCE = new LeapSceneManager();
	private final IntegerProperty currentScene;
	private final ListProperty<LeapScene> scenes;

	private LeapSceneManager() {
		currentScene = new SimpleIntegerProperty(0);
		final ObservableList<LeapScene> observableList = FXCollections
				.observableArrayList();
		scenes = new SimpleListProperty<LeapScene>(observableList);
		scenes.add(new LeapScene());
	}

	public static LeapSceneManager getInstance() {
		return INSTANCE;
	}

	public LeapScene getCurrentScene() {
		return scenes.get(currentScene.intValue());
	}

	public LeapScene setCurrentScene(int i) {
		if (i < scenes.size() && i >= 0) {
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

}
