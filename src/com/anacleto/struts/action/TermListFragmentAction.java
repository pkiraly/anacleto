//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Logging;
import com.anacleto.index.IndexStatistics;
import com.anacleto.index.PositionTermList;
import com.anacleto.struts.form.TermListFragmentForm;


/**
 * MyEclipse Struts Creation date: 02-07-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/termListFragment" name="termListFragmentForm"
 *                input="/form/termListFragment.jsp" scope="request"
 *                validate="true"
 */
public class TermListFragmentAction extends Action {

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
     * @throws IOException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws ConfigurationException, IOException {

    	Logger logger = Logging.getUserEventsLogger();
    	TermListFragmentForm tLForm = (TermListFragmentForm) form;
        
        
        String pattern = "";//= "*";
        if ( tLForm.getPattern() != null )
            pattern = tLForm.getPattern();
        
        //Set field to the default query field:
        String field = tLForm.getSelectedField();
        
		logger.info("field: " + field);
        if (field == null 
        		|| tLForm.getSelectedField().trim().length() == 0)
        	field = Configuration.getDefaultQueryField();
        
    	LinkedList dispColl;
    	if ( pattern.indexOf("*") == -1 && pattern.indexOf("?") == -1){
	        //Position to a term:
    		
    		//handle paging:
    		if ((tLForm.getNextButt().pressed()
            		|| (tLForm.getPrevButt().pressed()))
            		&& tLForm.getOffset() != null)
        		pattern = tLForm.getOffset();
    		
    		PositionTermList pt = new PositionTermList();
    		logger.info("field: " + field + ", " + "pattern: " + pattern);
    		dispColl = pt.getTermsNear(field, pattern, 30);
    		
	    } else {
	    	IndexStatistics is = new IndexStatistics();
	    	
	    	int pageNo = tLForm.getPageNumber();
	    	if (tLForm.getNextButt().pressed())
	    		pageNo++;
	    	else if (tLForm.getPrevButt().pressed())
	    		pageNo--;
	    	
	    	if (pageNo < 0)
	    		pageNo = 0;
	    	tLForm.setPageNumber(pageNo);
	    	
	        //Filter for a wildcard
	    	dispColl = is.getFilteredTerms(field, pattern, "", (pageNo+1) * 30);
	    	for (int i=0; i<pageNo*30; i++){
	    		if (dispColl.size() > 0)
	    			dispColl.remove(0);
	    	}
	    	
	    }

    	tLForm.setTermColl(dispColl);
    	
        return mapping.getInputForward();
    }


}
