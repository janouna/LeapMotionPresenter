package edu.wpi.cs.lmp.objects.shapes;

import edu.wpi.cs.lmp.objects.IObject;

public class Circle extends Shape implements IObject {
	private static String path = "file:Shapes/Circle";	
	
	public Circle() {
		super(path);
		// TODO Auto-generated constructor stub
	}

	public Circle(double x, double y, double width, double height,
			double angle) {
		super(path, x, y, width, height, angle);
		// TODO Auto-generated constructor stub
	}

}
