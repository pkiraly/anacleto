//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 05-24-2005
 * 
 * XDoclet definition:
 * @struts:form name="nxt3QueryLinkForm"
 */
public class Nxt3QueryLinkForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** d property */
	private String d;

	/** xhitlist_d property */
	private String xhitlist_d;

	/** xhitlist_q property */
	private String xhitlist_q;

	/** q property */
	private String q;

	// --------------------------------------------------------- Methods

	

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	/** 
	 * Returns the d.
	 * @return String
	 */
	public String getD() {
		return d;
	}

	/** 
	 * Set the d.
	 * @param d The d to set
	 */
	public void setD(String d) {
        this.d = d;
	}

	/** 
	 * Returns the xhitlist_d.
	 * @return String
	 */
	public String getXhitlist_d() {
		return xhitlist_d;
	}

	/** 
	 * Set the xhitlist_d.
	 * @param xhitlist_d The xhitlist_d to set
	 */
	public void setXhitlist_d(String xhitlist_d) {
		this.xhitlist_d = xhitlist_d;
	}

	/** 
	 * Returns the xhitlist_q.
	 * @return String
	 */
	public String getXhitlist_q() {
		return xhitlist_q;
	}

	/** 
	 * Set the xhitlist_q.
	 * @param xhitlist_q The xhitlist_q to set
	 */
	public void setXhitlist_q(String xhitlist_q) {
		this.xhitlist_q = xhitlist_q;
	}

	/** 
	 * Returns the q.
	 * @return String
	 */
	public String getQ() {
		return q;
	}

	/** 
	 * Set the q.
	 * @param q The q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}

}