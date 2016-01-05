package org.securityfilter.authenticator;

public class UserAlreadyExistsException extends Exception {

	public UserAlreadyExistsException() {
		super();
	}

	public UserAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAlreadyExistsException(String message) {
		super(message);
	}

	public UserAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	
}
