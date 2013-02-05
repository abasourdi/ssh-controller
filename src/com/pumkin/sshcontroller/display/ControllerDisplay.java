package com.pumkin.sshcontroller.display;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pumkin.sshcontroller.activity.R;
import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

//This class will handle the actions of the controller
public class ControllerDisplay {

	public static Controller currentController=null;

	public static void resetLayout(Context context, RelativeLayout relativeLayout, Controller controller) {
		resetLayout(context, relativeLayout, controller, null, null, null);
	}
	
	public static void moveButton(Context context, RelativeLayout relativeLayout, Controller controller, String uuid, int x, int y){
		boolean find=false;
		int i=0;
		View buttonDisplayed=null;
		while(!find && i<relativeLayout.getChildCount()){
			buttonDisplayed=relativeLayout.getChildAt(i);
			if(uuid.equals(buttonDisplayed.getTag().toString())){
				find=true;
			}else{
				i++;
			}
		}
		if(find){
			//Then, we calculate if we have change the coordinate of the button
			Button button=controller.getButtonByUuid(uuid);
			//Calculate if
			if(relativeLayout.getWidth()<x+buttonDisplayed.getWidth()){
				x=relativeLayout.getWidth()-buttonDisplayed.getWidth();
			}
			if(relativeLayout.getHeight()<y+buttonDisplayed.getHeight()){
				y=relativeLayout.getHeight()-buttonDisplayed.getHeight();
			}
			
			int newMarginLeft=SshControllerUtils.convertPxToDp(context, x);
			int newMarginTop=SshControllerUtils.convertPxToDp(context, y);
			
			newMarginLeft=((int) newMarginLeft/30)*30;
			newMarginTop=((int) newMarginTop/30)*30;

			
			if(button.marginLeft!=newMarginLeft || button.marginTop!=newMarginTop){
				button.marginLeft=newMarginLeft;
				button.marginTop=newMarginTop;

				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(SshControllerUtils.convertDpToPx(context, newMarginLeft)
						, SshControllerUtils.convertDpToPx(context, newMarginTop), 0, 0);
				
				buttonDisplayed.setLayoutParams(layoutParams);
			}
		}
	}
	
	public static void resetLayout(Context context, RelativeLayout relativeLayout, Controller controller, OnClickListener onClickListener, OnLongClickListener onLongClickListener, OnTouchListener onTouchListener) {
		// First, we remove every element, then, we add them with their current
		// positions
		relativeLayout.removeAllViews();

		for (int i = 0; i < controller.buttons.size(); i++) {
			Button button=controller.buttons.get(i);
			TextView displayedButton = new TextView(context);
			displayedButton.setText(button.displayedName);

			displayedButton.setBackgroundDrawable(getDrawableFromType(context, button.type));
			displayedButton.setMinimumHeight(SshControllerUtils.convertDpToPx(
					context, button.height));
			displayedButton.setMinimumWidth(SshControllerUtils.convertDpToPx(
					context, button.width));
			// displayedButton.set

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			layoutParams.setMargins(SshControllerUtils.convertDpToPx(context, button.marginLeft)
					, SshControllerUtils.convertDpToPx(context, button.marginTop), 0, 0);
			
			displayedButton.setTag(button.uuid);
			displayedButton.setOnClickListener(onClickListener);
			displayedButton.setOnLongClickListener(onLongClickListener);
			displayedButton.setOnTouchListener(onTouchListener);
			
			if(displayedButton.getClass().equals(TextView.class)){
				((TextView)displayedButton).setGravity(Gravity.CENTER);
			}
			
			relativeLayout.addView(displayedButton, layoutParams);
		}
	}
	
	public static Drawable getDrawableFromType(Context context, String type){
		Drawable res=null;
		if(type.equals(context.getString(R.string.simple_button_green))){
			res=context.getResources()
					.getDrawable(R.drawable.button_simple_green);
		}else if(type.equals(context.getString(R.string.simple_button_blue))){
			res=context.getResources()
					.getDrawable(R.drawable.button_simple_blue);
		}else if(type.equals(context.getString(R.string.simple_button_red))){
			res=context.getResources()
					.getDrawable(R.drawable.button_simple_red);
		}else if(type.equals(context.getString(R.string.simple_button_blue_rectangular))){
			res=context.getResources()
					.getDrawable(R.drawable.button_simple_blue_rectangular);
		}
		return res;
	}
}
