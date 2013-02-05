package com.pumkin.sshcontroller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pumkin.sshcontroller.display.ControllerDisplay;
import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public class EditControllerActivity extends SshControllerActivity implements
		OnClickListener, OnLongClickListener, OnTouchListener {

	View currentVisibleView = null;
	RelativeLayout relativeLayout;

	public boolean isMoving = false;
	int initialPosX=0;
	int initialPosY=0;
	String uuid="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_controller);
		relativeLayout = (RelativeLayout) findViewById(R.id.controller_layout);
		Log.i(this.getClass().getName(), "nb buttons: "+controller.buttons.size());
	}
	@Override
	protected void onResume() {
		super.onResume();
		ControllerDisplay.resetLayout(this, relativeLayout, controller, this,
				this, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_controller_edit_mode, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (currentVisibleView == null) {
			super.onBackPressed();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		} else {
			hideCurrentVisibleView();
		}
	}

	public void quitEditMode(MenuItem menuItem) {
		hideCurrentVisibleView();
		onBackPressed();
	}

	public void hideCurrentVisibleView() {
		if (currentVisibleView != null) {
			currentVisibleView.setAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_top_out));
			currentVisibleView.setVisibility(View.GONE);
			currentVisibleView = null;
		}
	}

	public void showMenuCircularButtons(MenuItem menuItem) {
		hideCurrentVisibleView();
		View v = findViewById(R.id.menu_circular_buttons);
		v.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_top_in));
		v.setVisibility(View.VISIBLE);
		v.getParent().bringChildToFront(v);
		currentVisibleView = v;
	}
	
	public void showMenuRectangularButtons(MenuItem menuItem) {
		hideCurrentVisibleView();
		View v = findViewById(R.id.menu_rectangular_buttons);
		v.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_top_in));
		v.setVisibility(View.VISIBLE);
		v.getParent().bringChildToFront(v);
		currentVisibleView = v;
	}

	public void addButton(View v) {
		hideCurrentVisibleView();
		String buttonType = v.getTag().toString();
		Log.i(this.getClass().getName(), "current action: " + buttonType);
		
		Button tmpButton=new Button(buttonType);
		
		//
		
		try{
			LinearLayout lL=(LinearLayout) v;
			v=lL.getChildAt(0);
		}catch(Exception e){
			
		}
		tmpButton.height=SshControllerUtils.convertPxToDp(this, v.getHeight());
		tmpButton.width=SshControllerUtils.convertPxToDp(this, v.getWidth());
		
		
		controller.addButton(tmpButton);
		Controller.saveControllers();
		ControllerDisplay.resetLayout(this, relativeLayout, controller, this,
				this, this);
	}

	@Override
	public boolean onLongClick(View v) {
		Log.i(this.getClass().getName(), "long click on view: "
				+ v.getTag().toString());
		isMoving=true;
		return true;
	}

	@Override
	public void onClick(View view) {
		Log.i(this.getClass().getName(), "click on view: "
				+ view.getTag().toString());
		isMoving=false;
		Intent startNewActivityOpen = new Intent(
				EditControllerActivity.this, EditButtonActivity.class);
		
		//We get the action
		uuid=view.getTag().toString();
		startNewActivityOpen.putExtra("uuid", uuid);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			isMoving = false;
			Controller.saveControllers();
			break;
		case MotionEvent.ACTION_DOWN:
			uuid=view.getTag().toString();
			Button b=controller.getButtonByUuid(uuid);
			initialPosX=(int) event.getX();
			initialPosY=(int) event.getY();
			Log.i(this.getClass().getName(), "initial pos: "+initialPosX+"/"+initialPosY);
			break;
		case MotionEvent.ACTION_MOVE:
			if (isMoving) {
				Log.d(this.getClass().getName(), "touchevent: " + event.getX()
						+ "/" + event.getY() + " tag: " + uuid);
				Log.d(this.getClass().getName(), "touchevent: " + event.getAction());
				int x=(int) (view.getLeft()+event.getX())+initialPosX;;
				int y=(int) (view.getTop()+event.getY())+initialPosY;;
				ControllerDisplay.moveButton(this, relativeLayout, controller,  uuid, x-initialPosX, y-initialPosY);
			}
			break;
		}
		//Don't consume the event to allow onclick and onlongclick to work
		return false;
	}
}
