package edu.wpi.cs.lmp.view.radialmenu;

import edu.wpi.cs.lmp.leap.HandStateObservable;
import edu.wpi.cs.lmp.objects.IObject;
import edu.wpi.cs.lmp.objects.ObjectType;

public class RadialMenuFactory {
	private static final RadialMenuFactory INSTANCE = new RadialMenuFactory();
	
	private RadialMenuFactory() {};
	
	public static RadialMenuFactory getInstance() {
		return INSTANCE;
	}
	
	public RadialOptionsMenu createRadialMenu(IObject object, ObjectType type) {
		RadialOptionsMenu radialMenu = null;
		switch(type) {
		case IMAGE:
		case VIDEO:
		case TEXT:
			final double itemInnerRadius = 100;
			final double itemRadius = 180;
			final double centerClosedRadius = 28;
			final double centerOpenedRadius = 40;

			final String[] menus = new String[] { "FONT", "COLOR", "BOLD",
					"ITALICS", "UNDERLINE" };

			radialMenu = new RadialOptionsMenu(object, menus, itemInnerRadius, itemRadius,
					centerClosedRadius, centerOpenedRadius);
			break;
		default:
			break;
		}
		
		radialMenu.setTranslateX(HandStateObservable.getInstance().getObservableX().doubleValue());
		radialMenu.setTranslateY(HandStateObservable.getInstance().getObservableY().doubleValue());
		return radialMenu;
	}

}