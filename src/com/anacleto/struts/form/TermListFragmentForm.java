package com.anacleto.struts.form;

import java.io.UnsupportedEncodingException;
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
 * @struts:form name="termListFragmentForm"
 */
public class TermListFragmentForm extends ActionForm {

    // --------------------------------------------------------- Instance
    // Variables

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** pattern property */
    private String pattern;

    private String offset;
    
    private int    pageNumber = 0;
    
    /** selectedField property */
    private String selectedField;

    /** term collection property */
    private Collection termColl;

    protected HtmlButton prevButt = new HtmlButton();
    
    protected HtmlButton nextButt = new HtmlButton();
    
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
        try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			;
		}
    }

    /**
     * @return Returns the pattern.
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern The pattern to set.
     */
    public void setPattern(String pattern) {
    	this.pattern = pattern;
    }
    

    /**
	 * @return Returns the offset.
	 */
	public String getOffset() {
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(String offset) {
		this.offset = offset;
	}

	
	/**
	 * @return Returns the pageNumber.
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber The pageNumber to set.
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
     * @return Returns the termColl.
     */
    public Collection getTermColl() {
        return termColl;
    }

    /**
     * @param termColl
     *            The termColl to set.
     */
    public void setTermColl(Collection termColl) {
        this.termColl = termColl;
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
	 * @return Returns the nextButt.
	 */
	public HtmlButton getNextButt() {
		return nextButt;
	}

	/**
	 * @param nextButt The nextButt to set.
	 */
	public void setNextButt(HtmlButton nextButt) {
		this.nextButt = nextButt;
	}

	/**
	 * @return Returns the prevButt.
	 */
	public HtmlButton getPrevButt() {
		return prevButt;
	}

	/**
	 * @param prevButt The prevButt to set.
	 */
	public void setPrevButt(HtmlButton prevButt) {
		this.prevButt = prevButt;
	}

    
}