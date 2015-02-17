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

public class ShapeFactory {
	private static final ShapeFactory INSTANCE = new ShapeFactory();

	private ShapeFactory() {}

	public static ShapeFactory getInstance(){
		return INSTANCE;
	}

	public IObject createShape(ShapeType type){
		IObject o = null;

		switch(type){
		case CIRCLE:
			o = new Circle();
			break;
		case SQUARE:
			o = new Square();
			break;
		case LINE:
			o = new Line();
			break;
		case ARROW:
			o = new Arrow();
			break;
		default:
			break;
		}

		return o;
	}

}
