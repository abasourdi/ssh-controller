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
import com.pumkin.sshcontroller.object.GlobalConfiguration;

public class GlobalConfigurationActivity extends SshControllerActivity {

	CheckBox autoConnect;
	CheckBox lookForHiddenFiles;
	CheckBox lockScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_global_configuration);

		autoConnect = (CheckBox) findViewById(R.id.autoConnectCheckBox);
		autoConnect.setChecked(GlobalConfiguration.isAutoConnectEnabled());

		lockScreen = (CheckBox) findViewById(R.id.lockScreenCheckBox);
		lockScreen.setChecked(GlobalConfiguration.isLockScreenEnabled());

		lookForHiddenFiles = (CheckBox) findViewById(R.id.lookForHiddenFilesCheckBox);
		lookForHiddenFiles.setChecked(GlobalConfiguration
				.islookForHiddenFilesEnabled());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		GlobalConfiguration.setAutoConnect(autoConnect.isChecked());
		GlobalConfiguration.setLockScreen(lockScreen.isChecked());
		GlobalConfiguration.setLookForHiddenFiles(lookForHiddenFiles
				.isChecked());
		GlobalConfiguration.save();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}

	public void autoConnectCheckBox(View v) {
		autoConnect.setChecked(!autoConnect.isChecked());
	}
}
