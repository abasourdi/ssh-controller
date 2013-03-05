package com.pumkin.sshcontroller.activity;

import android.content.Intent;
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
		seekBarStroke.setMax(Values.possibleCornerRadius.length);
		seekBarStroke.setOnSeekBarChangeListener(this);
		relativeLayoutStroke = (RelativeLayout) findViewById(R.id.relativeLayoutStroke);

		textViewBorder = (TextView) findViewById(R.id.textViewBorder);
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

		switch (button.design.shape) {
		// Maybe do something better at some point?
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
		colorAdapter = new ColorAdapter(this, button.design.colors);
		colorsList.removeAllViews();
		for (int i = 0; i < colorAdapter.getCount(); i++) {
			colorsList.addView(colorAdapter.getView(i, null, colorsList));
		}
		colorAdapterPushed = new ColorAdapter(this, button.design.pressedColors);
		colorsListPushed.removeAllViews();
		for (int i = 0; i < colorAdapterPushed.getCount(); i++) {
			colorsListPushed.addView(colorAdapterPushed.getView(i, null,
					colorsListPushed));
		}

		// We also update the text

		textViewWidth.setText(getString(R.string.widthWithDp, button.design.width));
		textViewHeight.setText(getString(R.string.heightWithDp, button.design.height));

		seekBarWidth.setProgress(SshControllerUtils.getPositionInt(
				button.design.width, Values.possibleWidths));
		seekBarHeight.setProgress(SshControllerUtils.getPositionInt(
				button.design.height, Values.possibleHeights));

		textViewStroke.setText(getString(R.string.stroke, button.design.cornerRadius));
		seekBarStroke.setProgress(SshControllerUtils.getPositionInt(
				button.design.cornerRadius, Values.possibleCornerRadius));
		textViewBorder.setText(getString(R.string.border, button.design.borderWidth));
		seekBarBorder.setProgress(SshControllerUtils.getPositionInt(
				button.design.borderWidth, Values.possibleBorder));

		if (button.design.shape == GradientDrawable.OVAL) {
			relativeLayoutStroke.setVisibility(View.GONE);
		} else {
			relativeLayoutStroke.setVisibility(View.VISIBLE);
		}
		// finally, we regenerate the button to have a nice layout
		Drawable drawable = ControllerDisplay.getStateListDrawableFromDesign(
				this, button.design);
		templateButton.setBackgroundDrawable(drawable);

		int height = SshControllerUtils.convertDpToPx(this, button.design.height);
		int width = SshControllerUtils.convertDpToPx(this, button.design.width);
		templateButton.setMinimumHeight(height);
		templateButton.setMaxHeight(height);
		templateButton.setMinimumWidth(width);
		templateButton.setMaxWidth(width);
		templateButton.setText(button.label);
		templateButton.setTextSize(button.labelSizeSp);
		templateButton.setTextColor(button.labelColor);
	}

	public void chooseTemplate(View v) {
		Log.d(this.getClass().toString(), "chooseTemplate for uuid "
				+ button.uuid);

		Intent startNewActivityOpen = new Intent(EditButtonDesignActivity.this,
				ChooseTemplateActivity.class);
		startNewActivityOpen.putExtra("uuid", button.uuid);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	// Is used for the width/height
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == seekBarHeight) {
			button.design.height = Values.possibleHeights[progress];
		} else if (seekBar == seekBarWidth) {
			button.design.width = Values.possibleWidths[progress];
		} else if (seekBar == seekBarStroke) {
			button.design.cornerRadius = Values.possibleCornerRadius[progress];
		} else if (seekBar == seekBarBorder) {
			button.design.borderWidth = Values.possibleBorder[progress];
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
			if (text.equals(getText(R.string.oval))) {
				button.design.shape = GradientDrawable.OVAL;
			} else if (text.equals(getText(R.string.rectangle))) {
				button.design.shape = GradientDrawable.RECTANGLE;
			}
		}
		refresh();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
