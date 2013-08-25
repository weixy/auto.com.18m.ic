package com.ibm.bpm.automation.ic.operations;

import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.utils.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class ServerOperation extends BaseOperation {

	private static final String CLASSNAME = ServerOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	@Override
	public void run() throws AutoException {
		// TODO Auto-generated method stub
		logger.log(LogLevel.INFO, "Invoke operation '" + ServerOperation.class.getSimpleName() + "'");
	}

}
