package com.ibm.bpm.automation.ic.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationCommand;
import com.ibm.bpm.automation.ic.constants.OperationSystem;
import com.ibm.bpm.automation.ic.tap.ExecutionContext;
import com.ibm.bpm.automation.ic.utils.CommandUtil;
import com.ibm.bpm.automation.ic.utils.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class BPMConfigOperation extends BaseOperation {
	
	private static final String CLASSNAME = BPMConfigOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	public BPMConfigOperation() {
		command = OperationCommand.BPMCONFIG.getCommand();
	}
	
	@Override
	public void run(HashMap<String, Object> config) throws AutoException {
		logger.log(LogLevel.INFO, "Invoke operation '" + BPMConfigOperation.class.getSimpleName() + "'");
		Date startTime = new Date();
		//prepare for execute the command in test environment
		String osName = System.getProperty("os.name").toLowerCase();
		String workingFolder = config.get(Configurations.BPMPATH.getKey()) + File.separator + "bin";
		
		/*String parameters = ((this.getAction()!=null) ? this.getAction() + " " : "") + 
				((this.getType()!=null) ? this.getType() + " " : "") + 
				((this.getPropFile()!=null) ? this.getPropFile() : "");*/
		
		List<String> cmds = new ArrayList<String>();
		cmds.add(workingFolder + File.separator + CommandUtil.getCommandByOS(command));
		if (null != getAction()) {
			cmds.add(getAction());
		}
		if (null != getType()) {
			cmds.add(getType());
		}
		if (null != getPropFile()) {
			cmds.add(getPropFile());
		}
		//execute operation
		String result = CommandUtil.executeCommnd(cmds, workingFolder);
		//save log path
		Pattern logPattern = Pattern.compile("\\s[^\\s]+BPMConfig_[0-9]{8}\\-[0-9]{6}.log");
		Matcher matcher = logPattern.matcher(result);
		if (matcher.find()) {
			config.put(Configurations.BPMCONFLOG.getKey(), matcher.group().trim());
		}
		else {
			logger.log(LogLevel.WARNING, "Failed to find BPMConfig log file's name in returned strings");
		}
		
		//parse result to get its success or failure
		Pattern succPattern = Pattern.compile("BPMConfig\\scompleted\\ssuccessfully");
		Matcher succMatcher = succPattern.matcher(result);
		Pattern failPattern = Pattern.compile("BPMConfig\\sfailed, check the log file for information");
		Matcher failMatcher = failPattern.matcher(result);
		if(succMatcher.find()) {
			//execution succeed
			logger.log(LogLevel.INFO, "Operation '" + BPMConfigOperation.class.getSimpleName() + "' completed successfully, please check log for information");
			this.successPoints = this.getPoints();
		} else if (failMatcher.find()) {
			//execution failed
			logger.log(LogLevel.ERROR, "Operation '" + BPMConfigOperation.class.getSimpleName() + "' failed, please check log for information.");
			this.failedPoints = this.getPoints();
		} else {
			//TODO code problem?
			logger.log(LogLevel.WARNING, "Can't determine if the operation '" + this.getName() + "' succeeded or failed!");
		}
		
		Date endTime = new Date();
		
		if (failedPoints != 0) {
			logger.log(LogLevel.ERROR, "Step '" + getStep() + "' failed with points [" + failedPoints +"]!");
			if (null != ExecutionContext.getExecutionContext().getAutomationService()) {
				ExecutionContext.getExecutionContext().submitExeuctionResultWithNewER(
						getStep(), getPoints(), ExecutionContext.ER_STATUS_FAILED, successPoints, startTime, endTime, "", result.toString());
			}
		}
		else {
			if (successPoints == 0) {
				successPoints = getPoints();
			}
			logger.log(LogLevel.INFO, "Step '" + getStep() + "' Succeeded with points [" + successPoints +"]!");
			if (null != ExecutionContext.getExecutionContext().getAutomationService()) {
				ExecutionContext.getExecutionContext().submitExeuctionResultWithNewER(
						getStep(), getPoints(), ExecutionContext.ER_STATUS_SUCCESSFUL, successPoints, startTime, endTime, "", result.toString());
			}
		}		
	}	
}
