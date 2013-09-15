/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationParameters;
import com.ibm.bpm.automation.ic.constants.RegularPatterns;

public class BPMConfigUtil extends CommandUtil {
	
	public static String executeCommnd(List<String> cmds, String workingFolder) {
		String result = null;
		String osCommand = OperationParameters.BPMCONFIG.getCommand();
		cmds.add(0, workingFolder + File.separator + getCommandByOS(osCommand));
		
		result = CommandUtil.executeCommnd(cmds, workingFolder);
		
		return result;
	}
	
	public static File findBPMConfigLog(HashMap<String, Object> config) {
		File confLog = null;
		String confLogPath = null;
		
		if(config.containsKey(Configurations.BPMCONFLOG.getKey())) { //Just completed a BPMConfig
			confLogPath = (String)config.get(Configurations.BPMCONFLOG.getKey());
			confLog = new File(confLogPath);
		} 
		else { // Have to parse the latest one.
			String logFolderPath = config.get(Configurations.BPMPATH.getKey()) + File.separator + "logs" + File.separator + "config";
			File logFolder = new File(logFolderPath);
			File[] logs = logFolder.listFiles(new AutoFileFilter(RegularPatterns.REG_BPM_CONFIG_LOG_FILEFILTER));
			for (File file : logs) {
				if (null == confLog) {
					confLog = file;
				}
				else if (confLog.lastModified() < file.lastModified()) {
					confLog = file;
				}
			}
		}
		return confLog;
	}
}
