package edu.wpi.cs.lmp.fileChooser;

import javafx.stage.FileChooser;
import edu.wpi.cs.lmp.objects.ObjectType;

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
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extAll = new FileChooser.ExtensionFilter(
				"All Files (*.*)", "*.*");

		switch (object) {
		case IMAGE:
			fileChooser.setTitle("Open Image File");
			FileChooser.ExtensionFilter extJPG = new FileChooser.ExtensionFilter(
					"JPG files (*.jpg)", "*.JPG", "*.jpg");
			FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter(
					"PNG files (*.png)", "*.PNG", "*.png");
			fileChooser.getExtensionFilters().addAll(extAll, extJPG, extPNG);
			break;
		case TEXT:
			fileChooser.setTitle("Open Text File");
			FileChooser.ExtensionFilter extPDF = new FileChooser.ExtensionFilter(
					"PDF files (*.pdf)", "*.PDF");
			fileChooser.getExtensionFilters().addAll(extAll, extPDF);
			break;
		case VIDEO:
			fileChooser.setTitle("Open Video File");
			FileChooser.ExtensionFilter extMP4 = new FileChooser.ExtensionFilter(
					"MP4 files (*.mp4)", "*.MP4");
			fileChooser.getExtensionFilters().addAll(extAll, extMP4);
			break;
		case PRESENTATION:
			fileChooser.setTitle("Open Presentation File");
			FileChooser.ExtensionFilter extLMP = new FileChooser.ExtensionFilter("LeapMotion Presentation files (*.lmp)", "*.lmp", "*.LMP");
			fileChooser.getExtensionFilters().add(extLMP);
			break;
		default:
			break;
		}

		return fileChooser;
	}

}
