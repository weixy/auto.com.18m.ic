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
