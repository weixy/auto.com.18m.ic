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
import com.ibm.bpm.automation.ic.constants.OperationParameters;
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
		
		if (OperationParameters.MGRCONF_ACT_VALID.getAction().equalsIgnoreCase(getAction())) {
			if (OperationParameters.MGRCONF_TYPE_CTXROOT.getType().equalsIgnoreCase(getType())) {
				
				result.append(managerConfigValidCtxRoot(config));
				
			}
			else {
				logger.log(LogLevel.ERROR, "Undefined type '" + getType() + "'.");
				result.append("Undefined type '" + getType() + "'.");
				failedPoints = getPoints();
			}
		}
		else {
			logger.log(LogLevel.ERROR, "Undefined action '" + getAction() + "'.");
			result.append("Undefined action '" + getAction() + "'.");
			failedPoints = getPoints();
		}
		
		System.out.println(result);
		submit(result.toString(), logger);
	}
	
	public String managerConfigValidCtxRoot(HashMap<String, Object> config) {
		final StringBuffer result = new StringBuffer();
		final String appsFolder = Application.getAppFolderRootPath(config);
		//System.out.println(appsFolder);
		//System.out.println((String)config.get(Configurations.APPCLUSTER.getKey()));
		
		logger.log(LogLevel.INFO, "Get all application names.");
		final HashMap<String, String> appsMap = Application.getInstalledAppList(appsFolder, (String)config.get(Configurations.APPCLUSTER.getKey()));
		if (appsMap == null) {
			result.append("The path of applicaiton folder you specified is wrong:" 
			+ System.getProperty("line.separator")
			+ appsFolder
			+ System.getProperty("line.separator"));
			failedPoints = getPoints();
		}
		else {
			result.append("Succeed to get application list.");
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
						result.append("Applicaiton '" + appName 
								+ "' will be ignored" + System.getProperty("line.separator"));
						logger.log(LogLevel.INFO, "Applicaiton '" + appName 
								+ "' will be ignored" + System.getProperty("line.separator"));
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
		}
		return result.toString();
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
				HashMap<String, String> webModuleMap = Application.getWebModuleCtxRoot(appFileName);
				Iterator<Map.Entry<String, String>> it = webModuleMap.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
					String webModule = entry.getKey();
					if (!webModule.equals(filter)) {
						String ctxRoot = entry.getValue();
						if (ctxRoot.startsWith("/" + this.getData() + "/")) {
							result.append(appName + ":" + webModule 
									+ "@" + ctxRoot 
									+ " is correct." + System.getProperty("line.separator"));
							logger.log(LogLevel.INFO, "Applicaiton '" + appName
									+ ", Web Module '" + webModule 
									+ "', ContextRoot '" + ctxRoot 
									+ "' is correct." + System.getProperty("line.separator"));
							successPoints++;
						}
						else {
							result.append(appName + " : " + webModule 
									+ " @ " + ctxRoot 
									+ " is WRONG!" + System.getProperty("line.separator"));
							logger.log(LogLevel.ERROR, "Applicaiton '" + appName 
									+ "', Web Module '" + webModule 
									+ "', ContextRoot '" + ctxRoot 
									+ "' is WRONG!" + System.getProperty("line.separator"));
							failedPoints++;
						}
					}
					else {
						result.append(appName + ":" + webModule
								+ "' will be ignored" + System.getProperty("line.separator"));
						logger.log(LogLevel.INFO, "Applicaiton '" + appName 
								+ "', Web Module '" + webModule 
								+ "' will be ignored" + System.getProperty("line.separator"));
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
				if (!line.startsWith("#") && !line.startsWith("//") && !line.equals("")) {
					line = line.trim();
					String [] namePair = line.split(",");
					map.put(namePair[0], namePair[1]);
				}
			}
			
		} catch (Exception e) {
			//throw new AutoException(e);
			logger.log(LogLevel.WARNING, "Can't find file 'appexempt.list', will analyse all apps", e);
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				//throw new AutoException(e);
				logger.log(LogLevel.WARNING, "Got IO Exception while closing File/Buffer reader of file 'index.txt'.", e);
			}
		}
		return map;
	}
	
	
	
	

}
