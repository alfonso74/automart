package com.orendel.common.exceptions;


public class ApplicationRuntimeException extends RuntimeException {


	private static final long serialVersionUID = 2860619101961087396L;

	
	public ApplicationRuntimeException() {
	}

	public ApplicationRuntimeException(String message) {
		super(message);
	}

	public ApplicationRuntimeException(Throwable cause) {
		super(cause);
	}

	public ApplicationRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
