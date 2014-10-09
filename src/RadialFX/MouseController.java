package RadialFX;
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

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * 
 * @author johan Modified to track multiple hands
 */
public class MouseController extends Listener {

	public final static int SCREEN_HEIGHT = 1080;
	public final static int SCREEN_WIDTH = 1920;

	int cnt = 0;
	long start = 0;
	private BooleanProperty keyTap = new SimpleBooleanProperty(false);
	private boolean isGrabbed = false;

	public MouseController() {
	}

	@Override
	public void onConnect(Controller controller) {
		// Enable the gestures you intend to use onConnect
		System.out.println("connected");
	}

	@Override
	public void onFrame(Controller controller) {
		Robot mouse = null;
		try {
			mouse = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
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

			final float x = (Math.min(1, Math.max(0, intersect.getX()) - 0.5f))
					* SCREEN_WIDTH * 2;
			final float y = (Math.min(1, Math.max(0, intersect.getY()) - 0.5f))
					* -SCREEN_HEIGHT * 2;

			mouse.mouseMove((int) x, (int) y);
		}
	}
}
