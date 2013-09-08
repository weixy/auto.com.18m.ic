package com.ibm.bpm.automation.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationParameters;
import com.ibm.bpm.automation.ic.constants.RegularPatterns;
import com.ibm.bpm.automation.ic.tap.TestRobot;
import com.ibm.bpm.automation.ic.utils.CommandUtil;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.ic.utils.WSAdminUtil;

public class ManagerConfigOperation extends BaseOperation {
	
	private static final String CLASSNAME = ManagerConfigOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run(HashMap<String, Object> config) {
		// TODO Auto-generated method stub
		super.run(config);
		logger.log(LogLevel.INFO, "Invoke operation '" + ManagerConfigOperation.class.getSimpleName() + "'");
		StringBuffer result = new StringBuffer();
		
		String workingFolder = config.get(Configurations.BPMPATH.getKey()) + File.separator + "bin";
		String propFileFolderPath = System.getProperty("user.dir") + File.separator + TestRobot.ICAUTO_OUTPUT_PATH;
		
		logger.log(LogLevel.INFO, "Get all application names.");
		List<String> apps = getInstalledAppList(workingFolder);
		logger.log(LogLevel.INFO, "Generate property file for all applications.");
		HashMap<String, String> propFilePathes = configPropertyFileForApps(propFileFolderPath, apps, workingFolder);
		logger.log(LogLevel.INFO, "Check the context root for applications.");
		for (Iterator<Map.Entry<String, String>> it = propFilePathes.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, String> entry = it.next();
			result.append(checkAppContextRoot(entry.getKey(), entry.getValue(), getData()));
		}
		
		//System.out.println(result);
		submit(result.toString(), logger);
	}
	
	public List<String> getInstalledAppList(String workingFolder) {
		List<String> apps = new ArrayList<String>();
		List<String> cmds = WSAdminUtil.getCmdsForAppList();
		String [] result = WSAdminUtil.executeCommnd(cmds, workingFolder).split("\r\n");
		for (String app : result) {
			if (!app.startsWith("WASX7357I") && app.length()>0) {
				apps.add(app);
			}
		}
		return apps;
	}
	
	public String configPropertyFileForApp(String fileFolder, String appName, String workingFolder) {
		String appPropFilePath = fileFolder + File.separator + appName.trim().replace(" ",  "") + WSAdminUtil.CONF_PROPFILE_SUFFIX;
		System.out.println(appPropFilePath);
		File propFile = new File(appPropFilePath);
		if (propFile.exists()) {
			propFile.delete();
		}
		List<String> cmds = WSAdminUtil.getCmdsForExtractConfigProperties();
		for (String cmd : cmds) {
			int index = cmds.indexOf(cmd);
			if (cmd.indexOf(WSAdminUtil.CMD_PARAMETER_APPNAME) != -1) {
				cmd = cmd.replaceAll(WSAdminUtil.CMD_PARAMETER_APPNAME, appName);
			}
			if (cmd.indexOf(WSAdminUtil.CMD_PARAMETER_PROPFILE) != -1) {
				cmd = cmd.replaceAll(WSAdminUtil.CMD_PARAMETER_PROPFILE, appPropFilePath.replace('\\', '/'));
			}
			cmds.set(index, cmd);
		}
		
		String result = WSAdminUtil.executeCommnd(cmds, workingFolder);
		System.out.println(result + "\n");

		Pattern failPattern = Pattern.compile(RegularPatterns.REG_WSADMIN_ERR);
		Matcher failMatcher = failPattern.matcher(result);
		if (failMatcher.find()) {
			logger.log(LogLevel.ERROR, "Failed to generate config propert file for Application: '" + appName + "'");
			//appPropFilePath = null;
		}
		return appPropFilePath;
	}
	
	public HashMap<String, String> configPropertyFileForApps(String fileFolder, List<String> apps, String workingFolder) {
		HashMap<String, String> propFilePathes = new HashMap<String, String>();
		for (String app : apps) {
			propFilePathes.put(app, configPropertyFileForApp(fileFolder, app, workingFolder));
		}
		return propFilePathes;
	}
	
	public String checkAppContextRoot(String appName, String propFilePath, String contextRoot) {
		StringBuffer result = new StringBuffer();
		File propFile = new File(propFilePath);
		result.append("Start to parse context root of application '" + appName + "'." + System.getProperty("line.separator"));
		
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(propFile);
			br = new BufferedReader(fr);
			String line = null;
			boolean startRecord = false;
			boolean foundContextRoot = false;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (!line.startsWith("#")) {
					if (("taskName="+WSAdminUtil.CONF_PROP_CONTEXTROOT).equals(line)) {
						result.append("Found context root section." + System.getProperty("line.separator"));
						startRecord = true;
						foundContextRoot = true;
					}
					if (line.startsWith("row0=")) {
						startRecord = false;
					}
					if (startRecord && line.startsWith("row")) {
						String webModule = null;
						String context = null;
						String record = line.substring(line.indexOf('{')+1, line.indexOf('}'));
						if (record.startsWith("\"")) {
							webModule = record.substring(1, record.lastIndexOf('\"'));
							context = record.substring(record.lastIndexOf(' ') + 1);
						} else {
							String[] row = record.split("\\s");
							webModule = row[0];
							context = row[2];
						}
						result.append("Web Module '" + webModule + "' has context root: <" + context + ">." + System.getProperty("line.separator"));
						if (context.startsWith("/" + contextRoot)) {
							result.append("Correct!" + System.getProperty("line.separator"));
							logger.log(LogLevel.INFO, "Web Module '" + webModule 
									+ "', Applicaiton '" + appName 
									+ "', ContextRoot '" + context 
									+ "' is correct." + System.getProperty("line.separator"));
							successPoints++;
						}
						else {
							result.append("Wrong!" + System.getProperty("line.separator"));
							logger.log(LogLevel.ERROR, "Web Module '" + webModule 
									+ "', Applicaiton '" + appName 
									+ "', ContextRoot '" + context 
									+ "' is WRONG!" + System.getProperty("line.separator"));
							failedPoints++;
						}
					}
				}
			}
			
			if (!foundContextRoot) {
				result.append("Didn't find context root property for application '" + appName + "'.");
				logger.log(LogLevel.WARNING, "Didn't find context root property for application '" + appName + "'.");
			}
			
		} catch (Exception e) {
			logger.log(LogLevel.ERROR, "Got exception while reading file '" + propFilePath + "'", e);
			failedPoints++;
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e) {
				logger.log(LogLevel.ERROR, "Got exception when finish reading file '" + propFilePath + "'", e);
				failedPoints++;
			}
		}
		result.append("Finished the check for the context root of application '" + appName + "'." + System.getProperty("line.separator"));
		
		return result.toString();
	}

}
