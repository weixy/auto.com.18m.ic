package com.ibm.bpm.automation.ic.constants;

public class RegularPatterns {

	public static String REG_BPM_CONFIG_LOG_FULLPATH = "\\s[^\\s]+BPMConfig_[0-9]{8}\\-[0-9]{6}.log";
	public static String REG_BPM_CONFIG_LOG_FILEFILTER = "BPMConfig_[0-9]{8}\\-[0-9]{6}.log";
	public static String REG_BPM_CONFIG_ERR = "CWMCB[0-9]{4}E:|Exception";
	public static String REG_BPM_SERVER_LOG = "System(Err|Out)(_*\\d{2}\\.\\d{2}\\.\\d{2})*\\.log$";
	public static String REG_BPM_SERVER_ERR = "\\s+E\\s+(\\w{5}\\d{4}E)";
	public static String REG_BPM_CONFIG_CMD_SUCC = "completed successfully";
	public static String REG_BPM_CONFIG_CMD_FAIL = "failed";
	public static String REG_BPM_CONFIG_CMD_USAGE = "Usage:\\sBPMConfig";
}
