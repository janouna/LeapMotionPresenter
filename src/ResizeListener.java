import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * 
 * @author johan Modified to track multiple hands
 */
public class ResizeListener extends Listener {

	public final static int SCREEN_HEIGHT = 1080;
	public final static int SCREEN_WIDTH = 1920;

	int cnt = 0;
	long start = 0;
	private BooleanProperty keyTap = new SimpleBooleanProperty(false);
	private final LeapConcepts app;
	private boolean isResizing = false;
	private double initialSpace = 0;

	public ResizeListener(LeapConcepts main) {
		this.app = main;
	}

	public BooleanProperty keyTapProperty() {
		return keyTap;
	}

	@Override
	public void onConnect(Controller controller) {
		// Enable the gestures you intend to use onConnect
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		System.out.println("connected");
	}

	@Override
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();
		final FingerList fingers = frame.fingers();
		final HandList hands = frame.hands();
		
		if (hands.count() == 2) {
			double xPos1 = hands.get(0).palmPosition().getX();
			double xPos2 = hands.get(1).palmPosition().getX();
			
			// Wasn't resizing before, new initial space needed
			if (!isResizing) {
				initialSpace = Math.abs(xPos2-xPos1);
			}
			double newSize = Math.abs(xPos2-xPos1);
			
			final double percentageChange = (newSize*100)/initialSpace;
			
			isResizing = true;

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					app.resizeImage(percentageChange);
				}

			});

		} else {
			isResizing = false;
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
