package com.pumkin.sshcontroller.object;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represent an action, basically a ssh command (or the path to a script)
 * 
 * @author Laurent S.
 * 
 */
public class Action implements Serializable {

	/**
	 * If we should ask for confirmation. It can be usefull for script such as a
	 * shutdown, reboot, restart tomcat, and so on
	 */
	public boolean confirmation = false;

	/**
	 * If set to true, we will execute the script command with the parameters,
	 * but we will also add 2 parameters, -X and -Y so that the relative
	 * position to the center of the button is sent. It can be usefull to do a
	 * robot with the repeative action
	 */
	public boolean sendCoordinate = false;

	/**
	 * If enable, we will execute the action continiuosly with the interval
	 * repeatedInterval
	 */
	public boolean repeatedEnabled = false;

	/**
	 * Only used if repeatedEnabled is set to true. The script will be execute
	 * every repeatedInterval ms
	 */
	public int repeatedInterval = 50;

	/**
	 * TO BE DEFINED LATER
	 */
	public boolean listAction = false;

	/**
	 * The command line that will be executed, usually a script
	 */
	public String command = "";

	/**
	 * Parameters that can be added to the command, we do not use it for now
	 */
	public ArrayList<String> parameters = new ArrayList<String>();

}
