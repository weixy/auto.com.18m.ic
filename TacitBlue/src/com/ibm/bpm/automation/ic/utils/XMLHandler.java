package com.ibm.bpm.automation.ic.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.ibm.bpm.automation.ic.OperationMapping;
import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.TestCase;
import com.ibm.bpm.automation.ic.operations.BaseOperation;

public class XMLHandler extends DefaultHandler {

	private TestCase testCase;
	private StringBuffer textBuffer;
	private boolean bActionElementStart;
	private HashMap<String, String> operationInfo;
	private HashMap<String, String> operationMapping;
	
	public XMLHandler(String filename, HashMap<String, Object> configuration) throws AutoException{
		
		testCase = new TestCase();
		
		operationMapping = OperationMapping.getMapping();
		
		XMLReader reader;
		// Initialize reader
		try {
			 reader = XMLReaderFactory.createXMLReader();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new AutoException("Can't create XMLReader", e);
		}
		reader.setContentHandler(this);
		reader.setErrorHandler(this);
		
		try {
			reader.parse(filename);
		} catch (FileNotFoundException e) {
			throw new AutoException("Can't find the file you specified", e);
		} catch (IOException e) {
			throw new AutoException("Got File IO Exception", e);
		} catch (SAXException e) {
			throw new AutoException("Got SAX Exception", e);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		System.out.println("Finished parsing XML ...");
		List<BaseOperation> list = testCase.getOperations();
		for(int i=0; i < list.size(); i++) {
			BaseOperation it = list.get(i);
			System.out.print(">" + it.getName());
			System.out.print(">" + it.getPoints());
			System.out.print(">" + it.getAction());
			System.out.print(">" + it.getType());
			System.out.print(">" + it.getOption());
			System.out.print(">" + it.getPropFile() + "\n");
		}
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		System.out.println("Start to parse XML ...");
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
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
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
				|| TestCase.TESTCASE_OPERATION_PROPFILE.equals(elemntName)) {
			operationInfo.put(elemntName, txt);
		} else if (TestCase.TESTCASE_OPERATION.equals(elemntName)) {
			bActionElementStart = false;
			String operationName = operationInfo.get(TestCase.TESTCASE_OPERATION_NAME);
			String operationClassName = operationMapping.get(operationName);
			try {
				Class<?> aClass = Class.forName(operationClassName);
				Object anObj = aClass.newInstance();
				if(anObj instanceof BaseOperation) {
					//hard-code here, if BaseOperation add/remove sub-elements, please modify following codes correspondingly.
					((BaseOperation) anObj).setName(operationInfo.get(TestCase.TESTCASE_OPERATION_NAME));
					
					String points = operationInfo.get(TestCase.TESTCASE_OPERATION_POINTS);
					((BaseOperation) anObj).setPoints(Integer.parseInt(points!=null?points:"0"));
					
					((BaseOperation) anObj).setAction(operationInfo.get(TestCase.TESTCASE_OPERATION_ACTION));
					((BaseOperation) anObj).setType(operationInfo.get(TestCase.TESTCASE_OPERATION_TYPE));
					((BaseOperation) anObj).setOption(operationInfo.get(TestCase.TESTCASE_OPERATION_OPTION));
					((BaseOperation) anObj).setPropFile(operationInfo.get(TestCase.TESTCASE_OPERATION_PROPFILE));
					
					testCase.addOperations((BaseOperation)anObj);
				} else {
					//Error
				}
				
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("...");
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
		}
		
		System.out.println("Find element 'Local Name':"+ localName + ", 'QName':" + qName);
	}

	public TestCase getTestCase() {
		return testCase;
	}
	
	
	
	
}
