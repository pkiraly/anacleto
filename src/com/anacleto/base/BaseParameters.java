/*
 * Created on May 2, 2005
 *
 */
package com.anacleto.base;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Base parameters
 * @author robi
 *
 */
public class BaseParameters {

	//Security levels:
	//no authentication
	public static final int noSecurity = 0;
	
	//administration tasks are authenticated   
	public static final int adminSecurity = 1;
	
	//all are authenticated 
	public static final int fullSecurity = 2;
	
	
	//base parameters:
    private String  indexDir;
    private String 	configDir;
    
    private int     indexingPriority = 5;
    
    //files, relative to the config. dir:
    private String  booksFile = "books.xml";
    
    
    private String  passwordFile = "password";
    
    private int     securityLevel = noSecurity;
    
    //locale for ordering:
    private Locale  locale;
    
    private String  defaultContentStyleSheet;
    private String  defaultHtmlIndexingscheme;
    
    private String  defaultTei2IndexingStyleSheet;
    private String  defaultXmlDisplayScheme;
    
    private int     sortType = Constants.SORT_BY_RELEVANCE;
    /**
     * Maximum number of toc entries to display when a node is opened
     */
    private int     maxTocEntryNumber = 50;
    
   
    private LoggingParameters logParams = new LoggingParameters();
    

    /**
     * Read configuration parameters from the properties file 
     * @param fileName
     * @throws ConfigurationException
     */
    public synchronized void load(String configFileName) 
    						throws ConfigurationException{
    	File configFile = new File(configFileName);
    	try {
			if (!configFile.exists())
				configFile.createNewFile();
		} catch (IOException e) {
			throw new ConfigurationException(
					"Unable to create configuration file: "
					+ configFileName);			
		}

		try {
			PropertiesConfiguration props = new PropertiesConfiguration( configFile );
			
			configDir = (String) props.getProperty("anacleto.ConfigDir");
			indexDir  = (String) props.getProperty("anacleto.IndexDir");
			booksFile = (String) props.getProperty("anacleto.BooksFile");
			
			//Set locale:
			setLocale((String) props.getProperty("anacleto.Locale"));
			if (locale == null) 
				locale = Locale.getDefault();
			
			try {
				indexingPriority = Integer.parseInt(
					(String) props.getProperty("anacleto.indexingPriority"));
			} catch (NumberFormatException e){
				indexingPriority = 5;
			}
			
			try {
				securityLevel = Integer.parseInt(
						(String) props.getProperty("anacleto.securityLevel"));
			} catch (NumberFormatException e){
			}
			
			defaultContentStyleSheet = (String) props.getProperty(
					"anacleto.defaultContentStyleSheet");
			defaultHtmlIndexingscheme  = (String) props.getProperty(
					"anacleto.defaultHtmlIndexingscheme");
			defaultTei2IndexingStyleSheet  = (String) props.getProperty(
					"anacleto.defaultTei2IndexingStyleSheet");
			defaultXmlDisplayScheme  = (String) props.getProperty(
					"anacleto.defaultXmlDisplayScheme");

			try {
				int s = Integer.parseInt(
						(String) props.getProperty("anacleto.sort"));
				if (s == Constants.SORT_BY_HIERARCHY ||
						s == Constants.SORT_BY_RELEVANCE){
					sortType = s;
				}
					
			} catch (NumberFormatException e){
			}
			 
			try {
				maxTocEntryNumber = Integer.parseInt(
					(String) props.getProperty("anacleto.maxTocEntryNumber"));
			} catch (NumberFormatException e){
				maxTocEntryNumber = 50;
			}

			logParams = new LoggingParameters(configDir);
    	    
    	    logParams.setLogDir( (String) props.getProperty(
					"anacleto.logDir"));
    	    
    	    logParams.setAdminFile((String) props.getProperty(
    	    		"anacleto.AdminLogFile"));
    	    logParams.setAdminLogLevel((String) props.getProperty(
    				"anacleto.AdminLogLevel"));
    	    
    	    logParams.setUsereventsFile((String) props.getProperty(
    				"anacleto.usereventsLogFile"));
    	    logParams.setUsereventsLogLevel((String) props.getProperty(
					"anacleto.usereventsLogLevel"));
    		
    	    logParams.setIndexingFile((String) props.getProperty(
				"anacleto.indexingLogFile"));
    	    logParams.setIndexingLogLevel((String) props.getProperty(
				"anacleto.indexingLogLevel"));
    	    
    	} catch (org.apache.commons.configuration.ConfigurationException e) {
			throw new ConfigurationException(e);
		}
		
    }
    
    /**
     * Save all system property that starts with "anacleto"
     * @param configFileName
     * @throws ConfigurationException
     */
    public synchronized void save(String configFileName) 
		throws ConfigurationException{

		try {
			PropertiesConfiguration props = new PropertiesConfiguration( 
		               configFileName );
			
			props.setProperty("anacleto.ConfigDir", configDir);
			props.setProperty("anacleto.IndexDir",  indexDir);
						
			props.setProperty("anacleto.BooksFile", booksFile);
			
			props.setProperty("anacleto.Locale", locale);
			
			props.setProperty("anacleto.indexingPriority", 
					String.valueOf(indexingPriority));
			
			props.setProperty("anacleto.securityLevel",
					String.valueOf(securityLevel));
			
			props.setProperty("anacleto.defaultContentStyleSheet",
					defaultContentStyleSheet );
			props.setProperty("anacleto.defaultHtmlIndexingscheme",
					defaultHtmlIndexingscheme);
			
			props.setProperty("anacleto.defaultTei2IndexingStyleSheet",
					defaultTei2IndexingStyleSheet);
	
			props.setProperty("anacleto.defaultXmlDisplayScheme",
					defaultXmlDisplayScheme);
			
			props.setProperty("anacleto.sort",
					String.valueOf(sortType));
			
			props.setProperty("anacleto.maxTocEntryNumber", 
					String.valueOf(maxTocEntryNumber));
			
			//Save log parameters:
			props.setProperty("anacleto.logDir", logParams.getLogDir());
    
		    props.setProperty("anacleto.AdminLogFile",
		    			logParams.getAdminFile());
		    props.setProperty("anacleto.AdminLogLevel",
		    			logParams.getAdminLogLevel());
		    
		    props.setProperty("anacleto.usereventsLogFile",
		    		logParams.getUsereventsFile());
		    props.setProperty("anacleto.usereventsLogLevel",
		    		logParams.getUsereventsLogLevel());
			
		    props.setProperty("anacleto.indexingLogFile",
		    		logParams.getIndexingFile());
		    props.setProperty("anacleto.indexingLogLevel",
		    		logParams.getIndexingLogLevel());
            
		    props.save();
		
		} catch (org.apache.commons.configuration.ConfigurationException e){
			throw new ConfigurationException(e);
		}
    }    
	
	/**
	 * @return Returns the booksFile.
	 */
	public String getBooksFile() {
		return booksFile;
	}
	/**
	 * @param booksFile The booksFile to set.
	 */
	public void setBooksFile(String booksFile) {
		this.booksFile = booksFile;
	}
	/**
	 * @return Returns the configDir.
	 */
	public String getConfigDir() {
		return configDir;
	}
	/**
	 * @param configDir The configDir to set.
	 */
	public void setConfigDir(String configDir) {
		this.configDir = configDir;
	}
	
	public Locale getLocale() {
		if (locale != null)
			return locale;
		else
			return Locale.getDefault();
	}

	public void setLocale(String locale) {
		

//		check locale for available locales
		Locale[] availLocs = Locale.getAvailableLocales();

		boolean foundLoc = false;
		for (int i = 0; i < availLocs.length; i++) {
			Locale availLoc = availLocs[i];
			if (availLoc.getLanguage().equals(locale)){
				this.locale = availLoc;
				foundLoc = true;
				break;
			}
		}
		
		if (foundLoc == false)
			locale = null;
	}

	/**
	 * @return Returns the defaultContentStyleSheet.
	 */
	public String getDefaultContentStyleSheet() {
		return defaultContentStyleSheet;
	}
	/**
	 * @param defaultContentStyleSheet The defaultContentStyleSheet to set.
	 */
	public void setDefaultContentStyleSheet(String defaultContentStyleSheet) {
		this.defaultContentStyleSheet = defaultContentStyleSheet;
	}
	/**
	 * @return Returns the defaultHtmlIndexingscheme.
	 */
	public String getDefaultHtmlIndexingscheme() {
		return defaultHtmlIndexingscheme;
	}
	/**
	 * @param defaultHtmlIndexingscheme The defaultHtmlIndexingscheme to set.
	 */
	public void setDefaultHtmlIndexingscheme(String defaultHtmlIndexingscheme) {
		this.defaultHtmlIndexingscheme = defaultHtmlIndexingscheme;
	}
	/**
	 * @return Returns the defaultTei2IndexingStyleSheet.
	 */
	public String getDefaultTei2IndexingStyleSheet() {
		return defaultTei2IndexingStyleSheet;
	}
	/**
	 * @param defaultTei2IndexingStyleSheet The defaultTei2IndexingStyleSheet to set.
	 */
	public void setDefaultTei2IndexingStyleSheet(
			String defaultTei2IndexingStyleSheet) {
		this.defaultTei2IndexingStyleSheet = defaultTei2IndexingStyleSheet;
	}
	/**
	 * @return Returns the defaultXmlDisplayScheme.
	 */
	public String getDefaultXmlDisplayScheme() {
		return defaultXmlDisplayScheme;
	}
	/**
	 * @param defaultXmlDisplayScheme The defaultXmlDisplayScheme to set.
	 */
	public void setDefaultXmlDisplayScheme(String defaultXmlDisplayScheme) {
		this.defaultXmlDisplayScheme = defaultXmlDisplayScheme;
	}
	/**
	 * @return Returns the indexDir.
	 */
	public String getIndexDir() {
		return indexDir;
	}
	/**
	 * @param indexDir The indexDir to set.
	 */
	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}

	/**
	 * @return Returns the indexingPriority.
	 */
	public int getIndexingPriority() {
		return indexingPriority;
	}
	/**
	 * @param indexingPriority The indexingPriority to set.
	 */
	public void setIndexingPriority(int indexingPriority) {
		this.indexingPriority = indexingPriority;
	}
	
	public String getBooksFileAbs(){
		return getAbs(getBooksFile());
	}

	
	public String getPasswordFile() {
		return passwordFile;
	}

	public String getPasswordFileAbs() {
		return getAbs(passwordFile);
	}
	/**
	 * @return Returns the defaultTei2IndexingStyleSheet.
	 */
	public String getDefaultContentStyleSheetAbs() {
		return getAbs(getDefaultContentStyleSheet());
	}
	

	/**
	 * @return Returns the defaultTei2IndexingStyleSheet.
	 */
	public String getDefaultHtmlIndexingschemeAbs() {
		return getAbs(getDefaultHtmlIndexingscheme());
	}
	

	/**
	 * @return Returns the defaultTei2IndexingStyleSheet.
	 */
	public String getDefaultTei2IndexingStyleSheetAbs() {
		return getAbs(getDefaultTei2IndexingStyleSheet());
	}
	

	/**
	 * @return Returns the defaultTei2IndexingStyleSheet.
	 */
	public String getDefaultXmlDisplaySchemeAbs() {
		return getAbs(getDefaultXmlDisplayScheme());
	}
	
	/**
	 * @return Returns the logParams.
	 */
	public synchronized LoggingParameters getLogParams() {
		return logParams;
	}
	
	
	/**
	 * @param logParams The logParams to set.
	 */
	public synchronized void setLogParams(LoggingParameters logParams) {
		this.logParams = logParams;
	}

	public String getAbs(String rel){
		
		if (getConfigDir() == null || rel == null)
			return null;
		
		return getConfigDir() + "/" + rel;
	}

	/**
	 * @return Returns the securityLevel.
	 */
	public int getSecurityLevel() {
		return securityLevel;
	}

	/**
	 * @param securityLevel The securityLevel to set.
	 */
	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}

	/**
	 * @return Returns the maxTocEntryNumber.
	 */
	public int getMaxTocEntryNumber() {
		return maxTocEntryNumber;
	}

	/**
	 * @param maxTocEntryNumber.
	 */
	public void setMaxTocEntryNumber(int maxTocEntryNumber) {
		this.maxTocEntryNumber = maxTocEntryNumber;
	}

	/**
	 * @return Returns the sortType.
	 */
	public int getSortType() {
		return sortType;
	}

	/**
	 * @param sortType The sortType to set.
	 */
	public void setSortType(int sortType) {
		this.sortType = sortType;
	}
	
	
	
}
