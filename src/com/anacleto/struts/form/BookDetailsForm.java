//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.hierarchy.Book;

/** 
 * MyEclipse Struts
 * Creation date: 05-01-2005
 * 
 * XDoclet definition:
 * @struts:form name="bookDetailsForm"
 */
public class BookDetailsForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** pages property */
	private Collection pages;

	/** book property */
	private Book book;

	/** bookName property */
	private String bookName;

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

		throw new UnsupportedOperationException(
			"Generated method 'validate(...)' not implemented.");
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		throw new UnsupportedOperationException(
			"Generated method 'reset(...)' not implemented.");
	}

	/** 
	 * Returns the pages.
	 * @return Collection
	 */
	public Collection getPages() {
		return pages;
	}

	/** 
	 * Set the pages.
	 * @param pages The pages to set
	 */
	public void setPages(Collection pages) {
		this.pages = pages;
	}

	/** 
	 * Returns the book.
	 * @return Book
	 */
	public Book getBook() {
		return book;
	}

	/** 
	 * Set the book.
	 * @param book The book to set
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	/** 
	 * Returns the bookName.
	 * @return String
	 */
	public String getBookName() {
		return bookName;
	}

	/** 
	 * Set the bookName.
	 * @param bookName The bookName to set
	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

}