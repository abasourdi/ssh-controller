package com.pumkin.sshcontroller.object;

import java.io.Serializable;

import com.pumkin.sshcontroller.ssh.SshClient;

public class SshConfiguration implements Serializable{

	public static SshConfiguration current=null;
	
	public String host;
	public String username;
	public String password;
	public int port;

	public SshConfiguration(String username, String password, String host){
		this(username, password, host, 22);
	}
	
	public SshConfiguration(String username, String password, String host, int port){
		this.username=username;
		this.password=password;
		this.host=host;
		this.port=port;
	}
	
	public boolean testConfiguration(){
		SshClient client=new SshClient(this);
		if(client.connect()){
			client.disconnect();
			return true;
		}else{
			return false;
		}
	}
	
	public SshClient getSshClient(){
		SshClient client=new SshClient(this);
		if(client.connect()){
			return client;
		}else{
			return null;
		}
	}
}
