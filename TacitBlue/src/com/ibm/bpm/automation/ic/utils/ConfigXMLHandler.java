/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class ConfigXMLHandler {
	
	private static final String CLASSNAME = ConfigXMLHandler.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	private String xpathString;
	private String filename;
	
	public ConfigXMLHandler(String filename, String xpath) {
		this.xpathString = xpath;
		this.filename = filename;
	}
	
	public NodeList evaluate() throws ParserConfigurationException, 
	IOException, SAXException, XPathExpressionException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(false);
		domFactory.setValidating(false);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		builder.setEntityResolver(
				new EntityResolver() {
					 public InputSource resolveEntity(String publicId, String systemId)   
							 throws SAXException, IOException {
						 return new InputSource(new StringReader(""));
					 }
				}
		);
		Document doc = builder.parse(filename);
		
		XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    XPathExpression expr = xpath.compile(xpathString);
	    
	    Object result = expr.evaluate(doc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) result;
	    
	    return nodes;
	}
	

}
