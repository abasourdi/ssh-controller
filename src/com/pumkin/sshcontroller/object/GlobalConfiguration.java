package com.pumkin.sshcontroller.object;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;
import android.util.Log;

public class GlobalConfiguration implements Serializable {

	private static final String filename = "sshcontrollerconf.info";

	private static GlobalConfiguration instance = null;

	private boolean autoConnectEnabled = true;
	private boolean lockScreenEnabled = false;
	private boolean lookForHiddenFilesEnabled = false;

	private static GlobalConfiguration getInstance() {
		if (instance == null) {
			load();
		}
		return instance;
	}

	public static boolean isAutoConnectEnabled() {
		return getInstance().autoConnectEnabled;
	}

	public static boolean setAutoConnect(boolean autoConnectEnabled) {
		return getInstance().autoConnectEnabled = autoConnectEnabled;
	}

	public static boolean isLockScreenEnabled() {
		return getInstance().lockScreenEnabled;
	}

	public static boolean setLockScreen(boolean lockScreenEnabled) {
		return getInstance().lockScreenEnabled = lockScreenEnabled;
	}

	public static boolean islookForHiddenFilesEnabled() {
		return getInstance().lookForHiddenFilesEnabled;
	}

	public static boolean setLookForHiddenFiles(boolean lookForHiddenFilesEnabled) {
		return getInstance().lookForHiddenFilesEnabled = lookForHiddenFilesEnabled;
	}

	private static void load() {
		try {
			FileInputStream fis = CurrentConfiguration.instance
					.getApplicationContext().openFileInput(filename);
			ObjectInputStream is = new ObjectInputStream(fis);
			instance = (GlobalConfiguration) is.readObject();
			is.close();
		} catch (Exception e) {
			Log.e(Controller.class.toString(),
					"couldn't load global configuration");
			instance = new GlobalConfiguration();
		}
	}

	public static void save() {
		try {
			FileOutputStream fos = CurrentConfiguration.instance
					.getApplicationContext().openFileOutput(filename,
							Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(instance);
			os.flush();
			os.close();
			Log.i(Controller.class.toString(),
					"saving the global configuration file");
		} catch (Exception e) {
			Log.e(Controller.class.toString(), "couldn't save file");
			e.printStackTrace();
		}
	}

}
