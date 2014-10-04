import com.leapmotion.leap.Controller;
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
public class MyLeapListener extends Listener {

	public final static int SCREEN_HEIGHT = 1080;
	public final static int SCREEN_WIDTH = 1920;

	int cnt = 0;
	long start = 0;
	private BooleanProperty keyTap = new SimpleBooleanProperty(false);
	private final LeapConcepts app;
	private boolean isGrabbed = false;

	public MyLeapListener(LeapConcepts main) {
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
		// locatedScreens() to calibrate on to screen cords is deprecated or
		// shoddy at best
		// A little research into this shows InteractionBox from the frame to be
		// the most
		// Accurate way of mapping leap coordinates on screen
		InteractionBox screen = frame.interactionBox();
		
		if (!fingers.isEmpty()) {
			final List<Float> xPos = new ArrayList<Float>();
			final List<Float> yPos = new ArrayList<Float>();
			final List<Float> zPos = new ArrayList<Float>();
			for (int i = 0; i < fingers.count(); i++) {
				if (fingers.get(i).isValid() && fingers.get(i).isExtended()) {
					// Better to not have this one stabilized for responsiveness
					// Multiply by -1 so it gets smaller the closer we get (Much
					// like Touchless for Windows/Mac)
					float z = fingers.get(i).tipPosition().getZ() * -1;
					Vector intersect = screen.normalizePoint(fingers.get(i)
							.stabilizedTipPosition());
					// These formulas may need a little work for better mapping,
					// its somewhat acceptable at this state
					xPos.add(((Math.min(1, Math.max(0, intersect.getX())) - 0.5f))
							* SCREEN_WIDTH);
					yPos.add(((Math.min(1, Math.max(0, intersect.getY())) - 0.5f))
							* -SCREEN_HEIGHT);
					zPos.add(z);
				}
			}

			Platform.runLater(new Runnable() {

				@Override
				public void run() {

					while (app.countCircles() < xPos.size()) {
						app.addCircle();
					}
					
					while (app.countCircles() > xPos.size()) {
						app.removeCircle();
					}

					for (int i = 0; i < xPos.size(); i++) {
						app.centerX(i).set(xPos.get(i));
						app.centerY(i).set(yPos.get(i));
						app.radius(i).set(50 - zPos.get(i) / 6);
					}

				}

			});

		}
		if (!hands.isEmpty()) {
			Hand thisHand = hands.get(0);
			Vector intersect = screen.normalizePoint(thisHand.stabilizedPalmPosition());
			
			final float x = (Math.min(1, Math.max(0, intersect.getX()) - 0.5f))
			* SCREEN_WIDTH;
			final float y = (Math.min(1, Math.max(0, intersect.getY()) - 0.5f))
			* -SCREEN_HEIGHT;
			
			Platform.runLater(new Runnable() {

				@Override
				public void run() {

						app.palmX().set(x);
						app.palmY().set(y);

				}

			});
			
			if (thisHand.isValid() && thisHand.sphereRadius() <= 50) {
				if (app.dragImage(x, y)) {
					app.setPolliceBind();
				}
			} else {
				app.setPolliceUnBind();
			}
		} else {
			app.setPolliceUnBind();
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
