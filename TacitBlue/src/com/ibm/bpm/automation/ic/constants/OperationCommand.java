package com.ibm.bpm.automation.ic.constants;

public enum OperationCommand {
	
	BPMCONFIG("BPMConfig"),
	BPMCONFLOG("BPMConfigLog"),
	STARTSERV("startServer"),
	STOPSERV("stopServer"),
	BPMLOG("BPMLog"),
	//options of BPMLog
	BPMLOG_ALL("All"),
	BPMLOG_DMGRSERVER("BPMDmgrServerLog"),
	BPMLOG_DMGRFFDC("BPMDmgrFFDCLog"),
	BPMLOG_NODESERVER("BPMNodeServerLog"),
	BPMLOG_NODEAGENT("BPMNodeAgent"),
	BPMLOG_NODEFFDC("BPMNodeFFDCLog");
	
	private OperationCommand(String osCmd) {
		key = osCmd;
	}
	
	private String key;
	
	public String getCommand() {
		return key;
	}
	
	public String getOption() {
		return key;
	}
	
	public static boolean isValid(String cmd) {
		boolean isValid = false;
		for (OperationCommand oc : OperationCommand.values()) {
			if (oc.getCommand().equals(cmd)) {
				isValid = true;
				break;
			}
		}
		
		return isValid;
	}

}
