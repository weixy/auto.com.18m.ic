package com.ibm.bpm.automation.ic.test;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.utils.XMLHandler;

public class TestExecution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			XMLHandler xmlHnd = new XMLHandler("./src/com/ibm/bpm/automation/ic/test/SampleCase.xml", null);
		} catch (AutoException e) {
			System.out.println(e.getMessage());
		}
		
	}

}
