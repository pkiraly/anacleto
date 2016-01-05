//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.struts.form.admin.ConfigureForm;

/** 
 * MyEclipse Struts
 * Creation date: 04-28-2005
 * 
 * XDoclet definition:
 * @struts:action path="/configure" name="configureForm" input="/form/configure.jsp" scope="request" validate="true"
 */
public class ConfigureAction extends Action {

	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		
		ConfigureForm conForm = (ConfigureForm) form;
		Configuration conf = new Configuration();
		try {
			//save and apply shanges:
			if (conForm.getSaveButton().pressed()){
				Configuration.saveParams();
				conf.destroy();
				conf.init();
			
			//apply changes but not save them:	
			} else if (conForm.getApplyButton().pressed()){
				conf.destroy();
				conf.init();
			
			//reload params
			} else if (conForm.getReloadButton().pressed()){
				conf.destroy();
				conf.init();
			}	
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//Add configuration errors:
		/*
		ActionErrors errors = new ActionErrors();
		Iterator iter = Configuration.getInitErrors().iterator();
		while (iter.hasNext()) {
			ErrorBean err = (ErrorBean) iter.next();
			errors.add(err.getErrorCode(), 
					new ActionError(err.getErrorCode(), err.getArg1()));
		}
		if (!errors.isEmpty())
			saveErrors(request, errors);
		*/
		
		
		
		return mapping.getInputForward();
	}
	
}