package com.ibm.bpm.automation.ic.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.OperationSystem;

public class CommandUtil {
	
	private static final String CLASSNAME = CommandUtil.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String getCommandByOS(String cmd) {
		String command = null;
		String osName = System.getProperty("os.name").toLowerCase();
		OperationSystem os = getSystem(osName);
		if (null != os) {
			command = cmd + os.getOsCmdSuffix();
		}
		else {
			logger.log(LogLevel.ERROR, "Unknown operation system type '" + osName + "' found!");
		}
		return command;
	}
	
	public static OperationSystem getSystem(String osName) {
		OperationSystem os = null;
		for(OperationSystem o : OperationSystem.values()) {
			if (osName.indexOf(o.getOsType())>-1) {
				os = o;
				break;
			}
		}
		return os;
	}
	
	public static String executeCommnd(List<String> cmds, String folder) {
		try {
			ProcessBuilder pb = new ProcessBuilder(cmds);
			pb.redirectErrorStream(true);
			pb.directory(new File(folder));
			Process p = pb.start();
			inheritIO(p.getInputStream(), System.out);
			//inheritIO(p.getErrorStream(), System.err);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer result = new StringBuffer("");
			String line;
			while(null != (line = reader.readLine())) {
				result.append(line + System.getProperty("line.separator"));
			}
			p.waitFor();
			reader.close();
			return result.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(LogLevel.ERROR, "Error executing commands: '" + cmds.get(0) + "'!", e);
			return null;
		}
		
	}
	
	private static void inheritIO(final InputStream src, final PrintStream dest) {
		new Thread(new Runnable() {
			public void run() {
				Scanner sc = new Scanner(src);
				while(sc.hasNextLine()) {
					dest.println(sc.nextLine());
				}
			}
		}).start();
	}

}
