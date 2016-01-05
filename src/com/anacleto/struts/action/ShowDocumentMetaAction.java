//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.anacleto.struts.action;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hits;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.index.IndexManager;
import com.anacleto.struts.form.ShowDocumentMetaForm;
import com.anacleto.util.MilliSecFormatter;

/**
 * Action to show a document(html page) MyEclipse Struts Creation date:
 * 01-22-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/showDocument" name="showDocumentForm"
 *                input="/form/showDocument.jsp" scope="request" validate="true"
 */
public class ShowDocumentMetaAction extends Action {

    protected static final int BUFFER_SIZE = 256;
 
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
     * @throws ConfigurationException
     * @throws UnsupportedEncodingException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

    	Logger logger = Logging.getUserEventsLogger();
    	
        long start = System.currentTimeMillis();
        ShowDocumentMetaForm sDForm = (ShowDocumentMetaForm) form;

        try {
            HierarchicalElement currEl = Configuration.getElement(sDForm.getName());

            sDForm.setPrevNextDocNavigation(false);
            //Previous page
            if (sDForm.getPrevPage().pressed()) {
                currEl = currEl.getPrevElement();
                sDForm.setPrevNextDocNavigation(true);
            }

            //Next page
            else if (sDForm.getNextPage().pressed()) {
                currEl = currEl.getNextElement();
                sDForm.setPrevNextDocNavigation(true);
            }

        	//Process Query related page commands:
            if (sDForm.getQuery() != null
                    && !sDForm.getQuery().trim().equals("")) {
                IndexManager im = new IndexManager();
                Hits hits = im.executeQuery(sDForm.getQuery());
                
                if( sDForm.getHitNo() < 1 ) {
                    sDForm.setTotalResult(hits.length());
                    
                    int docNumber = im.getPageNumber(currEl.getName());
                    if (docNumber > 0){
                        for( int i=0; i<hits.length(); i++) {
                        	if (hits.id(i) == docNumber){
                        		sDForm.setHitNo(i);
                        		break;
                        	}
	                        /*
	                    	doc = new BookPage(hits.doc(i));
	                        if( doc.getName().equals(currEl.getName()) ) {
	                            sDForm.setHitNo(i);
	                            break;
	                        }
	                        */
	                    }
                    }
                }

                if (sDForm.getPrevRes().pressed()) {
                    //Previous result
                    if (sDForm.getHitNo() > 0)
                        sDForm.setHitNo(sDForm.getHitNo() - 1);
                    
                    currEl = new BookPage(hits.doc(sDForm.getHitNo()));

                } else if (sDForm.getNextRes().pressed()) {
                    //Next result
                    if (sDForm.getHitNo() < hits.length() - 1)
                        sDForm.setHitNo(sDForm.getHitNo() + 1);
                    currEl = new BookPage(hits.doc(sDForm.getHitNo()));
                }

                sDForm.setAtLast(false);
                if (hits.length() == sDForm.getHitNo() + 1)
                    sDForm.setAtLast(true);
            }

            while (currEl.isFirstChildContent()) {
                Collection coll = currEl.getChildElements();
                Iterator it = coll.iterator();
                if (it.hasNext()) {
                    currEl = (HierarchicalElement) it.next();
                } else {
                	logger.error("Element: " + currEl.getName() + 
                			" is firstChildContent but has no childs");
                	break;
                }
            }

            sDForm.setName(currEl.getName());
            if (currEl instanceof BookPage) {
            	BookPage el = (BookPage)currEl;
            	sDForm.setBook(el.getBookName());
            	
			} else if (currEl instanceof Book) {
				Book el = (Book)currEl;
            	sDForm.setBook(el.getBookName());
			}
            
            sDForm.setContentType(currEl.getContentType());
            
            sDForm.setLinkedPath(
            	Configuration.getElementLinkedPath(sDForm.getName())
                + " " 
                + currEl.getTitle()
            );

        } catch (IOException e) {
            //Content was not found
        	logger.error(e);
            return null;
        } catch (ParseException e) {
        	logger.error("Malformed query: " + sDForm.getQuery() + "." +
        		" Error: " + e);
		}

        logger.info("Document meta served: " + sDForm.getName() + "." +
       		" Duration: " + MilliSecFormatter.toString(
       				(long)(System.currentTimeMillis() - start)) + "." +
       		" Query: " + sDForm.getQuery());
        
        return mapping.getInputForward();
    }
}