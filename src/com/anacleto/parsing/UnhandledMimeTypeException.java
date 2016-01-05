package com.anacleto.parsing;

public class UnhandledMimeTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 275946999334999737L;

	public UnhandledMimeTypeException() {
		super();
	}

	public UnhandledMimeTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnhandledMimeTypeException(String message) {
		super(message);
	}

	public UnhandledMimeTypeException(Throwable cause) {
		super(cause);
	}

}
