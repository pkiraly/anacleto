/*
 * Class to capture user input from a html button 
 * Created on Feb 7, 2005
 *
 */
package com.anacleto.struts.form;

import java.io.Serializable;

/**
 * HtmlButton
 */
public class HtmlButton implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3851450839399782524L;

	private String m_name = null;

    private Integer m_x = null;

    private Integer m_y = null;

    /**
     * Default Constructor
     */
    public HtmlButton() {
    }

    /**
     * Button Pressed
     * 
     * @return boolean
     */
    public boolean pressed() {
        return !(m_name == null || m_name.trim().length() <= 0)
                || (m_x != null || m_y != null);
    }

    /**
     * @return String
     */
    public String getName() {
        return m_name;
    }

    /**
     * @return Integer
     */
    public Integer getX() {
        return m_x;
    }

    /**
     * @return Integer
     */
    public Integer getY() {
        return m_y;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * Sets the x.
     * 
     * @param x
     *            The x to set
     */
    public void setX(Integer x) {
        m_x = x;
    }

    /**
     * Sets the y.
     * 
     * @param y
     *            The y to set
     */
    public void setY(Integer y) {
        m_y = y;
    }

}