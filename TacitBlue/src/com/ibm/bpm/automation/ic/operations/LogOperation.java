package com.ibm.bpm.automation.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationCommand;
import com.ibm.bpm.automation.ic.utils.AutoFileFilter;
import com.ibm.bpm.automation.ic.utils.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class LogOperation extends BaseOperation {
	
	private static final String CLASSNAME = LogOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run(HashMap<String, String> config) throws AutoException {
		logger.log(LogLevel.INFO, "Invoke operation '" + LogOperation.class.getSimpleName() + "'");
		
		File confLog = null;
		String confLogPath = null;
		
		if(OperationCommand.BPMCONFLOG.getCommand().equals(this.getOption())) { // Check BPMConfig log
			if(config.containsKey(Configurations.BPMCONFLOG.getKey())) { //Just completed a BPMConfig
				confLogPath = config.get(Configurations.BPMCONFLOG.getKey());
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
			try {
				BufferedReader bufReader = new BufferedReader(new FileReader(confLog));
				String line = null;
				int lineIndex = 1;
				int foundNum = 0;
				StringBuffer result = new StringBuffer();
				result.append("Start to parse BPMConfig log file '" + confLog.getName() + "' ..." + System.getProperty("line.separator"));
				while(null != (line = bufReader.readLine())) {
					lineIndex ++;
					Pattern ercodePattern = Pattern.compile("CWMCB[0-9]{4}E:");
					Matcher ercodeMatcher = ercodePattern.matcher(line);
					if (ercodeMatcher.find()) {
						result.append("[Line: " + lineIndex + "] ");
						result.append(line);
						result.append(System.getProperty("line.separator"));
						foundNum ++;
					}
				}
				result.append(System.getProperty("line.separator"));
				result.append("Total found " + foundNum + " errors, please check log file '" + confLog.getName() + "' for information.");
				
				//System.out.println(confLog.getName());
				//System.out.println(result);
				//TODO submit execution success **************
				
			} catch (FileNotFoundException e) {
				//TODO submit execution failure **************
				throw new AutoException("The corresponding BPMConfig log file '" + confLogPath + "' is not existing!", e);
			} catch (IOException e) {
				//TODO submit execution failure **************
				throw new AutoException("Can't read the BPMConfig log file '" + confLogPath + "'!", e);
			}
		} else if (OperationCommand.BPMSERVLOG.getCommand().equals(this.getOption())) { // Check BPM Server log
			//TODO *******************
			System.out.println(config.get(Configurations.TOPTYPE.getKey()));
			
		}
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
