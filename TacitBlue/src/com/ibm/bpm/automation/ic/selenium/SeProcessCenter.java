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
import com.ibm.bpm.automation.ic.selenium.components.ProcessCenter;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class SeProcessCenter extends SeBPMModule {
	
	private static final String CLASSNAME = SeProcessCenter.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public String DEFAULT_CONTEXTROOT = "ProcessCenter";
	
	public SeProcessCenter(SeRuntimeOptions options) {
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
			util.waitForElement(By.id(ProcessCenter.ID_LOGINHEADER));
			result.append("Process Center web page has been opened" + System.getProperty("line.separator"));
			successPoints ++;
			
			LoginPanel lgPanel = new LoginPanel(driver);
			lgPanel.login(options.getLoginUserName(), options.getLoginUserPwd());
			result.append("Login Process Center ..." + System.getProperty("line.separator"));
			successPoints ++;
			
			util.waitForElement(By.xpath(ProcessCenter.XPATH_CLOSEBUTTON));
			ProcessCenter pc = new ProcessCenter(util);
			pc.buttonClose.click();
			successPoints ++;
			result.append("Logined Process Center and ready for GUI test ..." + System.getProperty("line.separator"));
			logger.log(LogLevel.INFO, "Process Center is ready for futher test ...");
			
			result.append(pc.checkAllTabs());
			
			result.append(pc.checkInitialProcApp());
			
			result.append(pc.checkInitialToolkitTab());
			
			result.append(pc.checkInitialServersTab());
			
			result.append(pc.checkAdminTab());
			
			driver.quit();
			
			this.failedPoints += pc.failedPoints;
			this.successPoints += pc.successPoints;
			result.append("Process Center UI verification has been completed." 
					+ System.getProperty("line.separator"));
			logger.log(LogLevel.INFO, "Process Center UI verification has been completed.");
			
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
