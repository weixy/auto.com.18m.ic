/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author weixy
 *
 */
public class LoginPanel {
	public static String XPATH_LOGINFORM = "//form[@action='j_security_check' or @action='/ibm/console/j_security_check']";
	public static String XPATH_USERINPUT = "//input[@name='j_username']";
	public static String XPATH_PWDINPUT = "//input[@name='j_password']";
	public static String XPATH_SUBMITBUTN = "//input[@type='submit']";
	
	public WebElement formLogin;
	public WebElement inputUserName;
	public WebElement inputPassword;
	public WebElement buttonSubmit;

	public LoginPanel(WebDriver driver) {
		formLogin = driver.findElement(By.xpath(XPATH_LOGINFORM));
		inputUserName = formLogin.findElement(By.xpath(XPATH_USERINPUT));
		inputPassword = formLogin.findElement(By.xpath(XPATH_PWDINPUT));
		buttonSubmit = formLogin.findElement(By.xpath(XPATH_SUBMITBUTN));
	}
	
	public void login(String userName, String password) {
		inputUserName.sendKeys(userName);
		inputPassword.sendKeys(password);
		buttonSubmit.click();
	}
}
