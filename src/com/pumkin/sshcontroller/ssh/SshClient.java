package com.pumkin.sshcontroller.ssh;

import java.io.InputStream;
import java.util.ArrayList;

import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.pumkin.sshcontroller.object.GlobalConfiguration;
import com.pumkin.sshcontroller.object.SshConfiguration;
import com.pumkin.sshcontroller.object.SshFile;

public class SshClient {

	Session session;

	SshConfiguration configuration;

	String currentPath = "";

	// boolean isWindows = false;

	public SshClient(SshConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean connect() {
		try { 
			Log.e("toto", "TEST NEW CONNECTE");
			JSch shell = new JSch();
			session = shell.getSession(configuration.username,
					configuration.host, configuration.port);
			MyUserInfo ui = new MyUserInfo();
			ui.setPassword(configuration.password);

			session.setConfig("StrictHostKeyChecking", "no");

			session.setUserInfo(ui);
			// If more than 5 secs, fuck of
			session.setTimeout(30000);
			System.out.println("before connect");
			session.connect();
			System.out.println("after connect");
 
			if (isConnected()) {
				//We need to open a channel to speed things up later
				Thread t=new Thread(){
					@Override
					public void run(){
						System.out.println("EXECUTE");
						System.out.println(session.isConnected());
						System.out.println("ENDEXECUTE");
					}
				};
				t.start();
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
				// isWindows = false;
				return tmpRes.get(0).trim();
			}
			// Log.i(this.getClass().toString(),
			// "ignoring as pwd doesn't work");
			System.out.println("ignoring");
		}

		/*
		 * System.out.println("ignoring"); // WINDOWS SHELL, tmpRes =
		 * execute("cmd /c chdir", true); if (tmpRes.size() > 0) { if
		 * ("0".equals(tmpRes.get(tmpRes.size() - 1))) { // isWindows = true;
		 * return tmpRes.get(0).trim(); } Log.i(this.getClass().toString(),
		 * "ignoring as cmd doesn't work"); }
		 */
		return "/";
	}

	public ArrayList<SshFile> ls(String path) {
		String command;
		// if (isWindows) {
		// command = "ls -l '" + path + "'";
		// } else {
		// // command = "ls -lrth '" + path + "'| tr -s ' ' | cut -d' ' -f1,9";
		// command = "ls -l '" + path + "'";
		// }
		if(GlobalConfiguration.islookForHiddenFilesEnabled()){
			command = "ls -la '" + path + "'";
		}else{
			command = "ls -l '" + path + "'";	
		}
		/*
		 * Classic ls command is 1 rights
		 */
		ArrayList<String> tmpFiles = execute(command, true);
		ArrayList<SshFile> res = new ArrayList<SshFile>();
		for (int i = 1; i < tmpFiles.size() - 1; i++) {
			// For each file, we get the name and the rights
			String[] list = tmpFiles.get(i).split(" ");
			String rights = list[0];
			int j = 8;
			for (int k = 0; k < j; k++) {
				// We use that to remove the empty space (ie 1 space after 1 other)
				// other)
				if (list[k].equals("")) {
					j++;
				}
			}
			String name = list[j];
			j++;
			for (; j < list.length; j++) {
				name += " " + list[j];
			}
			//We do this verification to remove . and ..
			if(!name.equals(".") && !name.equals("..")){
				res.add(new SshFile(rights, name, path));
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

			// channel.setXForwarding(true);
			channel.setCommand(command);

			channel.connect();
			/*
			 * HERE, there should always be at least 10 channel opened or
			 * something like that
			 */
			System.out.println("after connect");
			channel.setInputStream(null);
			System.out.println("after setInputStream");
			((ChannelExec) channel).setErrStream(System.err);
			// InputStream inErr=channel.getErrStream();
			InputStream in = channel.getInputStream();

			byte[] tmp = new byte[1024];
			String tmpRes = "";
			while (true) {
				// while (inErr.available() > 0) {
				// int i = inErr.read(tmp, 0, 1024);
				// if (i < 0) {
				// break;
				// }
				// System.out.println("getting stringerror: "
				// + new String(tmp, 0, i));
				// }
				if (careForOutput)
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
				} catch (Exception e) {
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