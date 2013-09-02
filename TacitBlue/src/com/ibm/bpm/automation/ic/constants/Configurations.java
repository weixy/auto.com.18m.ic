package com.ibm.bpm.automation.ic.constants;

public enum Configurations {

	BPMPATH("bpmPath"), 
	DEUSERNAME("deUserName"), 
	DEUSERPWD("deUserPwd"), 
	CEUSERNAME("ceUserName"),
	CEUSERPWD("ceUserPwd"),
	DMGRPROF("dmgrProfile"),
	NODEPROF("nodeProfile"),
	TOPTYPE("topologyType"),
	APPSEVNAME("appServerName"),
	LOGFOLDER("autoLogFolder"), //ic auto mation log folder
	BPMLOGFDR("bpmLogFolder"), //will also retrieve all BPM server logs under this folder
	BPMLOGSAV("bpmLogSavePathes"),
	BPMCONFLOG("bpmConfLog");
	
	private Configurations(String k) {
		key = k;
	}
	
	private String key;
	
	public String getKey() {
		return key;
	}
	
}
