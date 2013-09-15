/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.utils.CommandUtil;

public class TestBasic {

	public static void main(String[] args) {
		List<String> cmds = new ArrayList<String>();
		String folder = "E:\\bpm\\85\\STANDARD\\deploy2\\AppServer\\bin";
		cmds.add("E:\\bpm\\85\\STANDARD\\deploy2\\AppServer\\bin\\wsadmin.bat");
		cmds.add("-lang");
		cmds.add("jython");
		cmds.add("-conntype");
		cmds.add("NONE");
		
		try {
			ProcessBuilder pb = new ProcessBuilder(cmds);
			pb.redirectErrorStream(true);
			pb.directory(new File(folder));
			Process p = pb.start();
			//inheritIO(p.getInputStream(), System.out);
			//inheritIO(p.getErrorStream(), System.out);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer result = new StringBuffer("");
			String line;
			while(null != (line = reader.readLine())) {
				System.out.println(">>>" + line);
				result.append(line + System.getProperty("line.separator"));
				if (line.startsWith("WASX7357I:")) {
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
					writer.write("print AdminApp.list()");
					writer.newLine();
					writer.flush();
					writer.close();
				}
			}
			p.waitFor();
			reader.close();
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
