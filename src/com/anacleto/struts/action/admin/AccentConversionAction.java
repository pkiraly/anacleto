//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.index.CustomAnalyzer;
import com.anacleto.struts.form.admin.AccentConversionForm;

/** 
 * MyEclipse Struts
 * Creation date: 05-19-2005
 * 
 * XDoclet definition:
 * @struts:action path="/accentConversion" name="accentConversionForm" input="/form/admin/accentConversion.jsp" scope="request" validate="true"
 */
public class AccentConversionAction extends Action {

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
		AccentConversionForm accForm = (AccentConversionForm) form;

		CustomAnalyzer analyzer = Configuration.getAnalyzer();
		try {
			if (accForm.getAddButt().pressed()){
				analyzer.addAccent(accForm.getNewFrom(), accForm.getNewTo());
				analyzer.saveAccents();
				//analyzer.loadAccents();
			} else if (accForm.getDelButt().pressed()){
				analyzer.deleteAccents(accForm.getSelectedAccents());
				analyzer.saveAccents();
			} else if (accForm.getReloadButt().pressed()){
				analyzer.loadDefaultAccents();
				analyzer.saveAccents();
			}
		} catch (IOException e){
		}
		
		accForm.setAccents(analyzer.getAccentMap());
		return mapping.getInputForward();
	}

}