/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class SeUtil {
	
	private static final String CLASSNAME = SeUtil.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	public static final int DEFAULT_WAIT_SECONDS = 10;

	public WebDriver webDriver;
	
	public SeUtil(WebDriver driver) {
		webDriver = driver;
		webDriver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_SECONDS, TimeUnit.SECONDS);
	}
	
	public void resetImplicitWait(int seconds) {
		webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		webDriver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}
	
	public void waitForElement(final By locator) {
		webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(webDriver, 5);
		wait.until(
			new ExpectedCondition<WebElement>() {
				public WebElement apply(WebDriver d) {
					return d.findElement(locator);
				}
			}
		);
		webDriver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_SECONDS, TimeUnit.SECONDS);
	}
	
	public void waitForListLoaded(final By locator) {
		webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(webDriver, 1);
		wait.until(
			new ExpectedCondition<Boolean>() {
				public int size = -1;
				public Boolean apply(WebDriver d) {
					int s = webDriver.findElements(locator).size();
					if (size == s) {
						System.out.println(size+"="+s);
						return true;
					} else {
						System.out.println(size+":"+s);
						size = s;
						return false;
					}
				}
			}
		);
		webDriver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_SECONDS, TimeUnit.SECONDS);
	}
	
	public boolean isElementExsit(By locator) {
        try {  
            webDriver.findElement(locator);  
            return true;  
        } catch (NoSuchElementException e) {  
            logger.log(LogLevel.ERROR, "Failed to find element with locator '" + locator.toString() + "'.", e);
        }  
        return false;
	}
	
	public boolean areElementsExist(By locator) {
		try {
			webDriver.findElements(locator);
			return true;
		} catch (NoSuchElementException e) {
			logger.log(LogLevel.ERROR, "Failed to find elements with locator '" + locator.toString() + "'.", e);
		}
		return false;
	}
	
}
