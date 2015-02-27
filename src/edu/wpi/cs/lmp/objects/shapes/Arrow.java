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
package edu.wpi.cs.lmp.objects.shapes;

import edu.wpi.cs.lmp.objects.IObject;

/**
 * The Arrow shape for the Leap Motion Presenter
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class Arrow extends Shape implements IObject {
	private static final String PATH = "Shapes/Arrow.png";	
	
	public Arrow() {
		super(PATH);
	}

	/**
	 * Creates a new Arrow shape
	 * @param x The x coordinate of the arrow
	 * @param y The y coordinate of the arrow
	 * @param width The width of the arrow
	 * @param height The height of the arrow
	 * @param angle The rotation angle of the arrow
	 */
	public Arrow(double x, double y, double width, double height,
			double angle) {
		super(PATH, x, y, width, height, angle);
	}
}
