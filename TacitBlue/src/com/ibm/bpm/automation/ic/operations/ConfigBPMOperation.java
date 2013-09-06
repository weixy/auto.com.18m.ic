package com.ibm.bpm.automation.ic.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationParameters;
import com.ibm.bpm.automation.ic.constants.RegularPatterns;
import com.ibm.bpm.automation.ic.utils.BPMConfigUtil;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class ConfigBPMOperation extends BaseOperation {
	
	private static final String CLASSNAME = ConfigBPMOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	@Override
	public void run(HashMap<String, Object> config) {
		super.run(config);
		logger.log(LogLevel.INFO, "Invoke operation '" + ConfigBPMOperation.class.getSimpleName() + "'");
		
		String workingFolder = config.get(Configurations.BPMPATH.getKey()) + File.separator + "bin";
		
		StringBuffer result = new StringBuffer();
		List<String> cmds = new ArrayList<String>();
		String action = getAction();
		String type = getType();
		String data = getData();
		
		if (null != action) {
			
			cmds.add("-" + action);
			if (OperationParameters.BPMCONFIG_ACT_CREATE.getAction().equals(action.toLowerCase())) {
				cmds.add(type!=null?("-"+type):"");
				cmds.add(data);
			} else if (OperationParameters.BPMCONFIG_ACT_UPDATE.getAction().equals(action.toLowerCase())) {
				if (OperationParameters.BPMCONFIG_TYP_CONTXTRT.getType().equals(type)) {
					cmds.add("-contextRootPrefix");
					cmds.add(data);
					cmds.add("-de");
					cmds.add((String)config.get(Configurations.DENAME.getKey()));
					cmds.add("-profile");
					cmds.add((String)config.get(Configurations.DMGRPROF.getKey()));
				} else {
					logger.log(LogLevel.WARNING, "Unsupported BPMConfig type found, please check it: '" + type + "'.");
					result.append("Unsupported BPMConfig type found, please check it: '" + type + "'.");
				}
			} else {
				logger.log(LogLevel.WARNING, "Unsupported BPMConfig action found, please check it: '" + action + "'.");
				result.append("Unsupported BPMConfig action found, please check it: '" + action + "'.");
			}
			
			result.append(BPMConfigUtil.executeCommnd(cmds, workingFolder));
		}
		else {
			logger.log(LogLevel.ERROR, "The corresponding BPMConfig action is not defined in case.");
			result.append("The corresponding BPMConfig action is not defined in case.");
			failedPoints = getPoints();
		}
		
		//save log path
		Pattern logPattern = Pattern.compile(RegularPatterns.REG_BPM_CONFIG_LOG_FULLPATH);
		Matcher matcher = logPattern.matcher(result);
		if (matcher.find()) {
			config.put(Configurations.BPMCONFLOG.getKey(), matcher.group().trim());
		}
		else {
			logger.log(LogLevel.WARNING, "Failed to find BPMConfig log file's name in returned strings.");
		}
		
		//parse log to get its success or failure
		Pattern succPattern = Pattern.compile(RegularPatterns.REG_BPM_CONFIG_CMD_SUCC);
		Matcher succMatcher = succPattern.matcher(result);
		Pattern failPattern = Pattern.compile(RegularPatterns.REG_BPM_CONFIG_CMD_FAIL);
		Matcher failMatcher = failPattern.matcher(result);
		Pattern syntaxPattern = Pattern.compile(RegularPatterns.REG_BPM_CONFIG_CMD_USAGE);
		Matcher syntaxMatcher = syntaxPattern.matcher(result);
		if(succMatcher.find()) {
			//execution succeed
			logger.log(LogLevel.INFO, "Operation '" + ConfigBPMOperation.class.getSimpleName() + "' completed successfully, please check log for information");
			successPoints = this.getPoints();
		} else if (failMatcher.find()) {
			//execution failed
			logger.log(LogLevel.ERROR, "Operation '" + ConfigBPMOperation.class.getSimpleName() + "' failed, please check log for information.");
			//failedPoints = this.getPoints();
		} else if (syntaxMatcher.find()) {
			logger.log(LogLevel.ERROR, "Operation '" + ConfigBPMOperation.class.getSimpleName() + "' may have syntax error, please check log for information.");
			failedPoints = 1;
		} else {
			//TODO code problem?
			logger.log(LogLevel.WARNING, "Can't determine if the operation '" + this.getName() + "' succeeded or failed!");
			//failedPoints = this.getPoints();
		}
		
		
		File confLog = BPMConfigUtil.findBPMConfigLog(config);
		
		result.append(analyseLog(confLog, RegularPatterns.REG_BPM_CONFIG_ERR));
		System.out.println(result);
		
		submit(result.toString(), logger);
	}
}
