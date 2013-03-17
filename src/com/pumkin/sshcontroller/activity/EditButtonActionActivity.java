package com.pumkin.sshcontroller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pumkin.sshcontroller.adapter.ColorAdapter;
import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;

public class EditButtonActionActivity extends SshControllerActivity implements
		TextWatcher {

	Button button;

	TextView action;

	LinearLayout actionStateLayout;
	TextView stateAction;
	CheckBox confirmation;
	CheckBox sendCoordinate;
	ColorAdapter colorAdapter;
	ListView colorsList;
	ColorAdapter colorAdapterPushed;
	ListView colorsListPushed;

	CheckBox repeated;
	EditText repeatedInterval;
	LinearLayout repeatedActionCheckBoxLinearLayout;
	LinearLayout repeatedActionEditBoxLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_button_action);

		button = CurrentConfiguration.controller.getButtonByUuid(getIntent()
				.getExtras().get("uuid").toString());

		// label=(EditText) findViewById(R.id.label);
		action = (TextView) findViewById(R.id.action);
		confirmation = (CheckBox) findViewById(R.id.confirmation);
		confirmation.setChecked(button.onPress.confirmation);
		sendCoordinate = (CheckBox) findViewById(R.id.sendCoordinate);
		sendCoordinate.setChecked(button.onPress.sendCoordinate);

		repeatedActionCheckBoxLinearLayout = (LinearLayout) findViewById(R.id.repeatedActionCheckBoxLinearLayout);
		repeated = (CheckBox) findViewById(R.id.repeatedActionCheckBox);
		repeated.setChecked(button.onPress.repeatedEnabled);
		repeatedActionEditBoxLinearLayout = (LinearLayout) findViewById(R.id.repeatedActionEditBoxLinearLayout);
		repeatedInterval = (EditText) findViewById(R.id.repeatedInterval);
		repeatedInterval.addTextChangedListener(this);
		repeatedInterval.setText("" + button.onPress.repeatedInterval);

		actionStateLayout = (LinearLayout) findViewById(R.id.actionStateLayout);
		stateAction = (TextView) findViewById(R.id.stateAction);

		colorsList = (ListView) findViewById(R.id.colorsList);
		colorsListPushed = (ListView) findViewById(R.id.colorsListPushed);
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
		Controller.saveControllers();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// save everything

		int newRepeatedInterval = 50;
		try {
			newRepeatedInterval = Integer.parseInt(repeatedInterval
					.getText().toString());
			if(newRepeatedInterval<10){
				newRepeatedInterval=10;
				//Warning message
			}
		} catch (Exception e) {
			// Exception caught when the user doesn't input anything, then,
			// default value
			button.onPress.repeatedInterval=50;
			repeatedInterval.setText("" + button.onPress.repeatedInterval);
		}
		if(button.onPress.repeatedInterval!=newRepeatedInterval){
			button.onPress.repeatedInterval=newRepeatedInterval;
			repeatedInterval.setText("" + button.onPress.repeatedInterval);
		}
		
		save();
	}

	public void refresh() {
		// Set text view for action label
		if (button.onPress.command == null || "".equals(button.onPress.command)) {
			action.setText(getText(R.string.noActionDefined));
		} else {
			action.setText(button.onPress.command);
		}

		/*
		 * never any repeat action now
		if (button.onPress.confirmation) {
			repeatedActionCheckBoxLinearLayout.setVisibility(View.GONE);
			repeatedActionEditBoxLinearLayout.setVisibility(View.GONE);
		} else {
			repeatedActionCheckBoxLinearLayout.setVisibility(View.VISIBLE);
			if (button.onPress.repeatedEnabled) {
				repeatedActionEditBoxLinearLayout.setVisibility(View.VISIBLE);
			} else {
				repeatedActionEditBoxLinearLayout.setVisibility(View.GONE);
			}
		}
		*/

	}

	public void editAction(View v) {
		Log.d(this.getClass().toString(), "editAction for uuid " + button.uuid);
		// refresh();
		Intent startNewActivityOpen = new Intent(EditButtonActionActivity.this,
				ChooseScriptActivity.class);
		startNewActivityOpen.putExtra("uuid", button.uuid);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	public void onConfirmationChange(View v) {
		Log.d(this.getClass().toString(), "onConfirmationChange for uuid "
				+ button.uuid);
		button.onPress.confirmation = !button.onPress.confirmation;
		// Then, we need to set repeatedEnabled to false
		if (button.onPress.confirmation) {
			if (button.onPress.repeatedEnabled) {
				button.onPress.repeatedEnabled = false;
				repeated.setChecked(false);
			}
		}
		Controller.saveControllers();
		refresh();
	}

	public void onSendCoordinateChange(View v) {
		Log.d(this.getClass().toString(), "onSendCoordinateChange for uuid "
				+ button.uuid);
		button.onPress.sendCoordinate = !button.onPress.sendCoordinate;
		Controller.saveControllers();
		refresh();
	}

	public void onRepeatedActionChange(View v) {
		Log.d(this.getClass().toString(), "onRepeatedActionChange for uuid "
				+ button.uuid);
		button.onPress.repeatedEnabled = !button.onPress.repeatedEnabled;
		Controller.saveControllers();
		refresh();
	}

	public void editStateAction(View v) {
		Log.d(this.getClass().toString(), "editStateAction for uuid "
				+ button.uuid);
		refresh();
	}
	
	//Display a warning message maybe?

	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
}
