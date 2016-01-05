package com.anacleto.parsing;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.anacleto.base.Logging;


/**
 * Class to handle common sax problem: How to remember the achestors
 * @author robi
 *
 */
public abstract class SAXHandlerWithStack implements ContentHandler {

	protected NodeStack stack;
	
	protected SAXHandlerNode parentNode;
	
	private static final int stackInitialSize = 10;
	
	/**
	 * Receive notification of the beginning of an element.
	 * @param node
	 */
	public abstract void startElement(SAXHandlerNode node);
	
	/**
	 * Receive notification of the end of an element.
	 * @param SAXHandlerNode node
	 */
	public abstract void endElement(SAXHandlerNode node);
	
	/**
	 * Get the parent node
	 * @return
	 */
	public SAXHandlerNode getParentNode(){
		return parentNode;
	}
	
	/**
	 * Receive notification of character data.
	 * @param ch - the characters from the XML document
	 * @param start - the start position in the array
	 * @param length - the number of characters to read from the array
	 */
	public void characters(char[] text, int start, int length) 
			throws SAXException {
		stack.addTextToTopNode(text, start, length);
	}

	public void endDocument() throws SAXException {
	}

	/**
	 * Receive notification of the end of an element.
	 * @param namespaceUri - the Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed
	 * @param localName - the local name (without prefix), or the empty string if Namespace processing is not being performed
	 * @param qName - the qualified XML name (with prefix), or the empty string if qualified names are not available 
	 */
	public void endElement(String namespaceUri, String localName, String qName)
			throws SAXException {
		
		SAXHandlerNode node = (SAXHandlerNode) stack.pop();
		
		if (stack.size() > 0) {
			parentNode = (SAXHandlerNode)stack.get();
		} else
			parentNode = null;

		endElement(node);
	}
	
	/**
	 * Return the stack of nodes
	 * @return
	 */
	public Collection getStack(){
		Collection retColl = new LinkedList();
		for (int i = 0; i < stack.size(); i++) {
			retColl.add(stack.get(i));
		}
		
		return retColl;
	}
	
	/**
	 * End the scope of a prefix-URI mapping.
	 * @param String prefix - the prefix that was being mapped. This is the empty string when a default mapping scope ends.
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	public void ignorableWhitespace(char[] ch, int start, int end)
			throws SAXException {
	}

	/**
	 * Receive notification of the beginning of a document.
	 */
	public void processingInstruction(String target, String data)
			throws SAXException {
	}
	
	public void setDocumentLocator(Locator arg0) {
	}
	
	/**
	 * Receive notification of a skipped entity.
	 */
	public void skippedEntity(String name) throws SAXException {
	}
	
	
	public void startDocument() throws SAXException {
		stack = new NodeStack(stackInitialSize);
	}
	
	public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
		if (stack.size() > 0) {
			parentNode = (SAXHandlerNode)stack.get();
		} else
			parentNode = null;
		
		stack.addNewNode(localName, atts);
		startElement((SAXHandlerNode)stack.get());
	}
	
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
	}
}


