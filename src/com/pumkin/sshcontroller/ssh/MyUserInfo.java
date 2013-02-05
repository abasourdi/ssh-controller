package com.pumkin.sshcontroller.ssh;

import com.jcraft.jsch.UserInfo;

public class MyUserInfo implements UserInfo {
	private String password;

	public void setPassword(String password) {
		this.password = password;

	}

	public String getPassphrase() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	public boolean promptPassword(String arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean promptPassphrase(String arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean promptYesNo(String arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	public void showMessage(String arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
	}

}