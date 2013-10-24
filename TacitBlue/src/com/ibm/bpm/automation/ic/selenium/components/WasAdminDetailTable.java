/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium.components;

import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class WasAdminDetailTable {
	
	private static final String CLASSNAME = WasAdminDetailTable.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	public static String XPATH_TABLE = "//table[@class='framing-table']";
	public static String XPATH_TABLE_ROW = "//table[@class='framing-table']/tbody/tr[@class='table-row']";
	private static String XPATH_TABLE_SELECTALL = "id('selectAllImg')";
	private static String XPATH_TABLE_DESELALL = "id('deselectAllImg')";
	
	private WebDriver driver;
	public List<WebElement> rows;
	
	public WasAdminDetailTable(WebDriver d) {
		driver = d;
		rows = driver.findElements(By.xpath(XPATH_TABLE_ROW));
	}
	
	public void selectAll() {
		driver.findElement(By.xpath(XPATH_TABLE_SELECTALL)).click();
	}
	
	public void deSelectAll() {
		driver.findElement(By.xpath(XPATH_TABLE_DESELALL)).click();
	}
	
	public WebElement getCellElement(int row, int col) {
		return rows.get(row-1).findElement(By.xpath(".//td[" + col +"]/child::*[1]"));
	}
	
	public String getCellElementValue(int row, int col) {
		WebElement ce = getCellElement(row, col);
		if(ce.getTagName().equals("a") || ce.getTagName().equals("div")) {
			String txt = ce.getText().trim();
			if (txt.isEmpty()) {
				try {
					WebElement img = ce.findElement(By.xpath(".//img"));
					Actions acts = new Actions(driver);
					acts.moveToElement(img).perform();
					return img.getAttribute("alt").trim();
				} catch (NoSuchElementException e) {
					logger.log(LogLevel.ERROR, "Failed to find img child element", e);
				}
			}
			return txt;
		}
		return null;
	}
	
	public void clickCellByIndex(int row, int col) {
		getCellElement(row, col).findElement(By.tagName("a")).click();
	}
	
	public void clickToolButtn(By by) {
		driver.findElement(by).click();
	}
}
