//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.anacleto.struts.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.*;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.struts.form.TreeViewForm;

/**
 * MyEclipse Struts Creation date: 01-21-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/treeView" name="treeViewForm"
 *                input="/form/treeView.jsp" scope="request" validate="true"
 */
public class TreeViewAction extends Action {

	private static int maxTocEntryNumber = 
		Configuration.params.getMaxTocEntryNumber();

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
            HttpServletRequest request, HttpServletResponse response) {

    	Logger log = Logging.getUserEventsLogger();
    	
        TreeViewForm treeViewForm = (TreeViewForm) form;
		try {

	        MenuHandler menu = new MenuHandler();
            if (!treeViewForm.getParentStr().equals("root")) {
                HierarchicalElement el = Configuration.getElement(treeViewForm
                        .getParentStr());
                treeViewForm.setTreeDesc(
                    menu.syncToElement(el, treeViewForm.getParentStr(), 50));
                treeViewForm.setName( treeViewForm.getParentStr() );
            } else {
                treeViewForm.setTreeDesc(
                    menu.getChildElements(
                        treeViewForm.getParentStr(), 
                        treeViewForm.getOffset(), 
                        treeViewForm.getBooleanDirection(), 
                        treeViewForm.getQuery(),
                        treeViewForm.getTitle(),
                        treeViewForm.isSyncronized()
                        ));

            }
            treeViewForm.setMaxTocEntryNumber(maxTocEntryNumber);

		} catch (IOException e) {
			log.error(e);
        } catch (ParseException e) {
        	log.error(e);
		}
		
        return mapping.getInputForward();
    }

}