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
package edu.wpi.cs.lmp.fileChooser;

import javafx.stage.FileChooser;
import edu.wpi.cs.lmp.objects.ObjectType;

/**
 * Facilitates choosing files for Images or Videos to be imported.  Also used for saving and opening files.
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class FileChooserFactory {

	private static final FileChooserFactory INSTANCE = new FileChooserFactory();

	/**
	 * Blank constructor, this is a singleton factory
	 */
	private FileChooserFactory() {}

	/**
	 * Get the instance of the FileChooserFactory to make use of it
	 * @return The singleton instance of FileChooserFactory
	 */
	public static FileChooserFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * Makes a standard FileChooser object with no modifications (No title bar name, No filters for extensions, etc)
	 * @return The FileChooser object
	 */
	public FileChooser makeFileChooser() {
		return new FileChooser();
	}

	/**
	 * Makes a file chooser tailored for the given object. A specific name is given and extension filters are created for each file
	 * @param object The object type that you wish to make a file chooser window for
	 * @return The FileChooser object 
	 */
	public FileChooser makeFileChooser(ObjectType object) {
		final FileChooser fileChooser = new FileChooser();

		final FileChooser.ExtensionFilter extAll = new FileChooser.ExtensionFilter(
				"All Files (*.*)", "*.*");

		switch (object) {
		case IMAGE:
			fileChooser.setTitle("Open Image File");
			final FileChooser.ExtensionFilter extJPG = new FileChooser.ExtensionFilter(
					"JPG files (*.jpg)", "*.JPG", "*.jpg");
			final FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter(
					"PNG files (*.png)", "*.PNG", "*.png");
			fileChooser.getExtensionFilters().addAll(extAll, extJPG, extPNG);
			break;
		case TEXT:
			fileChooser.setTitle("Open Text File");
			final FileChooser.ExtensionFilter extPDF = new FileChooser.ExtensionFilter(
					"PDF files (*.pdf)", "*.PDF");
			fileChooser.getExtensionFilters().addAll(extAll, extPDF);
			break;
		case VIDEO:
			fileChooser.setTitle("Open Video File");
			final FileChooser.ExtensionFilter extMP4 = new FileChooser.ExtensionFilter(
					"MP4 files (*.mp4)", "*.MP4");
			fileChooser.getExtensionFilters().addAll(extAll, extMP4);
			break;
		case PRESENTATION:
			fileChooser.setTitle("Open Presentation File");
			final FileChooser.ExtensionFilter extLMP = new FileChooser.ExtensionFilter("LeapMotion Presentation files (*.lmp)", "*.lmp", "*.LMP");
			fileChooser.getExtensionFilters().add(extLMP);
			break;
		default:
			break;
		}

		return fileChooser;
	}

}
