//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 02-01-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/advancedQuery" name="advancedQueryForm"
 *                input="/form/advancedQuery.jsp" scope="request"
 *                validate="true"
 */
public class AdvancedQueryAction extends Action {

    // --------------------------------------------------------- Instance
    // Variables

    // --------------------------------------------------------- Methods

    /**
     * Method execute
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

/*
        AdvancedQueryForm advancedQueryForm = (AdvancedQueryForm) form;
*/
        return mapping.getInputForward();
    }

}