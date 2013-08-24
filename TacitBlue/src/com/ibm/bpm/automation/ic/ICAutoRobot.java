package com.ibm.bpm.automation.ic;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.utils.ICAutoLogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.tap.adapter.AutomationService;
import com.ibm.bpm.automation.tap.adapter.IScenarioStarter;
import com.ibm.bpm.automation.tap.automationobjects.Environment;

public class ICAutoRobot implements IScenarioStarter{

	private static final String CLASSNAME = ICAutoRobot.class.getName();
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
			logger.log(ICAutoLogLevel.ERROR, "Failed to initiate the LogUtil.", e);
		}
		
		List<TestCase> caseList = null;
		
		try {
			caseList = TestCaseLoader.loadTestCases(System.getProperty("user.dir") 
					+ File.separator 
					+ ICAUTO_TESTCASE_PATH);
		} catch (AutoException e) {
			logger.log(ICAutoLogLevel.ERROR, "Failed to load test cases.", e);
		}
		
		if (null != caseList) {
			boolean runAllCases = true;
			
			//String regScriptName = autoService.getCurrentTestScript().getName();
			String regScriptName = "";
			
			if (null == regScriptName || "".equals(regScriptName)) {
				logger.log(ICAutoLogLevel.WARNING, "The script(step) name registed in TAP is empty.");
				logger.log(ICAutoLogLevel.INFO, "All existing cases will be executed.");
			}
			
			//Environment curEnv = autoService.getCurrentEnvironment();
			int caseIndex = 0;
			for (int i=0; i<caseList.size(); i++) {
				if (caseList.get(i).getTitle().equals(regScriptName)) {
					runAllCases = false;
					caseIndex = i;
				}
			}
			if (runAllCases) {
				for (Iterator<TestCase> it = caseList.iterator(); it.hasNext();) {
					it.next().execute();
				}
			}
			else {
				TestCase testCase = caseList.get(caseIndex);
				testCase.execute();
			}
		}
		else {
			logger.log(ICAutoLogLevel.WARNING, "No cases has been loaded.");
			//Submit the fail execution result
		}
	}
	
	

}
