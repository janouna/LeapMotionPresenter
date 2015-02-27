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
package edu.wpi.cs.lmp.objects;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.wpi.cs.lmp.scenes.LeapScene;

/**
 * All presentation objects implement the IObject interface.  It contains methods for moving, resizing, and other gestures, as well as saving and opening.
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public interface IObject {
	/**
	 * Signals the object to start moving
	 */
	void startMove();
	/**
	 * Signals the object to stop moving
	 */
	void endMove();
	/**
	 * Resizes the object
	 * @param percentChangeWidth The new width as a percentage of the old width
	 * @param percentChangeHeight The new height as a percentage of the old height
	 */
	void resize(double percentChangeWidth, double percentChangeHeight);
	/**
	 * Sets the X position of the IObject
	 * @param x The X position of the IObject
	 */
	void setX(double x);
	/**
	 * Sets the Y position of the IObject
	 * @param y The y position of the IObject
	 */
	void setY(double y);
	/**
	 * Rotates the IObject
	 * @param angle The angle to set the object to
	 */
	void rotate(double angle);
	
	/**
	 * Called when a Screen Tap gesture is performed
	 */
	void onScreenTap();
	/**
	 * Called when a counter-clockwise circle is performed
	 */
	void onCounterCircle();
	
	/**
	 * Called when a radial menu is created for the object
	 * @param action The action performed
	 */
	void radialMenuActions(int action);
	
	/**
	 * Copies the IObject to a file
	 * @param to The file to copy to
	 */
	void copyTo(File to);

	/**
	 * Checks if a certain point is within the bounds of the IObject
	 * @param parent The current Scene
	 * @param x The x position of the point
	 * @param y The y position of the point
	 * @return True if the point is within the bounds of the object
	 */
	boolean inBounds(LeapScene parent, double x, double y);
	
	/**
	 * Stores the object's data in an XML document
	 * @param doc The document to store the data in
	 * @return The filled document
	 */
	Element toXML(Document doc);
}
