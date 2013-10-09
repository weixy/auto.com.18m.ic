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
	public static String TEXT_TABPROCAPPS = "Process Apps";
	public static String XPATH_TABTOOLKIT = "//span[@id='repository.client.repositoryPanel.hyperlink_toolkits']";
	public static String TEXT_TABTOOLKIT = "Toolkits";
	public static String XPATH_TABSERVERS = "//span[@id='repository.client.repositoryPanel.hyperlink_servers']";
	public static String TEXT_TABSERVERS = "Servers";
	public static String XPATH_TABADMIN = "//span[@id='repository.client.repositoryPanel.hyperlink_admin']";
	public static String TEXT_TABADMIN = "Admin";
	
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
	
	public String checkAllTabs() {
		StringBuffer result = new StringBuffer();
		String scrFileName;
		result.append("--- Check All Tabs of Process Center ---" + System.getProperty("line.separator"));
		if (util.isElementExsit(By.xpath(XPATH_TABPROCAPPS))) {
			result.append("Succeed to find tab 'Process Apps'." + System.getProperty("line.separator"));
			logger.log(LogLevel.INFO, "Succeed to find tab 'Process Apps'.");
			successPoints ++;
			
			String title = util.webDriver.findElement(By.xpath(XPATH_TABPROCAPPS)).getText().trim();
			if (TEXT_TABPROCAPPS.equals(title)) {
				result.append("Tab 'Process Apps' has correct title." + System.getProperty("line.separator"));
				logger.log(LogLevel.INFO, "Tab 'Process Apps' has correct title.");
				successPoints ++;
			} else {
				scrFileName = util.takeScreenShot();
				result.append("Tab 'Process Apps' has wrong title: " 
						+ title + ". Please refer screenshot: " + scrFileName + "." 
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Tab 'Process Apps' has wrong title: " 
						+ title + ". Please refer screenshot: " + scrFileName + ".");
				failedPoints ++;
			}
		} else {
			scrFileName = util.takeScreenShot();
			result.append("Failed to find tab 'Process Apps'. Please refer screenshot: " 
					+ scrFileName + "." + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "Failed to find tab 'Process Apps'. Please refer screenshot: " 
					+ scrFileName +".");
			failedPoints ++; 
		}
		
		if (util.isElementExsit(By.xpath(XPATH_TABTOOLKIT))) {
			result.append("Succeed to find tab 'Toolkits'." + System.getProperty("line.separator"));
			logger.log(LogLevel.INFO, "Succeed to find tab 'Toolkits'.");
			successPoints ++;
			
			String title = util.webDriver.findElement(By.xpath(XPATH_TABTOOLKIT)).getText().trim();
			if (TEXT_TABTOOLKIT.equals(title)) {
				result.append("Tab 'Toolkits' has correct title." + System.getProperty("line.separator"));
				logger.log(LogLevel.INFO, "Tab 'Toolkits' has correct title.");
				successPoints ++;
			} else {
				scrFileName = util.takeScreenShot();
				result.append("Tab 'Toolkits' has wrong title: " 
						+ title + ". Please refer screenshot: " + scrFileName + "."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Tab 'Toolkits' has wrong title: " + title + ". Please refer screenshot: " + scrFileName + ".");
				failedPoints ++;
			}
		} else {
			scrFileName = util.takeScreenShot();
			result.append("Failed to find tab 'Toolkits'. Please refer screenshot: " 
					+ scrFileName + "." + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "Failed to find tab 'Toolkits'. Please refer screenshot: " 
					+ scrFileName +".");
			failedPoints ++;
		}
		
		if (util.isElementExsit(By.xpath(XPATH_TABSERVERS))) {
			result.append("Succeed to find tab 'Servers'." + System.getProperty("line.separator"));
			logger.log(LogLevel.INFO, "Succeed to find tab 'Servers'.");
			successPoints ++;
			
			String title = util.webDriver.findElement(By.xpath(XPATH_TABSERVERS)).getText().trim();
			if (TEXT_TABSERVERS.equals(title)) {
				result.append("Tab 'Servers' has correct title." + System.getProperty("line.separator"));
				logger.log(LogLevel.INFO, "Tab 'Servers' has correct title.");
				successPoints ++;
			} else {
				scrFileName = util.takeScreenShot();
				result.append("Tab 'Servers' has wrong title: " 
						+ title + ". Please refer screenshot: " + scrFileName + "."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Tab 'Servers' has wrong title: " 
						+ title + ". Please refer screenshot: " + scrFileName + ".");
				failedPoints ++;
			}
		} else {
			scrFileName = util.takeScreenShot();
			result.append("Failed to find tab 'Servers'. Please refer screenshot: " 
					+ scrFileName + "." + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "Failed to find tab 'Servers'. Please refer screenshot: " 
					+ scrFileName +".");
			failedPoints ++;
		}
		
		if (util.isElementExsit(By.xpath(XPATH_TABADMIN))) {
			result.append("Succeed to find tab 'Admin'." + System.getProperty("line.separator"));
			logger.log(LogLevel.INFO, "Succeed to find tab 'Admin'.");
			successPoints ++;
			
			String title = util.webDriver.findElement(By.xpath(XPATH_TABADMIN)).getText().trim();
			if (TEXT_TABADMIN.equals(title)) {
				result.append("Tab 'Admin' has correct title." + System.getProperty("line.separator"));
				logger.log(LogLevel.INFO, "Tab 'Admin' has correct title.");
				successPoints ++;
			} else {
				scrFileName = util.takeScreenShot();
				result.append("Tab 'Admin' has wrong title: " 
						+ title + ". Please refer screenshot: " + scrFileName + "."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Tab 'Admin' has wrong title: " 
						+ title + ". Please refer screenshot: " + scrFileName + ".");
				failedPoints ++;
			}
		} else {
			scrFileName = util.takeScreenShot();
			result.append("Failed to find tab 'Admin'. Please refer screenshot: " 
					+ scrFileName +"." + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "Failed to find tab 'Admin'. Please refer screenshot: " 
					+ scrFileName +".");
			failedPoints ++;
		}
		
		return result.toString();
	}
	
	public String checkInitialProcApp() {
		StringBuffer result = new StringBuffer();
		String scrFileName;
		result.append("--- Check Process App List ---" + System.getProperty("line.separator"));
		
		util.webDriver.findElement(By.xpath(XPATH_TABPROCAPPS)).click();
		util.waitForListLoaded(By.xpath(XPATH_BODYLISTITEM));
		List<WebElement> appLists = util.webDriver.findElements(By.xpath(XPATH_BODYLISTITEM));
		
		if (appLists.size() != existingApps.length) {
			scrFileName = util.takeScreenShot();
			result.append("There are more or less Apps shown than expected. Please refer screenshot: " 
					+ scrFileName +"." + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "There are more or less Apps shown than expected. Please refer screenshot: " 
					+ scrFileName + ".");
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
				result.append("Found pre-deployed applicaiton '" 
						+ name + "'." + System.getProperty("line.separator"));
				logger.log(LogLevel.INFO, "Found pre-deployed applicaiton '" 
						+ name + "'.");
				successPoints ++;
			} else {
				scrFileName = util.takeScreenShot();
				result.append("Failed to find pre-deployed applicaiton '" 
						+ name + "'. Please refer screenshot: " + scrFileName + "."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Failed to find pre-deployed applicaiton '" 
						+ name + "'. Please refer screenshot: " + scrFileName + ".");
				failedPoints ++;
			}
		}
		return result.toString();
	}
	
	public String checkInitialToolkitTab() {
		StringBuffer result = new StringBuffer();
		String scrFileName;
		result.append("--- Check Toolkit List ---" + System.getProperty("line.separator"));
		
		util.webDriver.findElement(By.xpath(XPATH_TABTOOLKIT)).click();
		util.waitForListLoaded(By.xpath(XPATH_BODYLISTITEM));
		List<WebElement> tkLists = util.webDriver.findElements(By.xpath(XPATH_BODYLISTITEM));
		
		if (tkLists.size() != existingToolkits.length) {
			scrFileName = util.takeScreenShot();
			result.append("There are more or less Toolkits shown than expected. Please refer screenshot: " 
					+ scrFileName + "."  + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "There are more or less Toolkits shown than expected. Please refer screenshot: " + scrFileName + ".");
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
				result.append("Found pre-deployed toolkit '" + name + "'."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.INFO, "Found pre-deployed toolkit '" + name + "'.");
				successPoints ++;
			} else {
				scrFileName = util.takeScreenShot();
				result.append("Failed to find pre-deployed toolkit '" 
						+ name + "'. Please refer screenshot: " + scrFileName + "."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Failed to find pre-deployed toolkit '" + name + "'. Please refer screenshot: " + scrFileName + ".");
				failedPoints ++;
			}
		}
		return result.toString();
	}
	
	public String checkInitialServersTab() {
		StringBuffer result = new StringBuffer();
		String scrFileName;
		result.append("--- Check Servers List ---" + System.getProperty("line.separator"));
		
		util.webDriver.findElement(By.xpath(XPATH_TABSERVERS)).click();
		util.waitForListLoaded(By.xpath(XPATH_BODYLISTITEM));
		List<WebElement> serLists = util.webDriver.findElements(By.xpath(XPATH_BODYLISTITEM));
		
		if (serLists.size() == 0) {
			result.append("The server list is empty." + System.getProperty("line.separator"));
			logger.log(LogLevel.INFO, "The server list is empty.");
			successPoints ++;
		} else {
			scrFileName = util.takeScreenShot();
			result.append("The server list is not empty. Please refer screenshot: " 
					+ scrFileName + "." + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "The server list is not empty. Please refer screenshot: " + scrFileName + ".");
			failedPoints ++;
		}
		return result.toString();
	}
	
	public String checkAdminTab() {
		StringBuffer result = new StringBuffer();
		String scrFileName;
		result.append("--- Check Admin Tab ---" + System.getProperty("line.separator"));
		
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
				result.append("Menu item '" + name + "' is correct." + System.getProperty("line.separator"));
				logger.log(LogLevel.INFO, "Menu item '" + name + "' is correct.");
				successPoints ++;
			} else {
				scrFileName = util.takeScreenShot();
				result.append("Failed to verify menu item '" 
						+ name + "'. Please refer screenshot: " 
						+ scrFileName + "."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Failed to verify menu item '" + name + "'. Please refer screenshot: " + scrFileName + ".");
				failedPoints ++;
			}
		}
		return result.toString();
	}
	
}
