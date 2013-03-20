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
import com.pumkin.sshcontroller.constants.Constants;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.object.GlobalConfiguration;
import com.pumkin.sshcontroller.ssh.SshClient;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public abstract class SshControllerActivity extends Activity {

	private BroadcastReceiver mReceiver;

	private static boolean isLaunched = false;
	private static boolean isControllerThreadRunning = false;
	private static boolean isRefresherControllerThreadRunning = false;

	private synchronized void launchThread() {
		if (!isControllerThreadRunning) {
			Thread t = new Thread() {
				@Override
				public void run() {
					// The thread is done until the application is quit
					isControllerThreadRunning = true;
					try {
						while (isLaunched) {
							Log.i(SshControllerActivity.class.getName(),
									"refreshing thread");
							// We refresh the controllers
							// refreshControllers();
							// Then, we check if the current controller is
							// connected
							if (CurrentConfiguration.controller != null) {
								if (CurrentConfiguration.controller.parent
										.getSshClient().isConnected()) {
									// We execute a basic request just to keep
									// the connection active
									Log.i(SshControllerActivity.class.getName(),
											"Still connected");

									System.out.println("DZADZA AVANT");
									CurrentConfiguration.controller.parent
											.getSshClient().execute("pwd");
									System.out.println("DZADZA APRES");
								} else {
									// Otherwise, we quit it as the connection
									// is lost
									Log.i(SshControllerActivity.class.getName(),
											"Not connected...");
									
									for (int i = 0; i < SshControllerUtils.getControllers()
											.size(); i++) {
										if (SshControllerUtils.getControllers()
												.get(i)
												.equals(CurrentConfiguration.controller)) {
											SshControllerUtils.getControllers().get(i).parent.state = Constants._SSHCONFIGURATION_DISCONNECTED;
										}
									}
									CurrentConfiguration.controller = null;
									SshControllerUtils
											.sendBroadcast(Action._NOTCONNECTED);
								}
							} else {
								//TODO MAYBE BIG FAILURE HERE
								if ( !isRefresherControllerThreadRunning) {
									refreshControllers();
								}
							}

							// We check every five seconds if the thread is
							// running
							Thread.sleep(5000);
						}
					} catch (InterruptedException e) {
					}
					isControllerThreadRunning = false;
				}
			};
			t.start();
		}
	}

	public static void refreshControllers() {
		Thread t = new Thread() {
			public void run() {
				isRefresherControllerThreadRunning=true;
				for (int i = 0; i < SshControllerUtils.getControllers().size(); i++) {
					if (SshControllerUtils.getControllers().get(i).parent
							.testConfiguration()) {
						SshControllerUtils.getControllers().get(i).parent.state = Constants._SSHCONFIGURATION_CONNECTED;
						Log.i(ControllerAdapter.class.getName(),
								"setting status to connected");
					} else {
						SshControllerUtils.getControllers().get(i).parent.state = Constants._SSHCONFIGURATION_DISCONNECTED;
						Log.i(ControllerAdapter.class.getName(),
								"setting status to disconnected");
					}
					SshControllerUtils.sendBroadcast(Action._REFRESHCONTROLLER);
				}
				isRefresherControllerThreadRunning=false;
			}
		};
		if(!isRefresherControllerThreadRunning){
			t.start();
		}
	}

	public boolean isConnected() {
		SshClient sshClient = getCurrentClient();
		if (sshClient == null) {
			return false;
		} else {
			return sshClient.isConnected();
		}
	}

	public static synchronized SshClient getCurrentClient() {
		if (CurrentConfiguration.controller == null) {
			return null;
		}
		if (CurrentConfiguration.controller.parent.getSshClient()
				.isConnected()) {
			return CurrentConfiguration.controller.parent
					.getSshClient();
		} else {
			return null;
		}
	}

	static PowerManager.WakeLock wakeLock;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO
		/*
		if (Controller.controllers == null) {
			CurrentConfiguration.instance = this;
			Controller.loadControllers();
		}
		*/

		if (GlobalConfiguration.isLockScreenEnabled()) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			if (wakeLock == null) {
				wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
						"tag");
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isLaunched = true;
		if (!isControllerThreadRunning) {
			launchThread();
		}
		CurrentConfiguration.instance = this;
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// extract our message from intent
				onAction(intent);
			}
		};
		// registering our receiver
		this.registerReceiver(mReceiver, new IntentFilter(Action._SSHSUCCESS));
		this.registerReceiver(mReceiver, new IntentFilter(Action._SSHFAILURE));
		this.registerReceiver(mReceiver, new IntentFilter(
				Action._REFRESHCONTROLLER));
		this.registerReceiver(mReceiver, new IntentFilter(Action._REFRESHSSH));
		this.registerReceiver(mReceiver, new IntentFilter(Action._NOTCONNECTED));
		if (wakeLock != null) {
			wakeLock.acquire();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		isLaunched = false;
		// unregister our receiver
		this.unregisterReceiver(this.mReceiver);

		if (wakeLock != null) {
			wakeLock.release();
		}
	}

	public void onAction(Intent intent) {

	}
}
