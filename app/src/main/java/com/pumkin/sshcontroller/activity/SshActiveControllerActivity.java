package com.pumkin.sshcontroller.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.pumkin.sshcontroller.adapter.ControllerAdapter;
import com.pumkin.sshcontroller.constants.Action;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.object.GlobalConfiguration;
import com.pumkin.sshcontroller.ssh.SshClient;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public abstract class SshActiveControllerActivity extends SshControllerActivity {

	@Override
	protected void onResume() {
		super.onResume();
		if(CurrentConfiguration.controller==null){
			//Then, we go back
			onBackPressed();
		}
	}
	
	@Override
	public void onAction(Intent intent) {
		String action = intent.getAction();
		if (Action._NOTCONNECTED.equals(action)) {
			MainActivity.autoChoose = true;
			onBackPressed();
		}
	}
}
