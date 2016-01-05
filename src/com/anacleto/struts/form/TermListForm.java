//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 02-07-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="termListForm"
 */
public class TermListForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------- Instance
    // Variables
    /*
     * Pattern is the field for ....
     */
    private String pattern;

    private Collection fieldList;

    private String selectedField;

    protected HtmlButton queryButt = new HtmlButton();

    private String selectedTerms;

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
     * @return Returns the pattern.
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern
     *            The pattern to set.
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return Returns the fieldList.
     */
    public Collection getFieldList() {
        return fieldList;
    }

    /**
     * @param fieldList
     *            The fieldList to set.
     */
    public void setFieldList(Collection fieldList) {
        this.fieldList = fieldList;
    }

    /**
     * @return Returns the selectedField.
     */
    public String getSelectedField() {
        return selectedField;
    }

    /**
     * @param selectedField
     *            The selectedField to set.
     */
    public void setSelectedField(String selectedField) {
        this.selectedField = selectedField;
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


    /**
     * @return Returns the selectedTerms.
     */
    public String getSelectedTerms() {
        return selectedTerms;
    }

    /**
     * @param selectedTerms
     *            The selectedTerms to set.
     */
    public void setSelectedTerms(String selectedTerms) {
        this.selectedTerms = selectedTerms;
    }
}