/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.constants;

public enum Configurations {

	BPMPATH("bpmPath"),
	DENAME("deName"),
	DEUSERNAME("deUserName"), 
	DEUSERPWD("deUserPwd"), 
	CEUSERNAME("cellUserName"),
	CEUSERPWD("cellUserPwd"),
	DMGRPROF("dmgrProfile"),
	CELLNAME("cellName"),
	NODEPROF("nodeProfile"),
	NODENAMES("nodeNames"),
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
