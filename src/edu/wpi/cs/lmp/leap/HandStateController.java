package edu.wpi.cs.lmp.leap;

import javafx.application.Platform;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;

import edu.wpi.cs.lmp.scenes.LeapSceneManager;

public class HandStateController extends Listener {
	
	// Used for ignoring swipe gestures recognized immediately after another
	// Useful as all fingers count in the gesture and may be recognized in another frame
	private static final long SWIPE_TIMEOUT = 200;
	// Grab strength needed to trigger a closed hand (0.0-1.0)
	private static final float GRAB_STRENGTH = 0.5f;
	
	private long lastSwipe = Long.MIN_VALUE;

	@Override
	public void onConnect(Controller controller) {
		// Enable the gestures you intend to use onConnect
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.config().setFloat("Gesture.Swipe.MinLength", 400);
		// controller.config().setFloat("Gesture.Swipe.MinVelocity", arg1);
		controller.config().save();
		System.out.println("connected: HandStateController");
	}

	@Override
	public void onFrame(Controller controller) {
		final Frame frame = controller.frame();
		final FingerList fingers = frame.fingers();
		final HandList hands = frame.hands();
		final GestureList gestures = frame.gestures();

		if (hands.count() == 1) {
			final Hand thisHand = hands.get(0);
			if (thisHand.isValid() && thisHand.grabStrength() >= GRAB_STRENGTH) {
				// The hand should be considered closed, change the state
				if (!HandStateObservable.getInstance().get()
						.equals(HandState.CLOSED)) {
					// handState.set(HandState.CLOSED);
					HandStateObservable.getInstance().set(HandState.CLOSED);
				}
			} else {
				// The hand is not in a fist (open)
				if (!HandStateObservable.getInstance().get()
						.equals(HandState.OPEN)) {
					// handState.set(HandState.OPEN);
					HandStateObservable.getInstance().set(HandState.OPEN);
				}
			}
		} else {
			if (!HandStateObservable.getInstance().get().equals(HandState.GONE)) {
				// handState.set(HandState.GONE);
				HandStateObservable.getInstance().set(HandState.GONE);
			}
		}

		if (!gestures.isEmpty()) {
			for (int i = 0; i < gestures.count(); i++) {
				switch (gestures.get(i).type()) {
				case TYPE_SWIPE:
					// If a swipe hasnt been detected, perform a swipe
					// This ensures multiple swipes from all fingers arent read
					final SwipeGesture swipe = new SwipeGesture(gestures.get(i));
					// Classify as horizontal or vertical
					boolean isHorizontal = Math.abs(swipe.direction().getX()) > Math
							.abs(swipe.direction().getY());
					if (isHorizontal && swipe.state().equals(State.STATE_STOP) && (lastSwipe < System.currentTimeMillis() - SWIPE_TIMEOUT)) {
						lastSwipe = System.currentTimeMillis();
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								int currentSlide = LeapSceneManager.getInstance().getCurrentSceneNumber();
								if (swipe.direction().getX() > 0) {
									// Right swipe
									LeapSceneManager.getInstance().setCurrentScene(--currentSlide);
								} else {
									// Left swipe
									LeapSceneManager.getInstance().setCurrentScene(++currentSlide);
								}
							}
							
						});

						break;
					}
				}
			}
		}
	}

}
