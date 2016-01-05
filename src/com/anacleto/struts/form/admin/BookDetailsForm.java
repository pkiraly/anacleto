//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

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
 * @struts:form name="bookdetailsForm"
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
	
	private int offset = 0;
	
	private int length = 100;

	private int numberOfPages = 0;
	
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

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the numberOfPages
	 */
	public int getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * @param numberOfPages the numberOfPages to set
	 */
	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}


	
}