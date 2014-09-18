import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Screen;
import com.leapmotion.leap.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;

/**
 * 
 * @author johan Modified to track multiple hands
 */
public class MyLeapListener extends Listener {

	int cnt = 0;
	long start = 0;
	private BooleanProperty keyTap = new SimpleBooleanProperty(false);
	private final LeapConcepts app;

	public MyLeapListener(LeapConcepts main) {
		this.app = main;
	}

	public BooleanProperty keyTapProperty() {
		return keyTap;
	}

	@Override
	public void onConnect(Controller controller) {
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
		System.out.println("connected");
	}

	@Override
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();
		final HandList hands = frame.hands();
		if (!hands.isEmpty()) {
			final List<Float> xPos = new ArrayList<Float>();
			final List<Float> yPos = new ArrayList<Float>();
			final List<Float> zPos = new ArrayList<Float>();
			for (int i = 0; i < hands.count(); i++) {
				Hand hand = hands.get(i);
				final float x = hand.palmPosition().getX();
				final float y = hand.palmPosition().getY();
				final float z = hand.palmPosition().getZ();
				xPos.add(x);
				yPos.add(y);
				zPos.add(z);
			}

			Platform.runLater(new Runnable() {

				@Override
				public void run() {

					while (app.countCircles() < hands.count()) {
						app.addCircle();
					}

					for (int i = 0; i < hands.count(); i++) {
						app.centerX(i).set(xPos.get(i));
						app.centerY(i).set(zPos.get(i));
						app.radius(i).set(50. - yPos.get(i) / 6);
					}

				}

			});

		}
		
        keyTap.set(false);
        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            if(gestures.get(i).type()==Gesture.Type.TYPE_KEY_TAP && app.overButton(0)){
                keyTap.set(true); break;
            }
        }

		// float f = frame.currentFramesPerSecond();
		// // System.out.println("got frame! "+controller.frame());
		// double tot = 12;
		// String msg = "";
		// long n = System.currentTimeMillis();
		// for (int i = 0; i < 3000; i++) {
		// double d = Math.random() * i;
		// tot = tot + d;
		// String u = UUID.randomUUID().toString();
		// msg = msg + u;
		// }
		// long m = System.currentTimeMillis();
		// System.out.println("TIME = "+(m-n));
		// if (start == 0) {
		// start = System.currentTimeMillis();
		// }
		// cnt++;
		// if (cnt%100==0) {
		// long now = System.currentTimeMillis();
		// double rate = (double)cnt/(now - start);
		// System.out.println("rate = "+rate+" -- "+f);
		// }
		//
	}

}
