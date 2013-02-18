package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.UUID;

public class Button implements Serializable {

	public Button(String type) {
		this.type = type;
		uuid = UUID.randomUUID().toString();
	}
	public Button(int[] colors, int[] pressedColor, int shape) {
		this.colors=colors;
		this.pressedColors=pressedColor;
		this.shape=shape;
		uuid = UUID.randomUUID().toString();
	}

	public String uuid;

	public String type;

	public Action onPress = new Action();
	// If 0, nothing, but if it's positive, we repeat this action until the
	// button is pressed with this interval
	public int repeatEvery = 0;

	public int currentState = 0;
	public Action state = new Action();

	public String name = "untitled";
	public String displayedName = "";

	public int[] colors;
	// Need to be implemented later on
	public int[] pressedColors;
	// Can be every shape according to android
	public int shape;

	public int marginLeft = 0;
	public int marginTop = 0;

	public int height = 80;
	public int width = 80;
}
