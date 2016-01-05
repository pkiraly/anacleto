//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 02-02-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="fieldQueryForm"
 */
public class FieldQueryForm extends ActionForm {

    // --------------------------------------------------------- Instance
    // Variables

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** andLogic property */
    private boolean andLogic;

    private boolean similarities;

    private boolean exactMatch;

    /** note property */
    private String title;

    private String author;

    private String editor;

    private String publisher;

    private String pubPlace;

    private String pubDate;

    private String content;

    /** command property */
    protected HtmlButton queryButt = new HtmlButton();

    /** command property */
    private String query;

    private String proximity;

    private int proxDistance = 10;

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
		} catch (UnsupportedEncodingException e) {
			;
		}
        
    }

    /**
     * Returns the andLogic.
     * 
     * @return Boolean
     */
    public boolean getAndLogic() {
        return andLogic;
    }

    /**
     * Set the andLogic.
     * 
     * @param andLogic
     *            The andLogic to set
     */
    public void setAndLogic(boolean andLogic) {
        this.andLogic = andLogic;
    }

    /**
     * @return Returns the queryButt.
     */
    public HtmlButton getQueryButt() {
        return queryButt;
    }

    /**
     * @param queryButt
     *            The queryButt to set.
     */
    public void setQueryButt(HtmlButton queryButt) {
        this.queryButt = queryButt;
    }

    /**
     * @return Returns the query.
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query
     *            The query to set.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the similarities.
     */
    public boolean isSimilarities() {
        return similarities;
    }

    /**
     * @param similarities
     *            The similarities to set.
     */
    public void setSimilarities(boolean similarities) {
        this.similarities = similarities;
    }

    /**
     * @return Returns the exactMatch.
     */
    public boolean isExactMatch() {
        return exactMatch;
    }

    /**
     * @param exactMatch
     *            The exactMatch to set.
     */
    public void setExactMatch(boolean exactMatch) {
        this.exactMatch = exactMatch;
    }

    /**
     * @return Returns the content.
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            The content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return Returns the proximity.
     */
    public String getProximity() {
        return proximity;
    }

    /**
     * @param proximity
     *            The proximity to set.
     */
    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    /**
     * @return Returns the proxDistance.
     */
    public int getProxDistance() {
        return proxDistance;
    }

    /**
     * @param proxDistance
     *            The proxDistance to set.
     */
    public void setProxDistance(int proxDistance) {
        this.proxDistance = proxDistance;
    }

    /**
     * @return Returns the author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     *            The author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return Returns the editor.
     */
    public String getEditor() {
        return editor;
    }

    /**
     * @param editor
     *            The editor to set.
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * @return Returns the pubDate.
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * @param pubDate
     *            The pubDate to set.
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * @return Returns the publisher.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher
     *            The publisher to set.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return Returns the pubPlace.
     */
    public String getPubPlace() {
        return pubPlace;
    }

    /**
     * @param pubPlace
     *            The pubPlace to set.
     */
    public void setPubPlace(String pubPlace) {
        this.pubPlace = pubPlace;
    }
}