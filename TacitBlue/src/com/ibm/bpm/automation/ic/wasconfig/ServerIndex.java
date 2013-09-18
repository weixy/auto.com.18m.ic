/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.wasconfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.bpm.automation.ic.utils.ConfigXMLHandler;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class ServerIndex extends XWASConfig {
	
	private static final String CLASSNAME = ServerIndex.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	public static String FILE_SERVERINDEX = "serverindex.xml";
	public static String TAG_SPECENDP = "specialEndpoints";
	public static String ATTR_SPECENDP_WSDEFAULT = "WC_defaulthost";
	public static String ATTR_SPECENDP_WSDEFAULT_SECURE = "WC_defaulthost_secure";
	public static String TAG_SPECENDP_ENDPOINT = "endPoint";
	public static String ATTR_SPECENDP_ENDPOINT_PORT = "port";
	public static String XPATH_WCDEFHOST = "//specialEndpoints[@endPointName='WC_defaulthost']/endPoint";
	public static String XPATH_WCDEFHOST_SECURE = "//specialEndpoints[@endPointName='WC_defaulthost_secure']/endPoint";
	
	public static String getWCDefaultHostPort(String fileName, String xPathExpress) throws ParserConfigurationException, 
	IOException, SAXException, XPathExpressionException {
		String port = null;
		Node node = getMatchedNode(fileName, xPathExpress).item(0);
		XPath xpath = XPathFactory.newInstance().newXPath();
		port = (String)xpath.evaluate("@port", node, XPathConstants.STRING);
		return port;
	}
}
