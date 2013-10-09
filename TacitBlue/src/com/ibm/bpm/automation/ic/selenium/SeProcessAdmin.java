/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium;

import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.selenium.components.LoginPanel;
import com.ibm.bpm.automation.ic.selenium.components.ProcessAdmin;
import com.ibm.bpm.automation.ic.selenium.components.ProcessCenter;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class SeProcessAdmin extends SeBPMModule {
	private static final String CLASSNAME = SeProcessAdmin.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public String DEFAULT_CONTEXTROOT = "ProcessAdmin";
	
	public SeProcessAdmin(SeRuntimeOptions options) {
		super();
		super.DEFAULT_CONTEXTROOT = this.DEFAULT_CONTEXTROOT;
		this.options = options;
	}
	
	public String verify() {
		StringBuffer result = new StringBuffer();
		try {
			WebDriver driver = options.getWebDriver();
			SeUtil util = new SeUtil(driver);
			driver.manage().window().maximize();
			String pcURL = getURL();
			
			System.out.println(pcURL);
			driver.navigate().to(pcURL);
			util.waitForElement(By.id(ProcessAdmin.ID_LOGINHEADER));
			result.append("Process Admin web page has been opened" + System.getProperty("line.separator"));
			successPoints ++;
			
			LoginPanel lgPanel = new LoginPanel(driver);
			lgPanel.login("celladmin", "celladmin");
			result.append("Login Process Admin ..." + System.getProperty("line.separator"));
			successPoints ++;
			
			util.waitForElement(By.xpath(ProcessAdmin.XPATH_PALOGO));
			ProcessAdmin pa = new ProcessAdmin(util);
			result.append(pa.checkPipesMenu());
			
			result.append(pa.checkSerAdminNaviTree());
			
			driver.quit();
			this.failedPoints += pa.failedPoints;
			this.successPoints += pa.successPoints;
			result.append("Process Admin UI verification has been completed." 
					+ System.getProperty("line.separator"));
			logger.log(LogLevel.INFO, "Process Admin UI verification has been completed.");
		} catch (Exception e) {
			result.append("Got exception when verifying Process Center UI." 
					+ System.getProperty("line.separator")
					+ e.getMessage() + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "Got exception when verifying Process Center UI.", e);
			failedPoints ++;
		}
		
		return result.toString();
	}
}
