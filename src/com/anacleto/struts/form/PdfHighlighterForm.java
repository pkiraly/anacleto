//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Logging;

/**
 * MyEclipse Struts Creation date: 02-07-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="termListForm"
 */
public class PdfHighlighterForm extends ActionForm {

    /**
	 * 
	 */
	Logger log = Logging.getUserEventsLogger();
	private static final long serialVersionUID = 1L;

    private String query;
    private String name;
    private String content;

    // --------------------------------------------------------- Methods

    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
     * Method validate
     * 
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        return null;
    }

    /**
     * Method reset
     * 
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	log.info("getQueryString: " + request.getQueryString());
    	/*
    	try {
    		request = URLDecoder.decode( request, "UTF-8" )
			request.setCharacterEncoding("utf-8");
    		request.
		} catch(UnsupportedEncodingException e) {
			;
		}
		*/
    }

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
/*		
		log.info("query: " + query);
    	try {
    		this.query = URLDecoder.decode( query, "UTF-8" );
    		log.info("this.query: " + this.query);
		} catch(UnsupportedEncodingException e) {
			;
		}
*/
		this.query = query;
	}
}