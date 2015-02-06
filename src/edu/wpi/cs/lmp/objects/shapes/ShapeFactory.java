package edu.wpi.cs.lmp.objects.shapes;

import edu.wpi.cs.lmp.objects.IObject;

public class ShapeFactory {
	private static final ShapeFactory INSTANCE = new ShapeFactory();

	private ShapeFactory() {}

	public static ShapeFactory getInstance(){
		return INSTANCE;
	}

	public IObject createShape(ShapeType type){
		IObject o = null;

		switch(type){
		case CIRCLE:
			o = new Circle();
			break;
		case SQUARE:
			o = new Square();
			break;
		case LINE:
			o = new Line();
			break;
		case ARROW:
			o = new Arrow();
			break;
		default:
			break;
		}

		return o;
	}

}
