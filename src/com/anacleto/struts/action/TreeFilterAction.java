//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

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

import com.anacleto.base.Configuration;
import com.anacleto.base.Logging;
import com.anacleto.base.MenuHandler;
import com.anacleto.struts.form.TreeFilterForm;

/** 
 * MyEclipse Struts
 * Creation date: 05-16-2005
 * 
 * XDoclet definition:
 * @struts:action path="/treeFilter" name="treeFilterForm" input="/form/treeFilter.jsp" scope="request" validate="true"
 */
public class TreeFilterAction extends Action {
  
    private Logger log = Logging.getUserEventsLogger();
	private static int maxTocEntryNumber = 
		Configuration.params.getMaxTocEntryNumber();


	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

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
		
		TreeFilterForm treeFilterForm = (TreeFilterForm) form;
		MenuHandler menu = new MenuHandler();

        try {
        	treeFilterForm.setMaxTocEntryNumber(maxTocEntryNumber);
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
        } catch (ArrayIndexOutOfBoundsException e){
          log.error( "ArrayIndexOutOfBoundsException" + e);
        } catch (IOException e){
          log.error(e);
        } catch (ParseException e) {
          log.error(e);
        }
		return mapping.getInputForward();
	}

}