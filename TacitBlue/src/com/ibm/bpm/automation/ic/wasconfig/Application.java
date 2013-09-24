/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.wasconfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.utils.AutoFileFilter;
import com.ibm.bpm.automation.ic.utils.LogUtil;


public class Application extends XWASConfig {
	
	private static final String CLASSNAME = Application.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	public static String FILE_APPLICATION = "application.xml";
	public static String TAG_APPLICATION_MODULE = "module";
	public static String TAG_APPLICATION_WEB = "web";
	public static String TAG_APPLICATION_CTXROOT = "context-root";
	
	public static String XPATH_WEBMODULE = "//module/web";
	
	public static String PROCESS_CENTER_APPNAME_PREFIX = "IBM_BPM_Repository";
	public static String PROCESS_CENTER_WEBMODULE_NAME = "Repository";
	
	public static HashMap<String, String> getWebModuleCtxRoot(String fileName) throws ParserConfigurationException, 
	IOException, SAXException, XPathExpressionException {
		HashMap<String, String> map = new HashMap<String, String>();
		NodeList nodes = getMatchedNode(fileName, XPATH_WEBMODULE);
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			XPath xpath = XPathFactory.newInstance().newXPath();
			String weburi = (String)xpath.evaluate("web-uri", node, XPathConstants.STRING);
			String ctxroot = (String)xpath.evaluate("context-root", node, XPathConstants.STRING);
			File app = new File(fileName);
			String webFileName = app.getParent() + File.separator + ".." + File.separator + weburi + File.separator + "WEB-INF" + File.separator + Web.FILE_WEB;
			String webModule = Web.getWebModuleName(webFileName, Web.XPATH_DISPNAME);
			if (webModule == null) {
				webModule = weburi;
			}
			map.put(webModule, ctxroot);
		}
		return map;
	}
	
	public static String getAppFolderRootPath(HashMap<String, Object> config) {
		String path = config.get(Configurations.BPMPATH.getKey()) + File.separator
				+ "profiles" + File.separator
				+ config.get(Configurations.DMGRPROF.getKey()) + File.separator
				+ "config" + File.separator + "cells" + File.separator
				+ config.get(Configurations.CELLNAME.getKey()) + File.separator
				+ "applications";
		
		return path;
	}
	
	public static String getApplicationXMLPath(HashMap<String, Object> config, String appNamePrefix) {
		String appFolderPath = getAppFolderRootPath(config);
		HashMap<String, String> apps = getInstalledAppList(appFolderPath, (String)config.get(Configurations.APPCLUSTER.getKey()));
		Set<String> names = apps.keySet();
		String appName = null;
		
		for (String name : names) {
			if (name.startsWith(appNamePrefix)) {
				appName = name;
				break;
			}
		}
		String appFilePath = appFolderPath + File.separator
				+ appName + File.separator
				+ "deployments" + File.separator
				+ appName.replace(".ear", "") + File.separator
				+ "META-INF" + File.separator
				+ "application.xml";
		return appFilePath;
	}
	
	public static HashMap<String, String> getInstalledAppList(String appFolderPath, String clusterName) {
		HashMap<String, String> apps = new HashMap<String, String>();
		File appFolder = new File(appFolderPath);
		if (appFolder.isDirectory()) {
			//Filter apps which has no cluster name as suffix, they should be cell scope apps.
			File [] subFolders = appFolder.listFiles(new AutoFileFilter("_" + clusterName + "\\.ear"));
			for (File d : subFolders) {
				if (d.isDirectory()) {
					apps.put(d.getName(), "-");
				}
			}
			return apps;
		}
		else {
			logger.log(LogLevel.ERROR, "The specified applications folder is a file, '" + appFolderPath + "'");
			return null;
		}
	}
	
}
