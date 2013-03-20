package com.pumkin.sshcontroller.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pumkin.sshcontroller.activity.SshControllerActivity;
import com.pumkin.sshcontroller.constants.Constants;
import com.pumkin.sshcontroller.object.Controller;
import com.pumkin.sshcontroller.object.CurrentConfiguration;
import com.pumkin.sshcontroller.object.SshConfiguration;

public class SshControllerUtils {

	public static ArrayList<SshConfiguration> sshConfigurations;
	
	public static synchronized ArrayList<Controller> getControllers(){
		if(sshConfigurations==null){
			loadSshConfigurations();
		}
		ArrayList<Controller> res=new ArrayList<Controller>();
		for(int i=0;i<sshConfigurations.size();i++){
			res.add(sshConfigurations.get(i).controllers.get(i));
		}
		return res;
	}

	public static void sendBroadcast(String message) {
		Intent intent = new Intent(message);
		CurrentConfiguration.instance.sendBroadcast(intent);
	}

	public static int convertDpToPx(Context context, int dps) {
		final float scale = context.getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}

	public static int convertPxToDp(Context context, int pxs) {
		final float scale = context.getResources().getDisplayMetrics().density;
		int dps = (int) (pxs / scale + 0.5f);
		return dps;
	}

	public static int getPositionInt(int value, int[] values) {
		for (int i = 0; i < values.length; i++) {
			if (value == values[i]) {
				return i;
			}
		}
		return -1;
	}

	public static void loadSshConfigurations() {
		try {
			FileInputStream fis = CurrentConfiguration.instance
					.getApplicationContext().openFileInput(Constants._SSHCONFIGURATION_FILENAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			sshConfigurations = (ArrayList<SshConfiguration>) is.readObject();
			is.close();
			for (int i = 0; i < sshConfigurations.size(); i++) {
				sshConfigurations.get(i).state = Constants._SSHCONFIGURATION_UNKNOWN;
				for(int j=0;j<sshConfigurations.get(i).controllers.size();j++){
					sshConfigurations.get(i).controllers.get(j).parent=sshConfigurations.get(i);
				}
			}
			SshControllerActivity.refreshControllers();
			saveSshConfigurations();
		} catch (Exception e) {
			Log.e(Controller.class.toString(), "couldn't load sshConfiguration");
			sshConfigurations = new ArrayList<SshConfiguration>();
			e.printStackTrace();
		}
		Log.d(Controller.class.toString(), "number of current SshConfiguration: "
				+ sshConfigurations.size());
	}

	public static void saveSshConfigurations() {
		try {
			FileOutputStream fos = CurrentConfiguration.instance
					.getApplicationContext().openFileOutput(
							Constants._SSHCONFIGURATION_FILENAME,
							Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(sshConfigurations);
			os.flush();
			os.close();
			Log.i(Controller.class.toString(), "saving the sshConfiguration file");
		} catch (Exception e) {
			Log.e(Controller.class.toString(), "couldn't save file");
			e.printStackTrace();
		}
	}

	public static void addController(Controller newController,
			SshConfiguration sshConfiguration) {
		sshConfiguration.controllers.add(newController);
		saveSshConfigurations();
	}
	
	public static void deleteController(Controller controller){
		for(int i=0;i<sshConfigurations.size();i++){
			for(int j=0;j<sshConfigurations.get(i).controllers.size();j++){
				if(sshConfigurations.get(i).controllers.get(j).equals(controller)){
					sshConfigurations.get(i).controllers.remove(j);
					if(sshConfigurations.get(i).controllers.size()==0){
						sshConfigurations.remove(i);
					}
				}
			}
		}
		saveSshConfigurations();
	}
}
