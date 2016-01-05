//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 03-01-2005
 * 
 * XDoclet definition:
 * @struts:form name="arcanumFieldQueryForm"
 */
public class ArcanumFieldQueryForm extends ActionForm {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------- Instance Variables
    /** andLogic property */
    private boolean andLogic;

    private boolean similarities;

    private boolean exactMatch;

    
    /** note property */
    private String author;

    private String creator;

    private String title;

    private String keyword;
    
    private String pubDate;

    private String pubPlace;

    private String picture;
    
    private String pagenumber;

    private String content;
    
    private String bookmark;

    private String queryString;

    /** command property */
    protected HtmlButton queryButt = new HtmlButton();

    /** command property */
    private String query;

    private String proximity;

    private int proxDistance = 10;
    
    private Collection shelves;
    
    private String[] selectedShelves = {};

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
		} catch (UnsupportedEncodingException e) {
			;
		}
        
    }
    
    public boolean isAndLogic() {
        return andLogic;
    }
    public void setAndLogic(boolean andLogic) {
        this.andLogic = andLogic;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public boolean isExactMatch() {
        return exactMatch;
    }
    public void setExactMatch(boolean exactMatch) {
        this.exactMatch = exactMatch;
    }
    public int getProxDistance() {
        return proxDistance;
    }
    public void setProxDistance(int proxDistance) {
        this.proxDistance = proxDistance;
    }
    public String getProximity() {
        return proximity;
    }
    public void setProximity(String proximity) {
        this.proximity = proximity;
    }
    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPubPlace() {
        return pubPlace;
    }
    public void setPubPlace(String pubPlace) {
        this.pubPlace = pubPlace;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public String getQueryString() {
        return queryString;
    }
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    public HtmlButton getQueryButt() {
        return queryButt;
    }
    public void setQueryButt(HtmlButton queryButt) {
        this.queryButt = queryButt;
    }
    public boolean isSimilarities() {
        return similarities;
    }
    public void setSimilarities(boolean similarities) {
        this.similarities = similarities;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public String getPagenumber() {
        return pagenumber;
    }
    public void setPagenumber(String pagenumber) {
        this.pagenumber = pagenumber;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    

	/**
	 * @return Returns the selectedShelves.
	 */
	public String[] getSelectedShelves() {
		return selectedShelves;
	}
	/**
	 * @param selectedShelves The selectedShelves to set.
	 */
	public void setSelectedShelves(String[] selectedShelves) {
		this.selectedShelves = selectedShelves;
	}
	
	/**
	 * @return Returns the shelves.
	 */
	public Collection getShelves() {
		return shelves;
	}
	/**
	 * @param shelves The shelves to set.
	 */
	public void setShelves(Collection shelves) {
		this.shelves = shelves;
	}

	public String getBookmark() {
		return bookmark;
	}

	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}
}