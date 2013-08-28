package com.ibm.bpm.automation.ic.operations;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationCommand;
import com.ibm.bpm.automation.ic.constants.OperationSystem;
import com.ibm.bpm.automation.ic.utils.CommandUtil;
import com.ibm.bpm.automation.ic.utils.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class BPMConfigureOperation extends BaseOperation {
	
	private static final String CLASSNAME = BPMConfigureOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	public BPMConfigureOperation() {
		command = OperationCommand.BPMCONFIG.getCommand();
	}
	
	@Override
	public void run(HashMap<String, String> config) throws AutoException {
		logger.log(LogLevel.INFO, "Invoke operation '" + BPMConfigureOperation.class.getSimpleName() + "'");
		
		//prepare for execute the command in test environment
		String osName = System.getProperty("os.name").toLowerCase();
		String workingFolder = config.get(Configurations.BPMPATH.getKey()) + File.separator + "bin";
		
		String parameters = ((this.getAction()!=null) ? this.getAction() + " " : "") + 
				((this.getType()!=null) ? this.getType() + " " : "") + 
				((this.getPropFile()!=null) ? this.getPropFile() : "");
		
		//execute operation
		String result = CommandUtil.executeCommnd(command, workingFolder, parameters);
		
		//save log path
		Pattern pattern = Pattern.compile("\\s[^\\s]+BPMConfig_[0-9]{8}\\-[0-9]{6}.log");
		Matcher matcher = pattern.matcher(result);
		if (matcher.find()) {
			config.put(Configurations.BPMCONFLOG.getKey(), matcher.group().trim());
		}
		else {
			logger.log(LogLevel.WARNING, "Failed to find BPMConfig log file's name in returned strings");
		}
		
		//TODO parse result to get its success or failure
		
		//TODO submitExecute
		
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		return command;
	}

	
	
	
}
