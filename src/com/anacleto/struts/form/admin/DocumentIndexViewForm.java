//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 01-30-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="documentIndexViewForm"
 */
public class DocumentIndexViewForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------- Instance
    // Variables
    private String book;

    private String name;

    private Collection fieldMap;

    // --------------------------------------------------------- Methods

    /**
     * Method validate
     * 
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {

        return null;
    }

    /**
     * Method reset
     * 
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
     * @param book
     *            The book to set.
     */
    public void setBook(String book) {
        this.book = book;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the hashMap.
     */
    public Collection getFieldMap() {
        return fieldMap;
    }

    /**
     * @param hashMap
     *            The hashMap to set.
     */
    public void setFieldMap(Collection fieldMap) {
        this.fieldMap = fieldMap;
    }
}