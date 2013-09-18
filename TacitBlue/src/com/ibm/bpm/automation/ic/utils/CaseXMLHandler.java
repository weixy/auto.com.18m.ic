/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.OperationMapping;
import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.TestCase;
import com.ibm.bpm.automation.ic.operations.BaseOperation;

public class CaseXMLHandler extends DefaultHandler {
	
	private static final String CLASSNAME = CaseXMLHandler.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	private TestCase testCase;
	private String fileName;
	private StringBuffer textBuffer;
	private boolean bActionElementStart;
	private HashMap<String, String> operationInfo;
	private HashMap<String, String> operationMapping;
	
	public CaseXMLHandler(String filename) throws AutoException{
		
		testCase = new TestCase();
		fileName = filename;
		
		operationMapping = OperationMapping.getMapping();
		
		XMLReader reader;
		// Initialize reader
		try {
			 reader = XMLReaderFactory.createXMLReader();
		} catch (Exception e) {
			throw new AutoException("Can't create XMLReader", e);
		}
		reader.setContentHandler(this);
		reader.setErrorHandler(this);
		
		try {
			reader.parse(filename);
		} catch (FileNotFoundException e) {
			logger.log(LogLevel.ERROR, "Can't find the file '" + filename + "'", e);
			testCase = null;
		} catch (IOException e) {
			logger.log(LogLevel.ERROR, "Got IO Exception while open file '" + filename + "'", e);
			testCase = null;
		} catch (SAXException e) {
			logger.log(LogLevel.ERROR, "Got SAX Exception while parsing file '" + filename + "'", e);
			testCase = null;
		}
	}

	@Override
	public void endDocument() throws SAXException {
		logger.log(LogLevel.INFO, "Finished parsing test case '" + testCase.getTitle() + "' (" + fileName + ").");
	}

	@Override
	public void startDocument() throws SAXException {
		logger.log(LogLevel.INFO, "Start to parse test case from file '" + fileName + "'");
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length);
		if(null == textBuffer) {
			textBuffer = new StringBuffer(s);
		} else {
			textBuffer.append(s);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		String elemntName = localName;
		
		String txt = (" " + textBuffer).trim();
		textBuffer = null;
		
		if (TestCase.TESTCASE_TITLE.equals(elemntName)) {
			testCase.setTitle(txt);
		} else if (TestCase.TESTCASE_DESCRIPTION.equals(elemntName)) {
			testCase.setDescription(txt);
		} else if (TestCase.TESTCASE_OPERATION_NAME.equals(elemntName) 
				|| TestCase.TESTCASE_OPERATION_ACTION.equals(elemntName)
				|| TestCase.TESTCASE_OPERATION_POINTS.equals(elemntName)
				|| TestCase.TESTCASE_OPERATION_TYPE.equals(elemntName)
				|| TestCase.TESTCASE_OPERATION_OPTION.equals(elemntName)
				|| TestCase.TESTCASE_OPERATION_DATA.equals(elemntName)) {
			operationInfo.put(elemntName, txt);
		} else if (TestCase.TESTCASE_OPERATION_SE_HOST.equals(elemntName)
				|| TestCase.TESTCASE_OPERATION_SE_PORT.equals(elemntName)
				|| TestCase.TESTCASE_OPERATION_SE_SECUPORT.equals(elemntName)
				|| TestCase.TESTCASE_OPERATION_SE_CTXROOT.equals(elemntName)
				|| TestCase.TESTCASE_OPERATION_SE_CHECKALLNODE.equals(elemntName)) {
			operationInfo.put(elemntName, txt);
		} else if (TestCase.TESTCASE_OPERATION.equals(elemntName)) {
			bActionElementStart = false;
			String operationName = operationInfo.get(TestCase.TESTCASE_OPERATION_NAME);
			String operationClassName = operationMapping.get(operationName);
			//System.out.println(operationClassName);
			//System.out.println(operationInfo.get(TestCase.TESTCASE_OPERATION_ACTION));
			if (null == operationClassName) {
				logger.log(LogLevel.ERROR, "Failed to find the corresponding class name for operation '" + operationName + "'.");
			} else {
				try {
					Class<?> aClass = Class.forName(operationClassName);
					Object anObj = aClass.newInstance();
					if(anObj instanceof BaseOperation) {
						//hard-code here, if BaseOperation add/remove sub-elements, please modify following codes correspondingly.
						((BaseOperation) anObj).setName(operationInfo.get(TestCase.TESTCASE_OPERATION_NAME));
						
						String points = operationInfo.get(TestCase.TESTCASE_OPERATION_POINTS);
						((BaseOperation) anObj).setPoints(Integer.parseInt(points!=null?points:"0"));
						
						((BaseOperation) anObj).setStep(operationInfo.get(TestCase.TESTCASE_OPERATION_STEP));
						((BaseOperation) anObj).setAction(operationInfo.get(TestCase.TESTCASE_OPERATION_ACTION));
						((BaseOperation) anObj).setType(operationInfo.get(TestCase.TESTCASE_OPERATION_TYPE));
						((BaseOperation) anObj).setOption(operationInfo.get(TestCase.TESTCASE_OPERATION_OPTION));
						((BaseOperation) anObj).setData(operationInfo.get(TestCase.TESTCASE_OPERATION_DATA));
						((BaseOperation) anObj).setHost(operationInfo.get(TestCase.TESTCASE_OPERATION_SE_HOST));
						((BaseOperation) anObj).setPort(operationInfo.get(TestCase.TESTCASE_OPERATION_SE_PORT));
						((BaseOperation) anObj).setSecurePort(operationInfo.get(TestCase.TESTCASE_OPERATION_SE_SECUPORT));
						((BaseOperation) anObj).setContextRoot(operationInfo.get(TestCase.TESTCASE_OPERATION_SE_CTXROOT));
						String chkAllNodes = operationInfo.get(TestCase.TESTCASE_OPERATION_SE_CHECKALLNODE);
						chkAllNodes = (chkAllNodes == null) ? "false" : chkAllNodes;
						((BaseOperation) anObj).setCheckAllNodes(Boolean.parseBoolean(chkAllNodes));
						testCase.addOperations((BaseOperation)anObj);
					} else {
						//Error
					}
					
					
				} catch (ClassNotFoundException e) {
					logger.log(LogLevel.ERROR, "Class '" + operationClassName + "' can't be resolved.", e);
				} catch (InstantiationException e) {
					logger.log(LogLevel.ERROR, "Can't create an instance for class '" + operationClassName + "'.", e);
				} catch (IllegalAccessException e) {
					logger.log(LogLevel.ERROR, "Has no legal access while instantating class '" + operationClassName + "'", e);
				}
			}
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		String elemntName = localName;
		
		if (TestCase.TESTCASE_OPERATION.equals(elemntName)) {
			bActionElementStart = true;
			if(operationInfo != null) {
				operationInfo.clear();
			} else {
				operationInfo = new HashMap<String, String>();
			}
			for (int i=0; i<attributes.getLength(); i++) {
				if (TestCase.TESTCASE_OPERATION_STEP.equals(attributes.getLocalName(i))) {
					operationInfo.put(TestCase.TESTCASE_OPERATION_STEP, attributes.getValue(i));
					break;
				}
			}
		}
		
		//System.out.println("Find element 'Local Name':"+ localName + ", 'QName':" + qName);
	}

	public TestCase getTestCase() {
		return testCase;
	}
	
	
	
	
}
