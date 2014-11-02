package edu.wpi.cs.lmp.leap;

import java.awt.AWTException;
import java.awt.Robot;

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
	
	private double SCREEN_HEIGHT= 1080;
	private double SCREEN_WIDTH = 1920;

	public MouseController(double width, double height) {
		this.SCREEN_HEIGHT = height;
		this.SCREEN_WIDTH = width;
	}

	@Override
	public void onConnect(Controller controller) {
		// Enable the gestures you intend to use onConnect
		System.out.println("connected: MouseController");
	}

	@Override
	public void onFrame(Controller controller) {
		Robot mouse = null;
		try {
			mouse = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		Frame frame = controller.frame();
		final HandList hands = frame.hands();
		// locatedScreens() to calibrate on to screen cords is deprecated or
		// shoddy at best
		// A little research into this shows InteractionBox from the frame to be
		// the most
		// Accurate way of mapping leap coordinates on screen
		InteractionBox screen = frame.interactionBox();
		if (!hands.isEmpty()) {
			final Hand thisHand = hands.get(0);
			Vector intersect = screen.normalizePoint(thisHand
					.stabilizedPalmPosition());
			
			final double x = intersect.getX() * SCREEN_WIDTH;
			final double y = (1 - intersect.getY()) * SCREEN_HEIGHT;

			mouse.mouseMove((int) x, (int) y);
			// System.out.println("X Position: " + intersect.getX() + " Y Position: " + intersect.getY());
		}
	}
}
