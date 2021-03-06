package com.pumkin.sshcontroller.object;

import java.util.ArrayList;

import android.util.Log;

/**
 * 
 * @author Laurent S.
 * 
 */
public class SshFile {

	public String directory;
	public String name;
	public boolean isDirectory;
	public boolean isReadable;
	public boolean isExecutable;

	/**
	 * Add the parent directory
	 * 
	 * @param lsPath
	 *            used to get the name of the file
	 */
	public SshFile(String lsPath) {
		this.name = "..";
		isDirectory = true;
		isReadable = true;
		isExecutable = false;
		setDirectory(lsPath);
	}

	/**
	 * 
	 * @param rights
	 *            rights of the define, as given by the ls -l
	 * @param name
	 *            name of the file, as given by the ls -l
	 * @param lsPath
	 *            directory where the file is
	 */
	public SshFile(String rights, String name, String lsPath) {
		this.name = name;
		// CHECK IF IT S READABLE
		isDirectory = rights.substring(0, 1).equalsIgnoreCase("d");
		// TODO DISTINGUISH EXECUTABLE AND READABLE FILE
		isReadable = true;
		isExecutable = true;
		setDirectory(lsPath);
	}

	/**
	 * Determine if it is a windows or linux system, and get the path
	 * accordinally
	 * 
	 * @param lsPath
	 *            current path
	 */
	public void setDirectory(String lsPath) {
		if (lsPath.endsWith("/") || lsPath.endsWith("\\")) {
			directory = lsPath;
		} else {
			if (lsPath.lastIndexOf("/") != -1) {
				directory = lsPath + "/";
			} else if (lsPath.lastIndexOf("\\") != -1) {
				directory = lsPath + "\\";
			} else {
				// TODO INVESTIGATE, no windows, no linux, wtf?
				directory = "/";
			}
		}
	}

	/**
	 * 
	 * @return full path of the sshFile, it will be the command line
	 */
	public String getFullPath() {
		return directory + name;
	}

	public String getParentDirectory() {
		String res = directory.substring(0, directory.length() - 1);
		if (res.lastIndexOf("/") != -1) {
			res = res.substring(0, res.lastIndexOf("/") + 1);
		} else if (res.lastIndexOf("\\") != -1) {
			res = res.substring(0, res.lastIndexOf("\\") + 1);
		}
		return res;
	}

	/**
	 * 
	 * @param files all the files given
	 * @param lsPath current path
	 * @return a sorted array, with the parent directory if there is one
	 */
	public static ArrayList<SshFile> sort(ArrayList<SshFile> files,
			String lsPath) {
		ArrayList<SshFile> res = new ArrayList<SshFile>();

		// Add null if needed
		if (lsPath != null) {
			SshFile parent = new SshFile(lsPath);
			if (parent.directory.contains("/")) {
				if (parent.directory.lastIndexOf("/") != parent.directory
						.indexOf("/")) {
					res.add(parent);
				}
			} else if (parent.directory.contains("\\")) {
				if (parent.directory.lastIndexOf("\\") != parent.directory
						.indexOf("\\")) {
					res.add(parent);
				}
			}

		}
		for (int i = 0; i < files.size(); i++) {
			if (files.get(i).isDirectory) {
				res.add(files.get(i));
			}
		}
		for (int i = 0; i < files.size(); i++) {
			if (!files.get(i).isDirectory) {
				res.add(files.get(i));
			}
		}
		return res;
	}
}
