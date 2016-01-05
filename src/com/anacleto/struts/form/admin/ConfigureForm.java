//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.BaseParameters;
import com.anacleto.base.Configuration;
import com.anacleto.struts.form.HtmlButton;

/** 
 * MyEclipse Struts
 * Creation date: 04-28-2005
 * 
 * XDoclet definition:
 * @struts:form name="configureForm"
 */
public class ConfigureForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BaseParameters params = Configuration.params;
	    
	protected HtmlButton saveButton  = new HtmlButton();
	
	protected HtmlButton applyButton = new HtmlButton();
	
	protected HtmlButton reloadButton = new HtmlButton();
	
	private Collection locales;
	
	
		
	// --------------------------------------------------------- Methods

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		 locales = new LinkedList();
		 Locale[] localeArr = Locale.getAvailableLocales();
		 for (int i = 0; i < localeArr.length; i++) {
			Locale locale = localeArr[i];
			locales.add(locale);
		}
		 
	}

	

	/**
	 * @return Returns the saveButton.
	 */
	public HtmlButton getSaveButton() {
		return saveButton;
	}
	/**
	 * @param saveButton The saveButton to set.
	 */
	public void setSaveButton(HtmlButton saveButton) {
		this.saveButton = saveButton;
	}
	
	
	
	/**
	 * @return Returns the applyButton.
	 */
	public HtmlButton getApplyButton() {
		return applyButton;
	}
	/**
	 * @param applyButton The applyButton to set.
	 */
	public void setApplyButton(HtmlButton applyButton) {
		this.applyButton = applyButton;
	}
	/**
	 * @return Returns the reloadButton.
	 */
	public HtmlButton getReloadButton() {
		return reloadButton;
	}
	/**
	 * @param reloadButton The reloadButton to set.
	 */
	public void setReloadButton(HtmlButton reloadButton) {
		this.reloadButton = reloadButton;
	}
	
	
	/**
	 * @return Returns the params.
	 */
	public BaseParameters getParams() {
		return params;
	}
	/**
	 * @param params The params to set.
	 */
	public void setParams(BaseParameters params) {
		this.params = params;
	}

	public Collection getLocales() {
		return locales;
	}

	public void setLocales(Collection locales) {
		this.locales = locales;
	}
	
	
}