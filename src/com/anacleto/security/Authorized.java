package com.anacleto.security;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;

import org.securityfilter.filter.SecurityRequestWrapper;

public class Authorized extends ConditionalTagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.jstl.core.ConditionalTagSupport#condition()
	 */
	protected boolean condition() throws JspTagException {
		
		if (pageContext.getSession().getAttribute(
				SecurityRequestWrapper.PRINCIPAL_SESSION_KEY) != null)
			return true;
		return false;
	}

	
}
