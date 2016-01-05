//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.struts.form.admin.StopWordsForm;

/** 
 * MyEclipse Struts
 * Creation date: 05-03-2005
 * 
 * XDoclet definition:
 * @struts:action path="/stopWords" name="stopWordsForm" input="/form/admin/stopWords.jsp" scope="request" validate="true"
 */
public class StopWordsAction extends Action {

	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws IOException, ConfigurationException {
		
		StopWordsForm stopWordsForm = (StopWordsForm) form;
		
		//Add stopword:
		if (stopWordsForm.getAddNewButt().pressed()){
			Configuration.getAnalyzer().addStopWord(	stopWordsForm.getNewStopWord());
			Configuration.getAnalyzer().saveStopWords();
			Configuration.getAnalyzer().loadStopWords();
		}
		
		//Delete stopwords
		if (stopWordsForm.getDeleteButt().pressed()){
			Configuration.getAnalyzer().deleteStopWord(
					stopWordsForm.getSelectedWords());
			Configuration.getAnalyzer().saveStopWords();
			Configuration.getAnalyzer().loadStopWords();
		}
		
		//Reload stopwords
		if (stopWordsForm.getReloadButt().pressed()){
			//config.loadStopWords();
			Configuration.getAnalyzer().loadStopWords();
		}
		
		//get stopwords:
		stopWordsForm.setStopWordList(
				Configuration.getAnalyzer().getStopSet());
		return mapping.getInputForward();
	}

}