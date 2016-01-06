package com.anacleto.parsing.filetype;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.cyberneko.html.parsers.DOMParser;
import org.xml.sax.InputSource;

import com.anacleto.base.Logging;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;
import com.anacleto.parsing.SAXHandlerNode;
import com.anacleto.parsing.SAXHandlerWithStack;

public class FastHtmlIndexer implements FileTypeParser {
	
	private javax.xml.transform.Transformer transformer;
	
	/**
     * 
     * @param xslFile
     * @param duplicateForContent Set this input field to true, if you want that
     * 			all fields are repeated in the content field (except the content field itself)
     * 			this is useful, when you want to have a field type "any field"
     * @throws TransformerConfigurationException
     */
    public FastHtmlIndexer(File xslFile) throws TransformerConfigurationException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        transformer = tFactory.newTransformer(new StreamSource(xslFile));
    }
    
    public void processStream(InputStream in, BookPage page) throws ParserException {

    	try {
    		DOMParser parser = new DOMParser();
        	if (page.getEncoding() != null)
        		parser.setProperty(
    				"http://cyberneko.org/html/properties/default-encoding", 
        			page.getEncoding());
    		
    		InputSource source = new InputSource(in);
            parser.parse(source);
            
            DOMSource src = new DOMSource(parser.getDocument());
            SAXResult saxRes = new SAXResult();
    		
            NodeHandler handler = new NodeHandler(page);
			saxRes.setHandler(handler);
			transformer.transform(src, saxRes);
			
    	} catch (Exception e) {
    		throw new ParserException(
					"Unable to execute transformation. Root cause: " + e);
    		
		}
	}
}

final class NodeHandler extends SAXHandlerWithStack {
	
	private static Logger logger = Logging.getIndexingLogger();
	
	private 	BookPage page;
	
	NodeHandler(BookPage page){
		this.page = page;
	}

	/**
	 * @return Returns the page.
	 */
	public BookPage getPage() {
		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anacleto.parsing.SAXHandlerWithStack#endElement(org.xml.sax.Attributes,
	 *      java.lang.String)
	 */
	public void startElement(SAXHandlerNode node) {
		// logger.info(node.getLocalName());
	}
	
	public void endElement(SAXHandlerNode node) {
		
		try {
			registerNode(node);	
			
			//register node under all the anchor's fields:
			Iterator it = getStack().iterator();
			while (it.hasNext()) {
				SAXHandlerNode currNode = (SAXHandlerNode) it.next();
				
				String fieldName = (String)(currNode.getAttributes().get("field"));
				if (fieldName != null){
					Map atts = currNode.getAttributes();
					atts.put("field", fieldName);
					node.setAttributes(atts);
					
					registerNode(node);	
				}
				
			}
		} catch (Exception e){
			logger.error(
				"Exception occured during idexing bookpage: " 
					+ page.getName() + "." +
				" Content: " + page.getURL()
			);
		}
	}
	
	/**
	 * Add new field entry to the index according to the name, 
	 * attributes and content of the node
	 * @param node - the node contains all the requested information
	 */
	private void registerNode(SAXHandlerNode node) {
		
		StringBuffer fieldValue = node.getValue();
	    //Empty content is filtered out:
	    if (fieldValue == null || fieldValue.length() == 0) {
	    	return;
	    }
	    
	    Map atts = node.getAttributes();
	    
	    String fieldName = (String)atts.get("field");
	    
	    //Empty content is filtered out:
	    if (fieldName == null || fieldName.length() == 0) {
            logger.error("fieldValue: " + node.toString());
	    	logger.error("Field name not found");
	        return;
	    }
	      
	    boolean isIndexed = true;
	    boolean isStored  = true;
	    boolean isTokenized = true;
	    boolean isTermVectorStored = false;
	      
	    getBooleanAttribute(atts, "indexed", isIndexed);
	    getBooleanAttribute(atts, "stored", isStored);
	    getBooleanAttribute(atts, "tokenized", isTokenized);
	    getBooleanAttribute(atts, "termVectorStored", isTermVectorStored);
	    
	    //logger.info("adding " + fieldName + ": " + fieldValue.toString());
	
	    page.addField(fieldName, fieldValue.toString(), 
	    		isIndexed, isStored, isTokenized, isTermVectorStored);
	}
	
	/**
	 * Read the boolean values from the attributes and write it to the
	 * 3rd parameter
	 * @param attributes - the attributes of the node
	 * @param key - the name of the searched attribute 
	 * @param value - the value to change
	 */
	private void getBooleanAttribute(Map attributes, String key, boolean value){
		String strValue = (String)attributes.get(key);
		if (strValue != null)
			value = strValue.equals("true");
	}
	
}
