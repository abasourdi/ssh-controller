package com.pumkin.sshcontroller.ssh;

import android.content.Intent;
import android.util.Log;

import com.pumkin.sshcontroller.activity.SshControllerActivity;
import com.pumkin.sshcontroller.constants.Action;
import com.pumkin.sshcontroller.object.SshConfiguration;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

//Will use the ssh client to communicate
public class SshConnection {

	public static void testConfiguration(SshConfiguration conf){
		final SshConfiguration current=conf; 
		new Thread(){
			public void run(){
			if(current.testConfiguration()){
				Log.i(SshConnection.class.toString(), "Test successfull");
				SshControllerUtils.sendBroadcast(Action._SSHSUCCESS);
			}else{
				Log.e(SshConnection.class.toString(), "Test is unsuccessful");
				SshControllerUtils.sendBroadcast(Action._SSHFAILURE);
			}
			}
		}.start();
	}
}