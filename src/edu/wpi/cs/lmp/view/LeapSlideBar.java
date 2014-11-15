package edu.wpi.cs.lmp.view;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.lmp.objects.ObjectType;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;

public class LeapSlideBar extends VBox {

	private static final double THUMBNAIL_WIDTH = 0.10;
	private static final double THUMBNAIL_HEIGHT = 0.10;
	// Number of seconds for slide in/out animations for the toolbar
	private static final int ANIMATION_TIME = 1;

	private LeapSlideBar instance;

	private int numSlides;
	private int currentSlide;
	
	private HBox slideControlContainer;
	private VBox slideGroupContainer;
	private HBox slideLabelContainer;
	private HBox slideContainer;
	private Button forward;
	private Button back;
	private Label slideLabel;

	private TranslateTransition animationIn;
	private TranslateTransition animationOut;
	private boolean isAnimating;
	private boolean isHidden;

	public LeapSlideBar() {
		// Do all the VBox standard stuff and set the sizes
		super();
		instance = this;
		this.setPrefHeight(Control.USE_COMPUTED_SIZE);
		this.setMaxHeight(Control.USE_PREF_SIZE);
		this.getStyleClass().add("leap-tool-bar");

		// The actual slide stuff:
		// slideControlContainer: contains all of the slides, the back button
		// and
		// the forward button all in one horizontal row
		// slideContainer: Should contain thumbnail of the slides for selection
		// forward and back should be self explanatory
		slideControlContainer = new HBox();
		slideGroupContainer = new VBox();
		
		Separator firstDivider = new Separator(Orientation.HORIZONTAL);
		firstDivider.getStyleClass().add("slide-seperator-vertical");

		// Divider, I should style something better in a stylesheet
		slideControlContainer.getChildren().add(firstDivider);

		// Instantiate the slide label, this is a placeholder to get the point
		// across but labels should be the only thing necessary in the final
		// version of this UI bar
		slideLabel = new Label("Slide: 0 / 0");
		slideLabel.setTextFill(Color.WHITE);
		slideLabelContainer = new HBox();
		slideLabelContainer.getChildren().add(slideLabel);
		slideGroupContainer.getChildren().add(slideLabelContainer);

		slideContainer = new HBox();
		slideContainer.getStyleClass().add("thumbnail-container");
		slideGroupContainer.getChildren().add(slideContainer);
		
		forward = new Button(">");
		forward.setPrefHeight(Screen.getPrimary().getVisualBounds().getWidth()*THUMBNAIL_HEIGHT);
		back = new Button("<");
		back.setPrefHeight(Screen.getPrimary().getVisualBounds().getWidth()*THUMBNAIL_HEIGHT);
		slideControlContainer.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());

		slideControlContainer.getChildren().addAll(back, slideGroupContainer,
				forward);
		HBox.setHgrow(slideContainer, Priority.ALWAYS);

		this.getChildren().add(slideControlContainer);

		this.getChildren().add(new Separator(Orientation.HORIZONTAL));

		// Instantiate main bar and behavior
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (instance.isHidden()) {
					instance.transitionIn();
				} else {
					instance.transitionOut();
					//instance.unselectAllButton();
				}
			}
		});

		// TODO Set button controls

		animationIn = TranslateTransitionBuilder.create()
				.duration(new Duration(ANIMATION_TIME * 1000)).node(this).fromY(-2).toY(0)
				.autoReverse(true).interpolator(Interpolator.EASE_OUT).build();

		animationIn.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				isAnimating = false;
				instance.isHidden = false;
			}
		});

		animationOut = TranslateTransitionBuilder.create()
				.duration(new Duration(ANIMATION_TIME * 1000)).node(this).fromY(0).toY(-100)
				.autoReverse(true).interpolator(Interpolator.EASE_OUT).build();

		animationOut.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				isAnimating = false;
				instance.isHidden = true;
				instance.setOpacity(0);
			}
		});

		// TODO Remove
		// Example slide thumbnail
		// Experimenting with thumbnail sizes
		ImageView image = new ImageView("file:example-thumb.gif");
		Button thumbnail = new Button();
		image.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth()*THUMBNAIL_WIDTH);
		image.setFitHeight(Screen.getPrimary().getVisualBounds().getWidth()*THUMBNAIL_HEIGHT);
		thumbnail.setGraphic(image);
		slideContainer.getChildren().add(thumbnail);
	}

	public void transitionIn() {
		if (!isAnimating) {
			instance.setOpacity(1);
			animationIn.play();
			isAnimating = true;
		}
	}

	public void transitionOut() {
		if (!isAnimating) {
			animationOut.play();
			isAnimating = true;
		}
	}

	public Button getSlide(int num) {
		return (Button) slideContainer.getChildren().get(num);
	}

	public List<Button> getAllSlides() {
		List<Button> buttons = new ArrayList<Button>();
		for (int i = 0; i < slideContainer.getChildren().size(); i++) {
			buttons.add((Button) slideContainer.getChildren().get(i));
		}
		return buttons;
	}

	//	public void unselectAllButton() {
	//		for (int i = 0; i < buttonBar.getChildren().size(); i++) {
	//			ToggleButton thisButton = (ToggleButton) buttonBar.getChildren()
	//					.get(i);
	//			System.out.println("BUTTON OFF: " + thisButton.getText());
	//			thisButton.setSelected(false);
	//		}
	//
	//	}

	public boolean isHidden() {
		return isHidden;
	}

}
