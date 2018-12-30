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
import com.pumkin.sshcontroller.object.GlobalConfiguration;
import com.pumkin.sshcontroller.object.SshConfiguration;

public class SshControllerUtils {

	private static GlobalConfiguration globalConfiguration = null;
	public static ArrayList<SshConfiguration> sshConfigurations;

	public static synchronized ArrayList<Controller> getControllers() {
		if (sshConfigurations == null) {
			loadSshConfigurations();
		}
		ArrayList<Controller> res = new ArrayList<Controller>();
		for (int i = 0; i < sshConfigurations.size(); i++) {
			res.add(sshConfigurations.get(i).controllers.get(0));
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
					.getApplicationContext().openFileInput(
							Constants._SSHCONFIGURATION_FILENAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			sshConfigurations = (ArrayList<SshConfiguration>) is.readObject();
			is.close();
			for (int i = 0; i < sshConfigurations.size(); i++) {
				sshConfigurations.get(i).state = Constants._SSHCONFIGURATION_UNKNOWN;
				for (int j = 0; j < sshConfigurations.get(i).controllers.size(); j++) {
					sshConfigurations.get(i).controllers.get(j).parent = sshConfigurations
							.get(i);
				}
			}
			SshControllerActivity.refreshControllers();
			saveSshConfigurations();
		} catch (Exception e) {
			Log.e(Controller.class.toString(), "couldn't load sshConfiguration");
			sshConfigurations = new ArrayList<SshConfiguration>();
			e.printStackTrace();
		}
		Log.d(Controller.class.toString(),
				"number of current SshConfiguration: "
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
			Log.i(Controller.class.toString(),
					"saving the sshConfiguration file");
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

	public static void deleteController(Controller controller) {
		for (int i = 0; i < sshConfigurations.size(); i++) {
			for (int j = 0; j < sshConfigurations.get(i).controllers.size(); j++) {
				if (sshConfigurations.get(i).controllers.get(j).equals(
						controller)) {
					sshConfigurations.get(i).controllers.remove(j);
					if (sshConfigurations.get(i).controllers.size() == 0) {
						sshConfigurations.remove(i);
					}
				}
			}
		}
		saveSshConfigurations();
	}

	private static GlobalConfiguration getGlobalConfiguration() {
		if (globalConfiguration == null) {
			loadGlobalConfiguration();
		}
		return globalConfiguration;
	}

	private static void loadGlobalConfiguration() {
		try {
			FileInputStream fis = CurrentConfiguration.instance
					.getApplicationContext().openFileInput(
							Constants._GLOBAL_CONFIGURATION_FILENAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			globalConfiguration = (GlobalConfiguration) is.readObject();
			is.close();
		} catch (Exception e) {
			Log.e(Controller.class.toString(),
					"couldn't load global configuration");
			e.printStackTrace();
			globalConfiguration = new GlobalConfiguration();
		}
	}

	public static void saveGlobalConfiguration() {
		try {
			FileOutputStream fos = CurrentConfiguration.instance
					.getApplicationContext().openFileOutput(
							Constants._GLOBAL_CONFIGURATION_FILENAME,
							Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(globalConfiguration);
			os.flush();
			os.close();
			Log.i(Controller.class.toString(),
					"saving the global configuration file");
		} catch (Exception e) {
			Log.e(Controller.class.toString(), "couldn't save file");
			e.printStackTrace();
		}
	}

	public static boolean isAutoConnectEnabled() {
		return getGlobalConfiguration().autoConnectEnabled;
	}

	public static boolean setAutoConnect(boolean autoConnectEnabled) {
		return getGlobalConfiguration().autoConnectEnabled = autoConnectEnabled;
	}

	public static boolean isLockScreenEnabled() {
		return getGlobalConfiguration().lockScreenEnabled;
	}

	public static boolean setLockScreen(boolean lockScreenEnabled) {
		return getGlobalConfiguration().lockScreenEnabled = lockScreenEnabled;
	}

	public static boolean islookForHiddenFilesEnabled() {
		return getGlobalConfiguration().lookForHiddenFilesEnabled;
	}

	public static boolean setLookForHiddenFiles(
			boolean lookForHiddenFilesEnabled) {
		return getGlobalConfiguration().lookForHiddenFilesEnabled = lookForHiddenFilesEnabled;
	}
}
