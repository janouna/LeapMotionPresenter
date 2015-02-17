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

public class ObjectFactory {
	private static final ObjectFactory INSTANCE = new ObjectFactory();

	private ObjectFactory() {}

	public static ObjectFactory getInstance(){
		return INSTANCE;
	}

	public IObject createObject(ObjectType type){
		IObject o = null;

		switch(type){
		case IMAGE:
			break;
		case TEXT:
			o = new TextBox();
			break;
		case VIDEO:
			break;
		case PRESENTATION:
			break;
		default:
			break;
		}

		return o;
	}

	public IObject createObject(ObjectType type, String path){
		IObject o = null;

		switch(type){
		case IMAGE:
			o = new Image(path);
			break;
		case TEXT:
			o = new TextBox();
			break;
		case VIDEO:
			o = new Video(path);
			break;
		case PRESENTATION:
			break;
		default:
			break;
		}

		return o;
	}
}
