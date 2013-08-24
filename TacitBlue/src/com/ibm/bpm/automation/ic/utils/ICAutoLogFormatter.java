package com.ibm.bpm.automation.ic.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ICAutoLogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(MessageFormat.format("{0,date,MM/dd/yyyy} {0, time,HH:mm:ss zzz}", 
				new Object[] {new Date(record.getMillis())})).append("]\t");
		sb.append(record.getSourceClassName()).append("\t");
		sb.append(record.getLevel().getName().charAt(0)).append("\t");
		sb.append(record.getMessage()).append(System.getProperty("line.separator"));
		//if(Level.SEVERE.equals(record.getLevel())){
        //	sb.append(System.getProperty("line.separator"));
        //}
		//sb.append(formatMessage(record)).append(System.getProperty("line.separator"));
		
		if (null != record.getThrown()) {
			Throwable thr = record.getThrown();
			PrintWriter pwt = null;
			try {
                StringWriter sw = new StringWriter();
                pwt = new PrintWriter(sw);
                thr.printStackTrace(pwt);
                sb.append(sw.toString());
            } finally {
                if (pwt != null) {
                    try {
                        pwt.close();
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
		}
		
		return sb.toString();
	}

}
