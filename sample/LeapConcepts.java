import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import com.leapmotion.leap.Controller;

/**
 * 
 * @author johan
 * Modified to support controlling multiple circles
 */
public class LeapConcepts extends Application {
	
	// These control the position of the fingers circles
	private List<DoubleProperty> centerY = new ArrayList<DoubleProperty>();
	private List<DoubleProperty> centerX = new ArrayList<DoubleProperty>();
	private List<DoubleProperty> radius = new ArrayList<DoubleProperty>();
	// List of actual circle objects
	private List<Circle> circleObjects = new ArrayList<Circle>();
	
	// These control the position of the palm
	private DoubleProperty palmX = new SimpleDoubleProperty(10);
	private DoubleProperty palmY = new SimpleDoubleProperty(10);
	private DoubleProperty palmZ = new SimpleDoubleProperty(0);
	// Palm cursor rectangle
	private Rectangle rectangleObject;
	
	// For resizing pollice
	private DoubleProperty imgWidth;
	private DoubleProperty imgHeight;

	MyLeapListener listener;
	ResizeListener resizer;
	Controller c;
	private StackPane root;
	private Button btn;
	private ImageView img;

	public DoubleProperty palmX() {
		return palmX;
	}
	
	public DoubleProperty palmY() {
		return palmY;
	}
	
	public DoubleProperty palmZ() {
		return palmZ;
	}
	
	public DoubleProperty imgWidth() {
		return imgWidth;
	}
	
	public DoubleProperty imgHeight() {
		return imgHeight;
	}
	
	public DoubleProperty centerX(int i) {
		return centerX.get(i);
	}

	public DoubleProperty centerY(int i) {
		return centerY.get(i);
	}

	public DoubleProperty radius(int i) {
		return radius.get(i);
	}
	
	public boolean dragImage(double handX, double handY) {
		double x = img.getTranslateX();
		double y = img.getTranslateY();
		double width = imgWidth.doubleValue();
		double height = imgHeight.doubleValue();
		
		return (handX > x-(width/2) && handX < x+(width/2)) && (handY > y-(height/2) && handY < y+(height/2));		
	}
	
	public void resizeImage(double percentageChange) {
		imgWidth.set((imgWidth.doubleValue()*percentageChange)/100);
		imgHeight.set((imgHeight.doubleValue()*percentageChange)/100);
	}
	
	public void setPolliceBind() {
		img.translateXProperty().bind(palmX);
		img.translateYProperty().bind(palmY);
	}
	
	public void setPolliceUnBind() {
		img.translateXProperty().unbind();
		img.translateYProperty().unbind();
	}

	public int countCircles() {
		return circleObjects.size();
	}

	@Override
	public void start(Stage primaryStage) {
		btn = new Button();
		img = new ImageView("file:gary.JPG");
		// Properly set height and width
		imgWidth = new SimpleDoubleProperty(img.getImage().getWidth());
		imgHeight = new SimpleDoubleProperty(img.getImage().getHeight());
		// Now bind it to the image view and use it later to resize
		img.fitWidthProperty().bind(imgWidth);
		img.fitHeightProperty().bind(imgHeight);
		
		rectangleObject = new Rectangle(40, 40);
		rectangleObject.translateXProperty().bind(palmX);
		rectangleObject.translateYProperty().bind(palmY);
		rectangleObject.setFill(Color.rgb((int) (Math.random() * 255),
				(int) (Math.random() * 255), (int) (Math.random() * 255)));
		rectangleObject.setOpacity(0.7);
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World!");
			}
		});

		root = new StackPane();
		
		root.getChildren().add(img);
		root.getChildren().add(rectangleObject);

		Scene scene = new Scene(root, MyLeapListener.SCREEN_WIDTH, MyLeapListener.SCREEN_HEIGHT);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
		c = new Controller();
		listener = new MyLeapListener(this);
        listener.keyTapProperty().addListener(new ChangeListener<Boolean>(){
            @Override public void changed(ObservableValue<? extends Boolean> ov, Boolean t, final Boolean t1) {
                if(t1.booleanValue()){
                    Platform.runLater(new Runnable(){
                        @Override public void run() {
                            btn.fire();
                        }
                    });
                }
            }
        });
        resizer = new ResizeListener(this);
		c.addListener(listener);
		c.addListener(resizer);
	}

	public void addCircle() {
		centerX.add(new SimpleDoubleProperty(0));
		centerY.add(new SimpleDoubleProperty(0));
		radius.add(new SimpleDoubleProperty(10));
		Circle newCircle = new Circle(20);
		newCircle.setFill(Color.rgb((int) (Math.random() * 255),
				(int) (Math.random() * 255), (int) (Math.random() * 255)));
		newCircle.translateXProperty().bind(centerX.get(countCircles()));
		newCircle.translateYProperty().bind(centerY.get(countCircles()));
		newCircle.radiusProperty().bind(radius.get(countCircles()));
		newCircle.setOpacity(0.4);
		root.getChildren().add(newCircle);
		circleObjects.add(newCircle);
	}
	
	public void removeCircle() {
		root.getChildren().remove(circleObjects.get(circleObjects.size()-1));
		circleObjects.remove(circleObjects.size()-1);
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
