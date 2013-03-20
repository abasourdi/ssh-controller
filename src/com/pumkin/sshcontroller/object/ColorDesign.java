package com.pumkin.sshcontroller.object;

import java.io.Serializable;

import android.graphics.Color;

public class ColorDesign extends AbstractDesign implements Serializable {

	/**
	 * 
	 * @param colors
	 *            basic colors
	 * @param pressedColors
	 *            colors when the button is pressed
	 * @param shape
	 *            shape of the button, either Rectangle or Oval
	 * @param borderColor
	 *            Color of the border
	 * @param borderWidth
	 *            Width of the border
	 * @param cornerRadius
	 *            Radius of the corner, only used when shape = Rectangle
	 * @param width
	 *            width, should be something times 30 (except 0)
	 * @param height
	 *            height, should be something times 30 (except 0)
	 */
	public ColorDesign(int[] colors, int[] pressedColors, int shape,
			int borderColor, int borderWidth, int cornerRadius, int width,
			int height, int angle) {
		this.colors = colors;
		this.pressedColors = pressedColors;
		this.shape = shape;
		this.borderColor = borderColor;
		this.borderWidth = borderWidth;
		this.cornerRadius = cornerRadius;
		this.width = width;
		this.height = height;
		this.angle=angle;
	}


	public String label = "";
	public int labelSizeSp=15;
	public int labelColor=Color.BLACK;
	
	public int[] colors;
	public int[] pressedColors;
	public int shape;
	public int borderColor;
	public int borderWidth;
	public int cornerRadius;
	public int angle;

	public ColorDesign clone() {
		return new ColorDesign(colors, pressedColors, shape, borderColor,
				borderWidth, cornerRadius, width, height, angle);
	}

}
