package edu.wpi.cs.lmp.view;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class LeapToolBarGroupEventHandler implements EventHandler<MouseEvent>{
	
	private Node mousedNode;
	private LeapToolBarGroup container;
	private LeapToolBar newMenu;
	
	public LeapToolBarGroupEventHandler(Node mousedNode, LeapToolBarGroup container, LeapToolBar newMenu) {
		this.mousedNode = mousedNode;
		this.container = container;
		this.newMenu = newMenu;
	}

	@Override
	public void handle(MouseEvent event) {
		if(event.getEventType() == MouseEvent.MOUSE_EXITED) {
			
		}
	}

}
