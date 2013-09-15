/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic;

public class AutoException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public AutoException() {
		super();
	}
	
	public AutoException(String message) {
		super(message);
	}
	
	public AutoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AutoException(Throwable cause) {
		super(cause);
	}

}
