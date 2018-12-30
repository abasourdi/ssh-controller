package com.pumkin.sshcontroller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.CurrentConfiguration;

public class ChooseColorActivity extends SshControllerActivity {

	//Will not be used maybe?
	Button button;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_button_design);
		button=CurrentConfiguration.controller.getButtonByUuid(getIntent().getExtras().get("uuid").toString());
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
	
	public void save(){}
	
	@Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //save everything
        save();
    }

	
	//TODO
	public void refresh(){
		
		
	}
	

	public void changeColorStart(View v) {
		Log.d(this.getClass().toString(), "editAction for uuid "+button.uuid);
//		refresh();
		Intent startNewActivityOpen = new Intent(
				ChooseColorActivity.this, ChooseScriptActivity.class);
		startNewActivityOpen.putExtra("uuid", button.uuid);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
}
