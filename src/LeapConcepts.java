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
 */
public class LeapConcepts extends Application {
    
    private DoubleProperty centerY = new SimpleDoubleProperty(0);
    private DoubleProperty centerX = new SimpleDoubleProperty(0);
    private DoubleProperty radius = new SimpleDoubleProperty(10);
    
    public DoubleProperty centerX() {
        return centerX;
    }
    
    public DoubleProperty centerY() {
        return centerY;
    }
    
    public DoubleProperty radius () {
        return radius;
    }
    
    @Override
    public void start(Stage primaryStage) {
        Circle circle = new Circle(20);
        circle.setFill(Color.GREEN);
        circle.translateXProperty().bind(centerX);
        circle.translateYProperty().bind(centerY);
        circle.radiusProperty().bind(radius);
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(circle);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
         c = new Controller();
         listener = new MyLeapListener(this);
        c.addListener(listener);
    }
    Listener listener;
 Controller c;
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
