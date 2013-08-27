package com.ibm.bpm.automation.ic.operations;

import java.util.HashMap;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.constants.OperationCommand;
import com.ibm.bpm.automation.ic.utils.CommandUtil;
import com.ibm.bpm.automation.ic.utils.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class ServerOperation extends BaseOperation {

	private static final String CLASSNAME = ServerOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public ServerOperation() {
		if ("start".equals(this.getAction().toLowerCase())) {
			command = OperationCommand.STARTSERV.getCommand();
		} else if ("stop".equals(this.getAction().toLowerCase())) {
			command = OperationCommand.STOPSERV.getCommand();
		} else {
			command = null;
		}
	}
	
	@Override
	public void run(HashMap<String, String> config) throws AutoException {
		// TODO Auto-generated method stub
		logger.log(LogLevel.INFO, "Invoke operation '" + ServerOperation.class.getSimpleName() + "'");
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		return command;
	}

}
