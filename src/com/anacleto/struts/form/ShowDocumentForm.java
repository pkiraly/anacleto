//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 01-22-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="showDocumentForm"
 */
public class ShowDocumentForm extends ActionForm {

    // --------------------------------------------------------- Instance
    // Variables
    
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String termList;

    private String pageBody;

    private String book;

    /**
     * the path from the root to the current document, separated by '>'
     */
    //private String path;

    /**
     * path with link at each node
     */
    private String linkedPath;

    /**
     * Document name: composed by: book_name:page_name or shelf_name
     */
    private String name;

    /**
     * The query from that the document comes
     */
    private String query;

    /**
     * Terms to highlight in the text:
     */
    private String high;

    /**
     * The current hit number of the query result
     */
    private int hitNo;
    
    /**
     * The total hit number of the query result
     */
    private int totalResult;
    
    /**
     * bookCss
     */
    private String bookContentStyleSheet;

    /**
     * Previous page
     */
    protected HtmlButton prevPage = new HtmlButton();

    /**
     * Next page
     */
    protected HtmlButton nextPage = new HtmlButton();

    /**
     * Previous query result
     */
    protected HtmlButton prevRes = new HtmlButton();

    /**
     * Next query result
     */
    protected HtmlButton nextRes = new HtmlButton();

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
    }

    /**
     * @return Returns the high.
     */
    public String getHigh() {
        return high;
    }

    /**
     * @param high
     *            The high to set.
     */
    public void setHigh(String high) {
        this.high = high;
    }

    /**
     * @return Returns the pageBody.
     */
    public String getPageBody() {
        return pageBody;
    }

    /**
     * @param pageBody
     *            The pageBody to set.
     */
    public void setPageBody(String pageBody) {
        this.pageBody = pageBody;
    }

    /**
     * @return Returns the termList.
     */
    public String getTermList() {
        return termList;
    }

    /**
     * @param termList
     *            The termList to set.
     */
    public void setTermList(String termList) {
        this.termList = termList;
    }

    /**
     * @return Returns the book.
     */
    public String getBook() {
        return book;
    }

    /**
     * @param book
     *            The book to set.
     */
    public void setBook(String book) {
        this.book = book;
    }

    /**
     * @return Returns the page.
     */
    public String getName() {
        return name;
    }

    /**
     * @param page
     *            The page to set.
     */
    public void setName(String name) {
        this.name = name;
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
     * @return Returns the nextPage.
     */
    public HtmlButton getNextPage() {
        return nextPage;
    }

    /**
     * @param nextPage
     *            The nextPage to set.
     */
    public void setNextPage(HtmlButton nextPage) {
        this.nextPage = nextPage;
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
     * @return Returns the prevPage.
     */
    public HtmlButton getPrevPage() {
        return prevPage;
    }

    /**
     * @param prevPage
     *            The prevPage to set.
     */
    public void setPrevPage(HtmlButton prevPage) {
        this.prevPage = prevPage;
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

    
    /**
     * @return Returns the linkedPath.
     */
    public String getLinkedPath() {
      return linkedPath;
    }

    /**
     * @param linkedPath The linkedPath to set.
     */
    public void setLinkedPath(String linkedPath) {
      this.linkedPath = linkedPath;
    }

    /**
     * @return Returns the totalResult.
     */
    public int getTotalResult() {
      return totalResult;
    }

    /**
     * @param totalResult The totalResult to set.
     */
    public void setTotalResult(int totalResult) {
      this.totalResult = totalResult;
    }

	public String getBookContentStyleSheet() {
		return bookContentStyleSheet;
	}

	public void setBookContentStyleSheet(String bookCss) {
		this.bookContentStyleSheet = bookCss;
	}
}