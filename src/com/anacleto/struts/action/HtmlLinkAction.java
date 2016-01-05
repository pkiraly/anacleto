//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.9.210/xslt/JavaClass.xsl

package com.anacleto.struts.action;

import java.io.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Logging;
import com.anacleto.struts.form.HtmlLinkForm;

/** 
 * MyEclipse Struts
 * Creation date: 11-21-2005
 * 
 * XDoclet definition:
 * @struts:action path="/htmlLink" name="htmlLinkForm" input="/form/htmlLink.jsp" scope="request" validate="true"
 */
public class HtmlLinkAction extends Action {

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
		
		
		HtmlLinkForm linkForm = (HtmlLinkForm) form;
		
		downloadMimeContent(new File(linkForm.getLocation()), request, response);
		
		return null;
	}

    private void downloadMimeContent(File outFile, 
            HttpServletRequest request, HttpServletResponse response) {
        
        if (outFile.isDirectory())
            return;
        
        Logger logger = Logging.getUserEventsLogger();
        
        String mimeType;
        mimeType = getServlet().getServletContext().getMimeType(
                outFile.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream'"; // default
        }
        response.setContentType(mimeType);

        if (!"HTTP 1.0".equals(request.getProtocol())) {
            response.setContentLength((int) outFile.length());
            response.setHeader("Transfer-Encoding", null);
        }

        OutputStream out = null;
        InputStream inStream = null; 
        try {
            out = response.getOutputStream();
            inStream = new FileInputStream(outFile);
            
            byte buffer[] = new byte[1024];
            int n = 0;
            while (true) {
                n = inStream.read(buffer);

                if (n < 1)
                    break;
                out.write(buffer, 0, n);
            }
            
        } catch (IOException e) {
        	logger.error(e);
        } finally {
        	if (inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {
				}
        	}
        	if (out != null){
                try {
					out.flush();
					out.close();
				} catch (IOException e) {
				}
        	}
        	
        }

    }
}

