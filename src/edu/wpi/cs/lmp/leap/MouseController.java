package edu.wpi.cs.lmp.leap;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

/**
 * 
 * @author johan Modified to track multiple hands
 */
public class MouseController extends Listener {
	private double SCREEN_HEIGHT;
	private double SCREEN_WIDTH;

	//private static final float HEIGHT_BOTTOM_CLAMP = 0.2f;
	//private static final float HEIGHT_TOP_CLAMP = 0.8f;

	//private static final float WIDTH_LEFT_CLAMP = 0.2f;
	//private static final float WIDTH_RIGHT_CLAMP = 0.8f;


	private Robot mouse;

	public MouseController(double width, double height) {
		this.SCREEN_HEIGHT = height;
		this.SCREEN_WIDTH = width;
	}

	@Override
	public void onConnect(Controller controller) {
		// Enable the gestures you intend to use onConnect
		try {
			mouse = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		HandStateObservable.getInstance().getHandState().addListener(new ChangeListener<HandState>() {
			@Override
			public void changed(ObservableValue<? extends HandState> observable,
					HandState oldValue, final HandState newValue) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						switch(newValue) {
						case OPEN:
							System.out.println("Hand Open");
							mouse.mouseRelease(InputEvent.BUTTON1_MASK);
							break;
						case CLOSED:
							System.out.println("Hand Closed");
							mouse.mousePress(InputEvent.BUTTON1_MASK);
							break;
						case GONE:
							System.out.println("No Hand detected");
							mouse.mouseRelease(InputEvent.BUTTON1_MASK);
							break;
						case POINTING:
							break;
						}
					}
				});
			}
		});

		System.out.println("connected: MouseController");
	}

	@Override
	public void onFrame(Controller controller) {
		final Frame frame = controller.frame();
		final HandList hands = frame.hands();
		//final FingerList fingers = frame.fingers();

		// locatedScreens() to calibrate on to screen cords is deprecated or
		// shoddy at best
		// A little research into this shows InteractionBox from the frame to be
		// the most
		// Accurate way of mapping leap coordinates on screen
		final InteractionBox screen = frame.interactionBox();
		/*
		if (fingers.extended().count() == 1) {
			final Finger thisFinger = fingers.get(0);
			Vector intersect = screen.normalizePoint(thisFinger
					.stabilizedTipPosition());

			final double x = intersect.getX() * SCREEN_WIDTH;
			final double y = (1 - intersect.getY()) * SCREEN_HEIGHT;

			mouse.mouseMove((int) x, (int) y);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					HandStateObservable.getInstance().setX(x);
					HandStateObservable.getInstance().setY(y);
				}
			});
		}
		 */
		if (!hands.isEmpty()) {
			final Hand thisHand = hands.get(0);
			final Vector intersect = screen.normalizePoint(thisHand
					.stabilizedPalmPosition());
			// Vector unstableIntersect = screen.normalizePoint(thisHand.palmPosition());

			final double x = intersect.getX() * SCREEN_WIDTH;
			final double y = (1 - intersect.getY()) * SCREEN_HEIGHT;
			// final double y = unstableIntersect.getZ() * SCREEN_HEIGHT;

			mouse.mouseMove((int) x, (int) y);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					HandStateObservable.getInstance().setX(x);
					HandStateObservable.getInstance().setY(y);
				}
			});
			// System.out.println("X Position: " + intersect.getX() + " Y Position: " + intersect.getY());			
		}
	}
}
