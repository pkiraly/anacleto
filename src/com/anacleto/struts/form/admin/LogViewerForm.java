//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.form.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.LoggingParameters;
import com.anacleto.struts.form.HtmlButton;

/** 
 * MyEclipse Struts
 * Creation date: 05-06-2005
 * 
 * XDoclet definition:
 * @struts:form name="logViewerForm"
 */
public class LogViewerForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** indexingButt property */
	private HtmlButton indexingButt = new HtmlButton();

	/** usereventButt property */
	private HtmlButton usereventButt = new HtmlButton();

	/** adminButt property */
	private HtmlButton adminButt = new HtmlButton();

	private String     pageBody;
	
	private Collection logLines = new ArrayList();
	
	private LoggingParameters logParms 
			= Configuration.params.getLogParams();
	
	private int offset = 0;
	
	private int length = 100;

	private int numberOfPages = 0;
	
	private String logFileType = "userevents";

	private String logFileName = "";

	private String logLevel = "INFO";
	
	private Collection logFiles = new ArrayList();

	private Collection logFileTypes = new ArrayList();

	private Collection usereventsLogFiles = new ArrayList();
	private Collection adminLogFiles = new ArrayList();
	private Collection indexingLogFiles = new ArrayList();

	// --------------------------------------------------------- Methods

	
	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	/** 
	 * Returns the indexingButt.
	 * @return HtmlButton
	 */
	public HtmlButton getIndexingButt() {
		return indexingButt;
	}

	/** 
	 * Set the indexingButt.
	 * @param indexingButt The indexingButt to set
	 */
	public void setIndexingButt(HtmlButton indexingButt) {
		setLogFileType("indexing");
		this.indexingButt = indexingButt;
	}

	/** 
	 * Returns the usereventButt.
	 * @return HtmlButton
	 */
	public HtmlButton getUsereventButt() {
		return usereventButt;
	}

	/** 
	 * Set the usereventButt.
	 * @param usereventButt The usereventButt to set
	 */
	public void setUsereventButt(HtmlButton usereventButt) {
		setLogFileType("userevent");
		this.usereventButt = usereventButt;
	}

	/** 
	 * Returns the adminButt.
	 * @return HtmlButton
	 */
	public HtmlButton getAdminButt() {
		return adminButt;
	}

	/** 
	 * Set the adminButt.
	 * @param adminButt The adminButt to set
	 */
	public void setAdminButt(HtmlButton adminButt) {
		setLogFileType("admin");
		this.adminButt = adminButt;
	}

	
	/**
	 * @return Returns the logParms.
	 */
	public LoggingParameters getLogParms() {
		return logParms;
	}
	/**
	 * @param logParms The logParms to set.
	 */
	public void setLogParms(LoggingParameters logParms) {
		this.logParms = logParms;
	}
	
	
	/**
	 * @return Returns the pageBody.
	 */
	public String getPageBody() {
		return pageBody;
	}
	/**
	 * @param pageBody The pageBody to set.
	 */
	public void setPageBody(String pageBody) {
		this.pageBody = pageBody;
	}

	public Collection getLogLines() {
		return logLines;
	}

	public void setLogLines(Collection logLines) {
		this.logLines = logLines;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getLogFileType() {
		return logFileType;
	}

	public void setLogFileType(String logFileType) {
		this.logFileType = logFileType;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public Collection getLogFiles() {
		return logFiles;
	}

	public void setLogFiles(Collection logFiles) {
		this.logFiles = logFiles;
	}

	public Collection getAdminLogFiles() {
		return adminLogFiles;
	}

	public void setAdminLogFiles(Collection adminLogFiles) {
		this.adminLogFiles = adminLogFiles;
	}

	public Collection getIndexingLogFiles() {
		return indexingLogFiles;
	}

	public void setIndexingLogFiles(Collection indexingLogFiles) {
		this.indexingLogFiles = indexingLogFiles;
	}

	public Collection getUsereventsLogFiles() {
		return usereventsLogFiles;
	}

	public void setUsereventsLogFiles(Collection usereventsLogFiles) {
		this.usereventsLogFiles = usereventsLogFiles;
	}

	public Collection getLogFileTypes() {
		return logFileTypes;
	}

	public void setLogFileTypes(Collection logFileTypes) {
		this.logFileTypes = logFileTypes;
	}
	
	
}