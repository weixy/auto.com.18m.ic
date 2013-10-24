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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.selenium.components.LoginPanel;
import com.ibm.bpm.automation.ic.selenium.components.WasAdminConsole;
import com.ibm.bpm.automation.ic.selenium.components.WasAdminDetailTable;
import com.ibm.bpm.automation.ic.selenium.components.WasAdminPreferenceForm;
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
			lgPanel.login(options.getLoginUserName(), options.getLoginUserPwd());
			result.append("Login WAS Admin Console ..." + System.getProperty("line.separator"));
			successPoints ++;
			
			//WebElement naviFrame = util.webDriver.findElement(By.xpath(WasAdminConsole.XPATH_FRAME_NAVIGATION));
			//WebElement headFrame = util.webDriver.findElement(By.xpath(WasAdminConsole.XPATH_FRAME_BANNER));
			//WebElement detaFrame = util.webDriver.findElement(By.xpath(WasAdminConsole.XPATH_FRAME_CONTENT));
			util.waitForElement(By.xpath(WasAdminConsole.XPATH_FRAME_BANNER));
			
			WasAdminConsole wac = new WasAdminConsole(util.webDriver);

			//Check clusters
			result.append(wac.navigate("Servers>Clusters>WebSphere application server clusters") + System.getProperty("line.separator"));
			wac.switchToDetailFrame();
			WasAdminDetailTable wdt = new WasAdminDetailTable(util.webDriver);
			List<String> clusterNames = options.getClusterNames();
			if (clusterNames.size() != wdt.rows.size()) {
				failedPoints ++;
				StringBuffer expClusters = new StringBuffer();
				for (String n : clusterNames) {
					expClusters.append(n + " ");
				}
				StringBuffer actClusters = new StringBuffer();
				for (int i=1; i<=wdt.rows.size(); i++) {
					actClusters.append(wdt.getCellElementValue(i, 2) + " ");
				}
				result.append("Actual created clusters are not as expected:"
						+ System.getProperty("line.separator")
						+ "Expected: " + expClusters
						+ System.getProperty("line.separator")
						+ "Actual: " + actClusters
						+ System.getProperty("line.separator"));
				logger.log(LogLevel.ERROR, "Actual created clusters are not as expected."
						+ System.getProperty("line.separator")
						+ "Expected: " + expClusters
						+ System.getProperty("line.separator")
						+ "Actual: " + actClusters);
			} else {
				for(int i=0; i<wdt.rows.size(); i++) {
					String cne = clusterNames.get(i);
					String cna = wdt.getCellElementValue(i+1, 2);
					if (cne.trim().equals(cna)) {
						successPoints ++;
						result.append("Cluster '" + cne + "' has been created, ");
						logger.log(LogLevel.INFO, "Cluster '" + cne + "' has been created.");
						
						String status = wdt.getCellElementValue(i+1, 3);
						if ("Started".equalsIgnoreCase(status)) {
							successPoints ++;
							result.append("and started succesfully. " + System.getProperty("line.separator"));
							logger.log(LogLevel.INFO, "Cluster '" + cne + "' has been started.");
						} else {
							failedPoints ++;
							result.append("but failed to start. (" + status + ")" + System.getProperty("line.separator"));
							logger.log(LogLevel.INFO, "Cluster '" + cne + "' was not started. (Current status:" + status + ")");
						}
					}
					else {
						failedPoints ++;
						result.append("Cluster '" + cne + "' failed to be created, ");
						logger.log(LogLevel.ERROR, "Cluster '" + cne + "' failed to be created.");
					}
				}
			}
			
			//Check Applications
			result.append(wac.navigate("Applications>Application Types>WebSphere enterprise applications") + System.getProperty("line.separator"));
			wac.switchToDetailFrame();
			WasAdminPreferenceForm wapf = new WasAdminPreferenceForm(driver);
			wapf.expandPreferences();
			wapf.setMaxRows(100);
			wapf.markRetainChkBox(false);
			wapf.selectGroupLevel("All Roles");
			wapf.applyChanges();
			wapf.collapsePreferences();
			wdt = new WasAdminDetailTable(util.webDriver);
			for (int i=0; i<wdt.rows.size(); i++) {
				String appName = wdt.getCellElementValue(i+1, 2);
				String status = wdt.getCellElementValue(i+1, 3);
				if ("Started".equalsIgnoreCase(status)) {
					successPoints ++;
					result.append("Application '" + appName + "' has been started." + System.getProperty("line.separator"));
					logger.log(LogLevel.INFO, "Application '" + appName + "' has been started.");
				} else {
					failedPoints ++;
					result.append("Application '" + appName + "' was not started." + System.getProperty("line.separator"));
					logger.log(LogLevel.ERROR, "Application '" + appName + "' was not started.");
				}
			}
			
			//Check data sources connection
			result.append(wac.navigate("Resources>JDBC>Data sources") + System.getProperty("line.separator"));
			wac.switchToDetailFrame();
			wdt = new WasAdminDetailTable(util.webDriver);
			for (String n : clusterNames) {
				WebElement selScope = driver.findElement(By.id("currentScope"));
				List<WebElement> options = selScope.findElements(By.tagName("option"));
				for (WebElement option : options) {
					if(("Cluster="+n).equalsIgnoreCase(option.getText())) {
						option.click();
						break;
					}
				}
				
				wdt.selectAll();
				wdt.clickToolButtn(By.xpath("id('button.testConnectionDataSource')"));
				WebElement messageTable = driver.findElement(By.xpath("//table[@class='messagePortlet']"));
				List<WebElement> messages = messageTable.findElements(By.xpath(".//span"));
				for (WebElement msg : messages) {
					if ("validation-warn-info".equals(msg.getAttribute("class"))) {
						successPoints ++;
						result.append(msg.getText() + System.getProperty("line.separator"));
						logger.log(LogLevel.INFO, msg.getText());
					}
					if ("validation-error".equals(msg.getAttribute("class"))) {
						failedPoints ++;
						result.append(msg.getText() + System.getProperty("line.separator"));
						logger.log(LogLevel.ERROR, msg.getText());
					}
				}
			}
			
			//Check Buses/Messsage Engines
			result.append(wac.navigate("Service integration>Buses") + System.getProperty("line.separator"));
			wac.switchToDetailFrame();
			wdt = new WasAdminDetailTable(util.webDriver);
			for (int i=0; i<wdt.rows.size(); i++) {
				wdt.clickCellByIndex(i+1, 2);
				WebElement meLink = driver.findElement(By.linkText("Messaging engines"));
				meLink.click();
				WasAdminDetailTable met = new WasAdminDetailTable(util.webDriver);
				for (int j=0; j<met.rows.size(); j++) {
					String meName = met.getCellElementValue(j+1, 2);
					String meStatus = met.getCellElementValue(j+1, 4);
					if ("Started".equalsIgnoreCase(meStatus)) {
						successPoints ++;
						result.append("Messaging engine '" + meName + "' has been started." + System.getProperty("line.separator"));
						logger.log(LogLevel.INFO, "Messaging engine '" + meName + "' has been started.");
					} else {
						failedPoints ++;
						result.append("Messaging engine '" + meName + "' was not started." + System.getProperty("line.separator"));
						logger.log(LogLevel.INFO, "Messaging engine '" + meName + "' was not started.");
					}
				}
				driver.findElement(By.xpath("id('title-bread-crumb')/a[1]")).click();
			}
			
			wac.logout();
			
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
