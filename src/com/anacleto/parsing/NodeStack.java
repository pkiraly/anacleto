package com.anacleto.parsing;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import com.anacleto.base.Logging;

/**
 * A stack of document nodes, with some common methods
 * @author robi
 *
 */
public final class NodeStack extends ArrayStack {
	
	private static final long serialVersionUID = 8566785292524182786L;
	private Logger log = Logging.getIndexingLogger();

	public NodeStack(int initSize) {
		super(initSize);
	}
	
	/**
	 * add new node to the top of the stack
	 */
	public void addNewNode(String localName, Attributes atts){
		SAXHandlerNode newNode = new SAXHandlerNode(localName, atts);
		push(newNode);
	}

	/**
	 * add new node to the top of the stack
	 */
	public void addNewNode(String namespaceUri, String localName, String qName, Attributes atts){
		SAXHandlerNode newNode = new SAXHandlerNode(namespaceUri, localName, qName, atts);
		push(newNode);
	}

	public void addTextToTopNode(char[] text, int start, int length){
		
		if (size() > 0){
			SAXHandlerNode topNode = (SAXHandlerNode) get();
			topNode.appendText(text, start, length);
		}
	}

	public void addTextToTopNode(String text){
		
		if (size() > 0){
			SAXHandlerNode topNode = (SAXHandlerNode) get();
			topNode.appendText(text);
		}
	}
}
