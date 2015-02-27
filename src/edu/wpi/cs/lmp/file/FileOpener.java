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

	/**
	 * This method will attempt to open a LeapMotionPresenter presentation file
	 * using the given file object.
	 * 
	 * @param file
	 *            The file object to open. This file object should point to an
	 *            *.lmp file to obtain the references in a presentation
	 *            
	 */
	public static void openPresentation(File file) {
		try {
			LeapSceneManager.getInstance().setProjectDirectory(
					file.getParentFile());

			final DocumentBuilderFactory dbf = DocumentBuilderFactory
					.newInstance();
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final Document doc = db.parse(file);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			final NodeList sceneList = doc.getElementsByTagName("Scene");

			// Loop through all the scenes
			Platform.runLater(new Runnable() {

				@Override
				public void run() {

					for (int i = 0; i < sceneList.getLength(); i++) {
						LeapSceneManager.getInstance().addScene();
						LeapSceneManager.getInstance().setCurrentScene(
								LeapSceneManager.getInstance().getAllScenes()
										.size() - 1);
						Node scene = sceneList.item(i);

						NodeList objects = scene.getChildNodes();

						for (int j = 0; j < objects.getLength(); j++) {
							Node object = objects.item(j);

							if (object.getNodeType() == Node.ELEMENT_NODE) {

								Element e = (Element) object;

								if (e.getNodeName().equals("Image")) {
									LeapSceneManager
											.getInstance()
											.getCurrentScene()
											.addObject(
													Image.fromXML(
															e,
															LeapSceneManager
																	.getInstance()
																	.getProjectDirectory()));

								} else if (e.getNodeName().equals("Video")) {
									LeapSceneManager
											.getInstance()
											.getCurrentScene()
											.addObject(
													Video.fromXML(
															e,
															LeapSceneManager
																	.getInstance()
																	.getProjectDirectory()));
								} else if (e.getNodeName().equals("Text")) {
									LeapSceneManager
											.getInstance()
											.getCurrentScene()
											.addObject(
													TextBox.fromXML(
															e,
															LeapSceneManager
																	.getInstance()
																	.getProjectDirectory()));
								} else {
									// We didn't recognize the object reference, ignore it.
								}

							}

						}
					}

					LeapSceneManager.getInstance().setCurrentScene(1);
				}

			});

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
