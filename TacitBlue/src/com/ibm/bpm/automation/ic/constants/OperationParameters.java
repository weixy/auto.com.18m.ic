/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.constants;

public enum OperationParameters {
	
	BPMCONFIG("BPMConfig"),
	BPMCONFIG_ACT_CREATE("create"),
	BPMCONFIG_ACT_UPDATE("update"),
	BPMCONFIG_TYP_DE("de"),
	BPMCONFIG_TYP_CONTXTRT("contextRootPrefix"),
	
	WSADMIN("wsadmin"),
	
	STARTSERV("startServer"),
	STOPSERV("stopServer"),
	
	CHECKLOG("CheckLog"),
	CHECKLOG_TYP_BPMCONFLOG("BPMConfigLog"),
	CHECKLOG_TYP_BPMLOG("BPMLog"),
	
	//options of BPMLog
	BPMLOG_OPT_ALL("All"),
	BPMLOG_OPT_DMGRSERVER("BPMDmgrServerLog"),
	BPMLOG_OPT_DMGRFFDC("BPMDmgrFFDCLog"),
	BPMLOG_OPT_NODESERVER("BPMNodeServerLog"),
	BPMLOG_OPT_NODEAGENT("BPMNodeAgent"),
	BPMLOG_OPT_NODEFFDC("BPMNodeFFDCLog"),
	
	MGRCONF("ManageConfig"),
	MGRCONF_ACT_VALID("valid"),
	MGRCONF_TYPE_CTXROOT("ContextRoot"),
	
	CHECKGUI("CheckGUI"),
	CHECKGUI_TYP_PROCCENTER("ProcessCenter"),
	CHECKGUI_TYP_PROCADMIN("ProcessAdmin"),
	CHECKGUI_TYP_PROCPORTAL("ProcessPortal"),
	CHECKGUI_TYP_WASADMIN("WASAdmin");
	
	private OperationParameters(String osCmd) {
		key = osCmd;
	}
	
	private String key;
	
	public String getCommand() {
		return key;
	}
	
	public String getOption() {
		return key;
	}
	
	public String getAction() {
		return key;
	}
	
	public String getType() {
		return key;
	}
	
	public static boolean isValid(String cmd) {
		boolean isValid = false;
		for (OperationParameters oc : OperationParameters.values()) {
			if (oc.getCommand().equals(cmd)) {
				isValid = true;
				break;
			}
		}
		
		return isValid;
	}

}
