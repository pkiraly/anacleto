//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

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
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.struts.form.ImageLinkForm;

/** 
 * MyEclipse Struts
 * Creation date: 05-23-2005
 * 
 * XDoclet definition:
 * @struts:action path="/imageLink" name="imageLinkForm" input="/form/imageLink.jsp" scope="request" validate="true"
 */
public class ImageLinkAction extends Action {

	// --------------------------------------------------------- Instance Variables
    protected static final int BUFFER_SIZE = 256;

    private Logger log = Logging.getUserEventsLogger();
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
		
		ImageLinkForm imageLinkForm = (ImageLinkForm) form;
		
		String imageDir = Configuration.params.getConfigDir() 
					+ "/titlepages/";
		try {
			if (imageLinkForm.getBook() != null && 
					!imageLinkForm.getBook().trim().equals("")){
				HierarchicalElement el = Configuration.getElement(
							imageLinkForm.getBook());
				
				if (el != null)
					imageDir = el.getURL() + "/_images/";
			}
			
			String file = imageDir + imageLinkForm.getName();
			
			downloadMimeContent(new File(file), request, response);
		
		} catch (IOException e) {
			log.error("Error during opening of imagefile: " 
                    + imageDir + imageLinkForm.getName()
                    + ". Root cause: " + e.getMessage());
		}
		return null;
	}

    private void downloadMimeContent(File outFile, 
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        if (outFile.isDirectory())
            return;
        
        /* k.p. -- ez nem kell ide, mert ez let�lteti a file-t
        response.setHeader("Content-disposition", "attachment; filename=\""
                + outFile.getName() + "\"");
        */

        String mimeType;
        mimeType = getServlet().getServletContext().getMimeType(
                outFile.getName());
        if (mimeType == null) {
            mimeType = "image/jpeg"; // default
            // mimeType = "application/octet-stream'"; // default
        }
        response.setContentType(mimeType);

        if (!"HTTP 1.0".equals(request.getProtocol())) {
            response.setContentLength((int) outFile.length());
            response.setHeader("Transfer-Encoding", null);
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