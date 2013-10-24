/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.constants;

public enum Configurations {

	APPCLUSTER("appCluster"),
	APPSEVNAME("appServerName"),
	BPMCONFLOG("bpmConfLog"),
	BPMLOGFDR("bpmLogFolder"), //will also retrieve all BPM server logs under this folder
	BPMLOGSAV("bpmLogSavePathes"),
	BPMPATH("bpmPath"),
	CELLNAME("cellName"),
	CEUSERNAME("cellUserName"),
	CEUSERPWD("cellUserPwd"),
	CLUSTERS("clusters"),
	CONTEXTROOT("contextRoot"),
	DENAME("deName"),
	DEUSERNAME("deUserName"), 
	DEUSERPWD("deUserPwd"), 
	DMGRPROF("dmgrProfile"),
	LOGFOLDER("autoLogFolder"), //ic auto mation log folder
	NODEPROF("nodeProfile"),
	NODES("nodes"),
	TOPTYPE("topologyType");
	
	private Configurations(String k) {
		key = k;
	}
	
	private String key;
	
	public String getKey() {
		return key;
	}
	
}
