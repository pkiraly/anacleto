package com.anacleto.struts.action;

import java.io.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.Logging;
import com.anacleto.struts.form.CssLinkForm;

/**
 * MyEclipse Struts Creation date: 05-23-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/cssLink" name="cssLinkForm" input="/form/cssLink.jsp"
 *                scope="request" validate="true"
 */
public class CssLinkAction extends Action {

	// --------------------------------------------------------- Instance
	// Variables
	protected static final int BUFFER_SIZE = 256;

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

		CssLinkForm cssLinkForm = (CssLinkForm) form;

		String cssDir = Configuration.params.getConfigDir() + "/css/";
		String file = cssDir + cssLinkForm.getName();

		try {
			downloadMimeContent(new File(file), request, response);
		} catch (IOException e) {
			Logger log = Logging.getUserEventsLogger();
			log.error("Error during opening of css file at the location: "
					+ file + ". Root cause: " + e.getMessage());
		}

		return null;
	}

	private void downloadMimeContent(File outFile, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (!outFile.isFile())
			throw new IOException("Css file doesn't exists");

		/*
		 * response.setHeader("Content-disposition", "attachment; filename=\"" +
		 * outFile.getName() + "\"");
		 */

		String mimeType;
		mimeType = "text/css"; // default
		// response.setContentType(mimeType);
		response.setHeader("Content-Type", mimeType);
		response.setContentType(mimeType);

		if (!"HTTP 1.0".equals(request.getProtocol())) {
			response.setContentLength((int) outFile.length());
			// response.setHeader("Transfer-Encoding", null);
		}

		OutputStream out = response.getOutputStream();
		InputStream inStream = new FileInputStream(outFile);

		byte buffer[] = new byte[BUFFER_SIZE];
		int n = 0;
		while (true) {
			n = inStream.read(buffer);

			if (n < 1)
				break;
			out.write(buffer, 0, n);
		}
		inStream.close();
		out.flush();
		out.close();

	}
}