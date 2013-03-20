package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.UUID;

import android.graphics.Color;

public class Button implements Serializable {

	/**
	 * Create a button with the specified design
	 * @param design design of the button
	 */
	public Button(AbstractDesign design) {
		uuid = UUID.randomUUID().toString();
		this.design = design;
	}

	/**
	 * Unique uuid that is used as a unique id for the button
	 */
	public String uuid;

	/**
	 * action on press
	 */
	public Action onPress = new Action();

	/**
	 * Design of the button
	 */
	public AbstractDesign design;

	/**
	 * Left margin of the button, in dp
	 */
	public int marginLeft = 0;
	
	/**
	 * Top margin of the button, in dp
	 */
	public int marginTop = 0;
}
