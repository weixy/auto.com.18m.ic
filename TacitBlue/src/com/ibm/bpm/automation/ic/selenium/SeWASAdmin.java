/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.selenium.components.LoginPanel;
import com.ibm.bpm.automation.ic.selenium.components.WasAdminConsole;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class SeWASAdmin extends SeBPMModule {

	private static final String CLASSNAME = SeWASAdmin.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public String DEFAULT_CONTEXTROOT = "/ibm/console";
	
	public SeWASAdmin(SeRuntimeOptions options) {
		super();
		this.options = options;
	}
	
	public String verify() {
		StringBuffer result = new StringBuffer();
		try {
			WebDriver driver = options.getWebDriver();
			SeUtil util = new SeUtil(driver);
			util.resetImplicitWait(15);
			driver.manage().window().maximize();
			String pcURL = getURL();
			
			System.out.println(pcURL);
			driver.navigate().to(pcURL);
			util.waitForElement(By.id(WasAdminConsole.ID_LOGINTITLE));
			result.append("WAS Admin Console web page has been opened" + System.getProperty("line.separator"));
			successPoints ++;
			
			LoginPanel lgPanel = new LoginPanel(driver);
			lgPanel.login("celladmin", "celladmin");
			result.append("Login WAS Admin Console ..." + System.getProperty("line.separator"));
			successPoints ++;
			
			WebElement frame;
			util.waitForElement(By.xpath(WasAdminConsole.XPATH_FRAME_BANNER));
			
			frame = util.webDriver.findElement(By.xpath(WasAdminConsole.XPATH_FRAME_NAVIGATION));
			util.webDriver.switchTo().frame(frame);
			WasAdminConsole wac = new WasAdminConsole(util);
			result.append(wac.navigate("Service integration>Buses") + System.getProperty("line.separator"));
			
			frame = util.webDriver.findElement(By.xpath(WasAdminConsole.XPATH_FRAME_CONTENT));
			util.webDriver.switchTo().frame(frame);
			
			
			
			frame = util.webDriver.findElement(By.xpath(WasAdminConsole.XPATH_FRAME_BANNER));
			util.webDriver.switchTo().frame(frame);
			WebElement logOut = util.webDriver.findElement(By.xpath(WasAdminConsole.XPATH_LOGOUT));
			logOut.click();
			
			driver.quit();
			
		} catch (Exception e) {
			result.append("Got exception when verifying WAS Admin Console UI." 
					+ System.getProperty("line.separator")
					+ e.getMessage() + System.getProperty("line.separator"));
			logger.log(LogLevel.ERROR, "Got exception when verifying WAS Admin Console UI.", e);
			failedPoints ++;
		}
		return result.toString();
	}
	
	public String getURL() {
		String url = null;
		
		url = (options.isUseSecurity() ? "https://" : "http://")
				+ options.getHost() + ":"
				+ (options.isUseSecurity() ? options.getSecureAdminPort() : options.getAdminPort())
				+ DEFAULT_CONTEXTROOT;
		
		return url;
	}

}
