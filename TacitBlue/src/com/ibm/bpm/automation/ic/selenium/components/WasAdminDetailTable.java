/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium.components;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WasAdminDetailTable {
	
	public static String XPATH_TABLE = "//table[@class='framing-table']";
	public static String XPATH_TABLE_ROW = "//table[@class='framing-table']/tbody/tr[@class='table-row']";
	
	public WebDriver driver;
	public List<WebElement> rows;
	
	public WasAdminDetailTable(WebDriver d) {
		driver = d;
		rows = driver.findElements(By.xpath(XPATH_TABLE_ROW));
	}
	
	public void clickCellByIndex(int row, int col) {
		
	}
	
}
