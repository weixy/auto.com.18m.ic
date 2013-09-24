/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium;

import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
	
	public void verify() throws Exception {
		WebDriver driver = options.getWebDriver();
		SeUtil util = new SeUtil(driver);
		driver.manage().window().maximize();
		String pcURL = getURL();
		
		System.out.println(pcURL);
		driver.navigate().to(pcURL);
		util.waitForElement(By.id(ProcessCenter.ID_LOGINHEADER));
		successPoints ++;
		
		LoginPanel lgPanel = new LoginPanel(driver);
		lgPanel.login("celladmin", "celladmin");
		successPoints ++;
		
		util.waitForElement(By.xpath(ProcessCenter.XPATH_CLOSEBUTTON));
		ProcessCenter pcElements = new ProcessCenter(util);
		pcElements.buttonClose.click();
		successPoints ++;
		
		pcElements.checkAllTabs();
		
		pcElements.checkInitialProcApp();
		
		pcElements.checkInitialToolkitTab();
		
		pcElements.checkInitialServersTab();
		
		pcElements.checkAdminTab();
		
		driver.quit();
		this.failedPoints += pcElements.failedPoints;
		this.successPoints += pcElements.successPoints;
	}
}
