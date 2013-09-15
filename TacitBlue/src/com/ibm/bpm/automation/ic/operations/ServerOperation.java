/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.operations;

import java.util.HashMap;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.OperationParameters;
import com.ibm.bpm.automation.ic.utils.CommandUtil;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class ServerOperation extends BaseOperation {

	private static final String CLASSNAME = ServerOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	@Override
	public void run(HashMap<String, Object> config) {
		// TODO Auto-generated method stub **************
		logger.log(LogLevel.INFO, "Invoke operation '" + ServerOperation.class.getSimpleName() + "'");
		if ("start".equals(this.getAction().toLowerCase())) {
			command = OperationParameters.STARTSERV.getCommand();
		} else if ("stop".equals(this.getAction().toLowerCase())) {
			command = OperationParameters.STOPSERV.getCommand();
		}
	}
}
