//Created by MyEclipse Struts
//XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

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

import com.anacleto.base.Logging;
import com.anacleto.base.MenuHandler;
import com.anacleto.struts.form.TreeFilterFragmentForm;

/** 
* MyEclipse Struts
* Creation date: 05-16-2005
* 
* XDoclet definition:
* @struts:action path="/treeFilterFragment" name="treeFilterFragmentForm" input="/form/treeFilterFragment.jsp" scope="request" validate="true"
*/
public class TreeFilterFragmentAction extends Action {

    private Logger log = Logging.getUserEventsLogger();

    // ------------------- Instance Variables

    // ------------------- Methods

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
		
		TreeFilterFragmentForm treeFilterForm = (TreeFilterFragmentForm) form;
		MenuHandler menu = new MenuHandler();
		
		try {
			treeFilterForm.setMenuDesc(
				menu.getChildElements(
					treeFilterForm.getParentStr(),
					treeFilterForm.getOffset(),
					treeFilterForm.getBooleanDirection(),
					treeFilterForm.getQuery(),
					treeFilterForm.getTitle(),
					treeFilterForm.isSyncronized()
				)
			);
			log.info("MenuDesc: " + treeFilterForm.getMenuDesc());

        } catch (IOException e){
            System.out.println(e);
        } catch (ParseException e) {
            System.out.println(e);
        }
        return mapping.getInputForward();
    }

}