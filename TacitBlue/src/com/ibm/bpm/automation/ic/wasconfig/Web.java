/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.wasconfig;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Web extends XWASConfig {
	public static String FILE_WEB = "web.xml";
	public static String TAG_WEB_DISPNAME = "display-name";
	
	public static String XPATH_DISPNAME = "//display-name";
	
	public static String getWebModuleName(String fileName, String xPathExpress) throws ParserConfigurationException, 
	IOException, SAXException, XPathExpressionException {
		String name = null;
		Node node = getMatchedNode(fileName, xPathExpress).item(0);
		if (node != null) {
			name = node.getTextContent();
		}
		return name;
	}
	
}
