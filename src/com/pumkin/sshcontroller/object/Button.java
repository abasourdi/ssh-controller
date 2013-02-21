package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.UUID;

public class Button implements Serializable {

	public Button(Design design) {
		uuid = UUID.randomUUID().toString();
		this.design=design;
	}

	public String uuid;

	public Action onPress = new Action();
	// If 0, nothing, but if it's positive, we repeat this action with this
	// interval when the button is pressed
	public int repeatEvery = 0;

	public int currentState = 0;
	public Action state = new Action();

	public String name = "untitled";
	public String displayedName = "";

	public Design design;

	public int marginLeft = 0;
	public int marginTop = 0;
}
