package com.pumkin.sshcontroller.object;

import java.util.ArrayList;

import com.pumkin.sshcontroller.constants.Values;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

/**
 * Basic templates for the color design button
 * @author Laurent S.
 *
 */
public class Template {

	public String name;
	public ColorDesign design;

	public Template(String name, ColorDesign design) {
		this.name = name;
		this.design = design;
	}

	public Template(String name, int[] colors, int[] pressedColors, int shape,
			int borderColor, int borderWidth, int cornerRadius, int width,
			int height, int angle) {
		this.name = name;
		this.design = new ColorDesign(colors, pressedColors, shape, borderColor,
				borderWidth, cornerRadius, width, height, angle);
	}

	/**
	 * 
	 * @return all the available template
	 */
	public static ArrayList<Template> getTemplates() {
		ArrayList<Template> res = new ArrayList<Template>();

		int defaultAngle=0;
		
		int topBlue = Color.parseColor("#3b5998");
		int bottomBlue = Color.parseColor("#8b9dc3");

		int topRed= Color.parseColor("#98593b");
		int bottomRed = Color.parseColor("#c39d8b");

		int topGreen= Color.parseColor("#3b9859");
		int bottomGreen = Color.parseColor("#8bc39d");

		int topYellow= Color.parseColor("#98983b");
		int bottomYellow = Color.parseColor("#c3c38d");

		int topPink= Color.parseColor("#ee3A8C");
		int bottomPink = Color.parseColor("#cd3278");

		int border = Color.parseColor("#f7f7f7");

		int[] colorsBlue = { topBlue, bottomBlue };
		int[] pressedColorsBlue = { bottomBlue, topBlue };
		
		ColorDesign design = new ColorDesign(colorsBlue, pressedColorsBlue,
				GradientDrawable.OVAL, border, 2, 20, Values.defaultWidth,
				Values.defaultHeight, defaultAngle);

		Template template = new Template("Blue oval button", design);
		res.add(template);
		
		design=design.clone();
		design.shape=GradientDrawable.RECTANGLE;
		template = new Template("Blue rectangle button", design);
		res.add(template);
		
		int[] colorsGreen = { topGreen, bottomGreen };
		int[] pressedColorsGreen = { bottomGreen, topGreen };
		
		design = new ColorDesign(colorsGreen, pressedColorsGreen,
				GradientDrawable.OVAL, border, 2, 20, Values.defaultWidth,
				Values.defaultHeight, defaultAngle);

		template = new Template("Green oval button", design);
		res.add(template);
		
		design=design.clone();
		design.shape=GradientDrawable.RECTANGLE;
		template = new Template("Green rectangle button", design);
		res.add(template);
		
		int[] colorsYellow = { topYellow, bottomYellow };
		int[] pressedColorsYellow = { bottomYellow, topYellow };
		
		design = new ColorDesign(colorsYellow, pressedColorsYellow,
				GradientDrawable.OVAL, border, 2, 20, Values.defaultWidth,
				Values.defaultHeight, defaultAngle);

		template = new Template("Yellow oval button", design);
		res.add(template);
		
		design=design.clone();
		design.shape=GradientDrawable.RECTANGLE;
		template = new Template("Yellow rectangle button", design);
		res.add(template);
		
		int[] colorsRed = { topRed, bottomRed };
		int[] pressedColorsRed = { bottomRed, topRed };
		
		design = new ColorDesign(colorsRed, pressedColorsRed,
				GradientDrawable.OVAL, border, 2, 20, Values.defaultWidth,
				Values.defaultHeight, defaultAngle);

		template = new Template("Red oval button", design);
		res.add(template);
		
		design=design.clone();
		design.shape=GradientDrawable.RECTANGLE;
		template = new Template("Red rectangle button", design);
		res.add(template);
		
		int[] colorsPink = { topPink, bottomPink };
		int[] pressedColorsPink = { bottomPink, topPink };
		
		design = new ColorDesign(colorsPink, pressedColorsPink,
				GradientDrawable.OVAL, border, 2, 20, Values.defaultWidth,
				Values.defaultHeight, defaultAngle);

		template = new Template("Pink oval button", design);
		//res.add(template);
		
		design=design.clone();
		design.shape=GradientDrawable.RECTANGLE;
		template = new Template("Pink rectangle button", design);
//		res.add(template);
		
		return res;
	}
}
