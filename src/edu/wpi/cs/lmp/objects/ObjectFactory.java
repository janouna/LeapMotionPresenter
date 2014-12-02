package edu.wpi.cs.lmp.objects;

public class ObjectFactory {
	private static final ObjectFactory INSTANCE = new ObjectFactory();

	private ObjectFactory() {}

	public static ObjectFactory getInstance(){
		return INSTANCE;
	}

	public IObject createObject(ObjectType type){
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

	public IObject createObject(ObjectType type, String path){
		IObject o = null;

		switch(type){
		case IMAGE:
			o = new Image(path);
			break;
		case TEXT:
			o = new TextBox();
			break;
		case VIDEO:
			o = new Video(path);
			break;
		}

		return o;
	}
}
