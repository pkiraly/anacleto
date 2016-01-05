//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.Logging;
import com.anacleto.base.LoggingParameters;
import com.anacleto.struts.form.admin.LogViewerForm;
import com.anacleto.view.LogBean;
import com.anacleto.view.OptionBean;

/** 
 * MyEclipse Struts
 * Creation date: 05-06-2005
 * 
 * XDoclet definition:
 * @struts:action path="/logViewer" name="logViewerForm" input="/form/admin/logViewer.jsp" scope="request" validate="true"
 */
public class LogViewerAction extends Action {

	// --------------------------------------------------------- Instance Variables
	protected static final int BUFFER_SIZE = 256;
	
	protected static final int MAX_ALLOWED_LINES = 4000;
	
	protected static final Vector logLevels = new Vector();
	{
		if(logLevels.size() == 0){
			logLevels.addElement("NONE");
			logLevels.addElement("DEBUG");
			logLevels.addElement("INFO");
			logLevels.addElement("WARN");
			logLevels.addElement("ERROR");
			logLevels.addElement("FATAL");
			logLevels.addElement("ALL");
		}
	}
	
	protected static final Collection logFileTypeOptions = new ArrayList();
	{
		if(logFileTypeOptions.size() == 0){
			logFileTypeOptions.add(new OptionBean("indexing", "Indexing"));
			logFileTypeOptions.add(new OptionBean("userevents", "User events"));
			logFileTypeOptions.add(new OptionBean("admin", "Administration"));
		}
	}
	
	private Logger logger = Logging.getAdminLogger();
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
		
		LoggingParameters logParams = Configuration.params.getLogParams();
		LogViewerForm logViewerForm = (LogViewerForm) form;
		int offset = logViewerForm.getOffset();
		String logLevel = logViewerForm.getLogLevel();
		String logFileType = logViewerForm.getLogFileType();
		String logFileName = logViewerForm.getLogFileName();

		try {
			String logFileAbs = "";
			String logFile = "";
			if (logFileName != null && !logFileName.equals("")) {
				logFileAbs = logParams.getLogDir() + "/" + logFileName;
				logFile = logFileName;
			} else {
				if (logViewerForm.getUsereventButt().pressed()
						|| logFileType.equals("userevents")) {
					logFileAbs = logParams.getUsereventsFileAbs();
					logFile = logParams.getUsereventsFile();

				} else if (logViewerForm.getAdminButt().pressed()
						|| logFileType.equals("admin")) {
					logFileAbs = logParams.getAdminFileAbs();
					logFile = logParams.getAdminFile();

				} else if (logViewerForm.getIndexingButt().pressed()
						|| logFileType.equals("indexing")) {
					logFileAbs = logParams.getIndexingFileAbs();
					logFile = logParams.getIndexingFile();
				}
			}
			
			if(!logFileAbs.equals("")){
				logViewerForm.setLogFileName(logFile);
				logViewerForm.setLogLines(readLogFile(logFileAbs, logViewerForm));
			}

			logViewerForm.setLogFileTypes(logFileTypeOptions);
			logViewerForm.setUsereventsLogFiles(listFiles(new File(logParams.getLogDir()), logParams.getUsereventsFile()));
			logViewerForm.setAdminLogFiles(listFiles(new File(logParams.getLogDir()), logParams.getAdminFile()));
			logViewerForm.setIndexingLogFiles(listFiles(new File(logParams.getLogDir()), logParams.getIndexingFile()));

			
		} catch (IOException e){
			logger.error("Error during reading logfile: " + e);
		}
		
		return mapping.getInputForward();
	}

	private Collection readLogFile(String file, LogViewerForm logViewerForm) throws IOException {
		
		String logLevel = logViewerForm.getLogLevel();
		int offset = logViewerForm.getOffset();
		int length = logViewerForm.getLength();
		
        file = file.replace('\\', '/');
        
        InputStream fileStream = new FileInputStream(file);
        InputStreamReader in = new InputStreamReader(fileStream, "ISO-8859-2");

        // Collection logLines = new ArrayList(MAX_ALLOWED_LINES);
        Collection logLines = new ArrayList();
        
        char buffer[] = new char[BUFFER_SIZE];
        String lineSeparator = System.getProperty("line.separator");
        
        int    lineSeparatorLength = lineSeparator.length();
        
        String currentBuffer = "";
        int n = 0;
        int counter = 0;
        while (true) {
            n = in.read(buffer);

            // if (n < 1 || logLines.size() >= MAX_ALLOWED_LINES )
            if (n < 1)
                break;
            currentBuffer = currentBuffer + new String(buffer, 0, n);
            
            while (true){
            	int i = currentBuffer.indexOf(lineSeparator);
            	if (i<0)
            		break;
            	else {
            		String logLine = currentBuffer.substring(0, i);
            		String[] logCols = logLine.split("#");
            		if (logCols.length == 3){
            			Pattern pattern = Pattern.compile("^\\s*([A-Z]+)\\s");
            			Matcher matcher = pattern.matcher(logCols[1]);
            			String level = (matcher.find()) ? matcher.group(1) : "NONE";
            			if(logLevels.indexOf(level) <= logLevels.indexOf(logLevel))
            			{
            				if(counter >= offset && counter <= (offset+length)) {
                  				LogBean logBean = new LogBean(logCols[0], logCols[1], 
                    					logCols[2], level);
                    			logLines.add(logBean);
            				}
                			counter++;
            			}
            		}
            		
            		currentBuffer = currentBuffer.substring(i + lineSeparatorLength);
            	}
            }
        }
        in.close();
        logViewerForm.setNumberOfPages(counter);

        return logLines;
    }
	
	private Collection listFiles(File dir, String prefix) {
        String[] files = dir.list();
        Collection logFiles = new ArrayList();
        int len = files.length;
        for(int i=0; i<len; i++) {
        	if(files[i].startsWith(prefix)) {
        		String name = files[i];
        		String text = (files[i].equals(prefix))
        		            ? "actual"
        		            : files[i].substring(prefix.length()+1);
        		logFiles.add(new OptionBean(name, text));
        	}
        }
        return logFiles;
    }

	private class logFileFilter implements FileFilter {
		private String prefix;
		
		public logFileFilter(String prefix){
			this.prefix = prefix;
		}
		
		public boolean accept(File file){
			return file.isFile() && file.getName().startsWith(prefix);
		}
	}

}