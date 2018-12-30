package com.pumkin.sshcontroller.object;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;
import android.util.Log;

public class GlobalConfiguration implements Serializable {

	public boolean autoConnectEnabled = true;
	public boolean lockScreenEnabled = false;
	public boolean lookForHiddenFilesEnabled = false;

}
