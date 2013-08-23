package com.ibm.bpm.automation.ic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.bpm.automation.ic.utils.XMLHandler;

public class TestCaseLoader {
	
	private TestCaseLoader() {
		
	}

	public static TestCase loadTestCase (String filename) throws AutoException {
		XMLHandler xmlHnd = new XMLHandler(filename, null);
		return xmlHnd.getTestCase();
	}
	
	public static List<TestCase> loadTestCases (String folderPath) throws AutoException{
		List<TestCase> caseList = new ArrayList<TestCase>();
		
		File testFolder = new File(folderPath);
		
		if (!testFolder.exists()) {
			throw new AutoException("File '" + testFolder.getAbsolutePath() + "' does not exist, please check it!");
		}
		if (testFolder.isFile()) {
			throw new AutoException("The '" + testFolder.getAbsolutePath() + "' is not a directory but a file, please check it!");
		}
		if (testFolder.isDirectory()) {
			File indexFile = new File(testFolder.getAbsolutePath() + File.separator + "index.txt");
			
			if (indexFile == null || !indexFile.exists()) { // no index file, then load the cases without specific order
				for (File item : testFolder.listFiles()) {
					if (item.isFile() && item.getAbsolutePath().endsWith(".xml")) {
						TestCase testCase = loadTestCase(item.getAbsolutePath());
						if (null != testCase) {
							caseList.add(testCase);
						}
					}
				}
			}
			else { // has index, load case by index file.
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(indexFile);
					br = new BufferedReader(fr);
					
					String line = null;
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (!line.startsWith("#") && !line.startsWith("//") && !line.equals("")) {
							String casePath = testFolder.getAbsolutePath() + File.separator + (line.endsWith(".xml")?line:line+".xml");
							TestCase testCase = loadTestCase(casePath);
							if (null != testCase) {
								caseList.add(testCase);
							}
						}
					}
					
				} catch (FileNotFoundException e) {
					throw new AutoException(e);
				} catch (IOException e) {
					throw new AutoException(e);
				} finally {
					try {
						br.close();
						fr.close();
					} catch (IOException e) {
						throw new AutoException(e);
					}
				}
			}
			
		}
		
		return caseList;
	}
	
}
