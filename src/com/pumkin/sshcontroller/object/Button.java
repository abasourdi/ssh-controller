package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.UUID;

public class Button implements Serializable {

	public Button(String type) {
		this.type = type;
		uuid = UUID.randomUUID().toString();
	}

	public Button(int[] colors, int[] pressedColor, int shape, int colorBorder,
			int border, int stroke) {
		this.colors = colors;
		this.pressedColors = pressedColor;
		this.shape = shape;
		this.colorBorder = colorBorder;
		this.border = border;
		this.stroke = stroke;
		uuid = UUID.randomUUID().toString();
	}

	public String uuid;

	public String type;

	public Action onPress = new Action();
	// If 0, nothing, but if it's positive, we repeat this action with this
	// interval when the button is pressed
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

	public int colorBorder;
	public int border;
	// Only for rectangle button
	public int stroke;

	public int marginLeft = 0;
	public int marginTop = 0;

	public int height = 80;
	public int width = 80;
}
