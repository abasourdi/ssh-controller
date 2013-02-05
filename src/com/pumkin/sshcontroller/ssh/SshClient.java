package com.pumkin.sshcontroller.ssh;

import java.io.InputStream;
import java.util.ArrayList;

import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.pumkin.sshcontroller.object.SshConfiguration;
import com.pumkin.sshcontroller.object.SshFile;

public class SshClient {

	Session session;

	SshConfiguration configuration;

	String currentPath = "";

	boolean isWindows=false;
	
	public SshClient(SshConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean connect() {
		try {
			JSch shell = new JSch();
			session = shell.getSession(configuration.username,
					configuration.host, configuration.port);
			MyUserInfo ui = new MyUserInfo();
			ui.setPassword(configuration.password);

			session.setConfig("StrictHostKeyChecking", "no");

			session.setUserInfo(ui);
			session.setTimeout(20000);
			session.connect();
			
			if (isConnected()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getCurrentPath() {
		ArrayList<String> tmpRes = execute("pwd", true);
		System.out.println("after execute");
		if (tmpRes.size() > 0) {
			if ("0".equals(tmpRes.get(tmpRes.size() - 1))) {
				System.out.println("returning path: " + tmpRes.get(0));
				isWindows=false;
				return tmpRes.get(0).trim();
			}
//			Log.i(this.getClass().toString(), "ignoring as pwd doesn't work");
			System.out.println("ignoring");
		}

		System.out.println("ignoring");
		// WINDOWS SHELL,
		tmpRes = execute("cmd /c chdir", true);
		if (tmpRes.size() > 0) {
			if ("0".equals(tmpRes.get(tmpRes.size() - 1))) {
				isWindows=true;
				return tmpRes.get(0).trim();
			}
			Log.i(this.getClass().toString(), "ignoring as cmd doesn't work");
		}
		// => GO TO BASE DIRECTORY AS NO IDEA
		return "/";
	}

	public ArrayList<SshFile> ls(String path) {
		String command;
		if(isWindows){
			command = "ls -l '" + path + "'";
		}else{
			command = "/bin/ls -l '" + path + "'";
		}
		ArrayList<String> tmpFiles = execute(command, true);
		ArrayList<SshFile> res = new ArrayList<SshFile>();
		if (tmpFiles.size() > 1
				&& tmpFiles.get(tmpFiles.size() - 1).equals("0")) {
			for (int i = 1; i < tmpFiles.size() - 1; i++) {
				res.add(new SshFile(tmpFiles.get(i), path, isWindows));
			}
		}else if(tmpFiles.size() > 1
				&& tmpFiles.get(tmpFiles.size() - 1).equals("1")) {
			for (int i = 1; i < tmpFiles.size() - 1; i++) {
				if(tmpFiles.get(i).length()>57)
					res.add(new SshFile(tmpFiles.get(i), path, isWindows));
			}
		}
		return SshFile.sort(res, path);
	}
	
	public ArrayList<String> execute(String command) {
		return execute(command, false);
	}

	public ArrayList<String> execute(String command, boolean careForOutput) {
		ArrayList<String> res = new ArrayList<String>();
		try {
			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command);
			System.out.println(command);

			channel.connect();
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
//			InputStream inErr=channel.getErrStream();
			InputStream in = channel.getInputStream();

			byte[] tmp = new byte[1024];
			String tmpRes = "";
			while (true) {
//				while (inErr.available() > 0) {
//					int i = inErr.read(tmp, 0, 1024);
//					if (i < 0) {
//						break;
//					}
//					System.out.println("getting stringerror: "
//							+ new String(tmp, 0, i));
//				}
				if(careForOutput)
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0) {
						break;
					}
					System.out.println("getting string: "
							+ new String(tmp, 0, i));
					// res.add(new String(tmp, 0, i));
					tmpRes += new String(tmp, 0, i);
				}
				if (channel.isClosed()) {
					System.out.println("exit-status: "
							+ channel.getExitStatus());
					String[] tmp1 = tmpRes.split("\n");
					for (int i = 0; i < tmp1.length; i++) {
						res.add(tmp1[i]);
					}
					System.out.println("tobreak");
					res.add("" + channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			channel.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end execute");
		return res;
	}

	public boolean isConnected() {
		return (session != null && session.isConnected());
	}

	public void disconnect() {
		if (isConnected()) {
			session.disconnect();
		}
	}
}