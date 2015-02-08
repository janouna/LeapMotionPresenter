package edu.wpi.cs.lmp.objects.shapes;

import edu.wpi.cs.lmp.objects.IObject;

public class Square extends Shape implements IObject {
	private static final String path = "Shapes/Square.png";
	
	public Square() {
		super(path);
	}

	public Square(double x, double y, double width, double height,
			double angle) {
		super(path, x, y, width, height, angle);
	}
}
