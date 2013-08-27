package com.ibm.bpm.automation.ic.operations;

import java.util.HashMap;
import java.util.logging.Logger;
import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.utils.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class ValidateLogOperation extends BaseOperation {
	
	private static final String CLASSNAME = ValidateLogOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run(HashMap<String, String> config) throws AutoException {
		logger.log(LogLevel.INFO, "Invoke operation '" + ValidateLogOperation.class.getSimpleName() + "'");		
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
