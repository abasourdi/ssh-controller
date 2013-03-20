package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.ArrayList;

public class Action implements Serializable{

	public boolean confirmation=false;
	public boolean sendCoordinate=false;
	public boolean listAction=false;
	
	public String command="";
	public ArrayList<String> parameters=new ArrayList<String>();
	
	public boolean repeatedEnabled=false;
	public int repeatedInterval=50;
	
	public String getFullCommand(){
		String res=command;
		for(int i=0;i<parameters.size();i++){
			res+=" "+command;
		}
		return res;
	}
}
