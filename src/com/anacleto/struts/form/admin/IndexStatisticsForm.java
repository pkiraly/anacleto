//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 01-28-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="indexStatisticsForm"
 */
public class IndexStatisticsForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------- Instance
    // Variables
    private int numTerms;

    private Collection termFreq;

 
    public int getNumTerms() {
        return numTerms;
    }
    public void setNumTerms(int numTerms) {
        this.numTerms = numTerms;
    }
    /**
     * @return Returns the termFreq.
     */
    public Collection getTermFreq() {
        return termFreq;
    }

    /**
     * @param termFreq
     *            The termFreq to set.
     */
    public void setTermFreq(Collection termFreq) {
        this.termFreq = termFreq;
    }

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

}