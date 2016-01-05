/*
 * Created on Mar 17, 2005
 *
 */
package com.anacleto.parsing;

/**
 * @author robi
 * 
 */
public class ParserException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2550674705170561719L;

	/**
     *  
     */
    public ParserException() {
        super();
    }

    /**
     * @param arg0
     */
    public ParserException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public ParserException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public ParserException(Throwable arg0) {
        super(arg0);
    }
}
