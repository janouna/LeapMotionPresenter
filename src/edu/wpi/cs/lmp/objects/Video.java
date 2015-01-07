package edu.wpi.cs.lmp.objects;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import edu.wpi.cs.lmp.leap.HandStateObservable;
import edu.wpi.cs.lmp.scenes.LeapScene;

public class Video extends MediaView implements IObject {

	private DoubleProperty vidWidth;
	private DoubleProperty vidHeight;

	private DoubleProperty vidX;
	private DoubleProperty vidY;

	private boolean isPlaying;

	// Video source
	private final Media media;
	// Controls for the video
	private final MediaPlayer mediaPlayer;

	private final Video instance;
	
	private String path;

	public Video () {
		super();
		instance = this;
		isPlaying = false;
		// Modify the media variable to take in desired URL or FILE
		media = new Media(new File("big_buck_bunny.mp4").toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		this.setMediaPlayer(mediaPlayer);
		vidWidth = new SimpleDoubleProperty(mediaPlayer.getMedia().getWidth());
		vidHeight = new SimpleDoubleProperty(mediaPlayer.getMedia().getHeight());
		vidX = new SimpleDoubleProperty();
		vidY = new SimpleDoubleProperty(instance.getY());

		mediaPlayer.setOnReady(new Runnable() {
			@Override
			public void run() {
				vidWidth.set(mediaPlayer.getMedia().getWidth());
				vidHeight.set(mediaPlayer.getMedia().getHeight());
				vidX.set(instance.getX());
				vidY.set(instance.getY());
				instance.fitWidthProperty().bind(vidWidth);
				instance.fitHeightProperty().bind(vidHeight);
				System.out.println(instance.getFitWidth());
			}
		});
	}

	public Video (String path) {
		super();
		this.path = path;
		instance = this;
		isPlaying = false;
		// Modify the media variable to take in desired URL or FILE
		media = new Media(new File(path).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		this.setMediaPlayer(mediaPlayer);

		mediaPlayer.setOnReady(new Runnable() {
			@Override
			public void run() {
				vidWidth = new SimpleDoubleProperty(mediaPlayer.getMedia().getWidth());
				vidHeight = new SimpleDoubleProperty(mediaPlayer.getMedia().getHeight());
				vidX = new SimpleDoubleProperty(instance.getX());
				vidY = new SimpleDoubleProperty(instance.getY());
				instance.fitWidthProperty().bind(vidWidth);
				instance.fitHeightProperty().bind(vidHeight);
				System.out.println(instance.getFitWidth());
			}
		});
	}

	public Video(String file, final double x, final double y, final double width, final double height) {
		super();
		instance = this;
		isPlaying = false;
		// Modify the media variable to take in desired URL or FILE
		media = new Media(new File(file).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		this.setMediaPlayer(mediaPlayer);

		mediaPlayer.setOnReady(new Runnable() {
			@Override
			public void run() {
				vidWidth = new SimpleDoubleProperty(width);
				vidHeight = new SimpleDoubleProperty(height);
				vidX = new SimpleDoubleProperty(x);
				vidY = new SimpleDoubleProperty(y);
				instance.translateXProperty().bind(instance.vidX);
				instance.translateYProperty().bind(instance.vidY);
				instance.translateXProperty().unbind();
				instance.translateYProperty().unbind();
				instance.fitWidthProperty().bind(vidWidth);
				instance.fitHeightProperty().bind(vidHeight);
			}
		});
	}

	@Override
	public void startMove() {
		instance.layoutXProperty().set(-instance.getX());
		instance.layoutYProperty().set(-instance.getY());
		this.translateXProperty().bind(HandStateObservable.getInstance().getObservableX());
		this.translateYProperty().bind(HandStateObservable.getInstance().getObservableY());
		vidX.bind(HandStateObservable.getInstance().getObservableX());
		vidY.bind(HandStateObservable.getInstance().getObservableY());
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
		vidX.unbind();
		vidY.unbind();
	}

	@Override
	public void resize(double percentageChange) {
		vidWidth.set((vidWidth.doubleValue()*percentageChange)/100);
		vidHeight.set((vidHeight.doubleValue()*percentageChange)/100);
	}

	@Override
	public boolean inBounds(LeapScene parent, double x, double y) {
		final double xPos = vidX.doubleValue();
		final double yPos = vidY.doubleValue();
		final double width = vidWidth.doubleValue();
		final double height = vidHeight.doubleValue();

		// return (x > xPos-(width/2) && x < xPos+(width/2)) && (y > yPos-(height/2) && y < yPos+(height/2));

		return (x > xPos && x < xPos+(width)) && (y > yPos && y < yPos+(height));
	}

	@Override
	public void onScreenTap() {
		if (isPlaying) {
			mediaPlayer.pause();
			isPlaying = false;
		} else {
			mediaPlayer.play();
			isPlaying = true;
		}
	}

	@Override
	public void onCounterCircle() {
		mediaPlayer.seek(Duration.ZERO);
	}

	@Override
	public Element toXML(Document doc) {
		Element vid = doc.createElement("Video");
		// Source field
		File file = new File(media.getSource());
		Element src = doc.createElement("src");
		src.appendChild(doc.createTextNode("Assets/" + file.getName()));
		vid.appendChild(src);
		// Position fields
		Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode(String.valueOf(vidX.doubleValue())));
		vid.appendChild(x);
		Element y = doc.createElement("y");
		y.appendChild(doc.createTextNode(String.valueOf(vidY.doubleValue())));
		vid.appendChild(y);
		// Size fields
		Element width = doc.createElement("width");
		width.appendChild(doc.createTextNode(String.valueOf(vidWidth.doubleValue())));
		vid.appendChild(width);
		Element height = doc.createElement("height");
		height.appendChild(doc.createTextNode(String.valueOf(vidHeight.doubleValue())));
		vid.appendChild(height);
		
		return vid;
	}

	public static Video fromXML(Element e, File directory) {
		String file = e.getElementsByTagName("src").item(0).getTextContent();
		
		double x = (Double.parseDouble(e.getElementsByTagName("x").item(0).getTextContent()));

		double y = (Double.parseDouble(e.getElementsByTagName("y").item(0).getTextContent()));

		double width = (Double.parseDouble(e.getElementsByTagName("width").item(0).getTextContent()));

		double height = (Double.parseDouble(e.getElementsByTagName("height").item(0).getTextContent()));

		return new Video(directory.toString()+ "/" + file, x, y, width, height);
	}
	
	public void copyTo(File to) {
		File thisFile = new File(path);
		File newFile = new File(to.toString() + "/" + thisFile.getName());
		try {
			Files.copy(thisFile.toPath(), newFile.toPath());
		} catch (FileAlreadyExistsException e) {
			// Do nothing, it already exists
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
