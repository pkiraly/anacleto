/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.anacleto.struts.form.admin;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 12-22-2006
 * 
 * XDoclet definition:
 * @struts.form name="listFilesForm"
 */
public class XmlEditListFilesForm extends ActionForm {
	/*
	 * Generated fields
	 */
	private static final long serialVersionUID = 1L;

	/** fileNames property */
	private String[] fileNames;
	private int numOfFiles;
	boolean isAjaxian;

	/*
	 * Generated Methods
	 */

	public boolean getIsAjaxian() {
		return isAjaxian;
	}

	public void setIsAjaxian(boolean isAjaxian) {
		this.isAjaxian = isAjaxian;
	}

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
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
	 * Returns the fileNames.
	 * @return String[]
	 */
	public String[] getFileNames() {
		return fileNames;
	}

	/** 
	 * Set the fileNames.
	 * @param fileNames The fileNames to set
	 */
	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	public int getNumOfFiles() {
		return numOfFiles;
	}

	public void setNumOfFiles(int numOfFiles) {
		this.numOfFiles = numOfFiles;
	}
}