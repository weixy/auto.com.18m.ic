/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.constants.OperationParameters;

public class WSAdminUtil extends CommandUtil{
	
	private static final String CLASSNAME = WSAdminUtil.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String CONF_PROPFILE_SUFFIX = ".prop";
	public static String CONF_PROP_CONTEXTROOT = "CtxRootForWebMod";
	public static String CMD_PARAMETER_PROPFILE = "%PROPFILE%";
	public static String CMD_PARAMETER_APPNAME = "%APPNAME%";
	
	public static String executeCommnd(List<String> cmds, String workingFolder) {
		String result = null;
		String osCommand = OperationParameters.WSADMIN.getCommand();
		cmds.add(0, workingFolder + File.separator + getCommandByOS(osCommand));
		
		result = CommandUtil.executeCommnd(cmds, workingFolder);
		
		return result;
	}
	
	public static List<String> getCmdsForInitWSAdmin() {
		List<String> cmds = new ArrayList<String>();
		cmds.add("-lang");
		cmds.add("jython");
		cmds.add("-conntype");
		cmds.add("NONE");
		return cmds;
	}
	
	public static List<String> getCmdsForAppList() {
		List<String> cmds = getCmdsForInitWSAdmin();
		cmds.add("-c");
		cmds.add("print AdminApp.list()");
		return cmds;
	}
	
	public static List<String> getCmdsForExtractConfigProperties() {
		List<String> cmds = getCmdsForInitWSAdmin();
		cmds.add("-c");
		cmds.add("AdminTask.extractConfigProperties('[-propertiesFileName " + CMD_PARAMETER_PROPFILE + " -configData Deployment=" + CMD_PARAMETER_APPNAME + "]')");
		return cmds;
	}

}
