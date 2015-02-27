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
	void startMove();
	void endMove();
	void resize(double percentChangeWidth, double percentChangeHeight);
	void setX(double x);
	void setY(double y);
	void rotate(double angle);

	void onScreenTap();
	void onCounterCircle();
	
	void radialMenuActions(int action);
	
	void copyTo(File to);

	boolean inBounds(LeapScene parent, double x, double y);
	
	Element toXML(Document doc);
}
