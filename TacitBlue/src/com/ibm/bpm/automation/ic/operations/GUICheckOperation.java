/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.operations;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.OperationParameters;
import com.ibm.bpm.automation.ic.selenium.ProcessCenterVerification;
import com.ibm.bpm.automation.ic.selenium.RuntimeOptions;
import com.ibm.bpm.automation.ic.tap.TestRobot;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class GUICheckOperation extends BaseOperation {
	
	private static final String CLASSNAME = GUICheckOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	
	
	@Override
	public void run(HashMap<String, Object> config) {
		// TODO Auto-generated method stub
		super.run(config);
		
		RuntimeOptions runOptions = null;
		String option = this.getOption();
		if (null == option) {
			runOptions = RuntimeOptions.getRuntimeOptions();
		}
		else {
			String optFilePath = System.getProperty("user.dir") + File.separator + TestRobot.ICAUTO_TESTCASE_PATH + File.separator + option;
			if ((new File(optFilePath)).exists()) {
				try {
					runOptions = RuntimeOptions.getRuntimeOptions(optFilePath);
				} catch (AutoException e) {
					logger.log(LogLevel.ERROR, "Failed to load runtime option file '" + optFilePath + "' when preparing for selenium.", e);
					failedPoints++;
				}
			} else {
				logger.log(LogLevel.WARNING, "The specified selenium runtime option file '" + optFilePath + "' is not existing.");
				runOptions = RuntimeOptions.getRuntimeOptions();
			}
		}
		
		String target = this.getType();
		if (target != null) {
			if (OperationParameters.CHECKGUI_TYP_PROCCENTER.getType().equalsIgnoreCase(target)) {
				ProcessCenterVerification.VerifyProcessCenter(runOptions.getWebDriver());
			} else if (OperationParameters.CHECKGUI_TYP_WASADMIN.getType().equalsIgnoreCase(target)) {
				
			} else if (OperationParameters.CHECKGUI_TYP_PROCADMIN.getType().equalsIgnoreCase(target)) {
				
			} else if (OperationParameters.CHECKGUI_TYP_PROCPORTAL.getType().equalsIgnoreCase(target)) {
				
			} else {
				logger.log(LogLevel.ERROR, "Unsupported target web application specified: '" + target + "'");
				failedPoints++;
			}
		} else {
			logger.log(LogLevel.ERROR, "No target web application specified.");
			failedPoints++;
		}
		
	}
	
	

}
