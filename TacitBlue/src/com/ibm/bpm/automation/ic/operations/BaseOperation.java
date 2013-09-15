/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.RegularPatterns;
import com.ibm.bpm.automation.ic.tap.ExecutionContext;
import com.ibm.bpm.automation.ic.tap.TestRobot;
import com.ibm.bpm.automation.ic.utils.AutoFileFilter;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public abstract class BaseOperation {
	
	private static final String CLASSNAME = BaseOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);
	
	private String step;
	private String name;
	private int points;
	private String action;
	private String type;
	private String option;
	private String data;
	private Date startTime;
	protected int failedPoints = 0;
	protected int successPoints = 0;
	protected boolean isFailed = false;
	protected String command;
	
	
	public void run(HashMap<String, Object> config) {
		startTime = new Date();
	}
	
	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

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
	public String getData() {
		return data;
	}
	public void setData(String propFile) {
		this.data = propFile;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getValue() {
		return data;
	}
	public void setValue(String value) {
		this.data = value;
	}
	
	
	public void submit(String result, Logger logger) {
		String state = null;
		if (failedPoints != 0) {
			state = ExecutionContext.ER_STATUS_FAILED;
			if (failedPoints < getPoints()) {
				failedPoints = getPoints();
			}
			logger.log(LogLevel.ERROR, "Step '" + getStep() + "' failed with points [" + (failedPoints + successPoints) +"]!");
		}
		else {
			state = ExecutionContext.ER_STATUS_SUCCESSFUL;
			if (successPoints == 0 || successPoints < getPoints()) {
				successPoints = getPoints();
			}
			logger.log(LogLevel.INFO, "Step '" + getStep() + "' Succeeded with points [" + successPoints +"]!");
		}
		Date endTime = new Date();
		int totalPoints = failedPoints + successPoints;
		if (null != ExecutionContext.getExecutionContext().getAutomationService()) {
			String logUrl = ExecutionContext.getExecutionContext().getAutomationService().uploadFile(System.getProperty("user.dir") 
					+ File.separator + TestRobot.ICAUTO_LOG_PATH 
					+ File.separator + LogUtil.AutoLogName);
			String resultUrl = ExecutionContext.getExecutionContext().getAutomationService().uploadString(result);
			ExecutionContext.getExecutionContext().submitExeuctionResultWithNewER(
					getStep(), totalPoints, state, successPoints, startTime, endTime, logUrl, resultUrl);
		}
	}
	
	public String analyseLogs(String foldPath, String regMatch) {
		boolean failed = false;
		StringBuffer result = new StringBuffer();
		
		File logFolder = new File(foldPath);
		
		if(!logFolder.exists() || logFolder.isFile()) {
			failed = true;
			logger.log(LogLevel.ERROR, "Can't find the folder or find a file with duplicated name. Please check it. <" + foldPath + ">");
			result.append("Can't find the folder or find a file with duplicated name. Please check it. <" + foldPath + ">" + System.getProperty("line.separator"));
			this.failedPoints++;
		}
		else {
			result.append("Start to go through the server logs under '" + foldPath + "'.");
			result.append(System.getProperty("line.separator"));
			File logFiles[] = logFolder.listFiles(new AutoFileFilter(RegularPatterns.REG_BPM_SERVER_LOG));
			for (File file : logFiles) {
				result.append(analyseLog(file, regMatch));
			}
		}
		
		return result.toString();
	}
	
	public String analyseLog(File bpmLog, String regMatch) {
		boolean failed = false;
		StringBuffer result = new StringBuffer();
		result.append("Start to validate Log file '" + bpmLog.getName() + "' ..." + System.getProperty("line.separator"));
		result.append("-----------------------------------------------------");
		result.append(System.getProperty("line.separator"));
		try {
			BufferedReader bufReader = new BufferedReader(new FileReader(bpmLog));
			String line = null;
			int lineIndex = 1;
			int foundNum = 0;
			while(null != (line = bufReader.readLine())) {
				lineIndex ++;
				Pattern ercodePattern = Pattern.compile(regMatch);
				Matcher ercodeMatcher = ercodePattern.matcher(line);
				if (ercodeMatcher.find()) {
					result.append("[Line: " + lineIndex + "] ");
					result.append(line);
					result.append(System.getProperty("line.separator"));
					foundNum ++;
					failed = true;
				}
			}
			bufReader.close();
			result.append("Total found " + foundNum + " errors, please check log file '" + bpmLog.getName() + "' for information.");
			result.append(System.getProperty("line.separator"));
			result.append(System.getProperty("line.separator"));
			
		} catch (Exception e) {
			failed = true;
			result.append("Failed to validate Log file: " + e.getMessage());
			result.append(System.getProperty("line.separator"));
		}
		//cont points depends on 'failed'
		if (failed) {
			this.failedPoints++;
		}
		else {
			this.successPoints++;
		}
		//System.out.println(result.toString());
		return result.toString();
	}
}
