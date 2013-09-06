package com.ibm.bpm.automation.ic.operations;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import com.ibm.bpm.automation.ic.LogLevel;
import com.ibm.bpm.automation.ic.constants.Configurations;
import com.ibm.bpm.automation.ic.constants.OperationParameters;
import com.ibm.bpm.automation.ic.constants.RegularPatterns;
import com.ibm.bpm.automation.ic.tap.ExecutionContext;
import com.ibm.bpm.automation.ic.utils.AutoFileFilter;
import com.ibm.bpm.automation.ic.utils.LogUtil;

public class CheckLogOperation extends BaseOperation {
	
	private static final String CLASSNAME = CheckLogOperation.class.getName();
	private static Logger logger = LogUtil.getLogger(CLASSNAME);

	@Override
	public void run(HashMap<String, Object> config) {
		super.run(config);
		logger.log(LogLevel.INFO, "Invoke operation '" + CheckLogOperation.class.getSimpleName() + "'");
		//Date startTime = new Date();
		File confLog = null;
		String confLogPath = null;
		StringBuffer result = new StringBuffer();
		
		if(OperationParameters.BPMCONFLOG.getCommand().equals(this.getType())) { // Check BPMConfig log
			if(config.containsKey(Configurations.BPMCONFLOG.getKey())) { //Just completed a BPMConfig
				confLogPath = (String)config.get(Configurations.BPMCONFLOG.getKey());
				confLog = new File(confLogPath);
			} 
			else { // Have to parse the latest one.
				String logFolderPath = config.get(Configurations.BPMPATH.getKey()) + File.separator + "logs" + File.separator + "config";
				File logFolder = new File(logFolderPath);
				File[] logs = logFolder.listFiles(new AutoFileFilter(RegularPatterns.REG_BPM_CONFIG_LOG_FILEFILTER));
				for (File file : logs) {
					if (null == confLog) {
						confLog = file;
					}
					else if (confLog.lastModified() < file.lastModified()) {
						confLog = file;
					}
				}
			}
			
			result.append(analyseLog(confLog, RegularPatterns.REG_BPM_CONFIG_ERR));
			System.out.println(result);
			
		} else if (OperationParameters.BPMLOG.getCommand().equals(this.getType())) { // Check BPM Server log
			
			String bpmLogFolder = (String)config.get(Configurations.BPMLOGFDR.getKey());			
			HashMap<String, Object> collectedPathes = (HashMap<String, Object>)config.get(Configurations.BPMLOGSAV.getKey());
			String option = this.getOption();
			
			if (null == option) {
				option = OperationParameters.BPMLOG_OPT_ALL.getOption();
			}
			if (!OperationParameters.isValid(option)) {
				//TODO submit execution result as failed.
				logger.log(LogLevel.ERROR, "The option '" + option + "' is invalid. Please check the definition in your test case.");
				result.append("The option '" + option + "' is invalid. Please check the definition in your test case.");
				result.append(System.getProperty("line.separator"));
				this.failedPoints++;
			} else {
				
				logger.log(LogLevel.INFO, "Start to collect the BPM logs from all machines ...");
				if (null != ExecutionContext.getExecutionContext().getAutomationService()) {
					ExecutionContext.getExecutionContext().getAutomationService().retriveAllLogs(
							ExecutionContext.getExecutionContext().getAutomationService().getCurrentEnvironment(), bpmLogFolder);
					logger.log(LogLevel.INFO, "Finished collecting the BPM logs from all machines.");
				}
				
				boolean execAll = (null == option || OperationParameters.BPMLOG_OPT_ALL.getOption().toLowerCase().equals(option.toLowerCase()));
				if (OperationParameters.BPMLOG_OPT_NODESERVER.getOption().equals(option) || execAll) {
					List<String> listServLogPath = (List<String>)collectedPathes.get(ExecutionContext.COLLECTED_NODE_SERVLOGPATH);
					for(Iterator<String> it = listServLogPath.iterator(); it.hasNext();) {
						result.append(analyseLogs(bpmLogFolder + File.separator + it.next(), RegularPatterns.REG_BPM_SERVER_ERR));
					}
				} 
				if (OperationParameters.BPMLOG_OPT_NODEAGENT.getOption().equals(option) || execAll) {
					List<String> listAgentLogPath = (List<String>)collectedPathes.get(ExecutionContext.COLLECTED_NODE_AGENTLOGPATH);
					for (Iterator<String> it = listAgentLogPath.iterator(); it.hasNext();) {
						result.append(analyseLogs(bpmLogFolder + File.separator + it.next(), RegularPatterns.REG_BPM_SERVER_ERR));
					}
				} 
				if (OperationParameters.BPMLOG_OPT_NODEFFDC.getOption().equals(option) || execAll) {
					//TODO How to verify the FFDC, is it necessary?
				}
				if (OperationParameters.BPMLOG_OPT_DMGRSERVER.getOption().equals(option) || execAll) {
					result.append(analyseLogs(bpmLogFolder + File.separator + (String)collectedPathes.get(ExecutionContext.COLLECTED_DMGR_SERVLOGPATH), RegularPatterns.REG_BPM_SERVER_ERR));
				}
				if (OperationParameters.BPMLOG_OPT_DMGRFFDC.getOption().equals(option) || execAll) {
					//TODO How to verify the FFDC, is it necessary?
				}
			}
			System.out.println(result);
		} else { //Wrong types?
			logger.log(LogLevel.ERROR, "Unrecognized analysis file type found: '" + this.getType() + "'.");
			result.append("Unrecognized analysis file type found: '" + this.getType() + "'." + System.getProperty("line.separator"));
			this.failedPoints++;
		}
		
		submit(result.toString(), logger);
		
	}
}
