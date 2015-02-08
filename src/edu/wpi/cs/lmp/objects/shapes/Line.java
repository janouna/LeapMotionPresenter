package edu.wpi.cs.lmp.objects.shapes;

import edu.wpi.cs.lmp.objects.IObject;

public class Line extends Shape implements IObject {
	private static final String path = "Shapes/Line.png";
	
	public Line() {
		super(path);
	}

	public Line(double x, double y, double width, double height,
			double angle) {
		super(path, x, y, width, height, angle);
	}

}
