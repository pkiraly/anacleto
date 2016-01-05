/*
 * Created on 24-gen-2005
 *
 */
package com.anacleto.base;

/**
 * @author robert
 * 
 */
public class BookException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7394905006210675952L;

	/**
     *  
     */
    public BookException() {
        super();
    }

    /**
     * @param arg0
     */
    public BookException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public BookException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public BookException(Throwable arg0) {
        super(arg0);
    }
}