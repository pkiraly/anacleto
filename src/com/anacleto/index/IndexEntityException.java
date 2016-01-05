/*
 * Created on Mar 16, 2005
 *
 */
package com.anacleto.index;

/**
 * @author robi
 *
 */
public class IndexEntityException extends Exception {

    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4189090079427222614L;

	/**
     * 
     */
    public IndexEntityException() {
        super();
    }

    /**
     * @param arg0
     */
    public IndexEntityException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public IndexEntityException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public IndexEntityException(Throwable arg0) {
        super(arg0);
    }
}
