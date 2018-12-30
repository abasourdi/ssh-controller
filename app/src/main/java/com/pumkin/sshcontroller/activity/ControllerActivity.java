package com.pumkin.sshcontroller.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.pumkin.sshcontroller.display.ControllerDisplay;
import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.ssh.SshClient;

public class ControllerActivity extends SshActiveControllerActivity implements
		OnClickListener {

	RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		relativeLayout = (RelativeLayout) findViewById(R.id.activityControllerRelativeLayout);

		if (CurrentConfiguration.controller != null
				&& CurrentConfiguration.controller.buttons.size() == 0) {
			// We switch to the edit controller as there is no button
			enableEditMode(null);
		}

        registerForContextMenu(relativeLayout);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (CurrentConfiguration.controller != null) {
			Log.i(this.getClass().getName(), "nb buttons: "
					+ CurrentConfiguration.controller.buttons.size());
			ControllerDisplay.resetLayout(this, relativeLayout,
					CurrentConfiguration.controller, this, null, null);
			// Then, it's a choice of the user

			MainActivity.autoChoose = false;
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_controller, menu);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void enableEditMode(MenuItem menuItem) {
		Intent startNewActivityOpen = new Intent(ControllerActivity.this,
				EditControllerActivity.class);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	// TODO REPLACE WITH ONTOUCH TO SEND THE POSITION + REPEAT ACTION
	@Override
	public void onClick(final View view) {
		if (CurrentConfiguration.controller != null) {
			final Button button = CurrentConfiguration.controller
					.getButtonByUuid(view.getTag().toString());
			final Thread t = new Thread() {
				@Override
				public void run() {
					if (button != null) {
						if (button.onPress != null) {
							if (button.onPress.command != null) {
								Log.i(this.getClass().toString(), "starting3");
								Log.i(this.getClass().toString(),
										"executing command: "
												+ button.onPress.command);
								SshClient currentClient = getCurrentClient();
								if (currentClient != null) {
									// To escape space... Dunno any other
									// solution
									// yet...
									currentClient
											.execute(button.onPress.command
													.replace(" ", "\\ "));
									// currentClient.execute(button.onPress.command);
								}
							}
						}
					}
				}
			};

			if (button.onPress.confirmation) {
				AlertDialog deleteAlert = new AlertDialog.Builder(
						CurrentConfiguration.instance).create();
				deleteAlert.setTitle(getString(R.string.titleuseaction));
				deleteAlert.setMessage(getString(R.string.labeluseaction));
				deleteAlert.setButton(AlertDialog.BUTTON_POSITIVE,
						getString(R.string.yes),
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Log.i(this.getClass().toString(),
										"confirmation to push button");
								t.start();
							}

						});
				deleteAlert.setButton(AlertDialog.BUTTON_NEGATIVE,
						getString(R.string.cancel),
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});
				deleteAlert.show();
			} else {
				t.start();
			}
		}
	}
}
