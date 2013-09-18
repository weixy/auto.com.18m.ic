/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.wasconfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Application extends XWASConfig {

	public static String FILE_APPLICATION = "application.xml";
	public static String TAG_APPLICATION_MODULE = "module";
	public static String TAG_APPLICATION_WEB = "web";
	public static String TAG_APPLICATION_CTXROOT = "context-root";
	
	public static String XPATH_WEBMODULE = "//module/web";
	
	public static HashMap<String, String> getWebModuleCtxRoot(String fileName, String xPathExpress) throws ParserConfigurationException, 
	IOException, SAXException, XPathExpressionException {
		HashMap<String, String> map = new HashMap<String, String>();
		NodeList nodes = getMatchedNode(fileName, xPathExpress);
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
	
}
