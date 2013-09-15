/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.tap;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.tap.adapter.AutomationService;
import com.ibm.bpm.automation.tap.automationobjects.Environment;
import com.ibm.bpm.automation.tap.automationobjects.ExecutionRecord;
import com.ibm.bpm.automation.tap.automationobjects.ExecutionRecord.Status;
import com.ibm.bpm.automation.tap.automationobjects.Machine;
import com.ibm.bpm.automation.tap.automationobjects.Topology;
import com.ibm.bpm.qa.automation.newobject.Cluster;
import com.ibm.bpm.qa.automation.newobject.Node;
import com.ibm.bpm.qa.automation.newobject.Server;

public class ExecutionContext {
	
	private static final String CLASSNAME = ExecutionContext.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String COLLECTED_STANDALONE_SERVLOGPATH = "standaloneLogSavePath";
	public static String COLLECTED_STANDALONE_FFDCLOGPATH = "standaloneFFDCSavePath";
	public static String COLLECTED_DMGR_SERVLOGPATH = "dmgrLogSavePath";
	public static String COLLECTED_DMGR_FFDCLOGPATH = "dmgrFFDCSavePath";
	public static String COLLECTED_NODE_SERVLOGPATH = "nodeLogSavePath";
	public static String COLLECTED_NODE_FFDCLOGPATH = "nodeFFDCSavePath";
	public static String COLLECTED_NODE_AGENTLOGPATH = "nodeAgentLogSavePath";
	
	//FAILED, BLOCKED, NOTATTEMPTED, SUCCESSFUL, WORKING, STOPPED
	public static String ER_STATUS_FAILED = "FAILED";
	public static String ER_STATUS_BLOCKED = "BLOCKED";
	public static String ER_STATUS_NOTATTEMPTED = "NOTATTEMPTED";
	public static String ER_STATUS_SUCCESSFUL = "SUCCESSFUL";
	public static String ER_STATUS_WORKING = "WORKING";
	public static String ER_STATUS_STOPPED = "STOPPED";
	
	private static ExecutionContext context;
	private AutomationService automationService;
	private ExecutionContext() {}
		
	public AutomationService getAutomationService() {
		return automationService;
	}

	public void setAutomationService(AutomationService automationService) {
		this.automationService = automationService;
	}
	
	public void submitExecutionResult(String state, int successfulPoint, int failedPoint, String logUrl, String result) {
		
		Status status = getExecutionRecordStatus(state);
		if (null != status && null != automationService) {
			this.automationService.submitExecutionResult(status, successfulPoint, failedPoint, logUrl, result);
		}
		if (null == automationService) {
			logger.log(LogLevel.ERROR, "The automation service of ExecutionContext instance is not initialized!");
		}
	}
	
	public void submitExeuctionResultWithNewER(String testScriptName, int testScriptWeight, String state, int successfulPoint, Date startTime, Date endTime, String logUrl, String result) {
		Status status = getExecutionRecordStatus(state);
		if (null != status && null != automationService) {
			this.automationService.submitExecutionResultWithCreateNewTestScript(testScriptName, testScriptWeight, status, successfulPoint, startTime, endTime, logUrl, result);
		}
		if (null == automationService) {
			logger.log(LogLevel.ERROR, "The automation service of ExecutionContext instance is not initialized!");
		}
	}
	
	public Status getExecutionRecordStatus(String state) {
		if (ER_STATUS_FAILED.equals(state)) {
			return ExecutionRecord.Status.FAILED;
		} else if (ER_STATUS_BLOCKED.equals(state)) {
			return ExecutionRecord.Status.BLOCKED;
		} else if (ER_STATUS_NOTATTEMPTED.equals(state)) {
			return ExecutionRecord.Status.NOTATTEMPTED;
		} else if (ER_STATUS_SUCCESSFUL.equals(state)) {
			return ExecutionRecord.Status.SUCCESSFUL;
		} else if (ER_STATUS_WORKING.equals(state)) {
			return ExecutionRecord.Status.WORKING;
		} else if (ER_STATUS_STOPPED.equals(state)) {
			return ExecutionRecord.Status.STOPPED;
		} else {
			logger.log(LogLevel.ERROR, "No defined status for '" + state + "'!");
			return null;
		}
	}

	public static ExecutionContext getExecutionContext() {
		if (null == context) {
			synchronized(ExecutionContext.class) {
				context = new ExecutionContext();
			}
		}
		return context;
	}

	public static HashMap<String, Object> getBPMServerLogPath(Environment env) {
		HashMap<String, Object> pathes = new HashMap<String, Object>();
		
		if (env.isStandalone()) {
			//After 85, no STANDALONE supported.
			String stndaloneLogSavePath = "server1";
			String stndaloneFFDCSavePath = "ffdc";
			pathes.put(COLLECTED_STANDALONE_SERVLOGPATH, stndaloneLogSavePath);
			pathes.put(COLLECTED_STANDALONE_FFDCLOGPATH, stndaloneFFDCSavePath);
		}
		else {
			//dmgr, TAP uses fixed dmgr server name.
			String dmgrLogSavePath = "dmgr_logs" + File.separator + "dmgr";
			String dmgrFFDCSavePath = "dmgr_logs" + File.separator + "ffdc";
			pathes.put(COLLECTED_DMGR_SERVLOGPATH, dmgrLogSavePath);
			pathes.put(COLLECTED_DMGR_FFDCLOGPATH, dmgrFFDCSavePath);
			//nodes
			List<String> nodeLogSavePathes = new ArrayList<String>();
			List<String> nodeFFDCSavePathes = new ArrayList<String>();
			List<String> nodeAgentSavePathes = new ArrayList<String>();
			for (Node node : env.getTopology().getNodes()) {
				Machine machine = env.getMachineByID(node.getMachineID());
				String p = "custom_logs" + File.separator + machine.getMachineIP() + "_" + node.getProfileName() + File.separator;

				Topology tp = env.getTopology();
				for (Cluster cluster : tp.getClusters()) {
					for (Server server : cluster.getServersOnNode(node)) {
						nodeLogSavePathes.add( p + server.getName());
					}
				}
				
				nodeFFDCSavePathes.add(p + "ffdc");
				nodeAgentSavePathes.add(p + "nodeagent");
			}
			pathes.put(COLLECTED_NODE_SERVLOGPATH, nodeLogSavePathes);
			pathes.put(COLLECTED_NODE_FFDCLOGPATH, nodeFFDCSavePathes);
			pathes.put(COLLECTED_NODE_AGENTLOGPATH, nodeAgentSavePathes);
		}
		
		
		return pathes;
	}
	
	
	
}
