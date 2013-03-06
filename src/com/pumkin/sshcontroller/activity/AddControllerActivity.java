package com.pumkin.sshcontroller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pumkin.sshcontroller.constants.Action;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.object.SshConfiguration;
import com.pumkin.sshcontroller.ssh.SshConnection;

public class AddControllerActivity extends SshControllerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_controller);
	}

	public void addController(View v) {
		String username = ((TextView) findViewById(R.id.username)).getText()
				.toString();
		String password = ((TextView) findViewById(R.id.password)).getText()
				.toString();
		String host = ((TextView) findViewById(R.id.host)).getText().toString();
		
//		username="aba";
//		password="password";
//		host="192.168.0.146";
		int port;
		try {
			port = Integer.parseInt(((TextView) findViewById(R.id.port))
					.getText().toString());
			if (port <= 0) {
				Toast.makeText(getApplicationContext(),
						getText(R.string.port_is_not_a_valid_number),
						Toast.LENGTH_LONG).show();
				return;
			}
//			port=22;
		} catch (Exception e) {
			// Display port must be a number
			Log.e(AddControllerActivity.class.toString(),
					"port is not a number");
			Toast.makeText(getApplicationContext(),
					getText(R.string.port_is_not_a_valid_number),
					Toast.LENGTH_LONG).show();
			return;
		}
		// Then, we launch it

		mProgressDialog = ProgressDialog.show(this,
				getText(R.string.please_wait),
				getText(R.string.checking_ssh_parameters), true);

		currentConfiguration = new SshConfiguration(username, password, host,
				port);
		SshConnection.testConfiguration(currentConfiguration);
	}

	protected ProgressDialog mProgressDialog;
	protected SshConfiguration currentConfiguration;

	@Override
	protected void onResume() {
		super.onResume();
		if(mProgressDialog!=null){
			//Then, we have to check it again
			SshConnection.testConfiguration(currentConfiguration);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void cancelProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
			mProgressDialog = null;
		}
	}

	@Override
	public void onAction(Intent intent) {
		String action = intent.getAction();
		// log our message value
		Log.i("getting action:", action);
		if (Action._SSHSUCCESS.equals(action)) {
			cancelProgressDialog();
			Intent startNewActivityOpen = new Intent(
					AddControllerActivity.this, ControllerActivity.class);
			CurrentConfiguration.controller=new Controller(currentConfiguration);
			CurrentConfiguration.controller.state=Controller._CONNECTED;
			Controller.addController(CurrentConfiguration.controller);
			
			
			
			startActivityForResult(startNewActivityOpen, 0);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			finish();
		} else if (Action._SSHFAILURE.equals(action)) {
			cancelProgressDialog();
			Toast.makeText(getApplicationContext(),
					getText(R.string.cannot_get_ssh_connections),
					Toast.LENGTH_LONG).show();
		}
	}
}
