//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.index.IndexManager;
import com.anacleto.struts.form.admin.BookDetailsForm;

/**
 * MyEclipse Struts Creation date: 05-01-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/bookDetails" name="bookDetailsForm"
 *                input="/form/admin/bookDetails.jsp" scope="request"
 *                validate="true"
 */
public class BookDetailsAction extends Action {

	// --------------------------------------------------------- Instance
	// Variables

	// --------------------------------------------------------- Methods

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		BookDetailsForm bookdetailsForm = (BookDetailsForm) form;

		IndexManager im = new IndexManager();
		HierarchicalElement he = Configuration.getElement(bookdetailsForm
				.getBookName());
		if (he instanceof Book) {
			bookdetailsForm.setBook((Book) he);
		}

		// Get bookpages:
		Collection pages = new LinkedList();
		Hits hits = im.getBookEntries(bookdetailsForm.getBookName());
		
		bookdetailsForm.setNumberOfPages(hits.length());

		int limit = bookdetailsForm.getOffset() + bookdetailsForm.getLength();
		if (limit > hits.length()) {
			limit = hits.length();
		}

		for (int i = bookdetailsForm.getOffset(); i < limit; i++) {
			Document currDoc = hits.doc(i);
			BookPage currPage = new BookPage(currDoc);
			pages.add(currPage);
		}
		bookdetailsForm.setPages(pages);

		return mapping.getInputForward();
	}
}