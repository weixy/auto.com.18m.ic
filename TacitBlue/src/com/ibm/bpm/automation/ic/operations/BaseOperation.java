package com.ibm.bpm.automation.ic.operations;

import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.AutoException;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public abstract class BaseOperation {
	
	private String name;
	private int points;
	private String action;
	private String type;
	private String option;
	private String propFile;
	
	
	public abstract void run() throws AutoException;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPropFile() {
		return propFile;
	}
	public void setPropFile(String propFile) {
		this.propFile = propFile;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getValue() {
		return propFile;
	}
	public void setValue(String value) {
		this.propFile = value;
	}	
}
