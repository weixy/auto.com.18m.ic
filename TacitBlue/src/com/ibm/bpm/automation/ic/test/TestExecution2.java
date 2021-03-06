/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.test;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.TestCase;
import com.ibm.bpm.automation.ic.TestCaseLoader;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.tap.ExecutionContext;
import com.ibm.bpm.automation.ic.tap.TestRobot;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.ic.utils.CaseXMLHandler;
import com.ibm.bpm.automation.tap.adapter.AutomationService;
import com.ibm.bpm.qa.automation.newobject.type.TopologyType;

public class TestExecution2 {

	private static final String CLASSNAME = TestRobot.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String ICAUTO_TESTCASE_PATH = "test";
	public static String ICAUTO_LOG_PATH = "logs";
	public static String ICAUTO_OUTPUT_PATH = "outputs";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//TestRobot icAutoRobot = new TestRobot();
		//icAutoRobot.start(null);
		
		
		LogUtil.init(System.getProperty("user.dir") + File.separator + ICAUTO_LOG_PATH);
		
		String executionInfo = MessageFormat.format("Release: {0}\tBuild: {1}\tTopology: {2}" + System.getProperty("line.separator")
				+ "Environment: {3}\tMachine: {4}" + System.getProperty("line.separator")
				+ "ExecutionSet: {5}\tPackage: {6}", 
				
				new Object[] {"8550", "20130829", "SingleCluster", "STDSingleClusterDE", "9.110.191.189", "ConfigureSTD_SingleCLusterDE", "MyICAutoPackage"});
		
		logger.log(LogLevel.HEADER, executionInfo);
		
		List<TestCase> caseList = null;
		
		try {
			caseList = TestCaseLoader.loadTestCases(System.getProperty("user.dir") 
					+ File.separator 
					+ ICAUTO_TESTCASE_PATH);
		} catch (AutoException e) {
			logger.log(LogLevel.ERROR, "Failed to load test cases.", e);
		}
		
		if (null != caseList) {
			boolean runAllCases = true;
			
			//String regScriptName = autoService.getCurrentTestScript().getName();
			//replace this with above line. ********
			String regScriptName = "";
			
			if (null == regScriptName || "".equals(regScriptName)) {
				logger.log(LogLevel.WARNING, "The script(step) name registered in TAP is empty. Will execute any existing cases.");
			}
			
			//Construct Configuration with Environment info via autoSerivce.
			HashMap<String, Object> config = new HashMap<String, Object>();
			String bpmLogFolder = System.getProperty("user.dir") + File.separator + ICAUTO_LOG_PATH + File.separator + "bm";
			config.put(Configurations.LOGFOLDER.getKey(), ICAUTO_LOG_PATH);
			config.put(Configurations.BPMLOGFDR.getKey(), bpmLogFolder);
			//Environment curEnv = autoService.getCurrentEnvironment();
			//config.put(Configurations.BPMLOGSAV.getKey(), ExecutionContext.getBPMServerLogPath(curEnv));
			//autoService.retriveAllLogs(curEnv, bpmLogFolder);
			//ExecutionContext.getExecutionContext().setAutomationService(autoService);
			
			//Stub for config hash map
			config.put(Configurations.BPMLOGFDR.getKey(), "e:\\tmp\\outputs\\logs\\b2m");
			HashMap<String, Object> m = new HashMap<String, Object>();
			List<String> nodeLogSavePathes = new ArrayList<String>();
			nodeLogSavePathes.add("custom_logs\\9.110.191.189_Custom01\\SingleClusterMember2");
			nodeLogSavePathes.add("custom_logs\\9.110.191.188_Custom02\\SingleClusterMember1");
			List<String> nodeFFDCSavePathes = new ArrayList<String>();
			nodeFFDCSavePathes.add("custom_logs\\9.110.191.189_Custom01\\ffdc");
			nodeFFDCSavePathes.add("custom_logs\\9.110.191.188_Custom02\\ffdc");
			List<String> nodeAgentSavePathes = new ArrayList<String>();
			nodeAgentSavePathes.add("custom_logs\\9.110.191.189_Custom01\\nodeagent");
			nodeAgentSavePathes.add("custom_logs\\9.110.191.188_Custom02\\nodeagent");			
			m.put(ExecutionContext.COLLECTED_DMGR_SERVLOGPATH, "dmgr_logs\\dmgr");
			m.put(ExecutionContext.COLLECTED_DMGR_FFDCLOGPATH, "dmgr_logs\\ffdc");
			m.put(ExecutionContext.COLLECTED_NODE_AGENTLOGPATH, nodeAgentSavePathes);
			m.put(ExecutionContext.COLLECTED_NODE_FFDCLOGPATH, nodeFFDCSavePathes);
			m.put(ExecutionContext.COLLECTED_NODE_SERVLOGPATH, nodeLogSavePathes);			
			config.put(Configurations.BPMLOGSAV.getKey(), m);
			
			config.put(Configurations.APPCLUSTER.getKey(), "SingleCluster");
			config.put(Configurations.BPMPATH.getKey(), "C:\\ibm\\bpm");
			config.put(Configurations.CEUSERNAME.getKey(), "admin");
			config.put(Configurations.CEUSERPWD.getKey(), "admin");
			config.put(Configurations.TOPTYPE.getKey(), TopologyType.SingleCluster.toString());
			config.put(Configurations.DENAME.getKey(), "deicauto");
			config.put(Configurations.DMGRPROF.getKey(), "Dmgr01");
			config.put(Configurations.CELLNAME.getKey(), "BXV7V691Cell01");
			HashMap<String, String[]> nodes = new HashMap<String, String[]>();
			nodes.put("BXV7V691Node01", new String[] {"127.0.0.1", "localhost"});
			config.put(Configurations.NODES.getKey(), nodes);
			//config.put(Configurations.APPSEVNAME.getKey(), "server1");
			
			int caseIndex = 0;
			for (int i=0; i<caseList.size(); i++) {
				if (caseList.get(i).getTitle().equals(regScriptName)) {
					runAllCases = false;
					caseIndex = i;
				}
			}
			if (runAllCases) {
				for (Iterator<TestCase> it = caseList.iterator(); it.hasNext();) {
					it.next().execute(config);
				}
			}
			else {
				TestCase testCase = caseList.get(caseIndex);
				testCase.execute(config);
			}
		}
		else {
			logger.log(LogLevel.WARNING, "No cases has been loaded.");
			//Submit the fail execution result
		}
		
	}

}
