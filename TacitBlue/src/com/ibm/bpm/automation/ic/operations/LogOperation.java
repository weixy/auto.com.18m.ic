package com.ibm.bpm.automation.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationCommand;
import com.ibm.bpm.automation.ic.tap.ExecutionContext;
import com.ibm.bpm.automation.ic.utils.AutoFileFilter;
import com.ibm.bpm.automation.ic.utils.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.tap.adapter.AutomationService;

public class LogOperation extends BaseOperation {
	
	private static final String CLASSNAME = LogOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run(HashMap<String, Object> config) throws AutoException {
		logger.log(LogLevel.INFO, "Invoke operation '" + LogOperation.class.getSimpleName() + "'");
		
		File confLog = null;
		String confLogPath = null;
		StringBuffer result = new StringBuffer();
		
		if(OperationCommand.BPMCONFLOG.getCommand().equals(this.getType())) { // Check BPMConfig log
			if(config.containsKey(Configurations.BPMCONFLOG.getKey())) { //Just completed a BPMConfig
				confLogPath = (String)config.get(Configurations.BPMCONFLOG.getKey());
				confLog = new File(confLogPath);
			} 
			else { // Have to parse the latest one.
				String logFolderPath = config.get(Configurations.BPMPATH.getKey()) + File.separator + "logs" + File.separator + "config";
				File logFolder = new File(logFolderPath);
				File[] logs = logFolder.listFiles(new AutoFileFilter("BPMConfig_[0-9]{8}\\-[0-9]{6}.log"));
				for (File file : logs) {
					if (null == confLog) {
						confLog = file;
					}
					else if (confLog.lastModified() < file.lastModified()) {
						confLog = file;
					}
				}
			}
			
			result.append(analyseLog(confLog, "CWMCB[0-9]{4}E:|Exception"));
			System.out.println(result);
			
		} else if (OperationCommand.BPMLOG.getCommand().equals(this.getOption())) { // Check BPM Server log
			
			String bpmLogFolder = (String)config.get(Configurations.BPMLOGFDR.getKey());			
			HashMap<String, Object> collectedPathes = (HashMap<String, Object>)config.get(Configurations.BPMLOGSAV.getKey());
			String option = this.getOption();
			boolean isValid = OperationCommand.isValid(option);
			
			if (!isValid) {
				//TODO submit execution result as failed.
				logger.log(LogLevel.ERROR, "The option '" + option + "' is invalid. Please check the definition in your test case.");
			} else {
				boolean execAll = (null == option || OperationCommand.BPMLOG_ALL.getOption().equals(option));
				if (OperationCommand.BPMLOG_NODESERVER.getOption().equals(option) || execAll) {
					List<String> listServLogPath = (List<String>)collectedPathes.get(ExecutionContext.COLLECTED_NODE_SERVLOGPATH);
					for(Iterator<String> it = listServLogPath.iterator(); it.hasNext();) {
						//result.append(analyseLog(bpmLogFolder + File.separator + it.toString() + File.separator + "SystemOut.log", ""));
					}
				} 
				if (OperationCommand.BPMLOG_NODEAGENT.getOption().equals(option) || execAll) {
					
				} 
				if (OperationCommand.BPMLOG_NODEFFDC.getOption().equals(option) || execAll) {
					
				}
				if (OperationCommand.BPMLOG_DMGRSERVER.getOption().equals(option) || execAll) {
					
				}
				if (OperationCommand.BPMLOG_DMGRFFDC.getOption().equals(option) || execAll) {
					
				}
			}
			
		}
	}
	
	public String analyseLog(File bpmLog, String regMatch) {
		boolean failed = false;
		StringBuffer result = new StringBuffer();
		result.append("Start to validate Log file '" + bpmLog.getName() + "' ..." + System.getProperty("line.separator"));
		try {
			BufferedReader bufReader = new BufferedReader(new FileReader(bpmLog));
			String line = null;
			int lineIndex = 1;
			int foundNum = 0;
			while(null != (line = bufReader.readLine())) {
				lineIndex ++;
				Pattern ercodePattern = Pattern.compile(regMatch);
				Matcher ercodeMatcher = ercodePattern.matcher(line);
				if (ercodeMatcher.find()) {
					result.append("[Line: " + lineIndex + "] ");
					result.append(line);
					result.append(System.getProperty("line.separator"));
					foundNum ++;
					failed = true;
				}
			}
			bufReader.close();
			result.append(System.getProperty("line.separator"));
			result.append("Total found " + foundNum + " errors, please check log file '" + bpmLog.getName() + "' for information.");
			
			
		} catch (Exception e) {
			failed = true;
			result.append("Failed to validate Log file: " + e.getMessage());
			result.append(System.getProperty("line.separator"));
		}
		//TODO submit exeuction result depends on 'failed'
		if (failed) {
			//TODO submit execution result as failed.
		}
		else {
			//TODO submit execution result as successful.
		}
		
		return result.toString();
	}
}
