//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 01-28-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="errorForm"
 */
public class ErrorForm extends ActionForm {

	
    // --------------------------------------------------------- Instance
    // Variables

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** errorStr property */
    private String errorStr;

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

        throw new UnsupportedOperationException(
                "Generated method 'validate(...)' not implemented.");
    }

    /**
     * Method reset
     * 
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        throw new UnsupportedOperationException(
                "Generated method 'reset(...)' not implemented.");
    }

    /**
     * Returns the errorStr.
     * 
     * @return String
     */
    public String getErrorStr() {
        return errorStr;
    }

    /**
     * Set the errorStr.
     * 
     * @param errorStr
     *            The errorStr to set
     */
    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

}