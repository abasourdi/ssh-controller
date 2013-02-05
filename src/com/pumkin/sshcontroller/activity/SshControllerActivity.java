package com.pumkin.sshcontroller.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.pumkin.sshcontroller.constants.Action;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.ssh.SshClient;

public abstract class SshControllerActivity extends Activity {

	public static SshControllerActivity instance=null; 
	
	private BroadcastReceiver mReceiver;
	public static Controller controller;
	
	public static SshClient currentSshClient=null;
	
	public void backIfNotConnected(){
		Thread t=new Thread(){
			@Override
			public void run(){
				if(!isConnected()){
					Log.e("notconnected", "on back pressed because not connected");
					onBackPressed();
				}	
			}
		};
		t.start();
	}
	
	public boolean isConnected(){
		getCurrentClient();
		if(currentSshClient==null){
			return false;
		}else if(currentSshClient.isConnected()){
			return true;
		}else{
			//Try to connect the client
			currentSshClient.connect();
			return currentSshClient.isConnected();
		}
	}
	
	public static synchronized SshClient getCurrentClient(){
		if(controller==null){
			return null;
		}
		if(currentSshClient==null){
			currentSshClient=new SshClient(controller.sshConfiguration);
		}
		if(!currentSshClient.isConnected()){
			currentSshClient.connect();
			if(!currentSshClient.isConnected()){
				//Unable to connect
				return null;
			}
		}
		return currentSshClient;
	}
	
	public void reinitializeSshClient(){
		if(controller!=null){
			currentSshClient=new SshClient(controller.sshConfiguration);
			currentSshClient.connect();
		}
	}
	
	static PowerManager.WakeLock wakeLock ;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Controller.controllers==null){
	        instance=this;
			Controller.loadControllers();
		}
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		if(wakeLock==null)
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "tag");
	}
	  @Override
	    protected void onResume() {
	        super.onResume();
	        instance=this;
	        mReceiver = new BroadcastReceiver() {
	            @Override
	            public void onReceive(Context context, Intent intent) {
	                //extract our message from intent
	            	onAction(intent);
	            }
	        };
	        //registering our receiver
	        this.registerReceiver(mReceiver, new IntentFilter(Action._SSHSUCCESS));
	        this.registerReceiver(mReceiver, new IntentFilter(Action._SSHFAILURE));
	        this.registerReceiver(mReceiver, new IntentFilter(Action._REFRESHCONTROLLER));
	        this.registerReceiver(mReceiver, new IntentFilter(Action._REFRESHSSH));
	        this.registerReceiver(mReceiver, new IntentFilter(Action._NOTCONNECTED));

			wakeLock.acquire();
	    }
	    @Override
	    protected void onPause() {
	        // TODO Auto-generated method stub
	        super.onPause();
	        //unregister our receiver
	        this.unregisterReceiver(this.mReceiver);
	        

			wakeLock.release();
	    }
	    
	    public void onAction(Intent intent){
	    	
	    }
}
