package com.anacleto.struts.form;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 02-24-2008
 * 
 * XDoclet definition:
 * @struts.form name="pdfHolderForm"
 */
public class PdfHolderForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	/** URL property */
	private String url = "loading.jsp";

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
    	try {
			request.setCharacterEncoding("utf-8");
		} catch(UnsupportedEncodingException e) {
			;
		}
	}

	/** 
	 * Returns the name.
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/** 
	 * Set the name.
	 * @param name The name to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}