/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.utils.CommandUtil;
import com.ibm.bpm.automation.ic.utils.ConfigXMLHandler;
import com.ibm.bpm.automation.ic.wasconfig.Application;
import com.ibm.bpm.automation.ic.wasconfig.ServerIndex;

public class TestBasic {

	public static void main(String[] args) throws Exception{
		
		String filename = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL", new Date());
		System.out.println(filename);
		
		
		/*System.out.println(ServerIndex.getWCDefaultHostPort(
				"D:\\Bkups\\Work\\Automation\\com.18m.auto.ic\\TacitBlue\\outputs\\serverindex.xml", 
				ServerIndex.XPATH_WCDEFHOST));*/
		
		/*HashMap<String, String> map = Application.getWebModuleCtxRoot("E:/bpm/85/STANDARD/deploy2/AppServer/profiles/StandAloneProfile/config/cells/nodename1Node01Cell/applications/IBM_BPM_Teamworks_nodename1_server1.ear/deployments/IBM_BPM_Teamworks_nodename1_server1/META-INF/application.xml");
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}*/
		
		/*ConfigXMLHandler cxmlh = new ConfigXMLHandler(
				"D:\\Bkups\\Work\\Automation\\com.18m.auto.ic\\TacitBlue\\outputs\\application.xml",
				"//module/web");
		
		NodeList nodes = cxmlh.evaluate();
		for (int i=0; i<nodes.getLength();i++) {
			XPath xpath = XPathFactory.newInstance().newXPath();
			System.out.println((String)xpath.evaluate("web-uri", nodes.item(i), XPathConstants.STRING));
		}*/
		
		/*HashMap<String, String> map = new HashMap<String, String>();
		map.put("aaa", "123");
		map.put("bbb", "456");
		map.put("aaa", "999");
		Iterator iter = map.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>)iter.next();
			System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
		}*/
		
		/*List<String> cmds = new ArrayList<String>();
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
		}*/
		
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
