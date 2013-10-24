/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium.components;

import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
	
	private WebDriver driver;
	private WebElement headFrame;
	private WebElement naviFrame;
	private WebElement detaFrame;
	
	public WasAdminConsole(WebDriver d) {
		driver = d;
		headFrame = driver.findElement(By.xpath(XPATH_FRAME_BANNER));
		naviFrame = driver.findElement(By.xpath(XPATH_FRAME_NAVIGATION));
		detaFrame = driver.findElement(By.xpath(XPATH_FRAME_CONTENT));
	}
	
	
	public String navigate(String path) {
		StringBuffer result = new StringBuffer();
		String [] pathes = path.split(">");
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame(naviFrame);
		WebElement mainTask = driver.findElement(By.xpath(XPATH_MAINTASK + "/a[@title='" + pathes[0].trim() + "']"));
		if (null != mainTask) {
			mainTask.click();
			result.append("> Click entry '" + pathes[0] + "'"
					+ System.getProperty("line.separator"));
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
			subTask = mainTask.findElement(By.xpath(XPATH_MAINTASK + "/" + XPATH_NAVCHILD 
					+ "/div[@class='sub-task']/a[@title='" + pathes[1].trim() + "']"));
		}
		if (null != subTask) {
			subTask.click();
			result.append("> Click sub entry '" + pathes[1] + "'"
					+ System.getProperty("line.separator"));
		} else {
			result.append("Failed to find corresponding entry for sub task '" + pathes[1] + "'"
					+ System.getProperty("line.separator"));
			return result.toString();
		}
		
		WebElement subChild = null;
		if (pathes.length == 3) {
			subChild = subTask.findElement(By.xpath(XPATH_MAINTASK + "/" + XPATH_NAVCHILD 
					+ "/div[@class='sub-child-container']/ul/li/a[text()='" + pathes[2].trim() + "']"));
			if (null != subChild) {
				subChild.click();
				result.append("> Click sub child entry '" + pathes[2] + "'"
						+ System.getProperty("line.separator"));
			} else {
				result.append("Failed to find corresponding entry for sub child '" + pathes[2] + "'"
						+ System.getProperty("line.separator"));
			}
		}
		
		driver.switchTo().defaultContent();
		
		return result.toString();
	}
	
	public String switchToFrame(WebElement frame) {
		StringBuffer result = new StringBuffer();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(frame);
		return result.toString();
	}
	
	public String switchToHeadFrame() {
		return switchToFrame(headFrame);
	}
	
	public String swtichToNaviFrame() {
		return switchToFrame(naviFrame);
	}
	
	public String switchToDetailFrame() {
		return switchToFrame(detaFrame);
	}
	
	public String logout() {
		StringBuffer result = new StringBuffer();
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame(headFrame);
		WebElement logOut = driver.findElement(By.xpath(XPATH_LOGOUT));
		logOut.click();
		
		return result.toString();
	}
	
}
