package com.ibm.bpm.automation.ic.constants;

public enum Configurations {

	BPMPATH("bpmPath"), 
	DEUSERNAME("deUserName"), 
	DEUSERPWD("deUserPwd"), 
	CEUSERNAME("ceUserName"),
	CEUSERPWD("ceUserPwd"),
	DMGRPROF("dmgrProfile");
	
	private Configurations(String k) {
		key = k;
	}
	
	private String key;
	
	public String getKey() {
		return key;
	}
	
}
