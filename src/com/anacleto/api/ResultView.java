/*
 * Created on Jan 21, 2005
 *
 */
package com.anacleto.api;

import com.anacleto.hierarchy.BookPage;

/**
 * @author robi
 * 
 */
public class ResultView {

    private String name;

    private String url;

    private BookPage page;

    private String contextList;

    private String score;

    /**
     * Hit number
     */
    private int hitNo;

    /**
     * Hit number to display
     */
    private int dispHitNo;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Returns the score.
     */
    public String getScore() {
        return score;
    }

    /**
     * @param score
     *            The score to set.
     */
    public void setScore(String score) {
        this.score = score;
    }

    /**
     * @return Returns the page.
     */
    public BookPage getPage() {
        return page;
    }

    /**
     * @param page
     *            The page to set.
     */
    public void setPage(BookPage page) {
        this.page = page;
    }

    /**
     * @return Returns the contextList.
     */
    public String getContextList() {
        return contextList;
    }

    /**
     * @param contextList
     *            The contextList to set.
     */
    public void setContextList(String contextList) {
        this.contextList = contextList;
    }

    /**
     * @return Returns the hitNo.
     */
    public int getHitNo() {
        return hitNo;
    }

    /**
     * @param hitNo
     *            The hitNo to set.
     */
    public void setHitNo(int hitNo) {
        this.hitNo = hitNo;
    }

    /**
     * @return Returns the dispHitNo.
     */
    public int getDispHitNo() {
        return dispHitNo;
    }

    /**
     * @param dispHitNo
     *            The dispHitNo to set.
     */
    public void setDispHitNo(int dispHitNo) {
        this.dispHitNo = dispHitNo;
    }
}