package edu.wpi.cs.lmp.leap;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

import edu.wpi.cs.lmp.objects.IObject;

/**
 * 
 * @author johan Modified to track multiple hands
 */
public class ResizeListener extends Listener {

	private IObject obj;
	private boolean isResizing = false;
	private boolean unbindRequest = false;
	private double initialSpace = 0;

	public ResizeListener() {
		this.obj = null;
	}

	@Override
	public void onConnect(Controller controller) {
		// Enable the gestures you intend to use onConnect
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		System.out.println("connected: Resize/Gesture Listener");
	}

	@Override
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();
		final FingerList fingers = frame.fingers();
		final HandList hands = frame.hands();
		final GestureList gestures = frame.gestures();

		// This portion resizes with the both hands, the reason I use two
		// fingers is due to how bad the extended check is
		if (hands.count() == 2
				&& hands.get(0).fingers().extended().count() <= 2
				&& hands.get(1).fingers().extended().count() <= 2) {
			double xPos1 = hands.get(0).fingers().get(0).tipPosition().getX();
			double xPos2 = hands.get(1).fingers().get(0).tipPosition().getX();

			double zPos1 = hands.get(0).fingers().get(0).tipPosition().getZ();
			double zPos2 = hands.get(1).fingers().get(0).tipPosition().getZ();

			double initXPos1 = 0;
			double initXPos2 = 0;

			// Wasn't resizing before, new initial space needed
			if (!isResizing) {
				initialSpace = Math.abs(xPos2 - xPos1);
				initXPos1 = xPos1;
				initXPos2 = xPos2;

			}
			
			if (Math.abs(zPos2 - zPos1) <= 30) {
				// double newSize = Math.abs(Math.abs(((initXPos2 - xPos2)/2.0)
				// + xPos2) - ((initXPos1 - xPos1)/2.0) + xPos1);
				double newSize = Math.abs(xPos2 - xPos1);

				final double percentageChange = (newSize * 100) / initialSpace;
				isResizing = true;

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if (obj != null) {
							obj.resize(percentageChange);
						}
					}

				});

				initialSpace = newSize;
			}

		} else {
			isResizing = false;
			if (unbindRequest) {
				setIObject(null);
			}
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
							CircleGesture circle = new CircleGesture(gestures
									.get(i));
							if (circle.pointable().direction()
									.angleTo(circle.normal()) <= Math.PI / 2) {
								// Clockwise if angle is less than 90 degrees
								if (obj != null) {
									// obj.resize(101);
								}
							} else {
								// Testing acceptable circle
								if (obj != null
										&& circle.durationSeconds() >= 0.5) {
									// obj.resize(99);
									obj.onCounterCircle();
								}
							}
							break;
						case TYPE_SCREEN_TAP:
							if (obj != null) {
								obj.onScreenTap();
							}
							break;
						}
					}
				}
			}
		});
	}

	public void setIObject(IObject obj) {
		if (!isResizing && obj == null) {
			// Not resizing, unbin object
			unbindRequest = false;
			this.obj = obj;
		} else if (isResizing && obj == null) {
			// Currently resizing, set object to null when finished
			// unbindRequest is checked onFrame
			unbindRequest = true;
		} else {
			unbindRequest = false;
			this.obj = obj;
		}
	}
}
