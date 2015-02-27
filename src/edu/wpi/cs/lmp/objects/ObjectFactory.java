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

/**
 * Factory for creating new presentation objects, all in the form of IObjects
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class ObjectFactory {
	private static final ObjectFactory INSTANCE = new ObjectFactory();

	private ObjectFactory() {}

	public static ObjectFactory getInstance(){
		return INSTANCE;
	}

	/**
	 * Creates an IObject of the specified type
	 * @param type The type of the IObject to be created
	 * @return The new IObject
	 */
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

	/**
	 * Creates an IObject of the specified type
	 * @param type The type of the IObject to be created
	 * @param path The path to the object data in the file system
	 * @return The new IObject
	 */
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
