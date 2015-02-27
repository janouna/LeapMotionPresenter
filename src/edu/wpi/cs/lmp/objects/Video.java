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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.wpi.cs.lmp.leap.HandStateObservable;
import edu.wpi.cs.lmp.scenes.LeapScene;

/**
 * The Video presentation object for the Leap Motion Presenter
 * @author James Anouna
 * @author Johnny Hernandez
 *
 */
public class Video extends MediaView implements IObject {

	private DoubleProperty vidWidth;
	private DoubleProperty vidHeight;

	private DoubleProperty vidX;
	private DoubleProperty vidY;
	
	private DoubleProperty grabbedAtX;
	private DoubleProperty grabbedAtY;

	private boolean isPlaying;

	// Video source
	private final Media media;
	// Controls for the video
	private final MediaPlayer mediaPlayer;

	private final Video instance;
	
	private String path;

	public Video (String path) {
		super();
		this.path = path;
		instance = this;
		isPlaying = false;
		grabbedAtX = new SimpleDoubleProperty();
		grabbedAtY = new SimpleDoubleProperty();
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
		grabbedAtX = new SimpleDoubleProperty();
		grabbedAtY = new SimpleDoubleProperty();
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
		grabbedAtX.bind(HandStateObservable.getInstance().getObservableX());
		grabbedAtY.bind(HandStateObservable.getInstance().getObservableY());
		this.translateXProperty().bind(grabbedAtX.add(vidX.doubleValue() - grabbedAtX.doubleValue()));
		this.translateYProperty().bind(grabbedAtY.add(vidY.doubleValue() - grabbedAtY.doubleValue()));
		vidX.bind(this.translateXProperty());
		vidY.bind(this.translateYProperty());
	}

	@Override
	public void endMove() {
		this.translateXProperty().unbind();
		this.translateYProperty().unbind();
		grabbedAtX.unbind();
		grabbedAtY.unbind();
		vidX.unbind();
		vidY.unbind();
	}

	@Override
	public void resize(double percentageChangeWidth, double percentageChangeHeight) {
		vidWidth.set((vidWidth.doubleValue()*percentageChangeWidth)/100);
		vidHeight.set((vidHeight.doubleValue()*percentageChangeWidth)/100);
	}
	
	@Override
	public void rotate(double angle) {
		// TODO Auto-generated method stub
		
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
	public void radialMenuActions(int action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Element toXML(Document doc) {
		final Element vid = doc.createElement("Video");
		// Source field
		final File file = new File(media.getSource());
		final Element src = doc.createElement("src");
		src.appendChild(doc.createTextNode("Assets/" + file.getName()));
		vid.appendChild(src);
		// Position fields
		final Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode(String.valueOf(vidX.doubleValue())));
		vid.appendChild(x);
		final Element y = doc.createElement("y");
		y.appendChild(doc.createTextNode(String.valueOf(vidY.doubleValue())));
		vid.appendChild(y);
		// Size fields
		final Element width = doc.createElement("width");
		width.appendChild(doc.createTextNode(String.valueOf(vidWidth.doubleValue())));
		vid.appendChild(width);
		final Element height = doc.createElement("height");
		height.appendChild(doc.createTextNode(String.valueOf(vidHeight.doubleValue())));
		vid.appendChild(height);
		
		return vid;
	}

	public static Video fromXML(Element e, File directory) {
		final String file = e.getElementsByTagName("src").item(0).getTextContent();
		
		final double x = (Double.parseDouble(e.getElementsByTagName("x").item(0).getTextContent()));

		final double y = (Double.parseDouble(e.getElementsByTagName("y").item(0).getTextContent()));

		final double width = (Double.parseDouble(e.getElementsByTagName("width").item(0).getTextContent()));

		final double height = (Double.parseDouble(e.getElementsByTagName("height").item(0).getTextContent()));

		return new Video(directory.toString()+ "/" + file, x, y, width, height);
	}
	
	public void copyTo(File to) {
		final File thisFile = new File(path);
		final File newFile = new File(to.toString() + "/" + thisFile.getName());
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
