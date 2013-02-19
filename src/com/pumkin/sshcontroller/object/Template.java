package com.pumkin.sshcontroller.object;

import java.util.ArrayList;

import com.pumkin.sshcontroller.display.ControllerDisplay;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class Template {

	public String name;
	public int[] colors;
	public int[] pressedColors;
	public int shape;
	public int colorBorder;
	public int border;
	public int stroke;
	public int width;
	public int height;

	public Template(String name, int[] colors, int[] pressedColors, int shape,
			int colorBorder, int border, int stroke, int width, int height) {
		this.name = name;
		this.colors = colors;
		this.pressedColors = pressedColors;
		this.shape = shape;
		this.colorBorder = colorBorder;
		this.border = border;
		this.stroke = stroke;
		this.width=width;
		this.height=height;
	}

	public Button generateButtonFromTemplate() {
		Button res=new Button(colors, pressedColors, shape, colorBorder, border, stroke);
		res.width=width;
		res.height=height;
		return res;
	}

	/**
	 * 
	 * @return all the available template
	 */
	public static ArrayList<Template> getTemplates() {
		ArrayList<Template> res = new ArrayList<Template>();
		int[] colors = { Color.RED, Color.BLACK };
		Template template = new Template("toto", colors, colors,
				GradientDrawable.OVAL, Color.DKGRAY, 5, 0, 90, 90);

		res.add(template);
		res.add(template);
		res.add(template);
		return res;
	}
}
