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
import com.pumkin.sshcontroller.object.ColorDesign;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public class EditButtonDesignActivity extends SshActiveControllerActivity
		implements OnSeekBarChangeListener, OnItemSelectedListener {

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

		button = CurrentConfiguration.controller.getButtonByUuid(getIntent()
				.getExtras().get("uuid").toString());
		colorsList = (LinearLayout) findViewById(R.id.colorsList);
		colorsListPushed = (LinearLayout) findViewById(R.id.colorsListPushed);

		templateButton = (android.widget.Button) findViewById(R.id.template_button);

		textViewWidth = (TextView) findViewById(R.id.textViewWidth);
		seekBarWidth = (SeekBar) findViewById(R.id.seekBarWidth);
		seekBarWidth.setMax(Values.possibleWidths.length - 1);
		seekBarWidth.setOnSeekBarChangeListener(this);
		textViewHeight = (TextView) findViewById(R.id.textViewHeight);
		seekBarHeight = (SeekBar) findViewById(R.id.seekBarHeight);
		seekBarHeight.setMax(Values.possibleHeights.length - 1);
		seekBarHeight.setOnSeekBarChangeListener(this);

		textViewStroke = (TextView) findViewById(R.id.textViewStroke);
		seekBarStroke = (SeekBar) findViewById(R.id.seekBarStroke);
		seekBarStroke.setMax(Values.possibleCornerRadius.length - 1);
		seekBarStroke.setOnSeekBarChangeListener(this);
		relativeLayoutStroke = (RelativeLayout) findViewById(R.id.relativeLayoutStroke);

		textViewBorder = (TextView) findViewById(R.id.textViewBorder);
		seekBarBorder = (SeekBar) findViewById(R.id.seekBarBorder);
		seekBarBorder.setMax(Values.possibleBorder.length - 1);
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

		if (button.design.getClass().equals(ColorDesign.class)) {
			ColorDesign colorDesign = (ColorDesign) button.design;
			switch (colorDesign.shape) {
			// Maybe do something better at some point?
			case GradientDrawable.OVAL:
				shapeSpinner.setSelection(1);
				break;
			case GradientDrawable.RECTANGLE:
				shapeSpinner.setSelection(0);
				break;
			}
		}

		refresh();
	}

	public void save() {
		// button.displayedName=label.getText().toString();
		SshControllerUtils.saveSshConfigurations();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// save everything
		save();
	}

	public void refresh() {
		if (button.design.getClass().equals(ColorDesign.class)) {
			ColorDesign colorDesign = (ColorDesign) button.design;
			colorAdapter = new ColorAdapter(this, colorDesign.colors);
			colorsList.removeAllViews();
			for (int i = 0; i < colorAdapter.getCount(); i++) {
				colorsList.addView(colorAdapter.getView(i, null, colorsList));
			}
			colorAdapterPushed = new ColorAdapter(this,
					colorDesign.pressedColors);
			colorsListPushed.removeAllViews();
			for (int i = 0; i < colorAdapterPushed.getCount(); i++) {
				colorsListPushed.addView(colorAdapterPushed.getView(i, null,
						colorsListPushed));
			}

			// We also update the text

			textViewWidth.setText(getString(R.string.widthWithDp,
					button.design.width));
			textViewHeight.setText(getString(R.string.heightWithDp,
					button.design.height));

			seekBarWidth.setProgress(SshControllerUtils.getPositionInt(
					button.design.width, Values.possibleWidths));
			seekBarHeight.setProgress(SshControllerUtils.getPositionInt(
					button.design.height, Values.possibleHeights));

			textViewStroke.setText(getString(R.string.stroke,
					colorDesign.cornerRadius));
			seekBarStroke.setProgress(SshControllerUtils.getPositionInt(
					colorDesign.cornerRadius, Values.possibleCornerRadius));
			textViewBorder.setText(getString(R.string.border,
					colorDesign.borderWidth));
			seekBarBorder.setProgress(SshControllerUtils.getPositionInt(
					colorDesign.borderWidth, Values.possibleBorder));

			if (colorDesign.shape == GradientDrawable.OVAL) {
				relativeLayoutStroke.setVisibility(View.GONE);
			} else {
				relativeLayoutStroke.setVisibility(View.VISIBLE);
			}
			// finally, we regenerate the button to have a nice layout
			Drawable drawable = ControllerDisplay
					.getStateListDrawableFromDesign(this, colorDesign);
			templateButton.setBackgroundDrawable(drawable);

			int height = SshControllerUtils.convertDpToPx(this,
					button.design.height);
			int width = SshControllerUtils.convertDpToPx(this,
					button.design.width);
			templateButton.setMinimumHeight(height);
			templateButton.setMaxHeight(height);
			templateButton.setMinimumWidth(width);
			templateButton.setMaxWidth(width);
			templateButton.setText(colorDesign.label);
			templateButton.setTextSize(colorDesign.labelSizeSp);
			templateButton.setTextColor(colorDesign.labelColor);
		}
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
		if (button.design.getClass().equals(ColorDesign.class)) {
			ColorDesign colorDesign = (ColorDesign) button.design;
			if (seekBar == seekBarHeight) {
				button.design.height = Values.possibleHeights[progress];
			} else if (seekBar == seekBarWidth) {
				button.design.width = Values.possibleWidths[progress];
			} else if (seekBar == seekBarStroke) {
				colorDesign.cornerRadius = Values.possibleCornerRadius[progress];
			} else if (seekBar == seekBarBorder) {
				colorDesign.borderWidth = Values.possibleBorder[progress];
			}
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
			if (button.design.getClass().equals(ColorDesign.class)) {
				ColorDesign colorDesign = (ColorDesign) button.design;
				String text = ((TextView) textView).getText().toString();
				if (text.equals(getText(R.string.oval))) {
					colorDesign.shape = GradientDrawable.OVAL;
				} else if (text.equals(getText(R.string.rectangle))) {
					colorDesign.shape = GradientDrawable.RECTANGLE;
				}
			}
		}
		refresh();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
