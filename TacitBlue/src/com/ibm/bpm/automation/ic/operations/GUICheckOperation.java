/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.operations;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.OperationParameters;
import com.ibm.bpm.automation.ic.selenium.SeBPMModule;
import com.ibm.bpm.automation.ic.selenium.SeProcessAdmin;
import com.ibm.bpm.automation.ic.selenium.SeProcessCenter;
import com.ibm.bpm.automation.ic.selenium.SeRuntimeOptions;
import com.ibm.bpm.automation.ic.selenium.SeWASAdmin;
import com.ibm.bpm.automation.ic.tap.TestRobot;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class GUICheckOperation extends BaseOperation {
	
	private static final String CLASSNAME = GUICheckOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	@Override
	public void run(HashMap<String, Object> config) {
		// TODO Auto-generated method stub
		super.run(config);
		StringBuffer result = new StringBuffer();
		//generate runtime option for selenium
		SeRuntimeOptions runOptions = null;
		String option = this.getOption();
		if (null == option) {
			runOptions = SeRuntimeOptions.getRuntimeOptions();
		}
		else {
			String optFilePath = System.getProperty("user.dir") + File.separator + TestRobot.ICAUTO_TESTCASE_PATH + File.separator + option;
			if ((new File(optFilePath)).exists()) {
				try {
					runOptions = SeRuntimeOptions.getRuntimeOptions(optFilePath);
				} catch (AutoException e) {
					result.append("Failed to load runtime option file '" 
							+ optFilePath + "' when preparing for selenium."
							+ System.getProperty("line.separator"));
					logger.log(LogLevel.ERROR, "Failed to load runtime option file '" + optFilePath + "' when preparing for selenium.", e);
					failedPoints ++;
				}
			} else {
				result.append("The specified selenium runtime option file '" 
						+ optFilePath + "' is not existing."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.WARNING, "The specified selenium runtime option file '" + optFilePath + "' is not existing.");
				runOptions = SeRuntimeOptions.getRuntimeOptions();
			}
		}
		
		if (null == runOptions) {
			result.append("Failed to load runtime option." + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "Failed to load runtime option.");
			failedPoints ++;
			submit(result.toString(), logger);
			return;
		}
		
		String target = this.getType();
		if (target != null) {
			if (OperationParameters.CHECKGUI_TYP_PROCCENTER.getType().equalsIgnoreCase(target)) {
				SeProcessCenter spc = new SeProcessCenter(runOptions);
				result.append(spc.verify());
				failedPoints += spc.failedPoints;
				successPoints += spc.successPoints;
			} else if (OperationParameters.CHECKGUI_TYP_WASADMIN.getType().equalsIgnoreCase(target)) {
				SeWASAdmin swa = new SeWASAdmin(runOptions);
				result.append(swa.verify());
				failedPoints += swa.failedPoints;
				successPoints += swa.successPoints;
			} else if (OperationParameters.CHECKGUI_TYP_PROCADMIN.getType().equalsIgnoreCase(target)) {
				SeProcessAdmin spa = new SeProcessAdmin(runOptions);
				result.append(spa.verify());
				failedPoints += spa.failedPoints;
				successPoints += spa.successPoints;
			} else if (OperationParameters.CHECKGUI_TYP_PROCPORTAL.getType().equalsIgnoreCase(target)) {
				
			} else {
				result.append("Unsupported target web application specified: '" + target + "'" 
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Unsupported target web application specified: '" + target + "'");
				failedPoints++;
			}
		} else {
			logger.log(LogLevel.ERROR, "No target web application specified.");
			failedPoints++;
		}
		
		System.out.println(result);
		submit(result.toString(), logger);
		
	}
	
	

}
