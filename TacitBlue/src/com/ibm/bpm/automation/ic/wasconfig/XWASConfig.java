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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.bpm.automation.ic.utils.ConfigXMLHandler;

public class XWASConfig {
	
	public static NodeList getMatchedNode(String fileName, String xPathExpress) throws ParserConfigurationException, 
	IOException, SAXException, XPathExpressionException {
		ConfigXMLHandler handler = new ConfigXMLHandler(fileName, xPathExpress);
		NodeList nodes = handler.evaluate();
		
		return nodes;
	}

}
