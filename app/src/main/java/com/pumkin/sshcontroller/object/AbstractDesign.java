package com.pumkin.sshcontroller.object;

import java.io.Serializable;

/**
 * Used to represent the design, It can be either a picture, or a custom android
 * button with a label
 * 
 * @author Laurent S.
 * 
 */
public abstract class AbstractDesign implements Serializable {

	/**
	 * Width, in dp
	 */
	public int width;
	
	/**
	 * Height, in dp
	 */
	public int height;
}
