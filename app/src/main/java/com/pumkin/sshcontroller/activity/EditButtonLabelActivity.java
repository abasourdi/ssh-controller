package com.pumkin.sshcontroller.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.pumkin.sshcontroller.constants.Values;
import com.pumkin.sshcontroller.display.ControllerDisplay;
import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.ColorDesign;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public class EditButtonLabelActivity extends SshActiveControllerActivity
		implements OnSeekBarChangeListener, TextWatcher {

	Button button;

	EditText label;

	TextView textViewLabelSize;
	SeekBar seekBarLabelSize;

	TextView textViewAngle;
	SeekBar seekBarAngle;

	android.widget.Button templateButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_button_label);

		button = CurrentConfiguration.controller.getButtonByUuid(getIntent()
				.getExtras().get("uuid").toString());

		templateButton = (android.widget.Button) findViewById(R.id.template_button);

		textViewAngle = (TextView) findViewById(R.id.textViewAngle);
		// textViewAngle.setText(getString(R.string.angle,
		// button.design.angle));

		seekBarAngle = (SeekBar) findViewById(R.id.seekBarAngle);
		seekBarAngle.setMax(Values.possibleAngle.length - 1);
		seekBarAngle.setOnSeekBarChangeListener(this);

		textViewLabelSize = (TextView) findViewById(R.id.textViewLabelSize);
		// textViewLabelSize.setText(getString(R.string.labelSize,
		// button.labelSizeSp));
		seekBarLabelSize = (SeekBar) findViewById(R.id.seekBarLabelSize);
		seekBarLabelSize.setMax(Values.possibleLabelSize.length - 1);
		seekBarLabelSize.setOnSeekBarChangeListener(this);

		label = (EditText) findViewById(R.id.EditTextLabel);
		// label.setText(button.label);
		label.addTextChangedListener(this);

		refresh();
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
		refresh();
	}

	public void save() {
		SshControllerUtils.saveSshConfigurations();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// save everything
		save();
	}

	public void refresh() {
		if (button.design.getClass().equals(ColorDesign.class)) {
			ColorDesign colorDesign = (ColorDesign) button.design;
			// We also update the text
			seekBarAngle.setProgress(SshControllerUtils.getPositionInt(
					colorDesign.angle, Values.possibleAngle));
			seekBarLabelSize.setProgress(SshControllerUtils.getPositionInt(
					colorDesign.labelSizeSp, Values.possibleLabelSize));

			// finally, we regenerate the button
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
			templateButton.setGravity(Gravity.CENTER);
			// No rotation as api 11 is required for that
			// templateButton.setRotation((float)button.design.angle);
		}
	}

	public void chooseTemplate(View v) {
		Log.d(this.getClass().toString(), "chooseTemplate for uuid "
				+ button.uuid);
		Intent startNewActivityOpen = new Intent(EditButtonLabelActivity.this,
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
			if (seekBar == seekBarAngle) {
				colorDesign.angle = Values.possibleAngle[progress];
				textViewAngle.setText(getString(R.string.angle,
						colorDesign.angle));
			} else if (seekBar == seekBarLabelSize) {
				colorDesign.labelSizeSp = Values.possibleLabelSize[progress];
				textViewLabelSize.setText(getString(R.string.labelSize,
						colorDesign.labelSizeSp));
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
	public void afterTextChanged(Editable arg0) {
		if (button.design.getClass().equals(ColorDesign.class)) {
			ColorDesign colorDesign = (ColorDesign) button.design;
			colorDesign.label = label.getText().toString();
			refresh();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		if (button.design.getClass().equals(ColorDesign.class)) {
			ColorDesign colorDesign = (ColorDesign) button.design;
			colorDesign.label = label.getText().toString();
			refresh();
		}
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if (button.design.getClass().equals(ColorDesign.class)) {
			ColorDesign colorDesign = (ColorDesign) button.design;
			colorDesign.label = label.getText().toString();
			refresh();
		}
	}
}
