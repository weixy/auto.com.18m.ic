/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium.components;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.selenium.SeUtil;
import com.ibm.bpm.automation.ic.utils.LogUtil;


public class ProcessCenter {

	private static final String CLASSNAME = ProcessCenter.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String XPATH_CLOSEBUTTON = "//img[@class='closeButton']";
	public static String XPATH_TABPROCAPPS = "//span[@id='repository.client.repositoryPanel.hyperlink_processApps']";
	public static String XPATH_TABTOOLKIT = "//span[@id='repository.client.repositoryPanel.hyperlink_toolkits']";
	public static String XPATH_TABSERVERS = "//span[@id='repository.client.repositoryPanel.hyperlink_servers']";
	public static String XPATH_TABADMIN = "//span[@id='repository.client.repositoryPanel.hyperlink_admin']";
	public static String XPATH_BODYLISTITEM = "//table[@id='repository.client.projectPanel.flowPanel_processBody']/tbody/tr/td/div/a/span";
	public static String XPATH_MENULISTITEM = "//div/div[@id='gwtcommon.client.pipesMenu.link_0']";
	public static String ID_LOGINHEADER = "login-header";
	
	public static String[] existingApps = {
		"Hiring Sample", 
		"Saved Search Admin", 
		"Process Portal"
	};
	
	public static String[] existingToolkits = {
		"Content Management",
		"System Governance",
		"Dashboards",
		"Coaches",
		"System Data"
	};
	
	public static String[] adminMenuItems = {
		"Manage Users",
		"Activity Log",
		"Registration"
	};
	
	public WebElement buttonClose;
	public SeUtil util;
	public int failedPoints;
	public int successPoints;
	
	public ProcessCenter(SeUtil u) {
		failedPoints = 0;
		successPoints = 0;
		util = u;
		buttonClose = util.webDriver.findElement(By.xpath(XPATH_CLOSEBUTTON));
	}
	
	public void checkAllTabs() {
		if (util.isElementExsit(By.xpath(XPATH_TABPROCAPPS))) {
			logger.log(LogLevel.INFO, "Succeed to find tab 'Process Apps'.");
			successPoints ++;
		} else {
			logger.log(LogLevel.ERROR, "Failed to find tab 'Process Apps'.");
			failedPoints ++; 
		}
		if (util.isElementExsit(By.xpath(XPATH_TABTOOLKIT))) {
			logger.log(LogLevel.INFO, "Succeed to find tab 'Toolkits'.");
			successPoints ++;
		} else {
			logger.log(LogLevel.ERROR, "Failed to find tab 'Toolkits'.");
			failedPoints ++;
		}
		if (util.isElementExsit(By.xpath(XPATH_TABSERVERS))) {
			logger.log(LogLevel.INFO, "Succeed to find tab 'Servers'.");
			successPoints ++;
		} else {
			logger.log(LogLevel.ERROR, "Failed to find tab 'Servers'.");
			failedPoints ++;
		}
		if (util.isElementExsit(By.xpath(XPATH_TABADMIN))) {
			logger.log(LogLevel.INFO, "Succeed to find tab 'Admin'.");
			successPoints ++;
		} else {
			logger.log(LogLevel.ERROR, "Failed to find tab 'Admin'.");
			failedPoints ++;
		}
	}
	
	public void checkInitialProcApp() {
		util.webDriver.findElement(By.xpath(XPATH_TABPROCAPPS)).click();
		util.waitForListLoaded(By.xpath(XPATH_BODYLISTITEM));
		List<WebElement> appLists = util.webDriver.findElements(By.xpath(XPATH_BODYLISTITEM));
		
		if (appLists.size() != existingApps.length) {
			logger.log(LogLevel.ERROR, "There are more or less Apps shown than expected.");
			failedPoints ++;
		}
		for (String name : existingApps) {
			boolean found = false;
			for (Iterator<WebElement> it = appLists.iterator(); it.hasNext();) {
				if (name.equals(it.next().getText())) {
					found = true;
					break;
				}
			}
			if (found) {
				logger.log(LogLevel.INFO, "Found pre-deployed applicaiton '" + name + "'.");
				successPoints ++;
			} else {
				logger.log(LogLevel.ERROR, "Failed to find pre-deployed applicaiton '" + name + "'.");
				failedPoints ++;
			}
		}
		
	}
	
	public void checkInitialToolkitTab() {
		util.webDriver.findElement(By.xpath(XPATH_TABTOOLKIT)).click();
		util.waitForListLoaded(By.xpath(XPATH_BODYLISTITEM));
		List<WebElement> tkLists = util.webDriver.findElements(By.xpath(XPATH_BODYLISTITEM));
		
		if (tkLists.size() != existingToolkits.length) {
			logger.log(LogLevel.ERROR, "There are more or less Toolkits shown than expected.");
			failedPoints ++;
		}
		for (String name : existingToolkits) {
			boolean found = false;
			for (Iterator<WebElement> it = tkLists.iterator(); it.hasNext();) {
				if (name.equals(it.next().getText())) {
					found = true;
					break;
				}
			}
			if (found) {
				logger.log(LogLevel.INFO, "Found pre-deployed toolkit '" + name + "'.");
				successPoints ++;
			} else {
				logger.log(LogLevel.ERROR, "Failed to find pre-deployed toolkit '" + name + "'.");
				failedPoints ++;
			}
		}
	}
	
	public void checkInitialServersTab() {
		util.webDriver.findElement(By.xpath(XPATH_TABSERVERS)).click();
		util.waitForListLoaded(By.xpath(XPATH_BODYLISTITEM));
		List<WebElement> serLists = util.webDriver.findElements(By.xpath(XPATH_BODYLISTITEM));
		
		if (serLists.size() == 0) {
			logger.log(LogLevel.INFO, "The server list is empty.");
			successPoints ++;
		} else {
			logger.log(LogLevel.ERROR, "The server list is not empty.");
			failedPoints ++;
		}
	}
	
	public void checkAdminTab() {
		util.webDriver.findElement(By.xpath(XPATH_TABADMIN)).click();
		util.waitForListLoaded(By.xpath(XPATH_MENULISTITEM));
		List<WebElement> menuList = util.webDriver.findElements(By.xpath(XPATH_MENULISTITEM));
		
		for (String name : adminMenuItems) {
			boolean found = false;
			for (Iterator<WebElement> it = menuList.iterator(); it.hasNext();) {
				if (name.equals(it.next().getText())) {
					found = true;
					break;
				}
			}
			if (found) {
				logger.log(LogLevel.INFO, "Menu item '" + name + "' is correct.");
				successPoints ++;
			} else {
				logger.log(LogLevel.ERROR, "Failed to verify menu item '" + name + "'.");
				failedPoints ++;
			}
		}
	}
	
}
