/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.zbkup;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.RegularPatterns;
import com.ibm.bpm.automation.ic.operations.BaseOperation;
import com.ibm.bpm.automation.ic.tap.TestRobot;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.ic.utils.WSAdminUtil;

public class OldManagerConfigOperation extends BaseOperation {
	
	private static final String CLASSNAME = OldManagerConfigOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run(HashMap<String, Object> config) {
		// TODO Auto-generated method stub
		super.run(config);
		logger.log(LogLevel.INFO, "Invoke operation '" + OldManagerConfigOperation.class.getSimpleName() + "'");
		final StringBuffer result = new StringBuffer();
		final String contextRoot = this.getData();
		final String workingFolder = config.get(Configurations.BPMPATH.getKey()) + File.separator + "bin";
		final String propFileFolderPath = System.getProperty("user.dir") + File.separator + TestRobot.ICAUTO_OUTPUT_PATH;
		
		logger.log(LogLevel.INFO, "Get all application names.");
		final List<String> apps = getInstalledAppList(workingFolder, result);
		System.out.println(apps.size());
		logger.log(LogLevel.INFO, "Generate and verify property file for all applications.");
		
		int threadNum = apps.size();
		ExecutorService exec = Executors.newCachedThreadPool();
		final Semaphore semp = new Semaphore(10);
		final CountDownLatch runningThreadNum = new CountDownLatch(threadNum);
		for (int i = 0; i < threadNum; i++) {
			final int index = i;
			Runnable run = new Runnable() {
				public void run() {
					StringBuffer r = new StringBuffer();
					try {
						semp.acquire();
						String pFilePath = configPropertyFileForApp(propFileFolderPath, apps.get(index), workingFolder, r);
						result.append(r);
						Thread.sleep(500);
						result.append(checkAppContextRoot(apps.get(index), pFilePath, contextRoot));
						runningThreadNum.countDown();
						semp.release();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			exec.execute(run);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		exec.shutdown();
		try {
			runningThreadNum.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(apps.size());
		//HashMap<String, String> propFilePathes = configPropertyFileForApps(propFileFolderPath, apps, workingFolder, result);
		//System.out.println(result);
		/*logger.log(LogLevel.INFO, "Check the context root for applications.");
		for (Iterator<Map.Entry<String, String>> it = propFilePathes.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, String> entry = it.next();
			if (!entry.getKey().startsWith("REST Services Gateway")) {//has problem if extract config properties for REST Service Gateway, need another way to check it.
				result.append(checkAppContextRoot(entry.getKey(), entry.getValue(), getData()));
			}
			else {
				
			}
		}*/
		
		System.out.println(result);
		submit(result.toString(), logger);
	}
	
	public List<String> getInstalledAppList(String workingFolder, StringBuffer result) {
		List<String> apps = new ArrayList<String>();
		List<String> cmds = WSAdminUtil.getCmdsForAppList();
		result.append("Get App List by wsadmin command." + System.getProperty("line.separator"));
		String rep = WSAdminUtil.executeCommnd(cmds, workingFolder);
		result.append(rep);
		String [] appList = rep.replace("\r", "").split("\n");
		for (String app : appList) {
			if (!app.startsWith("WASX7357I") && app.length()>0) {
				apps.add(app);
			}
		}
		
		return apps;
	}
	
	public String configPropertyFileForApp(String fileFolder, String appName, String workingFolder, StringBuffer result) {
		File folder = new File(fileFolder);
		if (!folder.exists()) {
			folder.mkdir();
		}
		else if (folder.isFile()){
			folder.delete();
			folder.mkdir();
		}
		
		String appPropFilePath = fileFolder + File.separator + appName.trim().replace(" ",  "") + WSAdminUtil.CONF_PROPFILE_SUFFIX;
		//System.out.println(appPropFilePath);
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
		
		result.append(WSAdminUtil.executeCommnd(cmds, workingFolder));
		System.out.println("generate config prop file for '" + appName + "'\n" + result + "\n");

		Pattern failPattern = Pattern.compile(RegularPatterns.REG_WSADMIN_ERR);
		Matcher failMatcher = failPattern.matcher(result);
		if (failMatcher.find()) {
			logger.log(LogLevel.ERROR, "Failed to generate config propert file for Application: '" + appName + "'." + System.getProperty("line.separator"));
			//appPropFilePath = null;
		}
		return appPropFilePath;
	}
	
	public HashMap<String, String> configPropertyFileForApps(String fileFolder, List<String> apps, String workingFolder, StringBuffer result) {
		HashMap<String, String> propFilePathes = new HashMap<String, String>();
		for (String app : apps) {
			if (!app.startsWith("REST Services Gateway")) {//has problem if extract config properties for REST Service Gateway, need another way to check it.
				//propFilePathes.put(app, configPropertyFileForApp(fileFolder, app, workingFolder, result));
				result.append(checkAppContextRoot(app, configPropertyFileForApp(fileFolder, app, workingFolder, result), this.getData()));
			}
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
					}
					if (line.startsWith("mutables=")) {
						startRecord = false;
					}
					if (startRecord && line.startsWith("row")) {
						foundContextRoot = true;
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
						if (context.startsWith("/" + contextRoot + "/")) {
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
