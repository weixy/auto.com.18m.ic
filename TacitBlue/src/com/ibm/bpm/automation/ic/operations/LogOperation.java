package com.ibm.bpm.automation.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationCommand;
import com.ibm.bpm.automation.ic.constants.RegularPatterns;
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
		Date startTime = new Date();
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
				File[] logs = logFolder.listFiles(new AutoFileFilter(RegularPatterns.REG_BPM_CONFIG_LOG));
				for (File file : logs) {
					if (null == confLog) {
						confLog = file;
					}
					else if (confLog.lastModified() < file.lastModified()) {
						confLog = file;
					}
				}
			}
			
			result.append(analyseLog(confLog, RegularPatterns.REG_BPM_CONFIG_ERR));
			System.out.println(result);
			
		} else if (OperationCommand.BPMLOG.getCommand().equals(this.getType())) { // Check BPM Server log
			
			String bpmLogFolder = (String)config.get(Configurations.BPMLOGFDR.getKey());			
			HashMap<String, Object> collectedPathes = (HashMap<String, Object>)config.get(Configurations.BPMLOGSAV.getKey());
			String option = this.getOption();
			
			if (null == option) {
				option = OperationCommand.BPMLOG_ALL.getOption();
			}
			if (!OperationCommand.isValid(option)) {
				//TODO submit execution result as failed.
				logger.log(LogLevel.ERROR, "The option '" + option + "' is invalid. Please check the definition in your test case.");
				result.append("The option '" + option + "' is invalid. Please check the definition in your test case.");
				result.append(System.getProperty("line.separator"));
				this.failedPoints++;
			} else {
				boolean execAll = (null == option || OperationCommand.BPMLOG_ALL.getOption().toLowerCase().equals(option.toLowerCase()));
				if (OperationCommand.BPMLOG_NODESERVER.getOption().equals(option) || execAll) {
					List<String> listServLogPath = (List<String>)collectedPathes.get(ExecutionContext.COLLECTED_NODE_SERVLOGPATH);
					for(Iterator<String> it = listServLogPath.iterator(); it.hasNext();) {
						result.append(analyseLogs(bpmLogFolder + File.separator + it.next(), RegularPatterns.REG_BPM_SERVER_ERR));
					}
				} 
				if (OperationCommand.BPMLOG_NODEAGENT.getOption().equals(option) || execAll) {
					List<String> listAgentLogPath = (List<String>)collectedPathes.get(ExecutionContext.COLLECTED_NODE_AGENTLOGPATH);
					for (Iterator<String> it = listAgentLogPath.iterator(); it.hasNext();) {
						result.append(analyseLogs(bpmLogFolder + File.separator + it.next(), RegularPatterns.REG_BPM_SERVER_ERR));
					}
				} 
				if (OperationCommand.BPMLOG_NODEFFDC.getOption().equals(option) || execAll) {
					//TODO How to verify the FFDC, is it necessary?
				}
				if (OperationCommand.BPMLOG_DMGRSERVER.getOption().equals(option) || execAll) {
					result.append(analyseLogs(bpmLogFolder + File.separator + (String)collectedPathes.get(ExecutionContext.COLLECTED_DMGR_SERVLOGPATH), RegularPatterns.REG_BPM_SERVER_ERR));
				}
				if (OperationCommand.BPMLOG_DMGRFFDC.getOption().equals(option) || execAll) {
					//TODO How to verify the FFDC, is it necessary?
				}
			}
			System.out.println(result);
		} else { //Wrong types?
			logger.log(LogLevel.ERROR, "Unrecognized analysis file type found: '" + this.getType() + "'.");
			result.append("Unrecognized analysis file type found: '" + this.getType() + "'." + System.getProperty("line.separator"));
			this.failedPoints++;
		}
		
		Date endTime = new Date();
		
		if (failedPoints != 0) {
			if (failedPoints < getPoints()) {
				failedPoints = getPoints();
			}
			logger.log(LogLevel.ERROR, "Step '" + getStep() + "' failed with points [" + failedPoints +"]!");
			if (null != ExecutionContext.getExecutionContext().getAutomationService()) {
				ExecutionContext.getExecutionContext().submitExeuctionResultWithNewER(
						getStep(), getPoints(), ExecutionContext.ER_STATUS_FAILED, successPoints, startTime, endTime, "", result.toString());
			}
		}
		else {
			if (successPoints == 0 || successPoints < getPoints()) {
				successPoints = getPoints();
			}
			logger.log(LogLevel.INFO, "Step '" + getStep() + "' Succeeded with points [" + successPoints +"]!");
			if (null != ExecutionContext.getExecutionContext().getAutomationService()) {
				ExecutionContext.getExecutionContext().submitExeuctionResultWithNewER(
						getStep(), getPoints(), ExecutionContext.ER_STATUS_SUCCESSFUL, successPoints, startTime, endTime, "", result.toString());
			}
		}
	}
	
	public String analyseLogs(String foldPath, String regMatch) {
		boolean failed = false;
		StringBuffer result = new StringBuffer();
		
		File logFolder = new File(foldPath);
		
		if(!logFolder.exists() || logFolder.isFile()) {
			failed = true;
			logger.log(LogLevel.ERROR, "Can't find the folder or find a file with duplicated name. Please check it. <" + foldPath + ">");
			result.append("Can't find the folder or find a file with duplicated name. Please check it. <" + foldPath + ">" + System.getProperty("line.separator"));
			this.failedPoints++;
		}
		else {
			result.append("Start to go through the server logs under '" + foldPath + "'.");
			result.append(System.getProperty("line.separator"));
			File logFiles[] = logFolder.listFiles(new AutoFileFilter(RegularPatterns.REG_BPM_SERVER_LOG));
			for (File file : logFiles) {
				result.append(analyseLog(file, regMatch));
			}
		}
		
		return result.toString();
	}
	
	public String analyseLog(File bpmLog, String regMatch) {
		boolean failed = false;
		StringBuffer result = new StringBuffer();
		result.append("Start to validate Log file '" + bpmLog.getName() + "' ..." + System.getProperty("line.separator"));
		result.append("-----------------------------------------------------");
		result.append(System.getProperty("line.separator"));
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
			result.append(System.getProperty("line.separator"));
			
		} catch (Exception e) {
			failed = true;
			result.append("Failed to validate Log file: " + e.getMessage());
			result.append(System.getProperty("line.separator"));
		}
		//cont points depends on 'failed'
		if (failed) {
			this.failedPoints++;
		}
		else {
			this.successPoints++;
		}
		
		return result.toString();
	}
}
