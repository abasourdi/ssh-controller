package com.pumkin.sshcontroller.activity;

import android.content.Intent;
import android.os.Bundle;
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

public class EditButtonActionActivity extends SshControllerActivity {

	Button button;

	TextView action;
	EditText repeatedInterval;
	LinearLayout actionStateLayout;
	TextView stateAction;
	CheckBox confirmation;
	ColorAdapter colorAdapter;
	ListView colorsList;
	ColorAdapter colorAdapterPushed;
	ListView colorsListPushed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_button_action);

		button = controller.getButtonByUuid(getIntent().getExtras().get("uuid")
				.toString());

		// label=(EditText) findViewById(R.id.label);
		action = (TextView) findViewById(R.id.action);
		confirmation = (CheckBox) findViewById(R.id.confirmation);
		repeatedInterval = (EditText) findViewById(R.id.repeatedInterval);
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
		save();
	}

	public void refresh() {
		//Set text view for action label
		action.setText(button.onPress.command);
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
		Controller.saveControllers();
		refresh();
	}

	public void editStateAction(View v) {
		Log.d(this.getClass().toString(), "editStateAction for uuid "
				+ button.uuid);
		refresh();
	}
}
