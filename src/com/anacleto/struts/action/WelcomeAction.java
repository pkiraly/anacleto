package com.anacleto.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.securityfilter.realm.SimplePrincipal;

import com.anacleto.struts.form.WelcomeForm;

/** 
 * MyEclipse Struts
 * Creation date: 02-16-2008
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class WelcomeAction extends Action {

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
		WelcomeForm welcomeForm = (WelcomeForm) form;
		String attr = "org.securityfilter.filter.SecurityRequestWrapper.PRINCIPAL";
		welcomeForm.setPrincipal((SimplePrincipal)request.getSession()
					.getAttribute(attr));
		
        return mapping.getInputForward();
	}
}