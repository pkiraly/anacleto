package com.anacleto.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.transform.TransformerConfigurationException;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.securityfilter.authenticator.PasswordFileHandler;

import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.hierarchy.Shelf;
import com.anacleto.index.BookIndexQueue;
import com.anacleto.index.CustomAnalyzer;
import com.anacleto.index.IndexManager;
import com.anacleto.index.LocalizedTermList;
import com.anacleto.parsing.source.SourceTypeHandlerFactory;

/**
 * Configuration class to set application data: Two ways of initialization: with
 * the static init() method, in this case the configuration file will be taken
 * from the system property: anacletoConfigurationFile otherwise the init(String
 * fileName) should be used The configuration file has to be conform to the DTD
 * ......
 * 
 * @author robi
 * 
 */
public class Configuration {

	/**
	 * The name of config file
	 */
	private static String configFile;

	// private static Node rootNode;

	/**
	 * The basic parameters
	 */
	public static BaseParameters params = new BaseParameters();

	/**
	 * The list of shelves
	 */
	private static Map shelfColl = new TreeMap();

	// private static Directory inDir;
	
	/**
	 * The Anacleto's customized analyzer (see Lucene analyzer)
	 */
	private static CustomAnalyzer analyzer;

	private static String defaultQueryField = "content";

	private static Logger logger = Logging.getAdminLogger();

	// Start a new thread to wait for indexing activity:
	private static BookIndexQueue bookIndexQueue = new BookIndexQueue();

	private static PasswordFileHandler authController = new PasswordFileHandler();
	
	
	/**
	 * Init configuration from a file
	 * 
	 * @param fileName
	 * @throws ConfigurationException
	 */
	public void init() {

		// Load params from the file: anacleto.properties
		String configFile;

		ClassLoader cloader = getClass().getClassLoader();
		URL configURL = cloader.getResource("anacleto.properties");
		if (configURL == null){
			configFile = System.getProperty("user.home")
					+ "/anacleto.properties";
		} else
			configFile = configURL.getFile();

		if (configFile == null) {
			logger.error("Please create a file called: "
					+ "<anacleto.properties> in your classpath.");
			return;
		} else {
			File confF = new File(configFile);
			try {
				if (!confF.exists())
					confF.createNewFile();
			} catch (IOException e) {
				;
			}			
			Configuration.configFile = configFile;
		}
		
		init(configFile);		
	}

	public void init(String configFile) {
		logger.info("Starting Anacleto using configuration from: " + configFile);
		File cfg = new File(configFile);
		
		if (!cfg.exists()) {
			logger.error("Inexistent configuration file: "+ configFile);
			logger.error("Anacleto cannot be started");
			return;
		} else
			Configuration.configFile = configFile;

		try {
			params.load(Configuration.configFile);
		} catch (ConfigurationException e) {
			logger.error(e);
		}

		startUp();
	}

	public void init(BaseParameters params) {
		Configuration.params = params;
		startUp();
	}

	
	public static void saveParams() throws ConfigurationException {
		params.save(configFile);
	}
	
	public void startUp() {
		ClassLoader cloader = getClass().getClassLoader();
		
//		 Init logging.
		
		InputStream logProps = cloader.getResourceAsStream(
				"com/anacleto/base/log4j.properties");
		Logging.initLogging(params.getLogParams(), logProps);
		
		
		// Init authentication
		authController.init(Configuration.params.getPasswordFileAbs());
		
		
		// Start index queue:
		bookIndexQueue.setName("BookIndexQueue");
		//bookIndexQueue.setPriority(params.getIndexingPriority());
		
		analyzer = new CustomAnalyzer(new File(params.getConfigDir()));

		try {
			loadShelfConfiguration();
		} catch (ConfigurationException e) {
			logger.error(e);
		}

		IndexManager.initialize(params.getIndexDir(), analyzer,
				defaultQueryField, params.getSortType());
		
		SourceTypeHandlerFactory.initialize();

		
		try {
			LocalizedTermList.initialize(params.getIndexDir(), 
					params.getLocale());
		} catch (IOException e) {
			logger.error("Unable to init termList. Root cause: " + e);
		}
		
		GlobalCache.startUp();
	}

	public void destroy() {
		try {
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			sched.shutdown();
		} catch (SchedulerException e) {
			;
		}

		bookIndexQueue.destroy();
		
		GlobalCache.shutdown();
	}

	public static synchronized void loadShelfConfiguration()
			throws ConfigurationException {

		String fileName = params.getBooksFileAbs();

		BooksFileParser booksFileParser = new BooksFileParser();
		try {
			shelfColl = booksFileParser.parse(new File(fileName));
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			System.setProperty("org.quartz.threadPool.threadCount", "1");
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			sched.start();
		} catch (SchedulerException e) {
			throw new ConfigurationException("Scheduler error", e);
		}

	}


	/**
	 * Returns with all the books and shelves in the configuration
	 * 
	 * @return Returns the shelfColl.
	 */
	public static Collection getShelfColl() {
		return Configuration.shelfColl.values();
	}

	public static void setShelfColl(Map shelfColl) {
		Configuration.shelfColl = shelfColl;
	}
	
	/**
	 * Return with a collection of books in the configuration
	 * 
	 * @return
	 */
	public static Collection getBookColl() {
		Collection retColl = new LinkedList();

		Collection shelfColl = Configuration.getShelfColl();
		Iterator it = shelfColl.iterator();
		while (it.hasNext()) {
			HierarchicalElement element = (HierarchicalElement) it.next();
			if (element instanceof Book)
				retColl.add(element);
		}

		return retColl;
	}

	
	/**
	 * Return with a collection of shelves in the configuration
	 * 
	 * @return
	 */
	public static Collection getShelves() {
		Collection retColl = new LinkedList();

		Collection shelfColl = Configuration.getShelfColl();
		Iterator it = shelfColl.iterator();
		while (it.hasNext()) {
			HierarchicalElement element = (HierarchicalElement) it.next();
			if (element instanceof Shelf)
				retColl.add(element);
		}

		return retColl;
	}

	public static HierarchicalElement getElement(String name)
			throws IOException {
		
		if (name == null || name.trim().length() == 0)
			return null;
		
		// book or shelf are in the configuration:
		HierarchicalElement el = (HierarchicalElement) shelfColl.get(name);
		if (el != null)
			return el;

		IndexManager in = new IndexManager();
		org.apache.lucene.document.Document doc = in.findNamedElement(name);
		if(doc != null) {
			return new BookPage(doc);
		} else {
			return null;
		}
	}

	/**
	 * Returns with the path name of the element of format: shelf1 > shelf2 >
	 * book > page > page
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static String getElementPath(String name) throws IOException {

		HierarchicalElement element = getElement(name);
		String retString = "";
		// get parent path:
		while (element != null && element.getParentName() != null
				&& !element.getParentName().trim().equals("")
				&& !element.getParentName().equals(element.getName())) {

			element = getElement(element.getParentName());
			if (element != null)
				retString = element.getTitle() + " > " + retString;
		}
		return retString;
	}

	/**
	 * Returns with the path name of the element of format: <a href="..">shelf1</a> >
	 * <a href="..">shelf2 > <a href="..">book</a> > <a href="..">page</a> >
	 * <a href="..">page</a>
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static String getElementLinkedPath(String name) throws IOException {

		HierarchicalElement element = getElement(name);
		String retString = "";
		String URL = "";
		// get parent path:
		while (element != null && element.getParentName() != null
				&& !element.getParentName().trim().equals("")
				&& !element.getParentName().equals(element.getName())) {

			element = getElement(element.getParentName());
			if (element != null) {
				URL = (element.getName().equals("root")) ? element.getTitle()
						: "<a href=\"showDocument.do?name=" + element.getName()
								+ "\">" + element.getTitle() + "</a>";
				retString = URL + " > " + retString;
			}
		}
		return retString;
	}

	public static CustomAnalyzer getAnalyzer() {
		return Configuration.analyzer;
	}

	public static String getDefaultQueryField() {
		return Configuration.defaultQueryField;
	}

	/**
	 * @return Returns the authController.
	 */
	public static PasswordFileHandler getAuthController() {
		return Configuration.authController;
	}


}