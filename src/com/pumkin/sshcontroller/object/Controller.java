package com.pumkin.sshcontroller.object;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pumkin.sshcontroller.activity.R;
import com.pumkin.sshcontroller.activity.SshControllerActivity;
import com.pumkin.sshcontroller.adapter.ControllerAdapter;
import com.pumkin.sshcontroller.constants.Action;
import com.pumkin.sshcontroller.utils.SshControllerUtils;

public class Controller implements Serializable {

	public static ArrayList<Controller> controllers;
	private static final String filename = "sshcontroller.info";

	public String name = "untitled";

	public SshConfiguration sshConfiguration;

	public static final int _CONNECTED = 0;
	public static final int _DISCONNECTED = 1;
	public static final int _UNKNOWN = 2;

	public int state = _UNKNOWN;

	public ArrayList<Button> buttons = new ArrayList<Button>();

	public Controller(SshConfiguration conf) {
		sshConfiguration = conf;
	}

	public void addButton(Button button) {
		buttons.add(button);
	}

	public static void loadControllers() {
		try {
			FileInputStream fis = SshControllerActivity.instance
					.getApplicationContext().openFileInput(filename);
			ObjectInputStream is = new ObjectInputStream(fis);
			controllers = (ArrayList<Controller>) is.readObject();
			is.close();
			for (int i = 0; i < controllers.size(); i++) {
				controllers.get(i).state = _UNKNOWN;
			}
			refreshControllers();
		} catch (Exception e) {
			Log.e(Controller.class.toString(), "couldn't load controllers");
			controllers = new ArrayList<Controller>();
			e.printStackTrace();
			controllers = new ArrayList<Controller>();
		}
		Log.d(Controller.class.toString(), "number of current controllers: "
				+ controllers.size());
	}

	public static void saveControllers() {
		try {
			Log.i("toto", "teat: " + SshControllerActivity.instance);
			FileOutputStream fos = SshControllerActivity.instance
					.getApplicationContext().openFileOutput(filename,
							Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(controllers);
			os.flush();
			os.close();
			Log.i(Controller.class.toString(), "saving the controller file");
		} catch (Exception e) {
			Log.e(Controller.class.toString(), "couldn't save file");
			e.printStackTrace();
		}
	}
	
	public Button getButtonByUuid(String uuid){
		for(int i=0;i<buttons.size();i++){
			if(uuid.equals(buttons.get(i).uuid)){
				return buttons.get(i);
			}
		}
		return null;
	}
	
	public boolean deleteButtonByUuid(String uuid){
		for(int i=0;i<buttons.size();i++){
			if(uuid.equals(buttons.get(i).uuid)){
				buttons.remove(i);
				return true;
			}
		}
		return false;
	}

	public static void addController(Controller newController) {
		if (controllers == null) {
			loadControllers();
		}
		controllers.add(newController);
		saveControllers();
	}

	public static void refreshControllers() {
		new Thread() {
			public void run() {
				for (int i = 0; i < controllers.size(); i++) {
					if (controllers.get(i).sshConfiguration.testConfiguration()) {
						controllers.get(i).state = _CONNECTED;
						Log.i(ControllerAdapter.class.getName(),
								"setting status to connected");
					} else {
						controllers.get(i).state = _DISCONNECTED;
						Log.i(ControllerAdapter.class.getName(),
								"setting status to disconnected");
					}
					SshControllerUtils.sendBroadcast(Action._REFRESHCONTROLLER);
				}
			}
		}.start();
	}
}
