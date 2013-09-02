package com.ibm.bpm.automation.ic.test;

import java.io.File;
import java.util.List;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.TestCase;
import com.ibm.bpm.automation.ic.TestCaseLoader;
import com.ibm.bpm.automation.ic.tap.TestRobot;
import com.ibm.bpm.automation.ic.utils.LogUtil;
import com.ibm.bpm.automation.ic.utils.XMLHandler;
import com.ibm.bpm.automation.tap.adapter.AutomationService;

public class TestExecution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(System.getProperty("user.dir"));
		/*try {
			LogUtil.init(System.getProperty("user.dir") + File.separator + ICAutoRobot.ICAUTO_LOG_PATH);
			//XMLHandler xmlHnd = new XMLHandler("./test/SampleCase.xml", null);
			//TestCase tstCase = xmlHnd.getTestCase();
			//System.out.println("Case Name:" + tstCase.getTitle() + "\nDescription:" + tstCase.getDescription());
			
			List<TestCase> caseList = TestCaseLoader.loadTestCases(System.getProperty("user.dir") 
											+ File.separator 
											+ ICAutoRobot.ICAUTO_TESTCASE_PATH);
			
			for (int i=0; i<caseList.size(); i++) {
				System.out.println(caseList.get(i).getTitle());
			}
			
		} catch (AutoException e) {
			System.out.println(e.getMessage());
		}*/
		
		TestRobot icAutoRobot = new TestRobot();
		
		icAutoRobot.start(null);
		
	}

}
