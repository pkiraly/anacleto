/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.anacleto.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Logging;
import com.anacleto.struts.form.PdfHolderForm;

/** 
 * MyEclipse Struts
 * Creation date: 02-24-2008
 * 
 * XDoclet definition:
 * @struts.action path="/pdfHolder" name="pdfHolderForm" input="/form/pdfHolder.jsp" scope="request" validate="true"
 */
public class PdfHolderAction extends Action {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Logger logger = Logging.getUserEventsLogger();

		PdfHolderForm pdfHolderForm = (PdfHolderForm) form;// TODO Auto-generated method stub
		logger.info("Url: " + pdfHolderForm.getUrl());
        return mapping.getInputForward();
	}
}