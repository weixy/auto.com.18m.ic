package com.ibm.bpm.automation.ic.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationParameters;
import com.ibm.bpm.automation.ic.utils.CommandUtil;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.ic.utils.WSAdminUtil;

public class ManagerConfigOperation extends BaseOperation {
	
	private static final String CLASSNAME = ManagerConfigOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run(HashMap<String, Object> config) {
		// TODO Auto-generated method stub
		super.run(config);
		logger.log(LogLevel.INFO, "Invoke operation '" + ManagerConfigOperation.class.getSimpleName() + "'");
		
		String workingFolder = config.get(Configurations.BPMPATH.getKey()) + File.separator + "bin";
		String type = getType();
		/*List<String> cmds = new ArrayList<String>();
		cmds.add(workingFolder + File.separator + CommandUtil.getCommandByOS(command));
		cmds.add("-lang jython");
		if (OperationParameters.WSADMIN_ACT_GETCONFPROP.equals(getType())) {
			cmds.add("-conntype NONE");
		} else if (null == getType()) {
			String userName = (String)config.get(Configurations.CEUSERNAME);
			String passWord = (String)config.get(Configurations.CEUSERPWD);
			cmds.add("-username");
			cmds.add(userName);
			cmds.add("-password");
			cmds.add(passWord);
		}*/
		
		
		
		String result = WSAdminUtil.executeCommnd(type, workingFolder);
		
	}

}
