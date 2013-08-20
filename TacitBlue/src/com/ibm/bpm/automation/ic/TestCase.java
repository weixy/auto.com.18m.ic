package com.ibm.bpm.automation.ic;

import java.util.ArrayList;
import java.util.List;

import com.ibm.bpm.automation.ic.actions.Action;

public class TestCase {

	public static final String TESTCASE_TITLE  = "title";
	public static final String TESTCASE_DESCRIPTION = "description";
	public static final String TESTCASE_ACTION = "action";
	public static final String TESTCASE_ACTION_NAME = "name";
	public static final String TESTCASE_ACTION_OPTION = "option";
	public static final String TESTCASE_ACTION_VALUE = "value";
	
	private String title;
	private String description;
	private List<Action> actions = new ArrayList<Action>();
	
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
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void exec() {
		
	}
}
