/*******************************************************************************
 * This file is part of James Anouna and Johnny Hernandez's MQP.
 * Leap Motion Presenter
 * Advised by Professor Gary Pollice
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * James Anouna
 * Johnny Hernandez
 *******************************************************************************/
package edu.wpi.cs.lmp.leap;

import javafx.application.Platform;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.Vector;

import edu.wpi.cs.lmp.objects.IObject;

/**
 * The leap listener responsible for listening for gestures on objects and
 * passing the appropriate information to the objects to react to
 * 
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class ObjectGestureListener extends Listener {

	private IObject obj;
	private boolean isResizing = false;
	private boolean isRotating = false;
	private boolean unbindRequest = false;
	private double initialSpaceX = 0;
	private double initialSpaceY = 0;

	private double initialRotation = 0;

	private static final long TAP_TIMEOUT = 200;
	private static final long ALIGNMENT_THRESHOLD = 30;

	private final long lastTap = Long.MIN_VALUE;

	public ObjectGestureListener() {
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
		final Frame frame = controller.frame();
		// final FingerList fingers = frame.fingers();
		final HandList hands = frame.hands();
		final GestureList gestures = frame.gestures();

		resizeObject(hands);
		//rotateObject(hands);

		// This portion resizes with the both hands, the reason I use two
		// fingers is due to how bad the extended check is

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
							} else {
								// Testing acceptable circle
								if (obj != null
										&& circle.durationSeconds() >= 0.4) {
									obj.onCounterCircle();
								}
							}
							break;
						case TYPE_SCREEN_TAP:
							ScreenTapGesture tap = new ScreenTapGesture(
									gestures.get(i));
							if (obj != null
									&& tap.state().equals(State.STATE_STOP)
									&& (lastTap < System.currentTimeMillis()
											- TAP_TIMEOUT)) {
								obj.onScreenTap();
							}
							break;
						case TYPE_SWIPE:
							break;
						case TYPE_KEY_TAP:
							break;
						case TYPE_INVALID:
							break;
						default:
							break;
						}
					}
				}
			}
		});
	}

	/**
	 * Handles the resizing of objects
	 * @param hands The current hands list
	 */
	private void resizeObject(HandList hands) {
		if (hands.count() == 2
				&& hands.get(0).fingers().extended().count() <= 2
				&& hands.get(1).fingers().extended().count() <= 2) {
			final double xPos1 = hands.get(0).fingers().get(0).tipPosition()
					.getX();
			final double xPos2 = hands.get(1).fingers().get(0).tipPosition()
					.getX();

			final double yPos1 = hands.get(0).fingers().get(0)
					.stabilizedTipPosition().getY();
			final double yPos2 = hands.get(1).fingers().get(0)
					.stabilizedTipPosition().getY();

			final double zPos1 = hands.get(0).fingers().get(0).tipPosition()
					.getZ();
			final double zPos2 = hands.get(1).fingers().get(0).tipPosition()
					.getZ();

			// Check if the fingers are aligned closely enough to begin resizing
			if (Math.abs(zPos2 - zPos1) <= ALIGNMENT_THRESHOLD) {

				// Wasn't resizing before, new initial space needed
				if (!isResizing) {
					initialSpaceX = Math.abs(xPos2 - xPos1);
					initialSpaceY = Math.abs(yPos2 - yPos1);
				}

				// double newSize = Math.abs(Math.abs(((initXPos2 - xPos2)/2.0)
				// + xPos2) - ((initXPos1 - xPos1)/2.0) + xPos1);
				final double newSizeX = Math.abs(xPos2 - xPos1);
				final double newSizeY = Math.abs(yPos2 - yPos1);

				final double percentageChangeX = (newSizeX * 100)
						/ initialSpaceX;
				final double percentageChangeY = (newSizeY * 100)
						/ initialSpaceY;
				isResizing = true;

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if (obj != null) {
							obj.resize(percentageChangeX, percentageChangeY);
						}
					}

				});

				initialSpaceX = newSizeX;
				initialSpaceY = newSizeY;
			} else {
				// Two hand count is there, but not aligned therefore not
				// resizing
				isResizing = false;
			}

		} else {
			// Second hand is gone, resizing is done.
			isResizing = false;
			if (unbindRequest) {
				setIObject(null);
			}
		}
	}

	/**
	 * Handles rotating objects
	 * @param hands The current hands list
	 */
	private void rotateObject(HandList hands) {
		if (hands.count() == 2
				&& hands.get(0).fingers().extended().count() == 2) {

			final Vector handNormal = hands.get(0).palmNormal();

			if (!isRotating) {
				initialRotation = Math.toDegrees(handNormal.roll());
				isRotating = true;
			}

			// Possible options for rotation detection: roll and yaw
			final double degreeChange = Math.toDegrees(handNormal.roll());

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					if (obj != null) {
						// System.out.println("Rotating: " + degreeChange);
						obj.rotate(degreeChange);
					}
				}

			});

			initialRotation = degreeChange;
		} else {
			isRotating = false;
			if (unbindRequest) {
				setIObject(null);
			}
		}
	}

	/**
	 * Sets the current IObject 
	 * @param obj The current IObject
	 */
	public void setIObject(IObject obj) {
		if ((isResizing && obj == null) || (isRotating && obj == null)) {
			// Currently resizing, set object to null when finished
			// unbindRequest is checked onFrame
			unbindRequest = true;
		} else {
			unbindRequest = false;
			this.obj = obj;
		}
	}
}
