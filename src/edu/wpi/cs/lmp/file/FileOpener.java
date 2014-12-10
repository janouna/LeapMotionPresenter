package edu.wpi.cs.lmp.file;

import java.io.File;
import java.io.IOException;

import javafx.application.Platform;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wpi.cs.lmp.objects.Image;
import edu.wpi.cs.lmp.objects.TextBox;
import edu.wpi.cs.lmp.objects.Video;
import edu.wpi.cs.lmp.scenes.LeapSceneManager;

public abstract class FileOpener {
	public static void openPresentation() {
		// TODO File Chooser

		try {
			File file = new File("file.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			Element root = doc.getDocumentElement();

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList sceneList = doc.getElementsByTagName("Scene");

			// Loop through all the scenes

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					/*
					 * for (int i = 0; i < sceneList.getLength(); i++) {
					 * LeapSceneManager.getInstance().addScene();
					 * LeapSceneManager.getInstance() .setCurrentScene(
					 * LeapSceneManager.getInstance().getAllScenes() .size() -
					 * 1); Node scene = sceneList.item(i);
					 * 
					 * NodeList objects = scene.getChildNodes();
					 * 
					 * for (int j = 0; j < objects.getLength(); j++) { Node
					 * object = objects.item(j);
					 * 
					 * if (object.getNodeType() == Node.ELEMENT_NODE) {
					 * 
					 * Element e = (Element) object;
					 * 
					 * if (e.getNodeName().equals("Image")) {
					 * LeapSceneManager.getInstance().getCurrentScene()
					 * .addObject(Image.fromXML(e));
					 * 
					 * }
					 * 
					 * }
					 * 
					 * } }
					 */
				}

			});

			parseNode(root.getFirstChild());

		} catch (ParserConfigurationException e) {

		} catch (SAXException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	private static void parseNode(Node node) {
		boolean recurse = true;
		for (Node n = node; n != null; n = n.getNextSibling()) { // Iterates
																	// through
																	// sibling
																	// nodes
			if (n.getNodeName().equals("Scene")) {
				LeapSceneManager.getInstance().addScene();
				LeapSceneManager.getInstance()
						.setCurrentScene(
								LeapSceneManager.getInstance().getAllScenes()
										.size() - 1);
			} else if (n.getNodeName().equals("Image")) {
				LeapSceneManager.getInstance().getCurrentScene()
						.addObject(Image.fromXML((Element) n));
				recurse = false;
			} else if (n.getNodeName().equals("Video")) {
				LeapSceneManager.getInstance().getCurrentScene()
						.addObject(Video.fromXML(n));
				recurse = false;
			} else if (n.getNodeName().equals("Text")) {
				LeapSceneManager.getInstance().getCurrentScene()
						.addObject(TextBox.fromXML(n));
				recurse = false;
			}

			if (recurse && n.hasChildNodes()) // Recursive call to child nodes
				parseNode(n.getFirstChild());
		}
	}
}
