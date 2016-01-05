package com.anacleto.parsing.filetype;

import java.io.File;
import java.io.IOException;
import java.util.EmptyStackException;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.cyberneko.html.parsers.SAXParser;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import com.anacleto.base.Logging;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexException;
import com.anacleto.index.IndexManager;
import com.anacleto.index.Indexer;

/**
 * Index a Html file according to the schema
 */
public class SAXHtmlIndexer implements Indexer {

    private TransformerHandler transformerHandler;
    private TemplatesHandler templatesHandler;
    private SAXTransformerFactory saxTFactory;
    
    private Logger log = Logging.getIndexingLogger();

    private IndexManager im;
    /**
     * 
     * @param xslFile
     * @param duplicateForContent Set this input field to true, if you want that
     * 			all fields are repeated in the content field (except the content field itself)
     * 			this is useful, when you want to have a field type "any field"
     * @throws TransformerConfigurationException
     */
    public SAXHtmlIndexer(File xslFile,  IndexManager im) throws TransformerConfigurationException {
        this.im = im;
    	
    	TransformerFactory tFactory = TransformerFactory.newInstance();
        saxTFactory = ((SAXTransformerFactory) tFactory);
        
        // Create a Templates ContentHandler to handle parsing of the 
		// stylesheet.
		templatesHandler = saxTFactory.newTemplatesHandler();
		
		// Create an XMLReader and set its ContentHandler.
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(templatesHandler);
			
			// Parse the stylesheet.                       
			reader.parse(xslFile.toString());
			
		} catch (SAXException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
    }

    public void getDocumentStructure(BookPage page, String fileName)
            throws IndexException {
        
    	log.info("Processing file: " + fileName);
        
        try {
	    	Templates templates =  templatesHandler.getTemplates();
		    transformerHandler = saxTFactory.newTransformerHandler(templates);
	    	
	    	XMLReader reader = new SAXParser();
	    	reader.setContentHandler(transformerHandler);
	    	
			SAXResult saxRes = new SAXResult(); 
			saxRes.setHandler(new DocumentHandler(page, im));
	    	
			transformerHandler.setResult(saxRes);
			
			reader.parse("file://localhost/"+fileName);
		} catch (IOException e) {
			log.error(e);
		} catch (SAXException e) {
			log.error(e);
		} catch (TransformerConfigurationException e) {
			log.error(e);
		}
    }
}

final class DocumentHandler implements ContentHandler {
	
	private IndexManager im;
    
	private BookPage page;
	
	private ArrayStack nodeBuffer;
	
	private Logger logger = Logging.getIndexingLogger();
	
	public DocumentHandler(BookPage page, IndexManager im){
		this.page = page;
		this.im = im;
	}
	
	public void characters(char[] text, int start, int length) 
			throws SAXException {
		try {
			DocumentNode docCont = (DocumentNode) nodeBuffer.get();
			docCont.appendToBuffer(text, start, length);
		}catch (EmptyStackException e){
			logger.debug("EmptyStackException occured");
		}
	}

	public void endDocument() throws SAXException {
		try {
			im.addPage(page);
		} catch (IOException e) {
			throw new SAXException(e);
		}
		
	}

	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
		
	    DocumentNode node = (DocumentNode) nodeBuffer.pop();
	    page.addField(node.fieldName, 
			node.stringBuffer.toString(),
			node.isIndexed, node.isStored, node.isTokenized, 
			node.isTermVectorStored);
	}

	public void endPrefixMapping(String arg0) throws SAXException {
	}

	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
	}

	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
	}
	
	public void setDocumentLocator(Locator arg0) {
	}
	
	public void skippedEntity(String arg0) throws SAXException {
	}
	
	public void startDocument() throws SAXException {
		nodeBuffer = new ArrayStack(10);
	}
	
	public void startElement(String namespaceURI, String localName,
            String rawName, Attributes atts) throws SAXException {
		
		DocumentNode node = new DocumentNode(atts);
		nodeBuffer.push(node);
	}
	
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
	}
	
}

final class DocumentNode {

	public String       fieldName;
	
	public boolean      isIndexed = true;
	public boolean      isStored  = true;
	public boolean      isTokenized = true;
	public boolean      isTermVectorStored = false;
	
	public StringBuffer stringBuffer = new StringBuffer();
    
    public int          childs;
    
    public DocumentNode(Attributes atts) {
    	
    	fieldName = atts.getValue("field");
    	
    	String ind = atts.getValue("indexed");
    	if (ind != null)
    		isIndexed = ind.equals("true");
    	
    	String store = atts.getValue("stored");
    	if (store != null)
    		isStored = store.equals("true");
    	
    	String token = atts.getValue("tokenized");
    	if (token != null)
    		isTokenized = token.equals("true");
    	
    	String term = atts.getValue("termVectorStored");
    	if (term != null)
    		isTermVectorStored = term.equals("true");
    	
    }
    
    public void appendToBuffer(char[] text, int start, int length){
    	stringBuffer.append(text, start, length);
	}
}