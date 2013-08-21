package com.ibm.bpm.automation.ic.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.ibm.bpm.automation.ic.ActionMapping;
import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.TestCase;
import com.ibm.bpm.automation.ic.actions.Action;

public class XMLHandler extends DefaultHandler {

	private TestCase testCase;
	private StringBuffer textBuffer;
	private boolean bActionElementStart;
	private HashMap<String, String> actionInfo;
	private HashMap<String, Object> actionMapping;
	
	public XMLHandler(String filename, HashMap<String, Object> configuration) throws AutoException{
		
		testCase = new TestCase();
		
		actionMapping = ActionMapping.getMapping();
		
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
		} else if (TestCase.TESTCASE_ACTION.equals(elemntName)) {
			bActionElementStart = false;
		} else if (TestCase.TESTCASE_ACTION_NAME.equals(elemntName)) {
			
		}
		
		System.out.println("...");
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		String elemntName = localName;
		
		if (TestCase.TESTCASE_ACTION.equals(elemntName)) {
			bActionElementStart = true;
			if(actionInfo != null) {
				actionInfo.clear();
			} else {
				actionInfo = new HashMap<String, String>();
			}
		}
		
		//Action anAction = new Action();

		System.out.println("Find element 'Local Name':"+ localName + ", 'QName':" + qName);
	}

	public TestCase getTestCase() {
		return testCase;
	}
	
	
	
	
}
