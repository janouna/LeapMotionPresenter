package edu.wpi.cs.lmp.slides;

import java.util.ArrayList;
import java.util.List;

public class SlideManager {
	
	private static final SlideManager INSTANCE = new SlideManager();
	private int currentSlide;
	private List<Slide> slides;
	
	private SlideManager() {
		currentSlide = 0;
		slides = new ArrayList<Slide>();
		slides.add(new Slide());
	}
	
	public static SlideManager getInstance() {
		return INSTANCE;
	}
	
	public Slide getCurrentSlide() {
		return slides.get(currentSlide);
	}

}
