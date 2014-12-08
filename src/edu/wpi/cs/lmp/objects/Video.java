package edu.wpi.cs.lmp.objects;

import java.io.File;

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

	public Video () {
		super();
		instance = this;
		isPlaying = false;
		// Modify the media variable to take in desired URL or FILE
		media = new Media(new File("big_buck_bunny.mp4").toURI().toString());
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

	public Video (String path) {
		super();
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
	public Element toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Video fromXML(Node n) {
		Node d = n.getFirstChild();

		Video i = new Video(d.getNodeValue());

		d = d.getNextSibling();
		i.vidX.setValue(Double.parseDouble(d.getNodeValue()));
		d = d.getNextSibling();
		i.vidY.setValue(Double.parseDouble(d.getNodeValue()));
		d = d.getNextSibling();
		i.vidWidth.setValue(Double.parseDouble(d.getNodeValue()));
		d = d.getNextSibling();
		i.vidHeight.setValue(Double.parseDouble(d.getNodeValue()));

		return i;
	}
}
