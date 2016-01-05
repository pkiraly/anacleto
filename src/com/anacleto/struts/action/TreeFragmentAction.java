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
import com.anacleto.struts.form.TreeFragmentForm;

/**
 * MyEclipse Struts Creation date: 02-04-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/treeFragment" name="treeFragmentForm"
 *                input="/form/treeFragment.jsp" scope="request" validate="true"
 */
public class TreeFragmentAction extends Action {
  
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
   	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		
		TreeFragmentForm treeFragmentForm = (TreeFragmentForm) form;
		MenuHandler menu = new MenuHandler();
		try {
			treeFragmentForm.setMenuDesc(
				menu.getChildElements(
					treeFragmentForm.getParentStr(),
					treeFragmentForm.getOffset(),
					treeFragmentForm.getBooleanDirection(),
					treeFragmentForm.getQuery(),
					treeFragmentForm.getTitle(),
					treeFragmentForm.isSyncronized()
				)
            );
			log.info("MenuDesc: " + treeFragmentForm.getMenuDesc());
		} catch (IOException e) {
			log.error(e);
		} catch (ParseException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e);
		}
		return mapping.getInputForward();
	}
}