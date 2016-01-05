//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

/*
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
*/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import org.apache.lucene.document.Document;
// import org.apache.lucene.document.Field;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
// import com.anacleto.index.IndexManager;
import com.anacleto.struts.form.admin.XmlEditForm;
// import com.anacleto.view.IndexFieldView;

/**
 * MyEclipse Struts Creation date: 01-30-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/documentIndexView" name="documentIndexViewForm"
 *                input="/form/documentIndexView.jsp" scope="request"
 *                validate="true"
 */
public class XmlEditAction extends Action {

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

        XmlEditForm xmlEditForm = (XmlEditForm) form;
		String booksFile = Configuration.params.getBooksFile();
		xmlEditForm.setBooksFile(booksFile);

        
        return mapping.getInputForward();
    }

}