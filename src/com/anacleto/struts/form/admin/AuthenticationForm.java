//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.9.210/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.list.TreeList;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.struts.form.HtmlButton;
import com.anacleto.view.UserBean;

/** 
 * MyEclipse Struts
 * Creation date: 09-22-2005
 * 
 * XDoclet definition:
 * @struts:form name="authenticationForm"
 */
public class AuthenticationForm extends ActionForm  {

	// --------------------------------------------------------- Instance Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HtmlButton addNewButt  = new HtmlButton();
	
	private HtmlButton modifyButt  = new HtmlButton();
	
	private HtmlButton deleteButt  = new HtmlButton();

	/** newuser property */
	private UserBean newUser = new UserBean();

	private boolean  newUserAdmin;
	
	private String   modifyUser;
	
	/** users property */
	private Collection adminUsers = new TreeList();

	/** users property */
	private Collection normalUsers = new TreeList();

	//Page status: true - new user is being inserted
	//             false- an existing user is being modified
	private boolean pageStatusIsInsert = true;
	// --------------------------------------------------------- Methods

	
	
	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		//newUser = new UserBean();
		try {
			request.setCharacterEncoding("utf-8");
		} catch(UnsupportedEncodingException e) {
			;
		}
	}

	public UserBean getNewUser() {
		return newUser;
	}

	public void setNewUser(UserBean newUser) {
		this.newUser = newUser;
	}

	public Collection getAdminUsers() {
		return adminUsers;
	}

	public void setAdminUsers(Collection adminUsers) {
		this.adminUsers = adminUsers;
	}

	public Collection getNormalUsers() {
		return normalUsers;
	}

	public void setNormalUsers(Collection normalUsers) {
		this.normalUsers = normalUsers;
	}

	public HtmlButton getAddNewButt() {
		return addNewButt;
	}

	public void setAddNewButt(HtmlButton addNewButt) {
		this.addNewButt = addNewButt;
	}

	/**
	 * @return Returns the deleteButt.
	 */
	public HtmlButton getDeleteButt() {
		return deleteButt;
	}

	/**
	 * @param deleteButt The deleteButt to set.
	 */
	public void setDeleteButt(HtmlButton deleteButt) {
		this.deleteButt = deleteButt;
	}

	public boolean isNewUserAdmin() {
		return newUserAdmin;
	}

	public void setNewUserAdmin(boolean newUserAdmin) {
		this.newUserAdmin = newUserAdmin;
	}

	public boolean isPageStatusIsInsert() {
		return pageStatusIsInsert;
	}

	public void setPageStatusIsInsert(boolean pageStatusIsInsert) {
		this.pageStatusIsInsert = pageStatusIsInsert;
	}

	public HtmlButton getModifyButt() {
		return modifyButt;
	}

	public void setModifyButt(HtmlButton modifyButt) {
		this.modifyButt = modifyButt;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}



}

