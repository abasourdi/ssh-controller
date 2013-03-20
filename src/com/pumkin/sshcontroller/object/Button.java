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

	public String uuid;

	public Action onPress = new Action();
	// If 0, nothing, but if it's positive, we repeat this action with this
	// interval when the button is pressed

	public String name = "untitled";
	// Also refered as label

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
