package com.pumkin.sshcontroller.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pumkin.sshcontroller.adapter.ControllerAdapter;
import com.pumkin.sshcontroller.constants.Action;
import com.pumkin.sshcontroller.object.Controller;

public class MainActivity extends SshControllerActivity implements
		OnItemClickListener, OnItemLongClickListener {

	ControllerAdapter controllerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		ListView lvListe = (ListView) findViewById(R.id.controllerList);
		controllerAdapter = new ControllerAdapter(this);
		lvListe.setAdapter(controllerAdapter);
		controllerAdapter.notifyDataSetChanged();

		lvListe.setOnItemLongClickListener(this);
		lvListe.setOnItemClickListener(this);
		

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(this.getClass().getName(), "calling onResume");
		controllerAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void addController(View v) {
		Intent startNewActivityOpen = new Intent(MainActivity.this,
				AddControllerActivity.class);
		startActivityForResult(startNewActivityOpen, 0);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	//If state of the activity is online => launch it, otherwise, 
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		if(true){
		//if(controller.state==Controller._CONNECTED){
			Intent startNewActivityOpen = new Intent(
					MainActivity.this, ControllerActivity.class);
			
			
			controller=Controller.controllers.get(position);
			
			startActivityForResult(startNewActivityOpen, 0);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}else{
			//Display error message
			Toast.makeText(getApplicationContext(),
					getText(R.string.cannot_get_ssh_connections),
					Toast.LENGTH_LONG).show();
		}
	}

	//A bit long, but it's only for rename and delete
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, final int position,
			long id) {
		Log.i(this.getClass().getName(), "click on item: " + position);
		
//		AlertDialog actionAlert=new AlertDialog.Builder(
//				SshControllerActivity.instance).create();
		
		AlertDialog actionAlert=new AlertDialog.Builder(this).create();
		actionAlert.setTitle(getString(R.string.manage_controller_title));
		actionAlert.setMessage(getString(R.string.manage_controller_message, Controller.controllers.get(position).name));
		actionAlert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete),new OnClickListener() {
			
 
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				AlertDialog deleteAlert = new AlertDialog.Builder(
						SshControllerActivity.instance).create();
				deleteAlert.setTitle(getString(R.string.delete_controller_title));
				deleteAlert.setMessage(getString(R.string.delete_controller_message, Controller.controllers.get(position).name));
				deleteAlert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),new OnClickListener() {
					

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Controller.controllers.remove(position);
						Controller.saveControllers();
						((MainActivity)SshControllerActivity.instance).controllerAdapter.notifyDataSetChanged();
					}

				});
				deleteAlert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {}
				});
				deleteAlert.show();
			}

		});
		actionAlert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.rename),new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				AlertDialog renameAlert = new AlertDialog.Builder(
						SshControllerActivity.instance).create();
				renameAlert.setTitle(getString(R.string.rename_controller_title));
				renameAlert.setMessage(getString(R.string.rename_controller_message, Controller.controllers.get(position).name));
				
				final EditText input = new EditText(SshControllerActivity.instance);
				renameAlert.setView(input);
				
				renameAlert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.rename),new OnClickListener() {
					

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Controller.controllers.get(position).name=input.getText().toString();
						Controller.saveControllers();
						((MainActivity)SshControllerActivity.instance).controllerAdapter.notifyDataSetChanged();
					}

				});
				renameAlert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {}
				});
				renameAlert.show();
			}
		});
		
		actionAlert.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.cancel),new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		});
		actionAlert.show();
		
		return true;
	}
	
	@Override
	public void onAction(Intent intent) {
		String action = intent.getAction();
		// log our message value
		Log.i("getting action:", action);
		if (Action._REFRESHCONTROLLER.equals(action)) {
			controllerAdapter.notifyDataSetChanged();
		}
	}
}
