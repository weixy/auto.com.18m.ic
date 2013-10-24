/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.tap;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.xerces.util.MessageFormatter;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.TestCase;
import com.ibm.bpm.automation.ic.TestCaseLoader;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.tap.adapter.AutomationService;
import com.ibm.bpm.automation.tap.adapter.IScenarioStarter;
import com.ibm.bpm.automation.tap.automationobjects.Environment;
import com.ibm.bpm.automation.tap.automationobjects.Machine;
import com.ibm.bpm.qa.automation.newobject.Cluster;
import com.ibm.bpm.qa.automation.newobject.Node;
import com.ibm.bpm.qa.automation.newobject.type.TopologyType;

public class TestRobot implements IScenarioStarter{

	private static final String CLASSNAME = TestRobot.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String ICAUTO_TESTCASE_PATH = "test";
	public static String ICAUTO_LOG_PATH = "logs";
	public static String ICAUTO_OUTPUT_PATH = "outputs";
	public static String ICAUTO_CONFIG_PATH = "config";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

	@Override
	public void start(AutomationService autoService) {
		
		LogUtil.init(System.getProperty("user.dir") + File.separator + ICAUTO_LOG_PATH);
		
		String executionInfo = MessageFormat.format("Release: {0}\tBuild: {1}\tTopology: {2}" + System.getProperty("line.separator")
				+ "Environment: {3}\tMachine: {4}" + System.getProperty("line.separator")
				+ "ExecutionSet: {5}\tPackage: {6}", 
				new Object[] {
					autoService.getCurrentRelease(),
					autoService.getCurrentBuildLevel(),
					autoService.getCurrentEnvironment().getTopology().getName(),
					autoService.getCurrentEnvironment().getEnvironmentName(),
					autoService.getCurrentMachine().getMachineIP(),
					autoService.getCurrentExecutionSet().getName(),
					autoService.getCurrentTestCase().getName()
				});
		
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
			
			String regScriptName = autoService.getCurrentTestScript().getName();
			
			if (null == regScriptName || "".equals(regScriptName)) {
				logger.log(LogLevel.WARNING, "The script(step) name registered in TAP is empty. Will execute any existing cases.");
			}
			
			//Construct Configuration with Environment info via autoSerivce.
			HashMap<String, Object> config = new HashMap<String, Object>();
			String bpmLogFolder = System.getProperty("user.dir") + File.separator + ICAUTO_LOG_PATH + File.separator + "bpm";
			config.put(Configurations.LOGFOLDER.getKey(), ICAUTO_LOG_PATH);
			config.put(Configurations.BPMLOGFDR.getKey(), bpmLogFolder);
			Environment curEnv = autoService.getCurrentEnvironment();
			config.put(Configurations.BPMLOGSAV.getKey(), ExecutionContext.getBPMServerLogPath(curEnv));
			config.put(Configurations.CEUSERNAME.getKey(), curEnv.getCellAdminUserName());
			config.put(Configurations.CEUSERPWD.getKey(), curEnv.getCellAdminUserPwd());
			config.put(Configurations.BPMPATH.getKey(), autoService.getCurrentMachine().getBpmHome());
			
			ExecutionContext.getExecutionContext().setAutomationService(autoService);
			
			config.put(Configurations.APPCLUSTER.getKey(), curEnv.getTopology().getAppCluster().getClusterName());
			Cluster[] clusters = curEnv.getTopology().getClusters();
			List<String> clusterNames = new ArrayList<String>();
			for(Cluster c : clusters) {
				clusterNames.add(c.getClusterName());
			}
			config.put(Configurations.CLUSTERS.getKey(), clusterNames);
			
			config.put(Configurations.DENAME.getKey(), curEnv.getTopology().getName());
			config.put(Configurations.DMGRPROF.getKey(), curEnv.getManageProfileName());
			config.put(Configurations.CELLNAME.getKey(), curEnv.getCellName());
			HashMap<String, String[]> nodes = new HashMap<String, String[]>();
			if (!curEnv.isStandalone()) {
				Node [] nds = curEnv.getTopology().getNodes();
				for (Node node : nds) {
					Machine machine = curEnv.getMachineByNode(node);
					nodes.put(node.getNodeName(), new String [] {machine.getMachineIP(), machine.getHostname()});
				}
			}
			else {
				Machine machine = curEnv.getMachines()[0];
				nodes.put(curEnv.getNodeName(), new String [] {machine.getMachineIP(), machine.getHostname()});
			}
			config.put(Configurations.NODES.getKey(), nodes);
			
			//execute cases
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
			String logUrl = autoService.uploadFile(System.getProperty("user.dir") + File.separator + ICAUTO_LOG_PATH + File.separator + "SystemOut_icauto.log");
			ExecutionContext.getExecutionContext().submitExecutionResult(ExecutionContext.ER_STATUS_FAILED, 0, 0, logUrl, "No cases has been loaded.");
		}
	}
	
	

}
