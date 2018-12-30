package com.pumkin.sshcontroller.object;

import com.pumkin.sshcontroller.activity.SshControllerActivity;
import com.pumkin.sshcontroller.ssh.SshClient;

/**
 * Class used to save the screensize, current activity and selected controller
 * 
 * @author Laurent S.
 * 
 */
public class CurrentConfiguration {

	public static int widthInPixel = -1;
	public static int heightInPixel = -1;

	/**
	 * Current instance of the application's activity. It can be useful to use
	 * it to save objects
	 */
	public static SshControllerActivity instance = null;
	
	/**
	 * Selected controller
	 */
	public static Controller controller;
}
