package com.ibm.bpm.automation.ic.constants;

public enum OperationParameters {
	
	BPMCONFIG("BPMConfig"),
	BPMCONFIG_ACT_CREATE("create"),
	BPMCONFIG_ACT_UPDATE("update"),
	BPMCONFIG_TYP_DE("de"),
	BPMCONFIG_TYP_CONTXTRT("contextRootPrefix"),
	
	BPMCONFLOG("BPMConfigLog"),
	STARTSERV("startServer"),
	STOPSERV("stopServer"),
	
	WSADMIN("wsadmin"),
	
	CHKCONF("CheckConfig"),
	CHKCONF_TYP_CONTEXTROOT("CheckContextRoot"),
	CHKCONF_OPT_ALL("All"),
	
	BPMLOG("BPMLog"),
	//options of BPMLog
	BPMLOG_OPT_ALL("All"),
	BPMLOG_OPT_DMGRSERVER("BPMDmgrServerLog"),
	BPMLOG_OPT_DMGRFFDC("BPMDmgrFFDCLog"),
	BPMLOG_OPT_NODESERVER("BPMNodeServerLog"),
	BPMLOG_OPT_NODEAGENT("BPMNodeAgent"),
	BPMLOG_OPT_NODEFFDC("BPMNodeFFDCLog");
	
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
