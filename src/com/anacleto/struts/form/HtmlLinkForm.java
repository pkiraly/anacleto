//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.9.210/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 11-21-2005
 * 
 * XDoclet definition:
 * @struts:form name="htmlLinkForm"
 */
public class HtmlLinkForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** location property */
	private String location;

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

		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		// TODO Auto-generated method stub
	}

	/** 
	 * Returns the location.
	 * @return String
	 */
	public String getLocation() {
		return location;
	}

	/** 
	 * Set the location.
	 * @param location The location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

}

