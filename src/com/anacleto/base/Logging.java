/*
 * Created on May 5, 2005
 *
 */
package com.anacleto.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * Initialize application logging.
 * Three category of logging are foreseen: admin, indexing and userevents. Each category
 * is logged in a dailyRollingFileAppeder. When a program wants to log an event, 
 * first it has to decide witch category to use, and has to call the appropriate function:
 * getAdminLogger(), getIndexingLogger() or getUserEventsLogger()
 * 
 * @author robi
 *
 */
public class Logging {
	
	public static final String admin      = "admin";
	public static final String userevents = "userevents";
	public static final String indexing   = "indexing";
	
	
	/**
	 * 
	 * @param params
	 * @param propsFile
	 * @throws IOException
	 */
	public static void initLogging(LoggingParameters params, InputStream propsFile) {

		Logger logger = Logging.getAdminLogger();
		
    	if (propsFile != null){
    	
			try {
				Properties props = new Properties();
				props.load(propsFile);
				
				props.setProperty("log4j.appender.indexing.File", 
						params.getIndexingFileAbs());
				props.setProperty("log4j.logger.indexing",
						params.getIndexingLogLevel() + ", indexing");
				
				props.setProperty("log4j.appender.admin.File", 
						params.getAdminFileAbs());
				props.setProperty("log4j.logger.admin",
						params.getAdminLogLevel() + ", admin");
				
				props.setProperty("log4j.appender.userevents.File", 
						params.getUsereventsFileAbs() );
				props.setProperty("log4j.logger.userevents",
						(params.getUsereventsLogLevel() + ", userevents"));
				
				PropertyConfigurator.configure(props);
				logger.info("Logging started in directory: " + params.getLogDir());
			} catch (IOException e1) {
				logger.error("Unable to init logging. Root Cause: " + e1);
			} finally {
				try {
					propsFile.close();
				} catch (IOException e) {
					logger.error("Unable to close logfile. Root Cause: " + e);
				}
			}
		} else {
			logger.error("Unable to init logging. Root casuse: init file not found" );
		}

	}
	
	public static Logger getAdminLogger(){
		return Logger.getLogger(Logging.admin);
	}

	
	public static Logger getIndexingLogger(){
		return Logger.getLogger(Logging.indexing);
	}
	
	public static Logger getUserEventsLogger(){
		return Logger.getLogger(Logging.userevents);
	}
	
}
