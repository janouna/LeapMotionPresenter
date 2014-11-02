package edu.wpi.cs.lmp.view;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public class LeapToolBarSubMenuHandler implements EventHandler<MouseEvent> {

	private ToggleButton mousedNode;
	private LeapToolBarGroup container;
	private LeapToolBar newMenu;
	private LeapToolBar mousedBar;

	public LeapToolBarSubMenuHandler(ToggleButton mousedNode,
			LeapToolBarGroup container, LeapToolBar mousedBar, LeapToolBar newMenu) {
		this.mousedNode = mousedNode;
		this.container = container;
		this.mousedBar = mousedBar;
		this.newMenu = newMenu;
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
			if (event.getY() > mousedNode.getLayoutY() + mousedNode.getHeight()) {
				// Remove higher level sub menus
				List<LeapToolBar> toRemove = new ArrayList<LeapToolBar>();
				for (int i = 0; i < container.getChildren().size(); i++) {
					LeapToolBar submenu = (LeapToolBar) container.getChildren()
							.get(i);
					if (submenu.getMenuLevel() >= newMenu.getMenuLevel()) {
						toRemove.add(submenu);
					}
				}
				container.getChildren().removeAll(toRemove);
				// Unselect any previously selected buttons
				for (int i=0; i < mousedBar.getAllButton().size(); i++) {
					mousedBar.getAllButton().get(i).setSelected(false);
				}
				// Set selected for new submenu
				mousedNode.setSelected(true);
				// Instantiate the desired submenu
				container.getChildren().add(newMenu);
				// I might reserve the animation transition soley for the main
				// toolbar rather than each new instantiation
				// newMenu.transitionIn();
			}
		}
	}

}
