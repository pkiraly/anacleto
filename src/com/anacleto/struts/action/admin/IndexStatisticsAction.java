//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

import java.io.IOException;
import java.util.Comparator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.list.TreeList;
import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.util.PriorityQueue;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.struts.form.admin.IndexStatisticsForm;
import com.anacleto.view.TermInfo;

/**
 * MyEclipse Struts Creation date: 01-28-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/indexStatistics" name="indexStatisticsForm"
 *                input="/form/indexStatistics.jsp" scope="request"
 *                validate="true"
 */
public class IndexStatisticsAction extends Action {

    // --------------------------------------------------------- Instance
    // Variables
    private static final int NumTermsDefault = 100;
    
    private Logger log = Logging.getAdminLogger();
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
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws ConfigurationException {

        IndexStatisticsForm indexStatisticsForm = (IndexStatisticsForm) form;
        if (indexStatisticsForm.getNumTerms() == 0)
        	indexStatisticsForm.setNumTerms(NumTermsDefault);
        
        try {
            IndexReader reader = IndexReader.open(Configuration.params.getIndexDir());
            TermInfoQueue tiq = new TermInfoQueue(
                    indexStatisticsForm.getNumTerms());
            
            TermEnum terms = reader.terms();
            
            while (terms.next()) {
            	String field = terms.term().field();
            	if (!Constants.isReservedField(field))
            		tiq.insert(new TermInfo(terms.term(), terms.docFreq()));
            }

            //Map termColl = new LinkedMap();
            TreeList termColl = new TreeList();
            while (tiq.size() != 0) {
                TermInfo termInfo = (TermInfo) tiq.pop();
                termColl.add(0, termInfo);
            }

            reader.close();
            indexStatisticsForm.setTermFreq(termColl);
        } catch (IOException e) {
            log.error("Error on IndexStatistics. Root cause: " + e);
        }

        return mapping.getInputForward();
    }
}

final class TermInfoQueue extends PriorityQueue {
    TermInfoQueue(int size) {
        initialize(size);
    }

    protected final boolean lessThan(Object a, Object b) {
        TermInfo termInfoA = (TermInfo) a;
        TermInfo termInfoB = (TermInfo) b;
        return termInfoA.getDocFreq() < termInfoB.getDocFreq();
    }
}

final class TermInfoComparator implements Comparator {

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object a, Object b) {
        TermInfo termInfoA = (TermInfo) a;
        TermInfo termInfoB = (TermInfo) b;
        if (termInfoA.getDocFreq() < termInfoB.getDocFreq()) {
            return -1;
        } else if (termInfoA.getDocFreq() == termInfoB.getDocFreq()) {
            return 0;
        } else {
            return 1;
        }
    }
}