/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.tap.ExecutionContext;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class SeRuntimeOptions {
	private static final String CLASSNAME = SeRuntimeOptions.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	private static SeRuntimeOptions runtimeOptions;
	
	public static String OPTION_BROWSER = "Browser";
	public static String OPTION_FIREFOXPATH = "FirefoxBin";
	public static String OPTION_FIREFOXPROFILE = "FirefoxProfile";
	
	private WebDriver webDriver;
	private String ip;
	private String host;
	private String port;
	private String securePort;
	private SeRuntimeOptions() {}
	
	public WebDriver getWebDriver() {	
		return webDriver;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSecurePort() {
		return securePort;
	}

	public void setSecurePort(String securePort) {
		this.securePort = securePort;
	}

	public static SeRuntimeOptions getRuntimeOptions() {
		logger.log(LogLevel.INFO, "Load default runtime options for selenium.");
		if (null == runtimeOptions) {
			synchronized(SeRuntimeOptions.class) {
				runtimeOptions = new SeRuntimeOptions();
				runtimeOptions.webDriver = new FirefoxDriver();
			}
		}
		return runtimeOptions;
	}
	
	public static SeRuntimeOptions getRuntimeOptions(String optFilePath) throws AutoException {
		logger.log(LogLevel.INFO, "Load the specified options file '" + optFilePath + "'.");
		if (null == runtimeOptions) {
			synchronized(SeRuntimeOptions.class) {
				try {
					runtimeOptions = loadRuntimeOptions(optFilePath);
				} catch (Exception e) {
					throw new AutoException(e);
				}
			}
		}
		return runtimeOptions;
	}
	
	private static SeRuntimeOptions loadRuntimeOptions(String optFilePath) throws Exception {
		SeRuntimeOptions runOptions = new SeRuntimeOptions();
		Properties prop = new Properties();
		InputStream ins = new BufferedInputStream(new FileInputStream(optFilePath));
		prop.load(ins);
		String browserType = prop.getProperty(OPTION_BROWSER);
		if (Browsers.IE.getName().equalsIgnoreCase(browserType)) {
			runOptions.webDriver = new InternetExplorerDriver();
		} else if (Browsers.CHROME.getName().equalsIgnoreCase(browserType)) {
			runOptions.webDriver = new ChromeDriver();
		} else if (Browsers.SAFARI.getName().equalsIgnoreCase(browserType)) {
			runOptions.webDriver = new SafariDriver();
		} else if (Browsers.FF.getName().equalsIgnoreCase(browserType)) {
			String ffBinPath = prop.getProperty(OPTION_FIREFOXPATH);
			String ffProPath = prop.getProperty(OPTION_FIREFOXPROFILE);
			File proFile = new File(ffProPath);
			FirefoxProfile profile = null;
			if (proFile.exists()) {
				profile = new FirefoxProfile(proFile);
				profile.setEnableNativeEvents(true);
			}
			
			if (null != ffBinPath) {
				FirefoxBinary binary = new FirefoxBinary(new File(ffBinPath));
				runOptions.webDriver = new FirefoxDriver(binary, profile);
			} else {
				runOptions.webDriver = new FirefoxDriver(profile);
			}
		} else {
			throw new AutoException("Unknown browser type '" + browserType + "' specified.");
		}
		return runOptions;
	}
	
}
