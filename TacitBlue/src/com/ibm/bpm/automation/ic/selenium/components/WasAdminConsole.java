/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium.components;

import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.bpm.automation.ic.selenium.SeUtil;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class WasAdminConsole {
	private static final String CLASSNAME = WasAdminConsole.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String XPATH_NAVTABLE = "id('navTable')";
	public static String XPATH_MAINTASK = "id('navTable')/tbody/tr/td/div[@class='main-task']";
	public static String XPATH_NAVCHILD = "following-sibling::div[1]";
	public static String XPATH_LOGOUT = "id('ibm-banner-content')/ul/li/a[@title='Logout']";
	public static String XPATH_WASWELCOME = "id('wpsPortletTitleTextSpan')";
	public static String XPATH_FRAME_CONTENT = "//frame[@name='detail']";
	public static String XPATH_FRAME_NAVIGATION = "//frame[@name='navigation']";
	public static String XPATH_FRAME_BANNER = "//frame[@name='header']";
	public static String ID_LOGINTITLE = "loginTitle";
	
	public SeUtil util;
	public int failedPoints;
	public int successPoints;
	
	public WasAdminConsole(SeUtil u) {
		failedPoints = 0;
		successPoints = 0;
		util = u;
	}
	
	public String navigate(String path) {
		StringBuffer result = new StringBuffer();
		String [] pathes = path.split(">");
		WebElement mainTask = util.webDriver.findElement(By.xpath(XPATH_MAINTASK + "/a[@title='" + pathes[0].trim() + "']"));
		if (null != mainTask) {
			mainTask.click();
		} else {
			result.append("Failed to find corresponding entry for main task '" + pathes[0] + "'"
					+ System.getProperty("line.separator"));
			return result.toString();
		}
		
		WebElement subTask = null;
		if (pathes.length == 2) {
			subTask = mainTask.findElement(By.xpath(XPATH_MAINTASK + "/" + XPATH_NAVCHILD 
					+ "/ul/li/a[text()='" + pathes[1].trim() + "']"));
		} else if (pathes.length == 3) {
			subTask = mainTask.findElement(By.xpath(XPATH_NAVCHILD 
					+ "/div/a[text()='" + pathes[2].trim() + "']"));
		}
		if (null != subTask) {
			subTask.click();
		} else {
			result.append("Failed to find corresponding entry for sub task '" + pathes[1] + "'"
					+ System.getProperty("line.separator"));
			return result.toString();
		}
		
		WebElement subChild = null;
		if (pathes.length == 3) {
			subChild = subTask.findElement(By.xpath(XPATH_NAVCHILD + "/ul/li/a[text()='" + pathes[2].trim() + "']"));
			if (null != subChild) {
				subChild.click();
			} else {
				result.append("Failed to find corresponding entry for sub child '" + pathes[2] + "'"
						+ System.getProperty("line.separator"));
			}
		}
		
		return result.toString();
	}
}
