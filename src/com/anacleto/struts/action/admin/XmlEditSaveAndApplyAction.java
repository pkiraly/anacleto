//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

/*
 import java.io.IOException;
 import java.util.Collection;
 import java.util.Enumeration;
 import java.util.LinkedList;
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import org.apache.lucene.document.Document;
// import org.apache.lucene.document.Field;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.struts.form.admin.XmlEditSaveAndApplyForm;

/**
 * MyEclipse Struts Creation date: 01-30-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/documentIndexView" name="documentIndexViewForm"
 *                input="/form/documentIndexView.jsp" scope="request"
 *                validate="true"
 */
public class XmlEditSaveAndApplyAction extends Action {

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

		XmlEditSaveAndApplyForm saveXmlForm = (XmlEditSaveAndApplyForm) form;
		String fileName = saveXmlForm.getFileName();

		String dirName = Configuration.params.getConfigDir();
		String path = dirName + "/" + fileName;
		ActionMessages errors = new ActionMessages();

		String err;
		try {
			FileOutputStream outFile = new FileOutputStream(path);
			String content = saveXmlForm.getContent();
			err = writeContent(outFile, "utf-8", content);
			if (err != "") {
				errors.add("errors.writecontent", new ActionMessage(err, ""));
			}

			Configuration conf = new Configuration();
			Configuration.params.setBooksFile(fileName);

			Configuration.saveParams();
			conf.destroy();
			conf.init();

		} catch (FileNotFoundException e) {
			errors.add("errors.filenotfound", new ActionMessage(
					"FileNotFoundException", ""));

		} catch (ConfigurationException e) {
			errors.add("errors.configuration", new ActionMessage(
					"ConfigurationException", ""));
		}

		if (errors.isEmpty() == false) {
			saveErrors(request, errors);
		} else {
			request.setAttribute("success", "1");
		}

		return mapping.getInputForward();
	}

	private String writeContent(FileOutputStream outFile, String code,
			String content) {
		String err;

		try {
			Writer out = new OutputStreamWriter(outFile, code);
			out.write(content);
			out.close();
			err = "";
		} catch (IOException e) {
			err = "IOException";
		}
		return err;
	}
}