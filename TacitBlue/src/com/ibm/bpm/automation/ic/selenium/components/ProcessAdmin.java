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

public class ProcessAdmin {
	private static final String CLASSNAME = ProcessAdmin.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String XPATH_PMENUITEM = "//div[@class='proPipes']/table/tbody/tr[@class='middle']/td[@class='middleCenter']/div/div";
	public static String XPATH_PMENUITEM_SERADMIN = XPATH_PMENUITEM + "[text()='Server Admin']";
	public static String XPATH_PMENUITEM_PROINSPT = XPATH_PMENUITEM + "[text()='Process Inspector']";
	public static String XPATH_PMENUITEM_INSTAPPS = XPATH_PMENUITEM + "[text()='Installed Apps']";
	public static String XPATH_SRADNAV = "//div[@id='repository.client.adminConsoleDisplayPanel.stackPanel_navTree']/div[@class='twTopLevelTreeItem']";
	public static String XPATH_SRADNAV_TOPLV = "table/tbody/tr/td/div";
	public static String XPATH_SRADNAV_SUBLV = XPATH_SRADNAV 
			+ "/div/div/div[@class='gwt-TreeItem']/div/div"
			+ "/div/table[@class='twSubTreeItem']/tbody/tr[@class='middle']/td[@class='middleCenter']"
			+ "/div/div";
	public static String XPATH_PALOGO = "//div[@id='processAdmin-logo']";
	public static String ID_LOGINHEADER = "login-header";
	
	public static String proPipes [] = {
		"Server Admin",
		"Process Inspector",
		"Installed Apps"
	};
	
	public static String navTopLvs [] = {
		"IBM BPM Admin",
		"User Management",
		"Monitoring",
		"Event Manager",
		"Admin Tools",
		"Saved Search Admin"
	};
	
	public SeUtil util;
	public int failedPoints;
	public int successPoints;
	
	public ProcessAdmin(SeUtil u) {
		util = u;
		failedPoints = 0;
		successPoints = 0;
	}
	
	public String checkPipesMenu() {
		StringBuffer result = new StringBuffer();
		String scrFileName;
		result.append("--- Check Pipe Menu List ---" + System.getProperty("line.separator"));
		
		List<WebElement> menuList = util.webDriver.findElements(By.xpath(XPATH_PMENUITEM));
		
		if (proPipes.length != menuList.size()) {
			scrFileName = util.takeScreenShot();
			result.append("Find menu items not as expected: ");
			for (WebElement e : menuList) {
				result.append(" >> " + e.getText());
			}
			result.append(System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "There are more or less menu items shown than expected. Please refer screenshot: " 
					+ scrFileName + ".");
			failedPoints ++;
		} 
		for (String name : proPipes) {
			boolean found = false;
			for (Iterator<WebElement> it = menuList.iterator(); it.hasNext();) {
				if (name.equals(it.next().getText())) {
					found = true;
					break;
				}
			}
			if (found) {
				result.append("Found pipes menu item '" + name + "'."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.INFO, "Found pipes menu item '" + name + "'.");
				successPoints ++;
			} else {
				scrFileName = util.takeScreenShot();
				result.append("Failed to find pipes menu item '" 
						+ name + "'. Please refer screenshot: " + scrFileName + "."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Failed to find pipes menu item '"
						+ name + "'. Please refer screenshot: " + scrFileName + ".");
				failedPoints ++;
			}
		}		
		return result.toString();
	}
	
	public String checkSerAdminNaviTree() {
		StringBuffer result = new StringBuffer();
		String scrFileName;
		result.append("--- Check Navigation list of Server Admin view ---" + System.getProperty("line.separator"));
		
		List<WebElement> rootList = util.webDriver.findElements(By.xpath(XPATH_SRADNAV));
		
		if (navTopLvs.length != rootList.size()) {
			scrFileName = util.takeScreenShot();
			result.append("Find navigation items not as expected: ");
			for (WebElement e : rootList) {
				result.append(" >> " + e.findElement(By.xpath(XPATH_SRADNAV_TOPLV)).getText());
			}
			result.append(System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "There are more or less navigation items shown than expected. Please refer screenshot: " 
					+ scrFileName + ".");
			failedPoints ++;
		} 
		for (String name : navTopLvs) {
			boolean found = false;
			for (Iterator<WebElement> it = rootList.iterator(); it.hasNext();) {
				if (name.equals(it.next().findElement(By.xpath(XPATH_SRADNAV_TOPLV)).getText())) {
					found = true;
					break;
				}
			}
			if (found) {
				result.append("Found navigation menu item '" + name + "'."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.INFO, "Found navigation menu item '" + name + "'.");
				successPoints ++;
			} else {
				scrFileName = util.takeScreenShot();
				result.append("Failed to find navigation menu item '" 
						+ name + "'. Please refer screenshot: " + scrFileName + "."
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Failed to find navigation menu item '"
						+ name + "'. Please refer screenshot: " + scrFileName + ".");
				failedPoints ++;
			}
		}
		
		return result.toString();
	}
	
}
