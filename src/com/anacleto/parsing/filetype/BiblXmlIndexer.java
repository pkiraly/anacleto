/*
 * Created on Feb 11, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.anacleto.index.IndexException;

/**
 * @author robi
 * 
 */
public class BiblXmlIndexer {

    private javax.xml.transform.Transformer transformer;

    public BiblXmlIndexer() {}
    
    public BiblXmlIndexer(File xslFile)
            throws IndexException {
    	
        try {
        	TransformerFactory tFactory = TransformerFactory.newInstance();
			transformer = tFactory.newTransformer(new StreamSource(xslFile));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
            throw new IndexException(
            	"Error while processing xsl file: " + xslFile + "." +
            	" Root cause: " + e);
		}
    }
    
	public void parse(String fileSource, String bookName) 
		throws IndexException {
		
		XMLReader xmlReader = null;
		try {
			XMLNodeHandler handler = new XMLNodeHandler(bookName);
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			spfactory.setValidating(false);
			SAXParser saxParser = spfactory.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(fileSource));
		} catch (IOException e) {
			throw new IndexException(
				"IO Error while transforming file: " + fileSource + "." +
				" Root cause: " + e);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new IndexException(
				"SAX error while transforming file: " + fileSource + "." +
				" Root cause: " + e);
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			throw new IndexException(
				"Configuration Error while transforming file: " + fileSource + "." +
				" Root cause: " + e1);
		}
		

    }

    public void transform(String fileSource, String bookName)
            throws IndexException{
        StreamSource src = new StreamSource(fileSource);
        
        SAXResult saxRes = new SAXResult();
        
		try {
			XMLNodeHandler handler = new XMLNodeHandler(bookName);
			saxRes.setHandler(handler);
	        transformer.transform(src, saxRes);
		} catch (IOException e) {
			throw new IndexException(e);
		} catch (TransformerException e1) {
			e1.printStackTrace();
            throw new IndexException(
            	"Error while transforming file: " + fileSource + "." +
            	" Root cause: " + e1);
        }
    }

}