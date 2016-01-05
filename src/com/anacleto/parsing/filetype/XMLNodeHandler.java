/*
 * Created on Feb 11, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexManager;
import com.anacleto.parsing.NodeStack;
import com.anacleto.parsing.DocumentContent;
import com.anacleto.parsing.SAXHandlerNode;
import com.anacleto.parsing.SAXHandlerWithStack;

/**
 * @author király péter
 */
public class XMLNodeHandler  extends SAXHandlerWithStack {

	private String bookName;

	private ArrayStack docBuffer;

	private IndexManager indMan;

	private int counter = 0;

	private Logger log = Logging.getIndexingLogger();

	/**
	 * @throws IOException
	 * 
	 */
	public XMLNodeHandler(String bookName) throws IOException {
		this.bookName = bookName;
		this.indMan = new IndexManager();
		this.stack = new NodeStack(10);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] text, int start, int length)
			throws SAXException {

		stack.addTextToTopNode(text, start, length);
	}
	
	public void startElement(SAXHandlerNode node) {
	}
	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		if(qName.equals("in:index") || qName.equals("in:field")) {
			if (stack != null && stack.size() > 0) {
				parentNode = (SAXHandlerNode)stack.get();
			} else
				parentNode = null;

			stack.addNewNode(namespaceURI, localName, qName, atts);
			startElement((SAXHandlerNode)stack.get());
		}

		if (qName.equals("in:index")) {

			DocumentContent docCont = new DocumentContent();
			try {
				docCont.page = new BookPage();
				docCont.page.setName(bookName + counter);

				// Get parent name ad childcount from the docBuffer:
				if (docBuffer.size() > 0) {
					DocumentContent parentCont = (DocumentContent) docBuffer.get();
					docCont.page.setParentName(parentCont.page.getName());
					docCont.page.setChildCount(parentCont.childs);
					parentCont.childs++;
				} else {
					docCont.page.setParentName(bookName);
				}
				docCont.page.setBookName(bookName);
				docCont.page.setContentType(Constants.TEI2XMLContent);
				docCont.page.setName(atts.getValue("name"));
				docCont.page.setLocation(atts.getValue("location"));
				docCont.page.setPath(atts.getValue("location"));
			} catch (Exception e) {
				log.error(e);
			}
			docBuffer.push(docCont);
		} else if(qName.equals("in:field")) {
			//
		} else {
			StringBuffer text = new StringBuffer();
			text.append("<" + qName);
			if(atts != null) {
				for(int i=0, len=atts.getLength(); i<len; i++) {
					String name = atts.getQName(i);
					String value = atts.getValue(i);
					text.append(" " + name + "=\"" + value + "\"");
				}
			}
			text.append(">");
			stack.addTextToTopNode(text.toString());
		}
	}
	
	/**
	 * Receive notification of the end of an element.
	 * @param namespaceUri - the Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed
	 * @param localName - the local name (without prefix), or the empty string if Namespace processing is not being performed
	 * @param qName - the qualified XML name (with prefix), or the empty string if qualified names are not available 
	 */
	public void endElement(String namespaceUri, String localName, String qName)
			throws SAXException {

		if(qName.equals("in:index") || qName.equals("in:field")) {
			SAXHandlerNode node = (SAXHandlerNode) stack.pop();
		
			if (stack.size() > 0) {
				parentNode = (SAXHandlerNode)stack.get();
			} else
				parentNode = null;

			endElement(node);
		} else {
			stack.addTextToTopNode("</" + qName + ">");
		}
	}

	public void endElement(SAXHandlerNode node) {
		if (node.getQName().equals("in:index")) {
			try {
				DocumentContent docCont = (DocumentContent) docBuffer.pop();
				if (docCont.doc != null) {
					if (docCont.buffer != null)
						docCont.page.addTextField("content", docCont.buffer.toString());

					indMan.addPage(docCont.page);
					// log.info("Page added to index: " + docCont.page.getName());
				}
			} catch (IOException e) {
				log.error("Error during adding page to index. Root cause: " + e);
			}
		} else if (node.getQName().equals("in:field")) {
			registerNode(node);
		} else {
			log.error("endElement #3: " + node);
		}
	}

	/**
	 * Add new field entry to the index according to the name, 
	 * attributes and content of the node
	 * @param node - the node contains all the requested information
	 */
	private void registerNode(SAXHandlerNode node){
		
		StringBuffer fieldValue = node.getValue();
	    //Empty content is filtered out:
	    if (fieldValue == null || fieldValue.length() == 0) {
	    	return;
	    }
	    
	    Map atts = node.getAttributes();
	    
	    String fieldName = (String)atts.get("field");
	    String fieldType = (String)atts.get("type");
	    
	    //Empty content is filtered out:
	    if (fieldName == null || fieldName.length() == 0) {
            log.error("fieldValue: " + node.toString());
	    	log.error("Field name not found");
	        return;
	    }
	      
		DocumentContent docCont = (DocumentContent) docBuffer.pop();
	    if(fieldType == null) {
	    	boolean isIndexed = true;
	    	boolean isStored  = true;
	    	boolean isTokenized = true;
	    	boolean isTermVectorStored = false;
	      
	    	isIndexed = getBooleanAttribute(atts, "indexed", isIndexed);
	    	isStored = getBooleanAttribute(atts, "stored", isStored);
	    	isTokenized = getBooleanAttribute(atts, "tokenized", isTokenized);
	    	isTermVectorStored = getBooleanAttribute(atts, "termVectorStored", isTermVectorStored);
	
	    	docCont.page.addField(fieldName, fieldValue.toString(), 
	    		isIndexed, isStored, isTokenized, isTermVectorStored);
	    } else {
	    	if(fieldType.equals("KW")) {
	    		docCont.page.addField(fieldName, fieldValue.toString(), 
	    				true, true, false, false);
	    	} else {
	    		docCont.page.addField(fieldName, fieldValue.toString(), 
	    				true, true, true, false);
	    	}
		}
		docBuffer.push(docCont);
	}
	
	/**
	 * Read the boolean values from the attributes and write it to the
	 * 3rd parameter
	 * @param attributes - the attributes of the node
	 * @param key - the name of the searched attribute 
	 * @param value - the value to change
	 */
	private boolean getBooleanAttribute(Map attributes, String key, boolean value){
		String strValue = (String)attributes.get(key);
		if (strValue != null)
			value = strValue.equals("true");
		return value;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String arg0) throws SAXException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
	 *      java.lang.String)
	 */
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator arg0) {
	}

	public void skippedEntity(String arg0) throws SAXException {
	}

	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
	}

	public void startDocument() throws SAXException {
		docBuffer = new ArrayStack(10);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
	}
}
