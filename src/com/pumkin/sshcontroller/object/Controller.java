package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.ArrayList;

public class Controller implements Serializable {

	public transient SshConfiguration parent;

	public String name = "untitled";

	public ArrayList<Button> buttons = new ArrayList<Button>();

	public void addButton(Button button) {
		buttons.add(button);
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
}
