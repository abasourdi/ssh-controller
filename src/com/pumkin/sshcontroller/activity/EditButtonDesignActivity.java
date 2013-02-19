package com.pumkin.sshcontroller.activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.pumkin.sshcontroller.adapter.ColorAdapter;
import com.pumkin.sshcontroller.constants.Values;
import com.pumkin.sshcontroller.display.ControllerDisplay;
import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public class EditButtonDesignActivity extends SshControllerActivity implements
		OnSeekBarChangeListener, OnItemSelectedListener {

	Button button;

	EditText label;
	TextView action;
	EditText repeatedInterval;
	LinearLayout actionStateLayout;
	TextView stateAction;
	CheckBox confirmation;
	ColorAdapter colorAdapter;
	LinearLayout colorsList;
	// ListView colorsList;
	ColorAdapter colorAdapterPushed;
	LinearLayout colorsListPushed;
	// ListView colorsListPushed;
	TextView textViewWidth;
	SeekBar seekBarWidth;
	TextView textViewHeight;
	SeekBar seekBarHeight;

	TextView textViewBorder;
	SeekBar seekBarBorder;
	TextView textViewStroke;
	SeekBar seekBarStroke;
	RelativeLayout relativeLayoutStroke;
	Spinner shapeSpinner;
	
	android.widget.Button templateButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_button_design);

		button = controller.getButtonByUuid(getIntent().getExtras().get("uuid")
				.toString());
		colorsList = (LinearLayout) findViewById(R.id.colorsList);
		colorsListPushed = (LinearLayout) findViewById(R.id.colorsListPushed);

		templateButton = (android.widget.Button) findViewById(R.id.template_button);
		
		textViewWidth = (TextView) findViewById(R.id.textViewWidth);
		seekBarWidth = (SeekBar) findViewById(R.id.seekBarWidth);
		seekBarWidth.setMax(Values.possibleWidths.length);
		seekBarWidth.setOnSeekBarChangeListener(this);
		textViewHeight = (TextView) findViewById(R.id.textViewHeight);
		seekBarHeight = (SeekBar) findViewById(R.id.seekBarHeight);
		seekBarHeight.setMax(Values.possibleHeights.length);
		seekBarHeight.setOnSeekBarChangeListener(this);

		textViewStroke = (TextView) findViewById(R.id.textViewStroke);
		seekBarStroke = (SeekBar) findViewById(R.id.seekBarStroke);
		seekBarStroke.setMax(Values.possibleStroke.length);
		seekBarStroke.setOnSeekBarChangeListener(this);
		relativeLayoutStroke=(RelativeLayout) findViewById(R.id.relativeLayoutStroke);

		textViewBorder= (TextView) findViewById(R.id.textViewBorder);
		seekBarBorder = (SeekBar) findViewById(R.id.seekBarBorder);
		seekBarBorder.setMax(Values.possibleBorder.length);
		seekBarBorder.setOnSeekBarChangeListener(this);


		shapeSpinner = (Spinner) findViewById(R.id.shapeSpinner);
		shapeSpinner.setOnItemSelectedListener(this);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(this.getClass().getName(), "calling onResume");
		
		switch(button.shape){
		//Maybe do something better at some point?
		case GradientDrawable.OVAL:
			shapeSpinner.setSelection(1);
			break;
		case GradientDrawable.RECTANGLE:
			shapeSpinner.setSelection(0);
			break;
		}
		
		refresh();
	}

	public void save() {
		// button.displayedName=label.getText().toString();
		Controller.saveControllers();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// save everything
		save();
	}

	public void refresh() {
		colorAdapter = new ColorAdapter(this, button.colors);
		colorsList.removeAllViews();
		for (int i = 0; i < colorAdapter.getCount(); i++) {
			colorsList.addView(colorAdapter.getView(i, null, colorsList));
		}
		colorAdapterPushed = new ColorAdapter(this, button.pressedColors);
		colorsListPushed.removeAllViews();
		for (int i = 0; i < colorAdapterPushed.getCount(); i++) {
			colorsListPushed.addView(colorAdapterPushed.getView(i, null,
					colorsListPushed));
		}

		// We also update the text

		textViewWidth.setText(getString(R.string.widthWithDp, button.width));
		textViewHeight.setText(getString(R.string.heightWithDp, button.height));

		seekBarWidth.setProgress(SshControllerUtils.getPositionInt(
				button.width, Values.possibleWidths));
		seekBarHeight.setProgress(SshControllerUtils.getPositionInt(
				button.height, Values.possibleHeights));


		textViewStroke.setText(getString(R.string.stroke, button.stroke));
		seekBarStroke.setProgress(SshControllerUtils.getPositionInt(
				button.stroke, Values.possibleStroke));
		textViewBorder.setText(getString(R.string.border, button.border));
		seekBarBorder.setProgress(SshControllerUtils.getPositionInt(
				button.border, Values.possibleBorder));
		
		if(button.shape==GradientDrawable.OVAL){
			relativeLayoutStroke.setVisibility(View.GONE);
		}else{
			relativeLayoutStroke.setVisibility(View.VISIBLE);
		}
		//finally, we regenerate the button to have a nice layout
		Drawable drawable=ControllerDisplay.getStateListDrawableFromType(this, button);
		templateButton.setBackgroundDrawable(drawable);

		int height=SshControllerUtils.convertDpToPx(
				this, button.height);
		int width=SshControllerUtils.convertDpToPx(
				this, button.width);
		templateButton.setMinimumHeight(height);
		templateButton.setMaxHeight(height);
		templateButton.setMinimumWidth(width);
		templateButton.setMaxWidth(width);
	}

	public void changeTemplate(View v) {
		// TODO
		Log.d(this.getClass().toString(), "changeTemplate for uuid "
				+ button.uuid);
		refresh();
	}

	// Is used for the width/height
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == seekBarHeight) {
			button.height = Values.possibleHeights[progress];
		} else if (seekBar == seekBarWidth) {
			button.width = Values.possibleWidths[progress];
		} else if (seekBar == seekBarStroke) {
			button.stroke= Values.possibleStroke[progress];
		} else if (seekBar == seekBarBorder) {
			button.border = Values.possibleBorder[progress];
		}
		save();
		refresh();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View textView, int arg2,
			long arg3) {
		if (textView.getClass() == android.widget.TextView.class) {
			String text = ((TextView) textView).getText().toString();
			if(text.equals(getText(R.string.oval))){
				button.shape=GradientDrawable.OVAL;
			}else if(text.equals(getText(R.string.rectangle))){
				button.shape=GradientDrawable.RECTANGLE;
			}
		}
		refresh();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
