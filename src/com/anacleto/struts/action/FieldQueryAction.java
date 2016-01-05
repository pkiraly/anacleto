//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.struts.form.FieldQueryForm;
import com.anacleto.struts.form.SearchResultForm;

/**
 * MyEclipse Struts Creation date: 02-02-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/fieldQuery" name="fieldQueryForm"
 *                input="/form/fieldQuery.jsp" scope="request" validate="true"
 */
public class FieldQueryAction extends Action {

    // --------------------------------------------------------- Instance
    // Variables
    private FieldQueryForm fqForm;

    // --------------------------------------------------------- Methods

    /**
     * Method execute
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        fqForm = (FieldQueryForm) form;

        //TODO: Quick workaround: this part will be done in a javascript, here
        // only the validation will be done
        String query = "";
        query = addFieldToQuery(query, "bookTitle", fqForm.getTitle());
        query = addFieldToQuery(query, "author", fqForm.getAuthor());
        query = addFieldToQuery(query, "editor", fqForm.getEditor());
        query = addFieldToQuery(query, "publisher", fqForm.getPublisher());
        query = addFieldToQuery(query, "pubPlace", fqForm.getPubPlace());
        query = addFieldToQuery(query, "pubDate", fqForm.getPubDate());
        query = addFieldToQuery(query, "content", fqForm.getContent());

        //Proximity
        if (fqForm.getProximity() != null
                && !fqForm.getProximity().trim().equals("")) {
            query = query + " \"" + fqForm.getProximity() + "\"~"
                    + String.valueOf(fqForm.getProxDistance());
        }

        if (fqForm.getQueryButt() != null && fqForm.getQueryButt().pressed()) {
            SearchResultForm newForm = new SearchResultForm();
            newForm.setQuery(query);
            request.setAttribute("searchResultForm", newForm);
            return mapping.findForward("searchResults");
        }
        return mapping.getInputForward();
    }

    private String addFieldToQuery(String query, String fieldName,
            String fieldValue) {

        if (fieldValue == null || fieldValue.trim().equals("")) {
            return query;
        }

        fieldValue = fieldValue.trim();
        query = query + (fqForm.getAndLogic() ? "+" : " ") + fieldName + ":";
        if (fqForm.isExactMatch()) {
            query = query + "\"" + fieldValue + "\"";
        } else {
            query = query + "(";
            //get termList:
            String termList[] = fieldValue.split(" ");
            for (int i = 0; i < termList.length; i++) {
                query = query + " " + termList[i]
                        + (fqForm.isSimilarities() ? "~" : "");
            }
            query = query + " )";
        }

        return query;
    }

}