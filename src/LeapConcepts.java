import java.util.ArrayList;
import java.util.List;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * 
 * @author johan
 * Modified to support controlling multiple circles
 */
public class LeapConcepts extends Application {

	private int numCircles;
	private List<DoubleProperty> centerY = new ArrayList<DoubleProperty>();
	private List<DoubleProperty> centerX = new ArrayList<DoubleProperty>();
	private List<DoubleProperty> radius = new ArrayList<DoubleProperty>();

	Listener listener;
	Controller c;
	private StackPane root;

	public DoubleProperty centerX(int i) {
		return centerX.get(i);
	}

	public DoubleProperty centerY(int i) {
		return centerY.get(i);
	}

	public DoubleProperty radius(int i) {
		return radius.get(i);
	}

	public int countCircles() {
		return numCircles;
	}

	@Override
	public void start(Stage primaryStage) {
		numCircles = 0;
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World!");
			}
		});

		root = new StackPane();

		Scene scene = new Scene(root, 1000, 500);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
		c = new Controller();
		listener = new MyLeapListener(this);
		c.addListener(listener);
	}

	public void addCircle() {
		centerX.add(new SimpleDoubleProperty(0));
		centerY.add(new SimpleDoubleProperty(0));
		radius.add(new SimpleDoubleProperty(10));
		Circle newCircle = new Circle(20);
		newCircle.setFill(Color.rgb((int) (Math.random() * 255),
				(int) (Math.random() * 255), (int) (Math.random() * 255)));
		newCircle.translateXProperty().bind(centerX.get(numCircles));
		newCircle.translateYProperty().bind(centerY.get(numCircles));
		newCircle.radiusProperty().bind(radius.get(numCircles));
		root.getChildren().add(newCircle);
		numCircles++;
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
