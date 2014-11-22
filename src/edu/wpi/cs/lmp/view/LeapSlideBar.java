package edu.wpi.cs.lmp.view;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.lmp.slides.Slide;
import edu.wpi.cs.lmp.slides.SlideManager;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
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
	private static final float ANIMATION_TIME = 0.5f;

	private LeapSlideBar instance;

	private int numSlides;
	private int currentSlide;

	Separator divider1, divider2;
	private HBox slideControlContainer;
	private VBox slideGroupContainer;
	private HBox slideLabelContainer;
	private HBox slideContainer;
	private Button forward;
	private Button back;
	private Label slideLabel;
	private ScrollPane scrollPane;

	private TranslateTransition animationIn;
	private TranslateTransition animationOut;
	private boolean isAnimating;

	public LeapSlideBar() {
		// TODO Slide buttons
		// TODO Display Slides properly

		// Do all the VBox standard stuff and set the sizes
		super();
		instance = this;

		// Positioning
		this.setPrefHeight(Control.USE_COMPUTED_SIZE);
		this.setMaxHeight(Control.USE_PREF_SIZE);
		this.getStyleClass().add("leap-tool-bar");

		setupBarChildren();
		setupListeners();
		setupAnimations();

		// TODO Set button controls

		slideManagerObservers();
		
		updateSlides();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double screen_height = Screen.getPrimary().getBounds().getHeight();
				instance.setTranslateY(screen_height);
				animationIn.setFromY(screen_height);
				animationIn.setToY(screen_height - instance.getLayoutBounds().getHeight());
				animationOut.setFromY(screen_height - instance.getLayoutBounds().getHeight());
				animationOut.setToY(screen_height);
			}
		});
	}

	private void setupBarChildren() {
		// The actual slide stuff:
		// slideControlContainer: contains all of the slides, the back button
		// and
		// the forward button all in one horizontal row
		// slideContainer: Should contain thumbnail of the slides for selection
		// forward and back should be self explanatory
		slideControlContainer = new HBox();
		slideGroupContainer = new VBox();
		scrollPane = new ScrollPane();
		
		// Divider, I should style something better in a stylesheet
		divider1 = new Separator(Orientation.HORIZONTAL);
		divider1.getStyleClass().add("slide-seperator-vertical");
		divider2 = new Separator(Orientation.HORIZONTAL);
		divider2.getStyleClass().add("slide-seperator-vertical");

		// Instantiate the slide label, this is a placeholder to get the point
		// across but labels should be the only thing necessary in the final
		// version of this UI bar
		slideLabel = new Label("Slide: 0 / 0");
		slideLabel.setTextFill(Color.WHITE);
		slideLabelContainer = new HBox();
		slideLabelContainer.getChildren().add(slideLabel);
		this.getChildren().add(slideLabelContainer);

		slideContainer = new HBox();
		slideContainer.getStyleClass().add("thumbnail-container");
		slideGroupContainer.getChildren().add(slideContainer);

		forward = new Button(">");
		forward.setPrefHeight(Screen.getPrimary().getBounds().getWidth()*THUMBNAIL_HEIGHT);
		forward.setTextOverrun(OverrunStyle.CLIP);
		back = new Button("<");
		back.setPrefHeight(Screen.getPrimary().getBounds().getWidth()*THUMBNAIL_HEIGHT);
		back.setTextOverrun(OverrunStyle.CLIP);
		slideControlContainer.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
		
		scrollPane.setContent(slideGroupContainer);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		double scrollWidth = Screen.getPrimary().getBounds().getWidth() - divider1.getLayoutBounds().getWidth() - divider2.getLayoutBounds().getWidth()
				- forward.getLayoutBounds().getWidth() - back.getLayoutBounds().getWidth();
		scrollPane.setPrefViewportWidth(scrollWidth);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

		slideControlContainer.getChildren().addAll(divider1, back, scrollPane,
				forward, divider2);
		HBox.setHgrow(slideContainer, Priority.ALWAYS);

		this.getChildren().add(slideControlContainer);
	}
	private void setupListeners() {
		// Instantiate main bar and behavior
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				instance.transitionIn();
			}
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				instance.transitionOut();
			}
		});
	}
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

	private void slideManagerObservers() {
		// Observer for the list of slides, any changes made and the onChange is fired
		SlideManager.getInstance().getSlidesProperty().addListener(new ListChangeListener<Slide>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Slide> changes) {
				updateSlides();
			}
		});
		
		SlideManager.getInstance().getCurrentSlideProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				updateSlides();
			}
		});
	}
	private void updateSlides(){
		// Ideally have a method to update the contents of the slide bar
		// onChanged gives variable that can be looped through to see what changes were made if necessary
		// Update the slide count
		
		List<Slide> slideList = SlideManager.getInstance().getAllSlides();
		numSlides = slideList.size();
		currentSlide = SlideManager.getInstance().getCurrentSlideNumber();
		slideContainer.getChildren().clear();
		for(int i = 0; i < numSlides; i++){
			ImageView image = new ImageView(slideList.get(i).snapshot(new SnapshotParameters(), null));
			
			Button thumbnail = new Button();
			image.setFitWidth(Screen.getPrimary().getBounds().getWidth()*THUMBNAIL_WIDTH);
			image.setFitHeight(Screen.getPrimary().getBounds().getWidth()*THUMBNAIL_HEIGHT);
			thumbnail.setGraphic(image);
			thumbnail.setOnMouseClicked(new SlideButtonHandler(i));
			
			slideContainer.getChildren().add(thumbnail);
		}
		
		slideLabel.setText("Slide: " + (currentSlide + 1) + " / " + numSlides);
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
	
	private class SlideButtonHandler implements EventHandler<MouseEvent>{
		int position;
		
		public SlideButtonHandler(int pos){
			super();
			position = pos;
		}
		
		@Override
		public void handle(MouseEvent arg0) {
			System.out.println("Slide " + position + " selected");
			SlideManager.getInstance().setCurrentSlide(position);
			updateSlides();
		}
	}
}
