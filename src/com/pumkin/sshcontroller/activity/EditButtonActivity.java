package com.pumkin.sshcontroller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.Controller;

public class EditButtonActivity extends SshControllerActivity {

	Button button;
	
	EditText label;
	TextView action;
	EditText repeatedInterval;
	LinearLayout actionStateLayout;
	TextView stateAction;
	CheckBox confirmation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_button);

		button=controller.getButtonByUuid(getIntent().getExtras().get("uuid").toString());
		
		label=(EditText) findViewById(R.id.label);
		action=(TextView) findViewById(R.id.action);
		confirmation=(CheckBox) findViewById(R.id.confirmation);
		repeatedInterval=(EditText) findViewById(R.id.repeatedInterval);
		actionStateLayout=(LinearLayout) findViewById(R.id.actionStateLayout);
		stateAction=(TextView) findViewById(R.id.stateAction);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in,
				R.anim.push_right_out);
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(this.getClass().getName(), "calling onResume");
		refresh();
	}
	
	public void save(){
		button.displayedName=label.getText().toString();
		Controller.saveControllers();
	}
	
	@Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //save everything
        save();
    }

	
	public void refresh(){
		label.setText(button.displayedName);
		String command=button.onPress.getFullCommand();
		if(command.trim().length()==0){
			action.setText("no action defined");
		}else{
			action.setText(command);	
		}

		String stateCommand=button.state.getFullCommand();
		if(stateCommand.trim().length()==0){
			stateAction.setText("no state action defined");	
		}else{
			stateAction.setText(stateCommand);
		}
		confirmation.setChecked(button.onPress.confirmation);
	}
	

	public void editAction(View v) {
		Log.d(this.getClass().toString(), "editAction for uuid "+button.uuid);
//		refresh();
		Intent startNewActivityOpen = new Intent(
				EditButtonActivity.this, ChooseScriptActivity.class);
		startNewActivityOpen.putExtra("uuid", button.uuid);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	public void onConfirmationChange(View v) {
		Log.d(this.getClass().toString(), "onConfirmationChange for uuid "+button.uuid);
		button.onPress.confirmation=!button.onPress.confirmation;
		Controller.saveControllers();
		refresh();
	}
	
	public void editStateAction(View v) {
		Log.d(this.getClass().toString(), "editStateAction for uuid "+button.uuid);
		refresh();
	}
}
