package com.ibm.bpm.automation.ic;

import java.util.ArrayList;
import java.util.List;

import com.ibm.bpm.automation.ic.operations.BaseOperation;

public class TestCase {

	public static final String TESTCASE_TITLE  = "title";
	public static final String TESTCASE_DESCRIPTION = "description";
	public static final String TESTCASE_OPERATION = "operation";
	public static final String TESTCASE_OPERATION_NAME = "name";
	public static final String TESTCASE_OPERATION_POINTS = "points";
	public static final String TESTCASE_OPERATION_ACTION = "action";
	public static final String TESTCASE_OPERATION_TYPE = "type";
	public static final String TESTCASE_OPERATION_OPTION = "option";
	public static final String TESTCASE_OPERATION_PROPFILE = "propertiesFile";
	
	private String title;
	private String description;
	private List<BaseOperation> operations = new ArrayList<BaseOperation>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<BaseOperation> getOperations() {
		return operations;
	}
	public void setOperations(List<BaseOperation> opers) {
		operations = opers;
	}
	
	public void addOperations(BaseOperation oper) {
		operations.add(oper);
	}
		
}
