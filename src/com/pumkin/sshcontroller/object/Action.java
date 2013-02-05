package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.ArrayList;

public class Action implements Serializable{

	public boolean confirmation=false;
	public String command="";
	public ArrayList<String> parameters=new ArrayList<String>();
	
	public int pressedInterval=0;
	
	public String getFullCommand(){
		String res=command;
		for(int i=0;i<parameters.size();i++){
			res+=" "+command;
		}
		return res;
	}
}
