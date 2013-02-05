package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.UUID;

public class Button implements Serializable {

	
	public Button(String type){
		this.type=type;
		uuid=UUID.randomUUID().toString();
	}
	
	public String uuid;
	
	public String type;
	
	
	public Action onPress=new Action();
	
	public int currentState=0;
	public Action state=new Action();
	
	public String name="untitled";
	public String displayedName="";

	public int marginLeft=0;
	public int marginTop=0;

	public int height=80;
	public int width=80;
}
