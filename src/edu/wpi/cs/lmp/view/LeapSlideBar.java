package edu.wpi.cs.lmp.view;

import javafx.animation.TranslateTransition;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class LeapSlideBar extends VBox {
	
	private static final double THUMBNAIL_WIDTH = 0.10;
	private static final double THUMBNAIL_HEIGHT = 0.10;

	private int numSlides;
	private int currentSlide;

	private HBox slideLabelContainer;
	private HBox slideControlContainer;
	private HBox slideContainer;
	private Button forward;
	private Button back;
	private Label slideLabel;

	private TranslateTransition animationIn;
	private TranslateTransition animationOut;

	public LeapSlideBar() {
		// Do all the VBox standard stuff and set the sizes
		super();
		this.setPrefHeight(Control.USE_COMPUTED_SIZE);
		this.setMaxHeight(Control.USE_PREF_SIZE);
		this.getStyleClass().add("leap-tool-bar");

		// Instantiate the slide label, this is a placeholder to get the point
		// across but labels should be the only thing necessary in the final
		// version of this UI bar
		slideLabel = new Label("Slide: 0 / 0");
		slideLabel.setTextFill(Color.WHITE);
		slideLabelContainer = new HBox();
		slideLabelContainer.getChildren().add(slideLabel);
		this.getChildren().add(slideLabelContainer);
		
		Separator firstDivider = new Separator(Orientation.HORIZONTAL);
		firstDivider.getStyleClass().add("slide-seperator-vertical");

		// Divider, I should style something better in a stylesheet
		this.getChildren().add(firstDivider);

		// The actual slide stuff:
		// slideControlContainer: contains all of the slides, the back button
		// and
		// the forward button all in one horizontal row
		// slideContainer: Should contain thumbnail of the slides for selection
		// forward and back should be self explanatory
		slideControlContainer = new HBox();
		slideContainer = new HBox();
		slideContainer.getStyleClass().add("thumbnail-container");
		forward = new Button(">");
		forward.setPrefHeight(Screen.getPrimary().getVisualBounds().getWidth()*THUMBNAIL_HEIGHT);
		back = new Button("<");
		back.setPrefHeight(Screen.getPrimary().getVisualBounds().getWidth()*THUMBNAIL_HEIGHT);

		slideControlContainer.getChildren().addAll(back, slideContainer,
				forward);
		HBox.setHgrow(slideContainer, Priority.ALWAYS);

		this.getChildren().add(slideControlContainer);

		this.getChildren().add(new Separator(Orientation.HORIZONTAL));
		
		// Example slide thumbnail
		// Experimenting with thumbnail sizes
		ImageView image = new ImageView("file:example-thumb.gif");
		Button thumbnail = new Button();
		image.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth()*THUMBNAIL_WIDTH);
		image.setFitHeight(Screen.getPrimary().getVisualBounds().getWidth()*THUMBNAIL_HEIGHT);
		thumbnail.setGraphic(image);
		slideContainer.getChildren().add(thumbnail);

	}

}
