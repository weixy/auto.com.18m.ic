package com.ibm.bpm.automation.ic.test;

import java.io.File;

import com.ibm.bpm.automation.tap.adapter.AutomationService;
import com.ibm.bpm.automation.tap.adapter.IScenarioStarter;
import com.ibm.bpm.automation.tap.automationobjects.Environment;

public class TestOperation implements IScenarioStarter {
	
	public static String ICAUTO_LOG_PATH = "logs";

	@Override
	public void start(AutomationService autoService) {
		// TODO Auto-generated method stub
		Environment curEnv = autoService.getCurrentEnvironment();
		autoService.retriveAllLogs(curEnv, System.getProperty("user.dir") + File.separator + ICAUTO_LOG_PATH + File.separator + "bmp");
		
		//autoService.su
		
	}

}
