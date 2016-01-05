//Created by MyEclipse Struts
//XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
* MyEclipse Struts
* Creation date: 05-23-2005
* 
* XDoclet definition:
* @struts:form name="imageLinkForm"
*/
public class CssLinkForm extends ActionForm {

    // --------------------------------------------------------- Instance Variables

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** book property */
    private String book;

    /** name property */
    private String name;

    // --------------------------------------------------------- Methods

    /** 
     * Method validate
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(
        ActionMapping mapping,
        HttpServletRequest request) {

        return null;
    }

    /** 
     * Method reset
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

        
    /**
     * @return Returns the book.
     */
    public String getBook() {
        return book;
    }
    /**
     * @param book The book to set.
     */
    public void setBook(String book) {
        this.book = book;
    }
    
    /** 
     * Returns the name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /** 
     * Set the name.
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}