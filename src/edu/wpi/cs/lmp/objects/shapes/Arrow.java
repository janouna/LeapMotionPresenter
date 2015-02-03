package edu.wpi.cs.lmp.objects.shapes;

import edu.wpi.cs.lmp.objects.IObject;

public class Arrow extends Shape implements IObject {
	private static String path = "Shapes/Arrow.png";	
	
	public Arrow() {
		super(path);
	}

	public Arrow(double x, double y, double width, double height,
			double angle) {
		super(path, x, y, width, height, angle);
	}
}
