/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.TestCase;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.RegularPatterns;
import com.ibm.bpm.automation.ic.tap.TestRobot;
import com.ibm.bpm.automation.ic.utils.AutoFileFilter;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.ic.utils.WSAdminUtil;
import com.ibm.bpm.automation.ic.wasconfig.Application;

public class ManagerConfigOperation extends BaseOperation {
	
	private static final String CLASSNAME = ManagerConfigOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run(HashMap<String, Object> config) {
		// TODO Auto-generated method stub
		super.run(config);
		logger.log(LogLevel.INFO, "Invoke operation '" + ManagerConfigOperation.class.getSimpleName() + "'");
		final StringBuffer result = new StringBuffer();
		final String contextRoot = getData();
		final String appsFolder = getAppFolderRootPath(config);
		System.out.println(appsFolder);
		System.out.println((String)config.get(Configurations.APPCLUSTER.getKey()));
		
		logger.log(LogLevel.INFO, "Get all application names.");
		final HashMap<String, String> appsMap = getInstalledAppList(appsFolder, (String)config.get(Configurations.APPCLUSTER.getKey()), result);
		System.out.println(appsMap.size());		
		
		logger.log(LogLevel.INFO, "Get and sort application exempt list.");
		HashMap<String, String> exemptMap = getExemptList();
		Iterator<Map.Entry<String, String>> iter = exemptMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>)iter.next();
			String appName = (String) entry.getKey() + "_" + (String)config.get(Configurations.APPCLUSTER.getKey()) + ".ear";
			String filter = (String) entry.getValue();
			if (appsMap.containsKey(appName)) {
				if (filter.equals("*")) {
					appsMap.remove(appName);
				}
				else {
					appsMap.put(appName, filter);
				}
			}
		}
		
		logger.log(LogLevel.INFO, "Check context root for apps.");
		ExecutorService exec = Executors.newCachedThreadPool();
		final CountDownLatch runningThreadNum = new CountDownLatch(appsMap.size());
		Iterator<Map.Entry<String, String>> it = appsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
			//String appName = (String) entry.getKey();
			//String filter = (String) entry.getValue();
			//result.append(checkAppCtxRoots(appsFolder, appName, filter));
			final String appName = (String) entry.getKey();
			final String filter = (String) entry.getValue();
			Runnable run = new Runnable() {
				public void run() {
					result.append(checkAppCtxRoots(appsFolder, appName, filter));
					runningThreadNum.countDown();
				}
			};
			exec.execute(run);
		}
		exec.shutdown();
		try {
			runningThreadNum.await();
		} catch (InterruptedException e) {
			logger.log(LogLevel.ERROR, "Met exception when waiting sub thread complete.", e);
		}
		
		System.out.println(result);
		submit(result.toString(), logger);
	}
	
	public String checkAppCtxRoots(String appFolderPath, String appName, String filter) {
		StringBuffer result = new StringBuffer();
		if (filter.equals("*")) {
			logger.log(LogLevel.WARNING, "The application '" + appName + "' won't be checked since all modules are filtered.");
		}
		else {
			String appFileName = appFolderPath + File.separator
					+ appName + File.separator
					+ "deployments" + File.separator
					+ appName.replace(".ear", "") + File.separator
					+ "META-INF" + File.separator
					+ "application.xml";
			try {
				HashMap<String, String> webModuleMap = Application.getWebModuleCtxRoot(appFileName, Application.XPATH_WEBMODULE);
				Iterator<Map.Entry<String, String>> it = webModuleMap.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
					String webModule = entry.getKey();
					if (!webModule.equals(filter)) {
						String ctxRoot = entry.getValue();
						if (ctxRoot.startsWith("/" + this.getData() + "/")) {
							result.append("Correct!" + System.getProperty("line.separator"));
							logger.log(LogLevel.INFO, "Web Module '" + webModule 
									+ "', Applicaiton '" + appName 
									+ "', ContextRoot '" + ctxRoot 
									+ "' is correct." + System.getProperty("line.separator"));
							successPoints++;
						}
						else {
							result.append("Wrong!" + System.getProperty("line.separator"));
							logger.log(LogLevel.ERROR, "Web Module '" + webModule 
									+ "', Applicaiton '" + appName 
									+ "', ContextRoot '" + ctxRoot 
									+ "' is WRONG!" + System.getProperty("line.separator"));
							failedPoints++;
						}
					}
				}
			} catch (Exception e) {
				result.append("Failed to extract context root of application '" + appName + "'." + System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Failed to extract context root of application '" + appName + "'.", e);
				failedPoints ++;
			}
		}
		return result.toString();
	}
	
	public HashMap<String, String> getExemptList() {
		HashMap<String, String> map = new HashMap<String, String>();
		String filePath = System.getProperty("user.dir") + File.separator 
				+ TestRobot.ICAUTO_CONFIG_PATH + File.separator
				+ "appexempt.list";
		File appExemptList = new File(filePath);
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(appExemptList);
			br = new BufferedReader(fr);
			
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				String [] namePair = line.split(",");
				map.put(namePair[0], namePair[1]);
			}
			
		} catch (Exception e) {
			//throw new AutoException(e);
			logger.log(LogLevel.ERROR, "Can't find file 'appexempt.list', will analyse all apps", e);
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				//throw new AutoException(e);
				logger.log(LogLevel.ERROR, "Got IO Exception while closing File/Buffer reader of file 'index.txt'.", e);
			}
		}
		return map;
	}
	
	public String getAppFolderRootPath(HashMap<String, Object> config) {
		String path = config.get(Configurations.BPMPATH.getKey()) + File.separator
				+ "profiles" + File.separator
				+ config.get(Configurations.DMGRPROF.getKey()) + File.separator
				+ "config" + File.separator + "cells" + File.separator
				+ config.get(Configurations.CELLNAME.getKey()) + File.separator
				+ "applications";
		
		return path;
	}
	
	public HashMap<String, String> getInstalledAppList(String appFolderPath, String clusterName, StringBuffer result) {
		HashMap<String, String> apps = new HashMap<String, String>();
		File appFolder = new File(appFolderPath);
		if (appFolder.isDirectory()) {
			File [] subFolders = appFolder.listFiles(new AutoFileFilter("_" + clusterName + "\\.ear"));
			for (File d : subFolders) {
				if (d.isDirectory()) {
					apps.put(d.getName(), "-");
				}
			}
			return apps;
		}
		else {
			logger.log(LogLevel.ERROR, "The specified applications folder is a file, '" + appFolderPath + "'");
			failedPoints =  getPoints();
			return null;
		}
	}

}
