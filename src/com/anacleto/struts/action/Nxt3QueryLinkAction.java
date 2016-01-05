//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hits;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.*;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.index.IndexManager;
import com.anacleto.struts.form.Nxt3QueryLinkForm;
import com.anacleto.struts.form.SearchResultForm;
import com.anacleto.struts.form.ShowDocumentForm;

/** 
 * MyEclipse Struts
 * Creation date: 05-24-2005
 * 
 * XDoclet definition:
 * @struts:action path="/nxt3QueryLink" name="nxt3QueryLinkForm" input="/form/nxt3QueryLink.jsp" scope="request" validate="true"
 */
public class Nxt3QueryLinkAction extends Action {

	// --------------------------------------------------------- Instance Variables
	IndexManager im;
	// --------------------------------------------------------- Methods

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		
		Nxt3QueryLinkForm qlForm = (Nxt3QueryLinkForm) form;
		
		Logger log = Logging.getUserEventsLogger();
		
		try{
			
			im =  new IndexManager();	
			//query 
	        String queryString;
	        queryString = qlForm.getQ();
	        
	        if (queryString == null)
	        	queryString = qlForm.getXhitlist_q();
	        
	        //Omit: [Field ...]
	        if (queryString != null && queryString.length()>7)
	        	queryString = queryString.substring(7, queryString.length()-1);
	        else
	        	queryString = "";
	        	
	        //logger.info("Querylink field: " + queryLink);
	        	
	        	
	        //Page domain defines one or more pages to search in:
	        String queryDomain = qlForm.getD();
	        if (queryDomain == null)
	        	queryDomain = qlForm.getXhitlist_d();
	        	
	        
	        //TODO: query domain is the path where the document resides
	        //the path is decoded to a path of hexadecimal values
	        //later this has to be changed
	        if (queryDomain == null)
	        	queryDomain = "";
	        else {
	        	Book bookFound = null;
	        	String[] hitPages = queryDomain.split("/");
	        	for (int i = 0; i < hitPages.length; i++) {
					String currEl = hitPages[i];
					HierarchicalElement el = 
						Configuration.getElement(currEl);
					
					if (el!= null && el instanceof Book){
						bookFound = (Book)el;
					}
				}
	        	
	        	if (bookFound != null){
	        		queryString = queryString + " AND book:" 
						+ bookFound.getName();
	        	
	        		//last hit
	        		String lastHit = hitPages[hitPages.length-1];
	        		if (!lastHit.equals(bookFound.getName())){
	        			try{
	        				Hits hits = im.executeQuery("book:"+ bookFound.getName() + 
	        						" AND nxt3name:"+lastHit);
	        				if (hits.length() == 1){
	        					String pageString = getChildPages(hits.doc(0));
				                if (!pageString.equals(""))
				                	queryString = queryString + " AND (" 
										+ pageString + ")";
	        				}		
			                
	        			} catch (NumberFormatException e){
	        				
	        			}
	        		}
	        	}
	        	//logger.debug("Querylink domain: " + queryDomain);
	    	}

	        log.info("Querylink query: " + queryString);
	        
			Hits hits = im.executeQuery(queryString);
	    	if (hits.length() == 1){
	    		Document doc = hits.doc(0);
	    		BookPage page = new BookPage(doc);
	    		
	    		ShowDocumentForm newForm = new ShowDocumentForm();
	            newForm.setName(page.getName());
	            request.setAttribute("showDocumentForm", newForm);
	            return mapping.findForward("showDocument");
	            
	    	} else{
	    		SearchResultForm newForm = new SearchResultForm();
	            newForm.setQuery(queryString);
	            request.setAttribute("searchResultForm", newForm);
	            return mapping.findForward("searchResult");
			}
		} catch (IOException e) {
			log.error("NXT3QueryLink error. Root cause: " + e);
		} catch (ParseException e) {

		}
		return null;
	}
	
	private String getChildPages(Document doc) throws IOException {
		
		String retStr = "";
		//Document doc = im.getPage(pageName);
		Field nxt2Field = doc.getField("nxt3name");
		if (doc != null && nxt2Field != null){
			BookPage page = new BookPage(doc);
			retStr = retStr + " nxt3name:" + nxt2Field.stringValue();
			
			Collection childColl = im.findChildDocuments(page.getName());
			Iterator it = childColl.iterator();
			while (it.hasNext()) {
				Document child = (Document) it.next();
				retStr = retStr + getChildPages(child);
			}
		}
		
		return retStr;
	}
	
}