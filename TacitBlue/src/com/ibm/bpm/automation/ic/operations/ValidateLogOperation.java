package com.ibm.bpm.automation.ic.operations;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationCommand;
import com.ibm.bpm.automation.ic.utils.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class ValidateLogOperation extends BaseOperation {
	
	private static final String CLASSNAME = ValidateLogOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run(HashMap<String, String> config) throws AutoException {
		logger.log(LogLevel.INFO, "Invoke operation '" + ValidateLogOperation.class.getSimpleName() + "'");
		
		if(OperationCommand.BPMCONFLOG.getCommand().equals(this.getOption())) { // Check BPMConfig log
			if(config.containsKey(Configurations.BPMCONFLOG.getKey())) { //Just completed a BPMConfig
				String confLogPath = config.get(Configurations.BPMCONFLOG.getKey());
				System.out.println(confLogPath);
				File confLog = new File(confLogPath);
				if (!confLog.exists()) {
					//TODO submit execution failure **************
					throw new AutoException("The corresponding BPMConfig log file '" + confLogPath + "' is not existing!");
				}
				
				
			}
			else { // Have to parse the latest one.
				
			}
		} else if (OperationCommand.BPMSERVLOG.getCommand().equals(this.getOption())) { // Check BPM Server log
			//TODO *******************
		}
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
