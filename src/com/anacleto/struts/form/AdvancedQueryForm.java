//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 02-01-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="advancedQueryForm"
 */
public class AdvancedQueryForm extends ActionForm {

    // --------------------------------------------------------- Instance
    // Variables

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** query property */
    private String query;

    protected HtmlButton queryButt = new HtmlButton();

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
     * Returns the query.
     * 
     * @return String
     */
    public String getQuery() {
        return query;
    }

    /**
     * Set the query.
     * 
     * @param query
     *            The query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return Returns the queryButt.
     */
    public HtmlButton getQueryButt() {
        return queryButt;
    }

    /**
     * @param queryButt
     *            The queryButt to set.
     */
    public void setQueryButt(HtmlButton queryButt) {
        this.queryButt = queryButt;
    }
}