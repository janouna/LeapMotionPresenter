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
package edu.wpi.cs.lmp.view;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;
import edu.wpi.cs.lmp.scenes.LeapScene;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;
import edu.wpi.cs.lmp.state.PresenterState;
import edu.wpi.cs.lmp.state.PresenterStateObservable;

/**
 * The main class for the leap scene bar. This bar is responsible for displaying
 * the scenes in the presentation.
 * 
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class LeapSceneBar extends VBox {

	private static final double THUMBNAIL_WIDTH = 0.10;
	private static final double THUMBNAIL_HEIGHT = 0.10;
	// Number of seconds for slide in/out animations for the toolbar
	private static final float ANIMATION_TIME = 0.5f;

	private final LeapSceneBar instance;

	private int numScenes;
	private int currentScene;

	Separator divider1, divider2;
	private HBox sceneControlContainer;
	private VBox sceneGroupContainer;
	private HBox sceneLabelContainer;
	private HBox sceneContainer;
	private Button forward;
	private Button back;
	private Label sceneLabel;
	private ScrollPane scrollPane;

	private TranslateTransition animationIn;
	private TranslateTransition animationOut;
	private boolean isAnimating;

	private boolean isActive;

	/**
	 * Constructs the LeapSceneBar
	 */
	public LeapSceneBar() {
		// Do all the VBox standard stuff and set the sizes
		super();
		instance = this;

		isActive = true;

		// Positioning
		this.setPrefHeight(Control.USE_COMPUTED_SIZE);
		this.setMaxHeight(Control.USE_PREF_SIZE);
		this.getStyleClass().add("leap-tool-bar");

		setupBarChildren();
		setupListeners();
		setupAnimations();

		createObservers();
		updateScenes();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				final double screen_height = Screen.getPrimary().getBounds()
						.getHeight();
				instance.setTranslateY(screen_height);
				animationIn.setFromY(screen_height);
				animationIn.setToY(screen_height
						- instance.getLayoutBounds().getHeight());
				animationOut.setFromY(screen_height
						- instance.getLayoutBounds().getHeight());
				animationOut.setToY(screen_height);
			}
		});
	}

	/**
	 * This method creates the necessary JavaFX elements found in the
	 * LeapSceneBar. All visual elements and dividers are created and populated
	 * here.
	 */
	private void setupBarChildren() {
		// The actual scene stuff:
		// sceneControlContainer: contains all of the scenes, the back button
		// and
		// the forward button all in one horizontal row
		// sceneContainer: Should contain thumbnail of the scenes for selection
		// forward and back should be self explanatory
		sceneControlContainer = new HBox();
		sceneGroupContainer = new VBox();
		scrollPane = new ScrollPane();

		// Divider, I should style something better in a stylesheet
		divider1 = new Separator(Orientation.HORIZONTAL);
		divider1.getStyleClass().add("slide-seperator-vertical");
		divider2 = new Separator(Orientation.HORIZONTAL);
		divider2.getStyleClass().add("slide-seperator-vertical");

		// Instantiate the scene label, this is a placeholder to get the point
		// across but labels should be the only thing necessary in the final
		// version of this UI bar
		sceneLabel = new Label("scene: 0 / 0");
		sceneLabel.setTextFill(Color.WHITE);
		sceneLabelContainer = new HBox();
		sceneLabelContainer.getChildren().add(sceneLabel);
		this.getChildren().add(sceneLabelContainer);

		sceneContainer = new HBox();
		sceneContainer.getStyleClass().add("thumbnail-container");
		sceneGroupContainer.getChildren().add(sceneContainer);

		forward = new Button(">");
		forward.setPrefHeight(Screen.getPrimary().getBounds().getWidth()
				* THUMBNAIL_HEIGHT);
		forward.setTextOverrun(OverrunStyle.CLIP);
		back = new Button("<");
		back.setPrefHeight(Screen.getPrimary().getBounds().getWidth()
				* THUMBNAIL_HEIGHT);
		back.setTextOverrun(OverrunStyle.CLIP);
		sceneControlContainer.setPrefWidth(Screen.getPrimary().getBounds()
				.getWidth());

		scrollPane.setContent(sceneGroupContainer);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		final double scrollWidth = Screen.getPrimary().getBounds().getWidth()
				- divider1.getLayoutBounds().getWidth()
				- divider2.getLayoutBounds().getWidth()
				- forward.getLayoutBounds().getWidth()
				- back.getLayoutBounds().getWidth();
		scrollPane.setPrefViewportWidth(scrollWidth);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

		sceneControlContainer.getChildren().addAll(divider1, back, scrollPane,
				forward, divider2);
		HBox.setHgrow(sceneContainer, Priority.ALWAYS);

		this.getChildren().add(sceneControlContainer);
	}

	/**
	 * This sets the listeners for the bar as a whole to react to. Currently for
	 * hiding when the cursor is away and coming back to the scene when moused
	 * over.
	 */
	private void setupListeners() {
		// Instantiate main bar and behavior
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (isActive) {
					instance.transitionIn();
				}
			}
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (isActive) {
					instance.transitionOut();
				}
			}
		});
	}

	/**
	 * This creates the animation objects that are used for transitioning in and
	 * out of the scene when the bar is needed/not needed.
	 */
	private void setupAnimations() {
		animationIn = new TranslateTransition();
		animationIn.setDuration(new Duration(ANIMATION_TIME * 1000));
		animationIn.setNode(this);
		animationIn.setAutoReverse(true);
		animationIn.setInterpolator(Interpolator.EASE_OUT);
		animationIn.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				isAnimating = false;
			}
		});

		animationOut = new TranslateTransition();
		animationOut.setDuration(new Duration(ANIMATION_TIME * 1000));
		animationOut.setNode(this);
		animationOut.setAutoReverse(true);
		animationOut.setInterpolator(Interpolator.EASE_OUT);
		animationOut.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				isAnimating = false;
				instance.setOpacity(0);
			}
		});
	}

	/**
	 * This creates the observers that will change the LeapSceneBar when slides
	 * are changed/added/removed
	 */
	private void createObservers() {
		// Observer for the list of scenes, any changes made and the onChange is
		// fired
		LeapSceneManager.getInstance().getScenesProperty()
				.addListener(new ListChangeListener<LeapScene>() {
					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends LeapScene> changes) {
						updateScenes();
					}
				});

		LeapSceneManager.getInstance().getCurrentSceneProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> arg0,
							Number arg1, Number arg2) {
						updateScenes();
					}
				});

		PresenterStateObservable.getInstance().getPresenterState()
				.addListener(new ChangeListener<PresenterState>() {

					@Override
					public void changed(
							ObservableValue<? extends PresenterState> observable,
							PresenterState oldValue, PresenterState newValue) {
						if (newValue == PresenterState.PRESENTING) {
							instance.setDisabled(true);
							isActive = false;
						} else {
							instance.setDisabled(false);
							isActive = true;
						}
					}

				});
	}

	/**
	 * This method will be called when an update to the view of the LeapSceneBar
	 * is needed. This will pull all the Scenes and create thumbnails for each
	 * one. Then these thumbnails are added to the bar.
	 */
	private void updateScenes() {
		// Ideally have a method to update the contents of the scene bar
		// onChanged gives variable that can be looped through to see what
		// changes were made if necessary
		// Update the scene count

		final List<LeapScene> sceneList = LeapSceneManager.getInstance()
				.getAllScenes();
		numScenes = sceneList.size();
		currentScene = LeapSceneManager.getInstance().getCurrentSceneNumber();
		sceneContainer.getChildren().clear();
		for (int i = 0; i < numScenes; i++) {
			ImageView image = new ImageView(sceneList.get(i).snapshot(
					new SnapshotParameters(), null));

			Button thumbnail = new Button();
			image.setFitWidth(Screen.getPrimary().getBounds().getWidth()
					* THUMBNAIL_WIDTH);
			image.setFitHeight(Screen.getPrimary().getBounds().getWidth()
					* THUMBNAIL_HEIGHT);
			thumbnail.setGraphic(image);
			thumbnail.setOnMouseClicked(new SceneButtonHandler(i));

			sceneContainer.getChildren().add(thumbnail);
		}

		sceneLabel.setText("Scene: " + (currentScene + 1) + " / " + numScenes);
	}

	/**
	 * Method that will cause the bar to transition in to view
	 */
	public void transitionIn() {
		if (!isAnimating) {
			instance.setOpacity(1);
			animationIn.play();
			isAnimating = true;
		}
	}

	/**
	 * Method that will cause the bar to transition out of view
	 */
	public void transitionOut() {
		// if (!isAnimating) {
		animationOut.play();
		isAnimating = true;
		// }
	}

	/**
	 * Obtains the button for a scene object in the LeapSceneBar
	 * @param num The index into the list of scene buttons. Starts at 0.
	 * @return The Button in the LeapSceneBar that represents a particular scene in the presentation
	 */
	public Button getScene(int num) {
		return (Button) sceneContainer.getChildren().get(num);
	}

	/**
	 * Obtains all the buttons for a scene object in the LeapSceneBar
	 * @return A list of Button for every scene in the LeapSceneBar
	 */
	public List<Button> getAllScenes() {
		final List<Button> buttons = new ArrayList<Button>();
		for (int i = 0; i < sceneContainer.getChildren().size(); i++) {
			buttons.add((Button) sceneContainer.getChildren().get(i));
		}
		return buttons;
	}

	/**
	 * Facilitates selecting current slide by clicking on a slide in the tool
	 * bar.
	 * 
	 * @author James Anouna
	 * @author Johnny Hernandez
	 *
	 */
	private class SceneButtonHandler implements EventHandler<MouseEvent> {
		int position;

		private SceneButtonHandler(int pos) {
			super();
			position = pos;
		}

		@Override
		public void handle(MouseEvent arg0) {
			System.out.println("Scene " + position + " selected");
			LeapSceneManager.getInstance().setCurrentScene(position);
			updateScenes();
		}
	}
}
