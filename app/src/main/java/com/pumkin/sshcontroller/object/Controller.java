package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represent a controller, which is a set of button.
 * 
 * @author Laurent S.
 * 
 */
public class Controller implements Serializable {

	/**
	 * Transient parameter to have easy access to the current sshConfiguration
	 * and to its related client
	 */
	public transient SshConfiguration parent;

	/**
	 * Name of the controller, that will appear on the MainActivity
	 */
	public String name = "untitled";

	/**
	 * All the buttons of this controller, starts empty
	 */
	public ArrayList<Button> buttons = new ArrayList<Button>();

	public void addButton(Button button) {
		buttons.add(button);
	}

	/**
	 * Used to get a specified button just by his uuid
	 * @param uuid uuid of the button
	 * @return the selected button, or null
	 */
	public Button getButtonByUuid(String uuid) {
		for (int i = 0; i < buttons.size(); i++) {
			if (uuid.equals(buttons.get(i).uuid)) {
				return buttons.get(i);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param uuid uuid of the button
	 * @return true if the button was deleted, false otherwise
	 */
	public boolean deleteButtonByUuid(String uuid) {
		for (int i = 0; i < buttons.size(); i++) {
			if (uuid.equals(buttons.get(i).uuid)) {
				buttons.remove(i);
				return true;
			}
		}
		return false;
	}
}
