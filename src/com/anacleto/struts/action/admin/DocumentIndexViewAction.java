//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Logging;
import com.anacleto.index.IndexManager;
import com.anacleto.struts.form.admin.DocumentIndexViewForm;
import com.anacleto.view.IndexFieldView;

/**
 * MyEclipse Struts Creation date: 01-30-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/documentIndexView" name="documentIndexViewForm"
 *                input="/form/documentIndexView.jsp" scope="request"
 *                validate="true"
 */
public class DocumentIndexViewAction extends Action {

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
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws ConfigurationException {

        DocumentIndexViewForm indexForm = (DocumentIndexViewForm) form;
        
        try {
            //fsDir = FSDirectory.getDirectory(indexDir, false);
            IndexManager im = new IndexManager();
            Document doc = im.findNamedElement(indexForm.getName());
            if (doc == null) {
                return mapping.findForward("errorForward");
            }

            Collection fieldMap = new LinkedList();

            //Document doc = hits.doc(0);
            Enumeration fields = doc.fields();
            while (fields.hasMoreElements()) {
                Field currField = (Field) fields.nextElement();
                
                IndexFieldView currIndexField = new IndexFieldView();
                currIndexField.setName(currField.name());
                currIndexField.setValue(currField.stringValue());
                currIndexField.setIndexed(currField.isIndexed());
                currIndexField.setStored(currField.isStored());
                currIndexField.setTokenized(currField.isTokenized());
                currIndexField.setTermVectorStored(
                		currField.isTermVectorStored());

                fieldMap.add(currIndexField);
            }
            indexForm.setFieldMap(fieldMap);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return mapping.findForward("errorForward");
        }

        return mapping.getInputForward();
    }

}