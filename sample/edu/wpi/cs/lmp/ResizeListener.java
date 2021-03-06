package edu.wpi.cs.lmp;
import javafx.application.Platform;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

/**
 * 
 * @author johan Modified to track multiple hands
 */
public class ResizeListener extends Listener {

	public static final int SCREEN_HEIGHT = 1080;
	public static final int SCREEN_WIDTH = 1920;

	int cnt = 0;
	long start = 0;
	private final LeapConcepts app;
	private boolean isResizing = false;
	private double initialSpace = 0;

	public ResizeListener(LeapConcepts main) {
		this.app = main;
	}

	@Override
	public void onConnect(Controller controller) {
		// Enable the gestures you intend to use onConnect
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		System.out.println("connected");
	}

	@Override
	public void onFrame(Controller controller) {
		final Frame frame = controller.frame();
		final FingerList fingers = frame.fingers();
		final HandList hands = frame.hands();
		final GestureList gestures = frame.gestures();

		// This portion resizes with the both hands, the reason I use two
		// fingers is due to how bad the extended check is
		if (hands.count() == 2
				&& hands.get(0).fingers().extended().count() <= 2
				&& hands.get(1).fingers().extended().count() <= 2) {
			final double xPos1 = hands.get(0).fingers().get(0).tipPosition().getX();
			final double xPos2 = hands.get(1).fingers().get(0).tipPosition().getX();

			// Wasn't resizing before, new initial space needed
			if (!isResizing) {
				initialSpace = Math.abs(xPos2 - xPos1);
			}
			final double newSize = Math.abs(xPos2 - xPos1);

			final double percentageChange = (newSize * 100) / initialSpace;
			isResizing = true;

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					app.resizeImage(percentageChange);
				}

			});

			initialSpace = newSize;

		} else {
			isResizing = false;
		}

		// Implementing circle gesture for resizing
		/*
		 * Interestingly enough, since each finger counts itself as a different
		 * gesture, more fingers you have open the greater the change. Makes
		 * this a lot more feasible
		 */
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (!gestures.isEmpty()) {
					for (int i = 0; i < gestures.count(); i++) {
						switch (gestures.get(i).type()) {
						case TYPE_CIRCLE:
							CircleGesture circle = new CircleGesture(gestures.get(i));
							if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 2) {
								// Clockwise if angle is less than 90 degrees
								app.resizeImage(101);
							} else {
								app.resizeImage(99);
							}
							break;
						case TYPE_INVALID:
							break;
						case TYPE_KEY_TAP:
							break;
						case TYPE_SCREEN_TAP:
							break;
						case TYPE_SWIPE:
							break;
						default:
							break;
						}
					}
				}
			}
		});

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
