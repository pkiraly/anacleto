//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.9.210/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;
import org.securityfilter.authenticator.PasswordFileHandler;
import org.securityfilter.authenticator.UserAlreadyExistsException;

import com.anacleto.base.Configuration;
import com.anacleto.struts.form.admin.AuthenticationForm;
import com.anacleto.view.UserBean;


/** 
 * MyEclipse Struts
 * Creation date: 09-22-2005
 * 
 * XDoclet definition:
 * @struts:action path="/authentication" name="authenticationForm" input="/form/authentication.jsp" scope="request" validate="true"
 */
public class AuthenticationAction extends Action {

	// --------------------------------------------------------- Instance Variables
	
	// --------------------------------------------------------- Methods

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException 
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws IOException {
		
		AuthenticationForm authForm = (AuthenticationForm) form;
		PasswordFileHandler handler = Configuration.getAuthController();
		
		
		//modifu user link
		String modifyUser = authForm.getModifyUser();
		if (modifyUser != null){
			authForm.setModifyUser(null);
			
			UserBean user = handler.getUserForName(modifyUser);
			if (user != null)
				authForm.setNewUser(user);
			
			authForm.setPageStatusIsInsert(false);
		}
		
		if (authForm.getAddNewButt().pressed()){
			
			ActionMessages errors = new ActionMessages();
			
			UserBean newUser = authForm.getNewUser();
			if (newUser.getUser() == null ||
					newUser.getUser().trim().equals(""))
				errors.add("newUser.user", new ActionMessage("errors.mandatory"));
			
			if (newUser.getPassword() == null ||
					newUser.getPassword().trim().equals(""))
				errors.add("newUser.password", new ActionMessage("errors.mandatory"));
			
			
			try {
				if (errors.isEmpty())
					handler.addNewUser(authForm.getNewUser());
			} catch (UserAlreadyExistsException e) {
				errors.add("newUser.user", new ActionMessage("auth.userExists"));
			}	
			saveErrors(request, errors);
		}
		
		if (authForm.getModifyButt().pressed()){
//			modify existing user:
			ActionMessages errors = new ActionMessages();
			UserBean newUser = authForm.getNewUser();
			
			if (newUser.getPassword() == null ||
					newUser.getPassword().trim().equals(""))
				errors.add("newUser.password", new ActionMessage("errors.mandatory"));
			
			if (!errors.isEmpty()){
				//authForm.setModifyUser(modifyUser);
				authForm.setPageStatusIsInsert(false);
				saveErrors(request, errors);
			} else {
				handler.modifyUser(authForm.getNewUser());
				authForm.setNewUser( new UserBean());
			}				
		}
		
		if (authForm.getDeleteButt().pressed()){
			handler.deleteUser(authForm.getNewUser());
			authForm.setNewUser(new UserBean());
		}
		Enumeration atts = request.getAttributeNames();
		while (atts.hasMoreElements()) {
			String element = (String) atts.nextElement();
			System.out.println(element + "::" + request.getAttribute(element));
			
		}
		authForm.setAdminUsers(handler.getAdminUsers());
		authForm.setNormalUsers(handler.getNormalUsers());
		
		return mapping.getInputForward();
	}
	
}

