package com.ibm.bpm.automation.ic.constants;

public enum OperationCommand {
	
	BPMCONFIG("BPMConfig"),
	BPMCONFLOG("BPMConfigLog"),
	BPMSERVLOG("BPMServerLog"),
	STARTSERV("startServer"),
	STOPSERV("stopServer");
	
	private OperationCommand(String osCmd) {
		osCommand = osCmd;
	}
	
	private String osCommand;
	
	public String getCommand() {
		return osCommand;
	}

}
