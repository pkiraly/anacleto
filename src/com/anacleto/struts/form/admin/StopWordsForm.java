//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.struts.form.HtmlButton;

/** 
 * MyEclipse Struts
 * Creation date: 05-03-2005
 * 
 * XDoclet definition:
 * @struts:form name="stopWordsForm"
 */
public class StopWordsForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** stopWordList property */
	private Collection stopWordList;

	/** newStopWord property */
	private String newStopWord;

	private String[] selectedWords   = {};

	private HtmlButton addNewButt = new HtmlButton();
	
	private HtmlButton deleteButt = new HtmlButton();
	
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
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			
		}
	}

	/** 
	 * Returns the stopWordList.
	 * @return Collection
	 */
	public Collection getStopWordList() {
		return stopWordList;
	}

	/** 
	 * Set the stopWordList.
	 * @param stopWordList The stopWordList to set
	 */
	public void setStopWordList(Collection stopWordList) {
		this.stopWordList = stopWordList;
	}

	/** 
	 * Returns the newStopWord.
	 * @return String
	 */
	public String getNewStopWord() {
		return newStopWord;
	}

	/** 
	 * Set the newStopWord.
	 * @param newStopWord The newStopWord to set
	 */
	public void setNewStopWord(String newStopWord) {
		this.newStopWord = newStopWord;
	}

	
	/**
	 * @return Returns the addNewButt.
	 */
	public HtmlButton getAddNewButt() {
		return addNewButt;
	}
	/**
	 * @param addNewButt The addNewButt to set.
	 */
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
	 * @return Returns the selectesWords.
	 */
	public String[] getSelectedWords() {
		return selectedWords;
	}
	/**
	 * @param selectesWords The selectesWords to set.
	 */
	public void setSelectedWords(String[] selectedWords) {
		this.selectedWords = selectedWords;
	}
}