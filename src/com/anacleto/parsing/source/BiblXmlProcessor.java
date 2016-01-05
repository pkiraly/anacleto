/*
 * Created on May 2, 2005
 *
 */
package com.anacleto.parsing.source;

import java.io.*;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.anacleto.base.BookException;
import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Logging;
import com.anacleto.content.LocalEntityResolver;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexException;
import com.anacleto.index.IndexManager;
import com.anacleto.parsing.filetype.BiblXmlIndexer;
import com.anacleto.parsing.filetype.FastTeiIndexer;

/**
 * @author robi
 *
 */
public class BiblXmlProcessor extends SourceTypeHandler {

	Logger log = Logging.getUserEventsLogger();
	
	private static String configDir = Configuration.params.getConfigDir();

	private IndexManager indexMan;
	
    public void indexBook(Book book) 
    	throws BookException, IOException {
    	
    	this.indexMan = new IndexManager();
    	
    	try {
			indexMan.deleteBook(book.getName());
		} catch (IOException e) {
			throw new BookException("Error while deleting book from index." +
					" Root cause: " + e);
		}
        
		BiblXmlIndexer indexer;
		try {
			File xsl = book.getTei2IndexingStyleSheet();
			if(xsl.isFile() && 
				xsl.getName().indexOf(".xsl") > -1) 
			{
				indexer = new BiblXmlIndexer(xsl);
				log.info("book.getURL(): " + book.getURL());
				indexer.transform(book.getURL(), book.getName());
			} else {
				indexer = new BiblXmlIndexer();
				indexer.parse(book.getURL(), book.getName());
			}
			
		} catch (IndexException e2) {
			e2.printStackTrace();
			throw new BookException(e2);
		}	
		
        
        try {
			indexMan.updateSearcher();
		} catch (IOException e3) {
			throw new BookException(e3);
		}
    	
    }

	/* (non-Javadoc)
	 * @see com.anacleto.parsing.source.SourceTypeHandler#getContent(com.anacleto.hierarchy.BookPage)
	 */
	public ContentBean getContent(BookPage page) throws ContentReadException {
		
		String xmlContent;
		try {
			xmlContent = transformXmlContent(page.getField("xml").stringValue());
			// xmlContent = readTEIContent("file:///" + page.getBook().getURL(), page.getLocation());
			return new ContentBean(xmlContent);
		} catch (ConfigurationException e) {
			throw new ContentReadException(e);
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new ContentReadException(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ContentReadException(e);
		} catch (IOException e) {
			throw new ContentReadException(e);
		}
	}
	
	private String transformXmlContent(String xmlSource) 
		throws ConfigurationException, TransformerException, 
		FileNotFoundException {
		InputStream xslStr = new FileInputStream(configDir + "/pressdoc.default.xsl");

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(
				new StreamSource(xslStr));
		
		Writer out = new StringWriter();
		StreamResult dest = new StreamResult(out);
		
		// DOMSource src = new DOMSource(xmlSource);
		Source src = new StreamSource(new java.io.StringReader(xmlSource));

		transformer.transform(src, dest);
		String output = out.toString();
		
		// output = output.replaceAll("\n", "<p>");
		return output;
		
	}

	//TODO test this method
	private String readTEIContent(String URL, String xpathStr)
		throws ConfigurationException, TransformerException, FileNotFoundException {
		URL = URL.replace("c:", "C:");
		xpathStr = xpathStr.replaceAll("\\\\", "/");
		log.info("URL: " + URL);
		log.info("xpathStr: " + xpathStr);
		DOMParser parser = new DOMParser();
		try {
			parser.setEntityResolver(new LocalEntityResolver());
			parser.parse(URL);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new ConfigurationException("Unable to parse file: " + URL);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConfigurationException("Unable to read file: " + URL);
		}
		
		Document doc = parser.getDocument();
		//ServletContext application = servlet.getServletContext();
		
		ClassLoader cl = this.getClass().getClassLoader();
		// InputStream xslStr = cl.getResourceAsStream("xsl/teihtml-drama.xsl");
		log.info("xsl file: " + configDir + "/pressdoc.default.xsl");
		InputStream xslStr = new FileInputStream(configDir + "/pressdoc.default.xsl");
		log.info("xsl file content: " + xslStr.toString());
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(
				new StreamSource(xslStr));
		
		Writer out = new StringWriter();
		StreamResult dest = new StreamResult(out);
		
//		Xpath:
		NodeList titles = XPathAPI.selectNodeList(doc, xpathStr);
		
//		Iterate over node list and print article titles.
		for (int i = 0; i < titles.getLength(); i++) {
			DOMSource src = new DOMSource(titles.item(i));
			transformer.transform(src, dest);
		}
		String output = out.toString();
		
		// output = output.replaceAll("\n", "<p>");
		return output;
	}

    
}
