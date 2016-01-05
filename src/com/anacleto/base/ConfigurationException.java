/*
 * Created on Feb 3, 2005
 *
 */
package com.anacleto.base;

/**
 * @author robi
 * 
 */
public class ConfigurationException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4220088035482631544L;

	/**
     *  
     */
    public ConfigurationException() {
        super();
    }

    /**
     * @param arg0
     */
    public ConfigurationException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public ConfigurationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public ConfigurationException(Throwable arg0) {
        super(arg0);
    }
}