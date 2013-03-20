package com.pumkin.sshcontroller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.pumkin.sshcontroller.utils.SshControllerUtils;

public class GlobalConfigurationActivity extends SshControllerActivity {

	CheckBox autoConnect;
	CheckBox lookForHiddenFiles;
	CheckBox lockScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_global_configuration);

		autoConnect = (CheckBox) findViewById(R.id.autoConnectCheckBox);
		autoConnect.setChecked(SshControllerUtils.isAutoConnectEnabled());

		lockScreen = (CheckBox) findViewById(R.id.lockScreenCheckBox);
		lockScreen.setChecked(SshControllerUtils.isLockScreenEnabled());

		lookForHiddenFiles = (CheckBox) findViewById(R.id.lookForHiddenFilesCheckBox);
		lookForHiddenFiles.setChecked(SshControllerUtils
				.islookForHiddenFilesEnabled());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		SshControllerUtils.setAutoConnect(autoConnect.isChecked());
		SshControllerUtils.setLockScreen(lockScreen.isChecked());
		SshControllerUtils.setLookForHiddenFiles(lookForHiddenFiles
				.isChecked());
		SshControllerUtils.saveGlobalConfiguration();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}

	public void autoConnectCheckBox(View v) {
		autoConnect.setChecked(!autoConnect.isChecked());
	}
}
