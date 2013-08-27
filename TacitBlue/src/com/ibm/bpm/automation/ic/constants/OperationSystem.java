package com.ibm.bpm.automation.ic.constants;

public enum OperationSystem {
	
	WINDOWS("win", ".bat", "\\"),
	LINUX("linux", ".sh", "/"),
	AIX("aix", ".sh", "/"),
	SOLARIS("solaris", ".sh", "/");
	
	private OperationSystem(String type, String cmdSuffix, String seperator) {
		osType = type;
		osCmdSuffix = cmdSuffix;
		fileSeperator = seperator;
	}
	
	private String osType;
	private String osCmdSuffix;
	private String fileSeperator;
	
	public String getOsType() {
		return osType;
	}
	public String getOsCmdSuffix() {
		return osCmdSuffix;
	}
	public String getFileSeperator() {
		return fileSeperator;
	}
}
