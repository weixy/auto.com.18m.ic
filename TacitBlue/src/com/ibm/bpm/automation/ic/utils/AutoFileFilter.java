/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoFileFilter implements FilenameFilter {
	
	private String nameRegPattern;

	public AutoFileFilter(String namePattern) {
		nameRegPattern = namePattern;
	}
	
	
	@Override
	public boolean accept(File dir, String fileName) {
		Pattern pattern = Pattern.compile(nameRegPattern);
		Matcher matcher = pattern.matcher(fileName);
		return matcher.find();
	}

}
