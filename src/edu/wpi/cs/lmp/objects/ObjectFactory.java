package edu.wpi.cs.lmp.objects;

public class ObjectFactory {
	private static ObjectFactory INSTANCE = new ObjectFactory();
	
	private ObjectFactory() {}
	
	public static ObjectFactory getInstance(){
		return INSTANCE;
	}
	
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
