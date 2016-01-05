package com.anacleto.parsing.source;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.anacleto.base.*;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.hierarchy.Shelf;

/**
 * Base class to work with content sources. A content source
 * is something that can be indexed and displayed. The content manager of 
 * a content source is the class that implements the rules of indexing
 * and retreiving the content of this source. Es. the FileSystemSourceManager
 * is the class for indexing files that resides in 
 * @author robi
 *
 */
public abstract class SourceTypeHandlerFactory {

	private static Logger logger = Logging.getUserEventsLogger();
	
	private static HashMap handlers;
	
	public static void initialize(){
		handlers = new HashMap();
		
//		register standard content handlers
		handlers.put(Constants.FileSystemContent, FSProcessor.class);
		handlers.put(Constants.NXT3Content, StructuredBookIndexer.class);
		handlers.put(Constants.WebDavContent, WebDavProcessor.class);
		handlers.put(Constants.WebServerContent, WebServerProcessor.class);
		handlers.put(Constants.PdfPerPageContent, PdfPerPageProcessor.class);
		handlers.put(Constants.TEI2XMLContent, TeiProcessor.class);
		handlers.put(Constants.BiblXMLContent, BiblXmlProcessor.class);
	}
	
	public static SourceTypeHandler getHandler(HierarchicalElement element) throws IOException{
		
		if (element instanceof Shelf) {
			//Shelf new_name = (Shelf) element;
			
		} else if (element instanceof Book){
			Book book = (Book)element;
			logger.info("ContentType: " + book.getContentType() 
					+ ", ContentTypeHandler: " + book.getContentTypeHandler());
			return getRegisteredHandler( book.getContentType(), book.getContentTypeHandler());
			
		} else if (element instanceof BookPage){
			BookPage bookPage = (BookPage)element;
			Book book = bookPage.getBook();
			return getRegisteredHandler( book.getContentType(), book.getContentTypeHandler());
		}
		
		return null;
	}
	
	private static SourceTypeHandler getRegisteredHandler(String registryStr, String handler){
		
		//First search for built-in handlers
		Class cl = (Class)handlers.get(registryStr);
		
		//if not found, get plugin
		if (cl == null){
			try {
				cl = Class.forName(handler);
			} catch (ClassNotFoundException e) {
				logger.error("Handler class not found for contentType: " 
						+ registryStr + " class: " + handler);

			}
		}
		
		if (cl != null){
			try {
				return (SourceTypeHandler)cl.newInstance();
			} catch (Exception e) {
				logger.error("Unable to create handler for contentType: " 
						+ registryStr);
			}
		} else {
			logger.error("No registered handler found for contentType: " 
					+ registryStr + " class: " + handler);
		}
		return null;
	}
	
	
	public static void registerNewSourceTypeHandler(String sourceType, 
			String className) throws ClassNotFoundException{
		
		Class cl = Class.forName(className);
		handlers.put(sourceType, cl);
	}
		
	public static void registerNewSourceTypeHandler(String sourceType, 
			Class cl) {
		handlers.put(sourceType, cl);
	}
	
	
	

}
