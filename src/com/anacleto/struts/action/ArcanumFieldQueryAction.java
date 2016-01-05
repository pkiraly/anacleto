//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.*;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.hierarchy.Shelf;
import com.anacleto.struts.form.ArcanumFieldQueryForm;
import com.anacleto.struts.form.SearchResultForm;

/** 
 * MyEclipse Struts
 * Creation date: 03-01-2005
 * 
 * XDoclet definition:
 * @struts:action path="/arcanumFieldQuery" name="arcanumFieldQueryForm" input="/form/arcanumFieldQuery.jsp" scope="request" validate="true"
 */
public class ArcanumFieldQueryAction extends Action {

    // --------------------------------------------------------- Instance
    // Variables
    private ArcanumFieldQueryForm fqForm;
    
    private Logger log = Logging.getUserEventsLogger();

    // --------------------------------------------------------- Methods

    /**
     * Method execute
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws UnsupportedEncodingException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        fqForm = (ArcanumFieldQueryForm) form;

        try {
        //TODO: Quick workaround: this part will be done in a javascript, here
        // only the validation will be done
        String query = "";
        /*
        query = addFieldToQuery(query, "creator", fqForm.getCreator());
        query = addFieldToQuery(query, "title",    fqForm.getTitle());
        
        //query = addFieldToQuery(query, "pubPlace", fqForm.getPubPlace());
        //query = addFieldToQuery(query, "pubDate", fqForm.getPubDate());
        query = addFieldToQuery(query, "picture", fqForm.getPicture());
        query = addFieldToQuery(query, "pagenumber", fqForm.getPagenumber());
        query = addFieldToQuery(query, "content", fqForm.getContent());
        */

        //get books from the selected shelves:
        Collection books = new LinkedList();
        for (int i = 0; i < fqForm.getSelectedShelves().length; i++) {
        	String shelf = fqForm.getSelectedShelves()[i];

        	HierarchicalElement el = Configuration.getElement(shelf);
			if (el != null && el instanceof Shelf){
				Collection childs = getChildBooks((Shelf)el);
				books.addAll(childs);
			}
		}
        
        String bookString = "";
        Iterator it = books.iterator();
		while (it.hasNext()) {
			Book book = (Book)it.next();
            if( ! bookString.equals("") )
                bookString = bookString + " OR ";
            bookString = bookString + " book:" + book.getName();
		}
        
        /*
        //Proximity
        if (fqForm.getProximity() != null
            && !fqForm.getProximity().trim().equals(""))
        {
            query = query 
                  + " \"" + fqForm.getProximity() + "\"~"
                  + String.valueOf(fqForm.getProxDistance());
        }
        */
        // k.p.
        query = fqForm.getQueryString(); // addFieldToQuery("", "queryString", fqForm.getQueryString());
        if( ! bookString.equals("") )
            query = "( " + bookString + " ) AND " + query;


       if (query != null && ! query.equals("") ){
            SearchResultForm newForm = new SearchResultForm();
            newForm.setQuery(query);
            request.setAttribute("searchResultForm", newForm);
            return mapping.findForward("searchResults");
       }
        
        // set shelves:
        // fqForm.setShelves(Configuration.getShelves());
        } catch (IOException e){
        	log.error(e);
        }
        return mapping.getInputForward();
    }

    /**
     * 
     * @param shelf
     * @return all books that are under the hierarchy
     */
    private Collection getChildBooks(Shelf shelf){
    	Collection retColl = new LinkedList();
    	
    	Collection childs = shelf.getChildElements();
    	Iterator it = childs.iterator();
    	while (it.hasNext()) {
			HierarchicalElement element = (HierarchicalElement) it.next();
			if (element instanceof Book)
				retColl.add((Book)element);
			else if (element instanceof Shelf){
				Collection childBooks = getChildBooks((Shelf)element);
				Iterator it2 = childBooks.iterator();
				while (it2.hasNext()) {
					Book elb = (Book) it2.next();
					retColl.add(elb);
				}
			}
		}
    	return retColl;
    }

}