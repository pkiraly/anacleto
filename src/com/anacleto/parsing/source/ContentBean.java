package com.anacleto.parsing.source;

import java.io.*;

import com.anacleto.content.LocalContentReader;

/**
 * Store content related information
 * We can put the content in a string, or in an input stream
 * use exactly one of them
 *  
 * @author robi
 *
 */
public class ContentBean {
	
	private String 		mime = "";

	private String      stringValue;
	
	private File        fileValue;
	private String      fileEncoding;
	
	/**
	 * Use file to set content
	 * @param file
	 */
	public ContentBean(File file, String encoding){
		if (file.isDirectory())
			return;
		
		int lastPoint = file.getName().lastIndexOf('.');
		if (lastPoint >= 0)
			setMime(file.getName().substring(lastPoint+1));
		
		this.fileEncoding = encoding;
		this.fileValue    = file;
	}
	
	/**
	 * Use a string as the content, this content will be handled as a html
	 * @param stringValue
	 */
	public ContentBean(String stringValue){
		this.stringValue = stringValue;
		this.mime = "html";
	}
	
	/**
	 * Use a string as the content, this content will be handled as a html
	 * @param stringValue
	 */
	public ContentBean(String stringValue, String mime){
		this.mime = mime;
		this.stringValue = stringValue;
	}

	public String getStringContent() throws ContentReadException{
		
		if (stringValue != null){
			return stringValue;
		}
		
		if (fileValue != null){
			LocalContentReader reader = new LocalContentReader();
			
			return reader.readContent(fileValue, fileEncoding);
		}
		return "";
	}
	
	/**
	 * @return Returns the mime.
	 */
	public String getMime() {
		return mime.toLowerCase();
	}

	/**
	 * @param mime The mime to set.
	 */
	public void setMime(String mime) {
		this.mime = mime;
	}

}
