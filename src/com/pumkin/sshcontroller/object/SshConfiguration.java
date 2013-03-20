package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.ArrayList;

import com.pumkin.sshcontroller.ssh.SshClient;

/**
 * 
 * @author Laurent S.
 *
 */
public class SshConfiguration implements Serializable {

	public transient SshClient instance = null;

	public ArrayList<Controller> controllers = new ArrayList<Controller>();

	/**
	 * Host, either the hostname or the ip address
	 */
	public String host;
	/**
	 * Username used for the ssh access
	 */
	public String username;
	/**
	 * Password used for the ssh access
	 */
	public String password;
	/**
	 * Port used for the ssh access, by default it's 22
	 */
	public int port;

	/**
	 * If true, we save the password, if false, we have to ask for it
	 */
	public boolean savePassword = true;
	/**
	 * Public key, not used for now
	 */
	public String publicKey;

	/**
	 * Wake on lan ip/broadcast
	 */
	public String wakeOnLanIp = null;
	/**
	 * Wake on lan port, by default 9
	 */
	public Integer wakeOnLanPort = null;
	/**
	 * Mac address contained in the wake on lan packet
	 */
	public String macAddress = null;

	/**
	 * State of the connection, to know if it is currently connected or not
	 */
	public transient int state;

	public SshConfiguration(String username, String password, String host) {
		this(username, password, host, 22);
	}

	public SshConfiguration(String username, String password, String host,
			int port) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
	}

	/**
	 * 
	 * @return true if it manage to connect, false otherwise
	 */
	public boolean testConfiguration() {
		return getSshClient().connect();
	}

	/**
	 * Get the current ssh client associated with this configuration, or create
	 * one
	 * 
	 * @return an ssh client associated with this configuration
	 */
	public SshClient getSshClient() {
		if (instance == null) {
			instance = new SshClient(this);
		}
		return instance;
	}
}
