package com.pumkin.sshcontroller.utils;

import android.content.Context;
import android.content.Intent;

import com.pumkin.sshcontroller.object.CurrentConfiguration;

public class SshControllerUtils {

	public static void sendBroadcast(String message){
		Intent intent=new Intent(message);
		CurrentConfiguration.instance.sendBroadcast(intent);
	}
	
	public static int convertDpToPx(Context context, int dps){
		final float scale = context.getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}
	
	public static int convertPxToDp(Context context, int pxs){
		final float scale = context.getResources().getDisplayMetrics().density;
		int dps = (int) (pxs / scale + 0.5f);
		return dps;
	}
	
	public static int getPositionInt(int value, int[] values){
		for(int i=0;i<values.length;i++){
			if(value==values[i]){
				return i;
			}
		}
		return -1;
	}
}
