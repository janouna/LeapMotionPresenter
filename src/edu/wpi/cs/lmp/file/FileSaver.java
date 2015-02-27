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
package edu.wpi.cs.lmp.file;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.wpi.cs.lmp.scenes.LeapScene;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;

/**
 * An abstract class to handle saving presentations as .lmp files
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public abstract class FileSaver {

	public static void savePresentation(File file) {
		try {
			
			// Make the directory
			file.mkdir();
			
			// Make assets folder	
			final File assets = new File(file.toString() + "/Assets");
			assets.mkdir();

			final DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			final Document doc = docBuilder.newDocument();
			final Element rootElement = doc.createElement("LeapPresentation");
			doc.appendChild(rootElement);

			// get all scenes
			final List<LeapScene> allScenes = LeapSceneManager.getInstance()
					.getAllScenes();

			// loop through all scenes and append objects to their scene
			for (int i = 0; i < allScenes.size(); i++) {
				Element scene = doc.createElement("Scene");
				rootElement.appendChild(scene);
				List<Element> sceneObjs = allScenes.get(i).toXML(doc);
				allScenes.get(i).copyTo(assets);
				for (int j = 0; j < sceneObjs.size(); j++) {
					scene.appendChild(sceneObjs.get(j));
				}
			}
			// write the content into xml file
			final TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(file.toString() + "/" + file.getName()) + ".lmp");

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

}
