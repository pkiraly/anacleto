//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.struts.form.HtmlButton;

/** 
 * MyEclipse Struts
 * Creation date: 04-20-2005
 * 
 * XDoclet definition:
 * @struts:form name="indexMaintenanceForm"
 */
public class IndexMaintenanceForm extends ActionForm {

    // --------------------------------------------------------- Instance Variables


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** bookColl property */
    private Collection bookColl;

    private HtmlButton indexNowButt  = new HtmlButton();
    
    private HtmlButton indexAtButt   = new HtmlButton();
    
    private HtmlButton optimizeButt  = new HtmlButton();
    
    private HtmlButton checkButt     = new HtmlButton();
    
    private HtmlButton removeButt    = new HtmlButton();
    
    private HtmlButton removeSelectedButt = new HtmlButton();
    
    private String[]   selectedBooks  = {};
    
    private Collection missingBookColl;
    
	private String schedDate;
	private String schedTime;

	private String     currentlyIndexing;
	private Collection booksInQueue;
	
	private Collection simpleJobIndexing;
	private Collection cronJobIndexing;
	
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
    }

    /** 
     * Returns the bookColl.
     * @return Collection
     */
    public Collection getBookColl() {
        return bookColl;
    }

    /** 
     * Set the bookColl.
     * @param bookColl The bookColl to set
     */
    public void setBookColl(Collection bookColl) {
        this.bookColl = bookColl;
    }

    

	/**
	 * @return Returns the checkButt.
	 */
	public HtmlButton getCheckButt() {
		return checkButt;
	}
	/**
	 * @param checkButt The checkButt to set.
	 */
	public void setCheckButt(HtmlButton checkButt) {
		this.checkButt = checkButt;
	}
	/**
	 * @return Returns the indexAtButt.
	 */
	public HtmlButton getIndexAtButt() {
		return indexAtButt;
	}
	/**
	 * @param indexAtButt The indexAtButt to set.
	 */
	public void setIndexAtButt(HtmlButton indexAtButt) {
		this.indexAtButt = indexAtButt;
	}
	/**
	 * @return Returns the indexNowButt.
	 */
	public HtmlButton getIndexNowButt() {
		return indexNowButt;
	}
	/**
	 * @param indexNowButt The indexNowButt to set.
	 */
	public void setIndexNowButt(HtmlButton indexNowButt) {
		this.indexNowButt = indexNowButt;
	}
	
	/**
	 * @return Returns the selectedBooks.
	 */
	public String[] getSelectedBooks() {
		return selectedBooks;
	}
	/**
	 * @param selectedBooks The selectedBooks to set.
	 */
	public void setSelectedBooks(String[] selectedBooks) {
		this.selectedBooks = selectedBooks;
	}
	
	
	/**
	 * @return Returns the optimizeButt.
	 */
	public HtmlButton getOptimizeButt() {
		return optimizeButt;
	}
	/**
	 * @param optimizeButt The optimizeButt to set.
	 */
	public void setOptimizeButt(HtmlButton optimizeButt) {
		this.optimizeButt = optimizeButt;
	}
	
	
	/**
	 * @return Returns the schedDate.
	 */
	public String getSchedDate() {
		return schedDate;
	}
	/**
	 * @param schedDate The schedDate to set.
	 */
	public void setSchedDate(String schedDate) {
		this.schedDate = schedDate;
	}
	/**
	 * @return Returns the schedTime.
	 */
	public String getSchedTime() {
		return schedTime;
	}
	/**
	 * @param schedTime The schedTime to set.
	 */
	public void setSchedTime(String schedTime) {
		this.schedTime = schedTime;
	}
	
	
	/**
	 * @return Returns the currentlyIndexing.
	 */
	public String getCurrentlyIndexing() {
		return currentlyIndexing;
	}
	/**
	 * @param currentlyIndexing The currentlyIndexing to set.
	 */
	public void setCurrentlyIndexing(String currentlyIndexing) {
		this.currentlyIndexing = currentlyIndexing;
	}
	
	
	/**
	 * @return Returns the booksInQueue.
	 */
	public Collection getBooksInQueue() {
		return booksInQueue;
	}
	/**
	 * @param booksInQueue The booksInQueue to set.
	 */
	public void setBooksInQueue(Collection booksInQueue) {
		this.booksInQueue = booksInQueue;
	}


	/**
	 * @return Returns the cronJobIndexing.
	 */
	public Collection getCronJobIndexing() {
		return cronJobIndexing;
	}
	/**
	 * @param cronJobIndexing The cronJobIndexing to set.
	 */
	public void setCronJobIndexing(Collection cronJobIndexing) {
		this.cronJobIndexing = cronJobIndexing;
	}
	/**
	 * @return Returns the simpleJobIndexing.
	 */
	public Collection getSimpleJobIndexing() {
		return simpleJobIndexing;
	}
	/**
	 * @param simpleJobIndexing The simpleJobIndexing to set.
	 */
	public void setSimpleJobIndexing(Collection simpleJobIndexing) {
		this.simpleJobIndexing = simpleJobIndexing;
	}
	
	
	/**
	 * @return Returns the removeButt.
	 */
	public HtmlButton getRemoveButt() {
		return removeButt;
	}
	/**
	 * @param removeButt The removeButt to set.
	 */
	public void setRemoveButt(HtmlButton removeButt) {
		this.removeButt = removeButt;
	}
	
	/**
	 * @return Returns the missingBookColl.
	 */
	public Collection getMissingBookColl() {
		return missingBookColl;
	}
	/**
	 * @param missingBookColl The missingBookColl to set.
	 */
	public void setMissingBookColl(Collection missingBookColl) {
		this.missingBookColl = missingBookColl;
	}

	/**
	 * @return Returns the removeSelectedButt.
	 */
	public HtmlButton getRemoveSelectedButt() {
		return removeSelectedButt;
	}

	/**
	 * @param removeSelectedButt The removeSelectedButt to set.
	 */
	public void setRemoveSelectedButt(HtmlButton removeSelectedButt) {
		this.removeSelectedButt = removeSelectedButt;
	}
	
	
}