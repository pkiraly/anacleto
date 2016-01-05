/*
 * Created on May 5, 2005
 *
 */
package com.anacleto.base;

import java.io.File;

/**
 * Bean to store logging related parameters
 * @author robi
 */
public class LoggingParameters {

	//Log directory
	private String logDir;
	
	private String adminFile		  = "admin.log";
	private String adminLogLevel      = "DEBUG";
	
	private String usereventsFile     = "userevents.log";
	private String usereventsLogLevel = "DEBUG";

	private String indexingFile       = "indexing.log";
	private String indexingLogLevel   = "DEBUG";

	
	public LoggingParameters(){
	}
	
	public LoggingParameters(String logDir){
		this.logDir = logDir;
	}
	
	public boolean validate() throws ConfigurationException{
		File logDirectory = new File(logDir);
		
		if (!logDirectory.exists())
			throw new ConfigurationException("Inexistent logging directory: "
					+ logDir +". Please create it or specify an other one.");
		
		return true;
	}
		
	/**
	 * @return Returns the indexingLogLevel.
	 */
	public String getIndexingLogLevel() {
		return indexingLogLevel;
	}
	
	/**
	 * @param indexingLogLevel The indexingLogLevel to set.
	 */
	public void setIndexingLogLevel(String indexingLogLevel) {
		if (indexingLogLevel != null)
			this.indexingLogLevel = indexingLogLevel;
	}
	/**
	 * @return Returns the logDir.
	 */
	public String getLogDir() {
		return logDir;
	}
	/**
	 * @param logDir The logDir to set.
	 */
	public void setLogDir(String logDir) {
		if (logDir != null)
			this.logDir = logDir;
	}
	
	/**
	 * @return Returns the usereventsFile.
	 */
	public String getUsereventsFile() {
		return usereventsFile;
	}
	
	/**
	 * @return Returns the usereventsFile.
	 */
	public String getUsereventsFileAbs() {
		return logDir + "/" + usereventsFile;
	}
	
	/**
	 * @param usereventsFile The usereventsFile to set.
	 */
	public void setUsereventsFile(String usereventsFile) {
		if (usereventsFile != null)
			this.usereventsFile = usereventsFile;
	}
	/**
	 * @return Returns the usereventsLogLevel.
	 */
	public String getUsereventsLogLevel() {
		return usereventsLogLevel;
	}
	/**
	 * @param usereventsLogLevel The usereventsLogLevel to set.
	 */
	public void setUsereventsLogLevel(String usereventsLogLevel) {
		if (usereventsLogLevel != null)
			this.usereventsLogLevel = usereventsLogLevel;
	}
	
	
	/**
	 * @return Returns the adminFile.
	 */
	public String getAdminFile() {
		return adminFile;
	}
	
	/**
	 * @return Returns the adminFile.
	 */
	public String getAdminFileAbs() {
		return logDir + "/" + adminFile;
	}
	
	/**
	 * @param adminFile The adminFile to set.
	 */
	public void setAdminFile(String adminFile) {
		if (adminFile != null)
			this.adminFile = adminFile;
	}
	/**
	 * @return Returns the adminLogLevel.
	 */
	public String getAdminLogLevel() {
		return adminLogLevel;
	}
	/**
	 * @param adminLogLevel The adminLogLevel to set.
	 */
	public void setAdminLogLevel(String adminLogLevel) {
		if (adminLogLevel != null)
			this.adminLogLevel = adminLogLevel;
	}
	/**
	 * @return Returns the indexingFile.
	 */
	public String getIndexingFile() {
		return indexingFile;
	}
	
	/**
	 * @return Returns the adminFile.
	 */
	public String getIndexingFileAbs() {
		return logDir + "/" + indexingFile;
	}
	
	/**
	 * @param indexingFile The indexingFile to set.
	 */
	public void setIndexingFile(String indexingFile) {
		if (indexingFile != null)
			this.indexingFile = indexingFile;
	}
	
	public String getAbs(String rel){
		return logDir + "/" + rel;
	}
}
