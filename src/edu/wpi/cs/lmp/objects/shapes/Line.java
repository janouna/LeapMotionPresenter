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
 * The Line shape for the Leap Motion Presenter
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class Line extends Shape implements IObject {
	private static final String PATH = "Shapes/Line.png";
	
	public Line() {
		super(PATH);
	}

	/**
	 * Creates a new line shape
	 * @param x The x coordinate of the line
	 * @param y The y coordinate of the line
	 * @param width The width of the line
	 * @param height The height of the line
	 * @param angle The rotation angle of the line
	 */
	public Line(double x, double y, double width, double height,
			double angle) {
		super(PATH, x, y, width, height, angle);
	}

}
