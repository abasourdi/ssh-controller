package com.pumkin.sshcontroller.object;

import java.util.ArrayList;

import com.pumkin.sshcontroller.constants.Values;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class Template {

	public String name;
	public Design design;

	public Template(String name, Design design) {
		this.name = name;
		this.design = design;
	}

	public Template(String name, int[] colors, int[] pressedColors, int shape,
			int borderColor, int borderWidth, int cornerRadius, int width,
			int height) {
		this.name = name;
		this.design = new Design(colors, pressedColors, shape, borderColor,
				borderWidth, cornerRadius, width, height);
	}

	/**
	 * 
	 * @return all the available template
	 */
	public static ArrayList<Template> getTemplates() {
		ArrayList<Template> res = new ArrayList<Template>();

		int topBlue = Color.parseColor("#3b5998");
		int bottomBlue = Color.parseColor("#8b9dc3");

		int topRed= Color.parseColor("#98593b");
		int bottomRed = Color.parseColor("#c39d8b");

		int topGreen= Color.parseColor("#3b9859");
		int bottomGreen = Color.parseColor("#8bc39d");

		int topYellow= Color.parseColor("#98983b");
		int bottomYellow = Color.parseColor("#c3c38d");

		int border = Color.parseColor("#f7f7f7");

		int[] colorsBlue = { topBlue, bottomBlue };
		int[] pressedColorsBlue = { bottomBlue, topBlue };
		
		Design design = new Design(colorsBlue, pressedColorsBlue,
				GradientDrawable.OVAL, border, 2, 20, Values.defaultWidth,
				Values.defaultHeight);

		Template template = new Template("Blue oval button", design);
		res.add(template);
		
		design=design.clone();
		design.shape=GradientDrawable.RECTANGLE;
		template = new Template("Blue rectangle button", design);
		res.add(template);
		
		int[] colorsGreen = { topGreen, bottomGreen };
		int[] pressedColorsGreen = { bottomGreen, topGreen };
		
		design = new Design(colorsGreen, pressedColorsGreen,
				GradientDrawable.OVAL, border, 2, 20, Values.defaultWidth,
				Values.defaultHeight);

		template = new Template("Green oval button", design);
		res.add(template);
		
		design=design.clone();
		design.shape=GradientDrawable.RECTANGLE;
		template = new Template("Green rectangle button", design);
		res.add(template);
		
		int[] colorsYellow = { topYellow, bottomYellow };
		int[] pressedColorsYellow = { bottomYellow, topYellow };
		
		design = new Design(colorsYellow, pressedColorsYellow,
				GradientDrawable.OVAL, border, 2, 20, Values.defaultWidth,
				Values.defaultHeight);

		template = new Template("Yellow oval button", design);
		res.add(template);
		
		design=design.clone();
		design.shape=GradientDrawable.RECTANGLE;
		template = new Template("Yellow rectangle button", design);
		res.add(template);
		
		int[] colorsRed = { topRed, bottomRed };
		int[] pressedColorsRed = { bottomRed, topRed };
		
		design = new Design(colorsRed, pressedColorsRed,
				GradientDrawable.OVAL, border, 2, 20, Values.defaultWidth,
				Values.defaultHeight);

		template = new Template("Red oval button", design);
		res.add(template);
		
		design=design.clone();
		design.shape=GradientDrawable.RECTANGLE;
		template = new Template("Red rectangle button", design);
		res.add(template);
		
		return res;
	}
}
