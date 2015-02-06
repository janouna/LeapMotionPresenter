package edu.wpi.cs.lmp.objects.shapes;

import edu.wpi.cs.lmp.objects.IObject;

public class Circle extends Shape implements IObject {
	private static final String path = "Shapes/Circle.png";	
	
	public Circle() {
		super(path);
	}

	public Circle(double x, double y, double width, double height,
			double angle) {
		super(path, x, y, width, height, angle);
	}

}
