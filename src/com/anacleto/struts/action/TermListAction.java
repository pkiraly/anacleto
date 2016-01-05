package com.anacleto.struts.action;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.IndexReader;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Constants;
import com.anacleto.index.QueryComposer;
import com.anacleto.struts.form.SearchResultForm;
import com.anacleto.struts.form.TermListForm;

/**
 * MyEclipse Struts Creation date: 02-07-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/termList" name="termListForm"
 *                input="/form/termList.jsp" scope="request" validate="true"
 */
public class TermListAction extends Action {

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
     * @throws ConfigurationException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ConfigurationException {

        TermListForm termListForm = (TermListForm) form;

        if (termListForm.getQueryButt() != null
                && termListForm.getQueryButt().pressed()) {
            //Return to the query:
            SearchResultForm resForm = new SearchResultForm();
            //TODO it would be better get terms and fields separated in
            // a structure
            QueryComposer qC = new QueryComposer();
            
            String terms = termListForm.getSelectedTerms();
            String termQueries[] = terms.split(" OR ");
            for (int i = 0; i < termQueries.length; i++) {
				String term = termQueries[i];
				int sep = term.indexOf(":");
				if (sep > 0)
					qC.addTerm(term.substring(0, sep), term.substring(sep + 1), 
							QueryComposer.OR);
			}
            resForm.setQuery(qC.getQuery());
            
            request.setAttribute("searchResultForm", resForm);
            
            return mapping.findForward("searchResult");
        }

        
        IndexReader reader = null;
        reader = IndexReader.open(Configuration.params.getIndexDir());
        
        //Filter field list with the non technical ones:
        Collection fieldList = new LinkedList();
        Iterator it = reader.getFieldNames().iterator();
        while (it.hasNext()) {
			String currField = (String) it.next();
			if (!Constants.isReservedField(currField))
				fieldList.add(currField);
		}
        termListForm.setFieldList(fieldList);
        reader.close();
        
        return mapping.getInputForward();
    }

}