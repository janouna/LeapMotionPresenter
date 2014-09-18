import java.util.ArrayList;
import java.util.List;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

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
	
	private List<DoubleProperty> centerY = new ArrayList<DoubleProperty>();
	private List<DoubleProperty> centerX = new ArrayList<DoubleProperty>();
	private List<DoubleProperty> radius = new ArrayList<DoubleProperty>();
	private List<Circle> circleObjects = new ArrayList<Circle>();

	MyLeapListener listener;
	Controller c;
	private StackPane root;
	private Button btn;

	public DoubleProperty centerX(int i) {
		return centerX.get(i);
	}

	public DoubleProperty centerY(int i) {
		return centerY.get(i);
	}

	public DoubleProperty radius(int i) {
		return radius.get(i);
	}
	
	public boolean overButton(int i) {
		return btn.contains(circleObjects.get(i).getTranslateX(), circleObjects.get(i).getTranslateY());
	}

	public int countCircles() {
		return circleObjects.size();
	}

	@Override
	public void start(Stage primaryStage) {
		btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World!");
			}
		});

		root = new StackPane();
		
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 1000, 500);

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
		c.addListener(listener);
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
		root.getChildren().add(newCircle);
		circleObjects.add(newCircle);
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
