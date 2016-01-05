//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.anacleto.struts.action;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.api.Facade;
import com.anacleto.api.QueryStats;
import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Logging;
import com.anacleto.struts.form.SearchResultForm;
import com.anacleto.util.MilliSecFormatter;

/**
 * MyEclipse Struts Creation date: 01-21-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/searchResult" name="searchResultForm"
 *                input="/form/searchResult.jsp" scope="request" validate="true"
 */
public class SearchResultAction extends Action {

    // --------------------------------------------------------- Instance
    // Variables
	Logger log = Logging.getUserEventsLogger();
	
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
            throws ConfigurationException {

        double startTime = System.currentTimeMillis();
        SearchResultForm resForm = (SearchResultForm) form;
        
        QueryStats stats = new QueryStats();
        
        List queryResults = new LinkedList();
        try {
        	
        	processCommands(resForm);
        	
        	Facade.executeQuery(resForm.getQuery(),
        					resForm.getOffset(),
        					resForm.getMaxResult(),
        					stats,
        					queryResults);
        	setOffsetAndLength(resForm, stats.getNumberOfHits());

            resForm.setQueryResult(queryResults);
        } catch (IOException e) {
            log.error(e + " query: " + resForm.getQuery());
        } catch (ParseException e1) {
            //Query not valid: do not care
        	log.error(e1 + " query: " + resForm.getQuery());
        } catch (BooleanQuery.TooManyClauses e2) {
            //To many clauses: do not care
        	log.error(e2);
        }
        
        double endTime = System.currentTimeMillis();
        resForm.setSpeed(String.valueOf((endTime - startTime) / 1000));
        log.info("User Query: " + resForm.getQuery() + ". " +
        		"Duration: " 
        		+ MilliSecFormatter.toString(((long)endTime - (long)startTime)) 
        		+ ". " +
        		"Total hits: " + resForm.getFoundResult() + ". " +
        		"Offset: " + resForm.getOffset() + ". " +
        		"Limit: " + resForm.getMaxResult() );
        return mapping.getInputForward();
    }

    
    private void processCommands(SearchResultForm resForm){
//    	if new search done, we reset the offset:
    	// getOldSearch is not 0 when it is called from hits navigation bar
    	if ( resForm.getSearch().pressed() && resForm.getOldSearch() == 0 )
    		resForm.setOffset(0);
    	
    	if (resForm.getMaxResult() == 0)
    	        resForm.setMaxResult(10);
    	 
    	int offset = resForm.getOffset();
    	int length = resForm.getMaxResult();
    	
    	//Next event: we step ahead if not proceed the max num of docs:
    	if (resForm.getNextRes().pressed())
    		resForm.setOffset( offset + length);
    	
    	//previous event:
    	if (resForm.getPrevRes().pressed()){
    		offset = (offset - length) < 0 ? 0 : offset - length;
    		resForm.setOffset(offset);
    	}
    }
    
    private void setOffsetAndLength(SearchResultForm resForm, 
                                       int numDocsFound) {
    	
     	int offset = resForm.getOffset();
    	int length = resForm.getMaxResult();
    	
    	resForm.setFoundResult(numDocsFound);
     	
    	//if no documents found, start and endindex are zero:
    	if (numDocsFound == 0)
    		offset = -1;
    	
    	resForm.setFoundResult(numDocsFound);
    	resForm.setStartIndex(offset + 1);
    	resForm.setEndIndex(offset + length);
    	if (offset + length > numDocsFound) {
    		resForm.setEndIndex(numDocsFound);
    	}
    	
    	resForm.setOffset(offset);
    	if (offset < 0)
    		resForm.setOffset(0);
    	
    	resForm.setAtLast(false);
    	if ((resForm.getStartIndex() + resForm.getMaxResult()) > numDocsFound)
    		resForm.setAtLast(true);
    }
}