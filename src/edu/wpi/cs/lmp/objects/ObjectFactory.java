package edu.wpi.cs.lmp.objects;

public class ObjectFactory {
	public static final ObjectFactory INSTANCE = new ObjectFactory();
	
	private ObjectFactory() {}
	
	public IObject CreateObject(ObjectType type){
		IObject o = null;
		
		switch(type){
		case IMAGE:
			o = new Image();
			break;
		case TEXT:
			o = new TextBox();
			break;
		case VIDEO:
			o = new Video();
			break;
		}
		
		return o;
	}
}
