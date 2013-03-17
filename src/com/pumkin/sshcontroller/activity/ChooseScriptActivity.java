package com.pumkin.sshcontroller.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pumkin.sshcontroller.adapter.ChooseFileAdapter;
import com.pumkin.sshcontroller.constants.Action;
import com.pumkin.sshcontroller.object.Button;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.object.SshFile;
import com.pumkin.sshcontroller.ssh.SshClient;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public class ChooseScriptActivity extends SshControllerActivity implements OnItemClickListener{

	Button button;
	
	ChooseFileAdapter chooseFileAdapter;
	
	String currentPath=null;
	ArrayList<SshFile> sshFiles=null;
	ListView lvListe;
	TextView path;

	ProgressDialog mProgressDialog;
	
	ChooseScriptActivity instanceChooseActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_script);
	
		instanceChooseActivity=this;
		button=CurrentConfiguration.controller.getButtonByUuid(getIntent().getExtras().get("uuid").toString());
		lvListe = (ListView) findViewById(R.id.scriptList);
		
		path=(TextView) findViewById(R.id.currentPath);
		lvListe.setOnItemClickListener(this);
		
		reInitialize();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		SshFile file=sshFiles.get(position);
		if(file.isDirectory){
			if(file.name.equals("..")){
				reInitialize(file.getParentDirectory());
			}else{
				reInitialize(file.directory+file.name);
			}
		}else{
			//Check if isexecutable, if yes, do this action, then onback
			button.onPress.command=file.getFullPath();
			Controller.saveControllers();
			onBackPressed();
		}
	}
	
	public void reInitialize(){
		reInitialize(null);
	}
	
	public void reInitialize(final String newPath){
		Thread t=new Thread(){
			@Override
			public void run(){
				SshClient currentClient = getCurrentClient();
				if(currentClient!=null){
					if(newPath==null){
						currentPath=getCurrentClient().getCurrentPath();
					}else{
						currentPath=newPath;
					}
					if(currentClient!=null){
						sshFiles=currentClient.ls(currentPath);
					}
					SshControllerUtils.sendBroadcast(Action._REFRESHSSH);
				}
			}
		};
		t.start();
		mProgressDialog = ProgressDialog.show(this,
				getText(R.string.please_wait),
				getText(R.string.listing_files), true);
	}
	
	@Override
    public void onAction(Intent intent){
		String action = intent.getAction();
		// log our message value
		Log.i("getting action:", action);
		if (Action._REFRESHSSH.equals(action)) {
			if(mProgressDialog!=null && mProgressDialog.isShowing())
				mProgressDialog.cancel();
			path.setText(currentPath);
			chooseFileAdapter = new ChooseFileAdapter(instanceChooseActivity, sshFiles);
			lvListe.setAdapter(chooseFileAdapter);
			chooseFileAdapter.notifyDataSetChanged();
		}
    }
}
