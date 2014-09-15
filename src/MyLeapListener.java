import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Screen;
import com.leapmotion.leap.Vector;
import java.util.UUID;
import javafx.application.Platform;
import javafx.geometry.Point2D;

/**
 *
 * @author johan
 */
public class MyLeapListener extends Listener {

    int cnt = 0;
    long start = 0;
    private final LeapConcepts app;

    public MyLeapListener(LeapConcepts main) {
        this.app = main;
    }

    @Override
    public void onConnect(Controller controller) {
        System.out.println("connected");
    }

    @Override
    public void onFrame(Controller controller) {
        Frame frame = controller.frame();
        HandList hands = frame.hands();
        if (!hands.isEmpty()) {
            Hand hand = hands.get(0);
            final float x = hand.palmPosition().getX();
            final float y = hand.palmPosition().getY();
            final float z = hand.palmPosition().getZ();
            
            System.out.println("x = " + x + ", y = " + y + ", z = " + z);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {

                    app.centerX().set(x);
                    app.centerY().set(z);
                    app.radius().set(50.-y/6);
                }

            });

        }
//        float f = frame.currentFramesPerSecond();
//        // System.out.println("got frame! "+controller.frame());
//        double tot = 12;
//        String msg = "";
//        long n = System.currentTimeMillis();
//        for (int i = 0; i < 3000; i++) {
//            double d = Math.random() * i;
//            tot = tot + d;
//            String u = UUID.randomUUID().toString();
//            msg = msg + u;
//        }
//        long m = System.currentTimeMillis();
//        System.out.println("TIME = "+(m-n));
//        if (start == 0) {
//            start = System.currentTimeMillis();
//        }
//        cnt++;
//        if (cnt%100==0) {
//            long now = System.currentTimeMillis();
//            double rate = (double)cnt/(now - start);
//            System.out.println("rate = "+rate+" -- "+f);
//        }
//        
    }

}
