//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.FastTreeMap;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.struts.form.HtmlButton;

/** 
 * MyEclipse Struts
 * Creation date: 05-19-2005
 * 
 * XDoclet definition:
 * @struts:form name="accentConversionForm"
 */
public class AccentConversionForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** newTo property */
	private String newTo;

	/** newFrom property */
	private String newFrom;

	private String[] selectedAccents   = {};
	
	/** accents property */
	private Map accents = new FastTreeMap();

	/** add property */
	private HtmlButton addButt = new HtmlButton();

	/** delete property */
	private HtmlButton delButt = new HtmlButton();
	
	/** reload property */
	private HtmlButton reloadButt = new HtmlButton();

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
	  try {
	    request.setCharacterEncoding("utf-8");
	  } catch (UnsupportedEncodingException e) {}
	}

	/** 
	 * Returns the newTo.
	 * @return String
	 */
	public String getNewTo() {
		return newTo;
	}

	/** 
	 * Set the newTo.
	 * @param newTo The newTo to set
	 */
	public void setNewTo(String newTo) {
		this.newTo = newTo;
	}



	/**
	 * @return Returns the newFrom.
	 */
	public String getNewFrom() {
		return newFrom;
	}
	/**
	 * @param newFrom The newFrom to set.
	 */
	public void setNewFrom(String newFrom) {
		this.newFrom = newFrom;
	}
	/** 
	 * Returns the accents.
	 * @return Map
	 */
	public Map getAccents() {
		return accents;
	}

	/** 
	 * Set the accents.
	 * @param accents The accents to set
	 */
	public void setAccents(Map accents) {
		this.accents = accents;
	}


	/**
	 * @return Returns the addButt.
	 */
	public HtmlButton getAddButt() {
		return addButt;
	}
	/**
	 * @param addButt The addButt to set.
	 */
	public void setAddButt(HtmlButton addButt) {
		this.addButt = addButt;
	}
	/**
	 * @return Returns the delButt.
	 */
	public HtmlButton getDelButt() {
		return delButt;
	}
	/**
	 * @param delButt The delButt to set.
	 */
	public void setDelButt(HtmlButton delButt) {
		this.delButt = delButt;
	}
	
	
	/**
	 * @return Returns the reloadButt.
	 */
	public HtmlButton getReloadButt() {
		return reloadButt;
	}
	/**
	 * @param reloadButt The reloadButt to set.
	 */
	public void setReloadButt(HtmlButton reloadButt) {
		this.reloadButt = reloadButt;
	}
	
	
	/**
	 * @return Returns the selectedAccents.
	 */
	public String[] getSelectedAccents() {
		return selectedAccents;
	}
	/**
	 * @param selectedAccents The selectedAccents to set.
	 */
	public void setSelectedAccents(String[] selectedAccents) {
		this.selectedAccents = selectedAccents;
	}
    
}