package com.anacleto.parsing;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

/**
 * Node attributes used for the SAXHandlerWithStack class
 * @author robi
 *
 */
public class SAXHandlerNode {
	
	/**
	 * The URI of the namespace of the node
	 */
	private String namespaceURI;

	/**
	 * the local name of the node
	 */
	private String   localName;

	/**
	 * the qualified name of the node (with namespace prefix)
	 */
	private String   qName;

	/**
	 * the attributes of the node
	 */
	private Map      attributes;
	
	/**
	 * the content of the node
	 */
	private StringBuffer value;

	/**
	 * Create new node.
	 * @param localName - the local name of the node
	 * @param atts - the attributes of the node
	 */
	public SAXHandlerNode(String localName, Attributes atts) {
		this.localName  = localName;
		this.attributes = new HashMap();
		this.value = new StringBuffer();
		for (int i = 0; i < atts.getLength(); i++) {
			attributes.put(atts.getQName(i), atts.getValue(i));
		}
	}

	/**
	 * Create new node.
	 * @param localName - the local name of the node
	 * @param qName - the qualified name of the node
	 * @param atts - the attributes of the node
	 */
	public SAXHandlerNode(String localName, String qName, Attributes atts) {
		this.localName  = localName;
		this.qName  = qName;
		this.attributes = new HashMap();
		this.value = new StringBuffer();
		for (int i = 0; i < atts.getLength(); i++) {
			attributes.put(atts.getQName(i), atts.getValue(i));
		}
	}

	/**
	 * Create new node.
	 * @param namespaceUri - the URI of the namespace of the node
	 * @param localName - the local name of the node
	 * @param qName - the qualified name of the node
	 * @param atts - the attributes of the node
	 */
	public SAXHandlerNode(String namespaceUri, String localName, String qName, Attributes atts) {
		this.namespaceURI = namespaceURI;
		this.localName = localName;
		this.qName = qName;
		this.attributes = new HashMap();
		this.value = new StringBuffer();
		for (int i = 0; i < atts.getLength(); i++) {
			attributes.put(atts.getQName(i), atts.getValue(i));
		}
	}

	/**
	 * Append the node's content with text (char[])
	 * @param text
	 * @param start
	 * @param length
	 */
	public void appendText(char[] text, int start, int length){
		value.append(text, start, length);
	}

	/**
	 * Append the node's content with text (String)
	 * @param text
	 */
	public void appendText(String text){
		value.append(text);
	}

	/**
	 * @return Returns the attributes map.
	 */
	public Map getAttributes() {
		return attributes;
	}

	/**
	 * Set the attributes map.
	 * @param attributes The attributes to set.
	 */
	public void setAttributes(Map attributes) {
		this.attributes = attributes;
	}

	/**
	 * Returns the content of the node.
	 * @return Returns the value.
	 */
	public StringBuffer getValue() {
		return value;
	}

	/**
	 * Set the content of the node.
	 * @param value The value to set.
	 */
	public void setValue(StringBuffer value) {
		this.value = value;
	}

	/**
	 * Returns the local name (without namespace prefix) of the node
	 * @return Returns the localName.
	 */
	public String getLocalName() {
		return localName;
	}
	
	/**
	 * Returns the qualified name of the node (with namespace prefix)
	 * @return qName - the qualified name of the node (with namespace prefix) 
	 */
	public String getQName() {
		return qName;
	}

	/**
	 * Returns the URI of the namespace of the node
	 * @return qName - the URI of the namespace of the node
	 */
	public String getNamespaceURI() {
		return namespaceURI;
	}
	
	public String toString() {
		return qName;
	}
}

