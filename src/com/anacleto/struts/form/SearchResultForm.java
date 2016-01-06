//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 01-21-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="searchResultForm"
 */
public class SearchResultForm extends ActionForm {

    // --------------------------------------------------------- Instance
    // Variables

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** query property */
    private String query;

    /** offset property */
    private int offset;
    
    /** isOldSearch property 
     * true if the searchResult is called from hit navigation bar (contentHead)
     * */
    private int oldSearch = 0;

    /** startIndex property */
    private int startIndex;

    /** endIndex property */
    private int endIndex;

    /** maxResult property */
    private int maxResult;

    /** foundResult property */
    private int foundResult;

    /** queryResult property */
    private Collection queryResult;

    private String speed;

    private int showResult;

    /**
     * Previous query result
     */
    protected HtmlButton prevRes = new HtmlButton();

    /**
     * Next query result
     */
    protected HtmlButton nextRes = new HtmlButton();

    /**
     * Next query result
     */
    protected HtmlButton search = new HtmlButton();

    private boolean atLast;

    // --------------------------------------------------------- Methods

    /**
     * Method validate
     * 
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {

        return null;
    }

    /**
     * Method reset
     * 
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	try {
			request.setCharacterEncoding("utf-8");
		} catch(UnsupportedEncodingException e) {
			;
		}
    }

    /**
     * @return Returns the speed.
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * @param speed
     *            The speed to set.
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * Returns the query.
     * 
     * @return String
     */
    public String getQuery() {
        return query;
    }

    /**
     * Set the query.
     * 
     * @param query
     *            The query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Returns the offset.
     * 
     * @return Integer
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Set the offset.
     * 
     * @param offset
     *            The offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Returns the maxResult.
     * 
     * @return Integer
     */
    public int getMaxResult() {
        return maxResult;
    }

    /**
     * Set the maxResult.
     * 
     * @param maxResult
     *            The maxResult to set
     */
    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    /**
     * @return Returns the queryResult.
     */
    public Collection getQueryResult() {
        return queryResult;
    }

    /**
     * @param queryResult
     *            The queryResult to set.
     */
    public void setQueryResult(Collection queryResult) {
        this.queryResult = queryResult;
    }

    /**
     * @return Returns the foundResult.
     */
    public int getFoundResult() {
        return foundResult;
    }

    /**
     * @param foundResult
     *            The foundResult to set.
     */
    public void setFoundResult(int foundResult) {
        this.foundResult = foundResult;
    }

    /**
     * @return Returns the endIndex.
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * @param endIndex
     *            The endIndex to set.
     */
    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    /**
     * @return Returns the startIndex.
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * @param startIndex
     *            The startIndex to set.
     */
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * @return Returns the nextRes.
     */
    public HtmlButton getNextRes() {
        return nextRes;
    }

    /**
     * @param nextRes
     *            The nextRes to set.
     */
    public void setNextRes(HtmlButton nextRes) {
        this.nextRes = nextRes;
    }

    /**
     * @return Returns the prevRes.
     */
    public HtmlButton getPrevRes() {
        return prevRes;
    }

    /**
     * @param prevRes
     *            The prevRes to set.
     */
    public void setPrevRes(HtmlButton prevRes) {
        this.prevRes = prevRes;
    }

    /**
     * @return Returns the search.
     */
    public HtmlButton getSearch() {
        return search;
    }

    /**
     * @param search
     *            The search to set.
     */
    public void setSearch(HtmlButton search) {
        this.search = search;
    }

    /**
     * @return Returns the atLast.
     */
    public boolean isAtLast() {
        return atLast;
    }

    /**
     * @param atLast
     *            The atLast to set.
     */
    public void setAtLast(boolean atLast) {
        this.atLast = atLast;
    }


    public int getShowResult() {
        return showResult;
    }
    
    public void setShowResult(int showResult) {
        this.showResult = showResult;
    }

    /**
     * @return Returns the isOldSearch.
     */
    public int getOldSearch() {
      return oldSearch;
    }

    /**
     * @param isOldSearch The isOldSearch to set.
     */
    public void setOldSearch(int oldSearch) {
      this.oldSearch = oldSearch;
    }
}