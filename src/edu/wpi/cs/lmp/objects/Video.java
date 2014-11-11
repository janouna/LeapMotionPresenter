package edu.wpi.cs.lmp.objects;

import edu.wpi.cs.lmp.leap.HandStateObservable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Video extends MediaView implements IObject {
	
	private DoubleProperty vidWidth;
	private DoubleProperty vidHeight;
	
	private DoubleProperty vidX;
	private DoubleProperty vidY;
	
	// Video source
	private Media media;
	// Controls for the video
	private MediaPlayer mediaPlayer;
	
	private Video instance;
	
	public Video () {
		super();
		instance = this;		
		media = new Media("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
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
	public boolean inBounds(double x, double y) {
		double xPos = vidX.doubleValue();
		double yPos = vidY.doubleValue();
		double width = vidWidth.doubleValue();
		double height = vidHeight.doubleValue();
		
		// return (x > xPos-(width/2) && x < xPos+(width/2)) && (y > yPos-(height/2) && y < yPos+(height/2));
		return (x > xPos && x < xPos+(width)) && (y > yPos && y < yPos+(height));
	}

}
