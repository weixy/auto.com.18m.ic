/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.test;

import java.io.File;

import com.ibm.bpm.automation.tap.adapter.AutomationService;
import com.ibm.bpm.automation.tap.adapter.IScenarioStarter;
import com.ibm.bpm.automation.tap.automationobjects.Environment;

public class TestOperation implements IScenarioStarter {
	
	public static String ICAUTO_LOG_PATH = "logs";

	@Override
	public void start(AutomationService autoService) {
		
		Environment curEnv = autoService.getCurrentEnvironment();
		autoService.retriveAllLogs(curEnv, System.getProperty("user.dir") + File.separator + ICAUTO_LOG_PATH + File.separator + "bpm");
		
		//autoService.su
		
	}

}
