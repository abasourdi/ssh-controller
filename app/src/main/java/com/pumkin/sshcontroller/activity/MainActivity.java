package com.pumkin.sshcontroller.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pumkin.sshcontroller.adapter.ControllerAdapter;
import com.pumkin.sshcontroller.constants.Action;
import com.pumkin.sshcontroller.constants.Constants;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.object.GlobalConfiguration;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public class MainActivity extends SshControllerActivity implements
		OnItemClickListener, OnItemLongClickListener {

	private ControllerAdapter controllerAdapter;
	private ListView controllerList;

	static boolean autoChoose = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CurrentConfiguration.instance=this;
		
		controllerList = (ListView) findViewById(R.id.controllerList);
		controllerAdapter = new ControllerAdapter(this);
		controllerList.setAdapter(controllerAdapter);
		controllerAdapter.notifyDataSetChanged();

		controllerList.setOnItemLongClickListener(this);
		controllerList.setOnItemClickListener(this);

	}

	/**
	 * refresh the list of controllers
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(this.getClass().getName(), "calling onResume");
		startControllerAutomatically();
		controllerAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * start the AddControllerActivity
	 * 
	 * @param v
	 */
	public void addController(View v) {
		Intent startNewActivityOpen = new Intent(MainActivity.this,
				AddControllerActivity.class);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * This is mostly used to get the size of the screen to be able to get a
	 * good layout for the buttons on the controller screen
	 */
	@Override
	protected void onPause() {
		super.onPause();
		// We get the coordinate of the screen
		if (CurrentConfiguration.heightInPixel == -1) {
			RelativeLayout parent = ((RelativeLayout) controllerList
					.getParent());
			CurrentConfiguration.heightInPixel = parent.getHeight();
			CurrentConfiguration.widthInPixel = parent.getWidth();
		}
	}

	/**
	 * Start the controller activity.
	 */
	private void startControllerActivity() {
		Intent startNewActivityOpen = new Intent(MainActivity.this,
				ControllerActivity.class);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * Will start the controller activity with the specified controller if this
	 * one is enabled
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		if (SshControllerUtils.getControllers().get(position).parent.state == Constants._SSHCONFIGURATION_CONNECTED) {
			CurrentConfiguration.controller = SshControllerUtils.getControllers().get(position);
			startControllerActivity();
		} else {
			// Display error message
			Toast.makeText(getApplicationContext(),
					getText(R.string.cannot_get_ssh_connections),
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Method used to rename or delete a controller, to be changed
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v,
			final int position, long id) {
		Log.i(this.getClass().getName(), "click on item: " + position);

		// AlertDialog actionAlert=new AlertDialog.Builder(
		// SshControllerActivity.instance).create();

		AlertDialog actionAlert = new AlertDialog.Builder(this).create();
		actionAlert.setTitle(getString(R.string.manage_controller_title));
		actionAlert.setMessage(getString(R.string.manage_controller_message,
				SshControllerUtils.getControllers().get(position).name));
		actionAlert.setButton(AlertDialog.BUTTON_POSITIVE,
				getString(R.string.delete), new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						AlertDialog deleteAlert = new AlertDialog.Builder(
								CurrentConfiguration.instance).create();
						deleteAlert
								.setTitle(getString(R.string.delete_controller_title));
						deleteAlert.setMessage(getString(
								R.string.delete_controller_message,
								SshControllerUtils.getControllers().get(position).name));
						deleteAlert.setButton(AlertDialog.BUTTON_POSITIVE,
								getString(R.string.yes), new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										SshControllerUtils.deleteController(SshControllerUtils.getControllers().get(position));
										SshControllerUtils.saveSshConfigurations();
										((MainActivity) CurrentConfiguration.instance).controllerAdapter
												.notifyDataSetChanged();
									}

								});
						deleteAlert.setButton(AlertDialog.BUTTON_NEGATIVE,
								getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
									}
								});
						deleteAlert.show();
					}

				});
		actionAlert.setButton(AlertDialog.BUTTON_NEGATIVE,
				getString(R.string.rename), new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

						AlertDialog renameAlert = new AlertDialog.Builder(
								CurrentConfiguration.instance).create();
						renameAlert
								.setTitle(getString(R.string.rename_controller_title));
						renameAlert.setMessage(getString(
								R.string.rename_controller_message,
								SshControllerUtils.getControllers().get(position).name));

						final EditText input = new EditText(
								CurrentConfiguration.instance);
						renameAlert.setView(input);

						renameAlert.setButton(AlertDialog.BUTTON_POSITIVE,
								getString(R.string.rename),
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										SshControllerUtils.getControllers().get(position).name = input
												.getText().toString();
										SshControllerUtils.saveSshConfigurations();
										((MainActivity) CurrentConfiguration.instance).controllerAdapter
												.notifyDataSetChanged();
									}

								});
						renameAlert.setButton(AlertDialog.BUTTON_NEGATIVE,
								getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
									}
								});
						renameAlert.show();
					}
				});

		actionAlert.setButton(AlertDialog.BUTTON_NEUTRAL,
				getString(R.string.cancel), new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});
		actionAlert.show();
		return true;
	}

	/**
	 * 
	 */
	private void startControllerAutomatically() {
		if (SshControllerUtils.isAutoConnectEnabled() && autoChoose) {
			// Then, if there is a status = true, we connect to it
			for (int i = 0; i < SshControllerUtils.getControllers().size(); i++) {
				if (SshControllerUtils.getControllers().get(i).parent.state == Constants._SSHCONFIGURATION_CONNECTED) {
					CurrentConfiguration.controller = SshControllerUtils.getControllers().get(i);
					startControllerActivity();
				}
			}
		}
	}

	/**
	 * Method used to refresh the controllers, and eventually auto start the
	 * first controller found
	 */
	@Override
	public void onAction(Intent intent) {
		String action = intent.getAction();
		if (Action._REFRESHCONTROLLER.equals(action)) {
			startControllerAutomatically();
			controllerAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Start the global configuration activity
	 * 
	 * @param menuItem
	 */
	public void globalConfiguration(MenuItem menuItem) {
		Intent startNewActivityOpen = new Intent(MainActivity.this,
				GlobalConfigurationActivity.class);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
}
