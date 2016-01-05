/*
 * Created on 25-gen-2005
 *
 */
package com.anacleto.index;

/**
 * @author robert
 * 
 */
public class IndexException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3089276602816989069L;

	/**
     *  
     */
    public IndexException() {
        super();
    }

    /**
     * @param arg0
     */
    public IndexException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public IndexException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public IndexException(Throwable arg0) {
        super(arg0);
    }
}