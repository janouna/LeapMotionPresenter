package edu.wpi.cs.lmp.objects.shapes;

import edu.wpi.cs.lmp.objects.IObject;

public class Square extends Shape implements IObject {
	private static String path = "file:Shapes/Square";
	
	public Square() {
		super(path);
		// TODO Auto-generated constructor stub
	}

	public Square(double x, double y, double width, double height,
			double angle) {
		super(path, x, y, width, height, angle);
		// TODO Auto-generated constructor stub
	}
}
