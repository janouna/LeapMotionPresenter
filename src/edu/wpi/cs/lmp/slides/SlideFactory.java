package edu.wpi.cs.lmp.slides;

public class SlideFactory {
	
	public static final SlideFactory INSTANCE = new SlideFactory();
	
	private SlideFactory() {}
	
	public static SlideFactory getInstance() {
		return INSTANCE;
	}
	
	// Should be more here later
	public Slide makeSlide() {
		return new Slide();
	}

}
