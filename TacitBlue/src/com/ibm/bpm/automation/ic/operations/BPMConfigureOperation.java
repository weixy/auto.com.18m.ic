package com.ibm.bpm.automation.ic.operations;

import java.util.logging.Logger;
import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.utils.ICAutoLogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class BPMConfigureOperation extends BaseOperation {
	
	private static final String CLASSNAME = BPMConfigureOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run() throws AutoException {
		// TODO Auto-generated method stub
		logger.log(ICAutoLogLevel.ASSERT, "Executed!");
		
	}

	
	
}
