package com.pumkin.sshcontroller.display;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.object.ColorDesign;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

//This class will handle the actions of the controller
public class ControllerDisplay {

	public static Controller currentController = null;

	public static void resetLayout(Context context,
			RelativeLayout relativeLayout, Controller controller) {
		resetLayout(context, relativeLayout, controller, null, null, null);
	}

	public static void moveButton(Context context,
			RelativeLayout relativeLayout, Controller controller, String uuid,
			int x, int y) {
		boolean find = false;
		int i = 0;
		View buttonDisplayed = null;
		while (!find && i < relativeLayout.getChildCount()) {
			buttonDisplayed = relativeLayout.getChildAt(i);
			if (uuid.equals(buttonDisplayed.getTag().toString())) {
				find = true;
			} else {
				i++;
			}
		}
		if (find) {
			// Then, we calculate if we have change the coordinate of the button
			Button button = controller.getButtonByUuid(uuid);
			// Calculate if
			if (relativeLayout.getWidth() < x + buttonDisplayed.getWidth()) {
				x = relativeLayout.getWidth() - buttonDisplayed.getWidth();
			}
			if (relativeLayout.getHeight() < y + buttonDisplayed.getHeight()) {
				y = relativeLayout.getHeight() - buttonDisplayed.getHeight();
			}

			int newMarginLeft = SshControllerUtils.convertPxToDp(context, x);
			int newMarginTop = SshControllerUtils.convertPxToDp(context, y);

			newMarginLeft = ((int) newMarginLeft / 30) * 30;
			newMarginTop = ((int) newMarginTop / 30) * 30;

			if (button.marginLeft != newMarginLeft
					|| button.marginTop != newMarginTop) {
				button.marginLeft = newMarginLeft;
				button.marginTop = newMarginTop;

				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(SshControllerUtils.convertDpToPx(
						context, newMarginLeft), SshControllerUtils
						.convertDpToPx(context, newMarginTop), 0, 0);

				buttonDisplayed.setLayoutParams(layoutParams);
			}
		}
	}

	public static void resetLayout(Context context,
			RelativeLayout relativeLayout, Controller controller,
			OnClickListener onClickListener,
			OnLongClickListener onLongClickListener,
			OnTouchListener onTouchListener) {
		// First, we remove every element, then, we add them with their current
		// positions
		relativeLayout.removeAllViews();

		// We change the layout to center all the button

		int step = (SshControllerUtils.convertDpToPx(context, 30));

		relativeLayout.setPadding(
				(CurrentConfiguration.widthInPixel % step) / 2,
				(CurrentConfiguration.heightInPixel % step) / 2, 0, 0);

		for (int i = 0; i < controller.buttons.size(); i++) {
			Button button = controller.buttons.get(i);
			TextView displayedButton = new TextView(context);
			if (button.design.getClass().equals(ColorDesign.class)) {
				ColorDesign colorDesign=(ColorDesign) button.design;
				displayedButton.setText(colorDesign.label);
				displayedButton.setTextSize(colorDesign.labelSizeSp);
				displayedButton.setTextColor(colorDesign.labelColor);

				displayedButton
						.setBackgroundDrawable(getStateListDrawableFromDesign(
								context, colorDesign));
			}
			displayedButton.setMinimumHeight(SshControllerUtils.convertDpToPx(
					context, button.design.height));
			displayedButton.setMinimumWidth(SshControllerUtils.convertDpToPx(
					context, button.design.width));

			RelativeLayout.LayoutParams layoutParams = getLayoutParams(context,
					button);

			displayedButton.setTag(button.uuid);
			displayedButton.setOnClickListener(onClickListener);
			displayedButton.setOnLongClickListener(onLongClickListener);
			displayedButton.setOnTouchListener(onTouchListener);

			if (displayedButton.getClass().equals(TextView.class)) {
				((TextView) displayedButton).setGravity(Gravity.CENTER);
			}

			relativeLayout.addView(displayedButton, layoutParams);
		}
	}

	public static LayoutParams getLayoutParams(Context context, Button button) {
		return getLayoutParams(context, button.marginLeft, button.marginTop);
	}

	public static LayoutParams getLayoutParams(Context context, int marginLeft,
			int marginTop) {
		LayoutParams res = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		res.setMargins(SshControllerUtils.convertDpToPx(context, marginLeft),
				SshControllerUtils.convertDpToPx(context, marginTop), 0, 0);
		return res;
	}

	public static Drawable getDrawableFromDesign(Context context,
			ColorDesign design, boolean pressed) {

		GradientDrawable res;
		if (pressed) {
			res = new GradientDrawable(Orientation.TOP_BOTTOM, design.colors);
		} else {
			res = new GradientDrawable(Orientation.TOP_BOTTOM,
					design.pressedColors);
		}
		res.setShape(design.shape);
		if (design.shape != GradientDrawable.OVAL && design.cornerRadius != 0) {
			float f = Float.parseFloat(""
					+ SshControllerUtils.convertDpToPx(context,
							design.cornerRadius));
			res.setCornerRadius(f);
		}
		if (design.borderWidth != 0) {
			res.setStroke(SshControllerUtils.convertDpToPx(context,
					design.borderWidth), design.borderColor);
		}
		return res;
	}

	public static StateListDrawable getStateListDrawableFromDesign(
			Context context, ColorDesign design) {
		StateListDrawable res = new StateListDrawable();
		res.addState(new int[] { android.R.attr.state_pressed },
				getDrawableFromDesign(context, design, true));

		// Normal state
		res.addState(new int[] {},
				getDrawableFromDesign(context, design, false));
		return res;
	}

}
