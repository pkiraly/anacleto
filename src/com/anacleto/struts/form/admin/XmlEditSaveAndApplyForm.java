//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

// import java.util.Collection;

import java.io.UnsupportedEncodingException;

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
public class XmlEditSaveAndApplyForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** fileName property */
	private String fileName;

	/** content property */
	private String content;


	// --------------------------------------------------------- Instance
    // Variables

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
		} catch(UnsupportedEncodingException e) {
			;
		}
    }

	/** 
	 * Returns the fileName.
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}

	/** 
	 * Set the fileName.
	 * @param fileName The fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/** 
	 * Returns the content.
	 * @return String
	 */
	public String getContent() {
		return content;
	}

	/** 
	 * Set the content.
	 * @param content The content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
}