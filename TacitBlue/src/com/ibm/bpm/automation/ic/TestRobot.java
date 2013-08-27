package com.ibm.bpm.automation.ic;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.xerces.util.MessageFormatter;

import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.utils.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.tap.adapter.AutomationService;
import com.ibm.bpm.automation.tap.adapter.IScenarioStarter;
import com.ibm.bpm.automation.tap.automationobjects.Environment;

public class TestRobot implements IScenarioStarter{

	private static final String CLASSNAME = TestRobot.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String ICAUTO_TESTCASE_PATH = "test";
	public static String ICAUTO_LOG_PATH = "logs";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(AutomationService autoService) {
		
		try {
			LogUtil.init(System.getProperty("user.dir") + File.separator + ICAUTO_LOG_PATH);
		} catch (AutoException e) {
			logger.log(LogLevel.ERROR, "Failed to initiate the LogUtil.", e);
		}
		
		String executionInfo = MessageFormat.format("ExecutionSet:{0}\tRelease:{1}\tBuild:{2}" +
				System.getProperty("line.separator") + "Environment:{3}\t", 
				//TODO use real info when apply to field.
				/*new Object[] {
					autoService.getCurrentExecutionSet().getName(),
					autoService.getCurrentRelease(),
					autoService.getCurrentBuildLevel(),
					autoService.getCurrentEnvironment().getEnvironmentName()
				});*/
				new Object[] {"ConfigureSTD_SingleCLusterDE", "8550", "20130829","STDSingleClusterDE"});
		
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
			String regScriptName = "";
			
			if (null == regScriptName || "".equals(regScriptName)) {
				logger.log(LogLevel.WARNING, "The script(step) name registered in TAP is empty. Will execute existing any cases.");
			}
			
			//TODO Construct Configuration with Environment info via autoSerivce.
			HashMap<String, String> config = new HashMap<String, String>();
			//Environment curEnv = autoService.getCurrentEnvironment();
			
			//TODO Stub for config hash map
			config.put(Configurations.BPMPATH.getKey(), "E:\\bpm\\85\\STANDARD\\deploy2\\AppServer");
			config.put(Configurations.CEUSERNAME.getKey(), "admin");
			config.put(Configurations.CEUSERPWD.getKey(), "admin");
			
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
