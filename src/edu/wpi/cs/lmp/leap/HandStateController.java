package edu.wpi.cs.lmp.leap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

public class HandStateController extends Listener {

	@Override
	public void onConnect(Controller controller) {
		// Enable the gestures you intend to use onConnect
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		System.out.println("connected: HandStateController");
	}

	@Override
	public void onFrame(Controller controller) {
		Frame frame = controller.frame();
		final FingerList fingers = frame.fingers();
		final HandList hands = frame.hands();
		final GestureList gestures = frame.gestures();

		if (hands.count() == 1) {
			Hand thisHand = hands.get(0);
			if (thisHand.isValid() && thisHand.grabStrength() >= 0.7) {
				// The hand should be considered closed, change the state
				if (!HandStateObservable.getInstance().get().equals(HandState.CLOSED)) {
					// handState.set(HandState.CLOSED);
					HandStateObservable.getInstance().set(HandState.CLOSED);
				}
			} else {
				// The hand is not in a fist (open)
				if (!HandStateObservable.getInstance().get().equals(HandState.OPEN)) {
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
					break;
				}
			}
		}
	}

}
