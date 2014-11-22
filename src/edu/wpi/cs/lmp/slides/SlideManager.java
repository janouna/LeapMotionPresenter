package edu.wpi.cs.lmp.slides;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SlideManager {

	private static final SlideManager INSTANCE = new SlideManager();
	private final IntegerProperty currentSlide;
	private final ListProperty<Slide> slides;

	private SlideManager() {
		currentSlide = new SimpleIntegerProperty(0);
		final ObservableList<Slide> observableList = FXCollections
				.observableArrayList();
		slides = new SimpleListProperty<Slide>(observableList);
		slides.add(new Slide());
	}

	public static SlideManager getInstance() {
		return INSTANCE;
	}

	public Slide getCurrentSlide() {
		return slides.get(currentSlide.intValue());
	}

	public Slide setCurrentSlide(int i) {
		if (i < slides.size() && i >= 0) {
			currentSlide.set(i);
			return getCurrentSlide();
		} else {
			return null;
		}
	}

	public List<Slide> getAllSlides() {
		return slides.getValue();
	}

	public int getCurrentSlideNumber() {
		return currentSlide.intValue();
	}

	public void addSlide() {
		slides.add(new Slide());
	}

	public void addSlide(int i) {
		slides.add(i, new Slide());
	}

	public void removeSlide(int i) {
		slides.remove(i);
	}

	public void moveSlide(int from, int to) {
		final Slide temp = slides.get(from);
		removeSlide(from);
		slides.add(to, temp);
	}

	public ListProperty<Slide> getSlidesProperty() {
		return slides;
	}

	public IntegerProperty getCurrentSlideProperty() {
		return currentSlide;
	}

}
