/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium.components;

import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class WasAdminPreferenceForm {
	
	private static final String CLASSNAME = WasAdminPreferenceForm.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	private static String XPATH_PREFFORM = "//form[@name='PreferenceForm']";
	private static String XPATH_PREFBUTN = "id('prefs')/a";
	private static String XPATH_PREFBUTN_EXPAND = "id('com_ibm_ws_prefsTableImg')";
	private static String PREF_EXPANDED = "/ibm/console/images/arrow_expanded.gif";
	private static String PREF_COLLAPSED = "/ibm/console/images/arrow_collapsed.gif";
	private static String XPATH_PREFTABLE = "id('com_ibm_ws_prefsTable')/tbody/tr/td/table";
	private static String XPATH_PREFTABLE_MAXROWS = "id('text1')";
	private static String XPATH_PREFTABLE_RETAIN = "id('checkbox2')";
	private static String XPATH_PREFTABLE_GRPLEVL = "id('list3')";
	private static String XPATH_PREFTABLE_APPLY = "./tbody/tr/td/input[@value='Apply']";
	private static String XPATH_PREFTABLE_RESET = "./tbody/tr/td/input[@value='Reset']";
	
	private WebDriver driver;
	private WebElement prefExpandButn;
	private WebElement prefMaximumRows;
	private WebElement prefRetainFilter;
	private WebElement prefGroupLevel;
	private WebElement prefApplyButn;
	private WebElement prefResetButn;
	
	public WasAdminPreferenceForm(WebDriver d) {
		driver = d;
		try {
			WebElement preTable = driver.findElement(By.xpath(XPATH_PREFTABLE));
			prefMaximumRows = preTable.findElement(By.xpath(XPATH_PREFTABLE_MAXROWS));
			prefRetainFilter = preTable.findElement(By.xpath(XPATH_PREFTABLE_RETAIN));
			prefGroupLevel = preTable.findElement(By.xpath(XPATH_PREFTABLE_GRPLEVL));
			prefApplyButn = preTable.findElement(By.xpath(XPATH_PREFTABLE_APPLY));
			prefResetButn = preTable.findElement(By.xpath(XPATH_PREFTABLE_RESET));
		} catch (Exception e) {
			logger.log(LogLevel.ERROR, "Failed to locate element while initiating Preference Form", e);
		}
	}
	
	public void expandPreferences() {
		prefExpandButn = driver.findElement(By.xpath(XPATH_PREFBUTN));
		if (!hasPrefsExpanded()) {
			prefExpandButn.click();
		}
	}
	
	public void collapsePreferences() {
		prefExpandButn = driver.findElement(By.xpath(XPATH_PREFBUTN));
		if (hasPrefsExpanded()) {
			prefExpandButn.click();
		}
	}
	
	public boolean hasPrefsExpanded() {
		WebElement img = driver.findElement(By.xpath(XPATH_PREFBUTN_EXPAND));
		String title = img.getAttribute("src");
		if (null == title) {
			logger.log(LogLevel.ERROR, "There's no attribute 'title' for element: " + img.toString() + ".");
			return false;
		}
		if (title.endsWith(PREF_EXPANDED)) {
			return true;
		}
		if (title.endsWith(PREF_COLLAPSED)) {
			return false;
		}
		return false;
	}
	
	public void setMaxRows(int i) {
		prefMaximumRows.clear();
		prefMaximumRows.sendKeys((new Integer(i)).toString());
	}
	
	public void markRetainChkBox(boolean value) {
		if (value && !prefRetainFilter.isSelected()) {
			prefRetainFilter.click();
		} else if (!value && prefRetainFilter.isSelected()) {
			prefRetainFilter.click();
		}
	}
	
	public void selectGroupLevel(String level) {
		List<WebElement> options = prefGroupLevel.findElements(By.tagName("option"));
		for (WebElement option : options) {
			if(level.equalsIgnoreCase(option.getText())) {
				option.click();
				break;
			}
		}
	}
	
	public void applyChanges() {
		prefApplyButn.click();
	}
	
	public void resetPreferences() {
		prefResetButn.click();
	}

}
