//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

// import java.util.Collection;

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
public class XmlEditForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String booksFile;

	// --------------------------------------------------------- Instance
    // Variables

    // --------------------------------------------------------- Methods

    public String getBooksFile() {
		return booksFile;
	}

	public void setBooksFile(String booksFile) {
		this.booksFile = booksFile;
	}

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

}