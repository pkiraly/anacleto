//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.securityfilter.realm.SimplePrincipal;

/**
 * MyEclipse Struts Creation date: 01-21-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="treeViewForm"
 */
public class WelcomeForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private SimplePrincipal principal;

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

        ;
    }

	public SimplePrincipal getPrincipal() {
		return principal;
	}

	public void setPrincipal(SimplePrincipal principal) {
		this.principal = principal;
	}
}