/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.anacleto.struts.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.io.*;
// import java.util.LinkedList;
// import java.util.List;

import org.apache.log4j.Logger;
import com.anacleto.base.Configuration;
import com.anacleto.base.Logging;
import com.anacleto.struts.form.admin.XmlEditListFilesForm;

/** 
 * MyEclipse Struts
 * Creation date: 12-22-2006
 * 
 * XDoclet definition:
 * @struts.action path="/listFiles" name="ListFilesForm" scope="request" validate="true"
 */
public class XmlEditListFilesAction extends Action {
	/*
	 * Generated Methods
	 */
	private Logger log = Logging.getUserEventsLogger();

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		XmlEditListFilesForm listFilesForm = (XmlEditListFilesForm) form;
		
		String dirName = Configuration.params.getConfigDir();
		File dir = new File(dirName);
	    File[] files = listFiles(dir);
	    // List fileNames = new LinkedList();
	    String[] fileNames = new String[files.length];  
	    for( int i=0; i<files.length; i++ ) {
			fileNames[i] = files[i].toString()
			             .replace("\\", "/")
			             .replace(dirName, "")
			             .replace("/", "");
	    }
	    listFilesForm.setNumOfFiles(fileNames.length);
	    listFilesForm.setFileNames(fileNames);
	    
	    // return mapping.findForward("success");
	    return mapping.getInputForward();
	}
	
	private File[] listFiles(File dir) {
        File[] files = dir.listFiles(new xml());
        if (files == null)
            return new File[0];
        return files;
    }
	
	private class xml implements FileFilter {
	  public boolean accept( File file ){
	    return file.isFile() && file.getName().endsWith(".xml");
	  }
	}

}
