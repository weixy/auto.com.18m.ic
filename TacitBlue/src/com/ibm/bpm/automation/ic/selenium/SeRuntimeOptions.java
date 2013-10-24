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
import java.util.List;
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
	public static String OPTION_USESECURITY = "UseSecurity";
	public static String OPTION_HOST = "Host";
	public static String OPTION_PORT = "Port";
	public static String OPTION_SEPORT = "SecurePort";
	public static String OPTION_ADMIN_PORT = "AdminPort";
	public static String OPTION_ADMIN_SEPORT = "SecureAdminPort";
	public static String OPTION_CTXRTPRE = "ContextRootPrefix";
	
	public static String OPTION_USESECURITY_DEFAULT = "true";
	public static String OPTION_HOST_DEFAULT = "localhost";
	public static String OPTION_PORT_DEFAULT = "9080";
	public static String OPTION_SEPORT_DEFAULT = "9443";
	public static String OPTION_ADMIN_PORT_DEFAULT = "9060";
	public static String OPTION_ADMIN_SEPORT_DEFAULT = "9044";
	public static String OPTION_CTXRTPRE_DEFAULT = null;
	
	private WebDriver webDriver;
	private String ip;
	private String host;
	private String port;
	private String securePort;
	private String adminPort;
	private String secureAdminPort;
	private boolean useSecurity;
	private String ctxRoot;
	private String loginUserName;
	private String loginUserPwd;
	
	private List<String> clusterNames;
	
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

	public String getAdminPort() {
		return adminPort;
	}

	public void setAdminPort(String adminPort) {
		this.adminPort = adminPort;
	}

	public String getSecureAdminPort() {
		return secureAdminPort;
	}

	public void setSecureAdminPort(String secureAdminPort) {
		this.secureAdminPort = secureAdminPort;
	}

	public boolean isUseSecurity() {
		return useSecurity;
	}

	public void setUseSecurity(boolean useSecurity) {
		this.useSecurity = useSecurity;
	}

	public String getCtxRoot() {
		return ctxRoot;
	}

	public void setCtxRoot(String ctxRoot) {
		this.ctxRoot = ctxRoot;
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public String getLoginUserPwd() {
		return loginUserPwd;
	}

	public void setLoginUserPwd(String loginUserPwd) {
		this.loginUserPwd = loginUserPwd;
	}

	public List<String> getClusterNames() {
		return clusterNames;
	}

	public void setClusterNames(List<String> clusterNames) {
		this.clusterNames = clusterNames;
	}

	public static SeRuntimeOptions getRuntimeOptions() {
		logger.log(LogLevel.INFO, "Load default runtime options for selenium.");
		if (null == runtimeOptions) {
			synchronized(SeRuntimeOptions.class) {
				runtimeOptions = new SeRuntimeOptions();
				try {
					runtimeOptions.webDriver = new FirefoxDriver();
					runtimeOptions.useSecurity = true;
					runtimeOptions.host = OPTION_HOST_DEFAULT;
					runtimeOptions.port = OPTION_PORT_DEFAULT;
					runtimeOptions.securePort = OPTION_SEPORT_DEFAULT;
					runtimeOptions.adminPort = OPTION_ADMIN_PORT_DEFAULT;
					runtimeOptions.secureAdminPort = OPTION_ADMIN_SEPORT_DEFAULT;
					runtimeOptions.ctxRoot = OPTION_CTXRTPRE_DEFAULT;
				} catch (Exception e) {
					runtimeOptions = null;
					logger.log(LogLevel.ERROR, "Failed to create a web driver.", e);
				}
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
			String ffProPath = prop.getProperty(OPTION_FIREFOXPROFILE);
			FirefoxProfile profile = null;
			if (null != ffProPath) {
				File proFile = new File(ffProPath);
				if (proFile.exists()) {
					profile = new FirefoxProfile(proFile);
					profile.setEnableNativeEvents(true);
				}
			} else {
				profile = new FirefoxProfile();
				profile.setEnableNativeEvents(true);
			}
			
			String ffBinPath = prop.getProperty(OPTION_FIREFOXPATH);
			if (null != ffBinPath) {
				//System.setProperty("webdriver.firefox.bin", "F:/Program Files (x86)/Mozilla Firefox/firefox.exe");
				FirefoxBinary binary = new FirefoxBinary(new File(ffBinPath));
				runOptions.webDriver = new FirefoxDriver(binary, profile);
			} else {
				runOptions.webDriver = new FirefoxDriver(profile);
			}
		} else {
			throw new AutoException("Unknown browser type '" + browserType + "' specified.");
		}
		
		String useSec = prop.getProperty(OPTION_USESECURITY);
		runOptions.setUseSecurity(("true".equalsIgnoreCase(useSec)) ? true : false);
		
		String host = prop.getProperty(OPTION_HOST);
		runOptions.setHost((host != null) ? host : OPTION_HOST_DEFAULT);
		
		String port = prop.getProperty(OPTION_PORT);
		runOptions.setPort((port != null) ? port : OPTION_PORT_DEFAULT);
		
		String sePort = prop.getProperty(OPTION_SEPORT);
		runOptions.setSecurePort((sePort != null) ? sePort : OPTION_SEPORT_DEFAULT);
		
		String adminPort = prop.getProperty(OPTION_ADMIN_PORT);
		runOptions.setAdminPort((adminPort != null) ? adminPort : OPTION_ADMIN_PORT_DEFAULT);
		
		String seAdminPort = prop.getProperty(OPTION_ADMIN_SEPORT);
		runOptions.setSecureAdminPort((seAdminPort != null) ? seAdminPort : OPTION_ADMIN_SEPORT_DEFAULT);
		
		String ctxRootPre = prop.getProperty(OPTION_CTXRTPRE);
		runOptions.setCtxRoot((ctxRootPre != null) ? ctxRootPre : OPTION_CTXRTPRE_DEFAULT);
		
		return runOptions;
	}
	
}
