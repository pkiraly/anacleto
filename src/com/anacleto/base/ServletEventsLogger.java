package com.anacleto.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ServletEventsLogger extends Logger {

	HttpServletRequest request;
	
	public ServletEventsLogger(String arg0, HttpServletRequest request) {
		super(arg0);
		this.request = request;
	}
	
	

	/* (non-Javadoc)
	 * @see org.apache.log4j.Category#debug(java.lang.Object)
	 */
	public void debug(String arg0) {
		super.debug( "IP: " + request.getRemoteAddr() 
		           + ". Host: " + request.getRemoteHost() + arg0);
	}



	/* (non-Javadoc)
	 * @see org.apache.log4j.Category#error(java.lang.Object)
	 */
	public void error(String arg0) {
		super.error( "IP: " + request.getRemoteAddr() 
		          + ". Host: " +
				request.getRemoteHost() + arg0);
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.Category#info(java.lang.Object)
	 */
	public void info(String arg0) {
		super.info( "IP: " + request.getRemoteAddr() 
		         + ". Host: " + request.getRemoteHost() + arg0);
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.Category#warn(java.lang.Object)
	 */
	public void warn(String arg0) {
		super.warn( "IP: " + request.getRemoteAddr() 
		          + ". Host: " + request.getRemoteHost() + arg0);
	}
	
	

}
