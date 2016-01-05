/*
 * Created on Apr 25, 2005
 *
 */
package com.anacleto.index;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;

import com.anacleto.base.*;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.parsing.source.SourceTypeHandler;
import com.anacleto.parsing.source.SourceTypeHandlerFactory;
import com.anacleto.util.MilliSecFormatter;

/**
 * Main class to index books
 * @author robi
 */
public class BookIndexer {
	
	private Book   book;
	Logger log = Logging.getIndexingLogger();
	
	public BookIndexer(String bookName) throws JobExecutionException, IOException{		
		HierarchicalElement el = Configuration.getElement(bookName);
	    if (el == null || !(el instanceof Book)) 
	    	throw new JobExecutionException("Collection not found!");
	    
	    this.book = (Book)el;
	}
	
	public synchronized void indexBook(){

		long start = System.currentTimeMillis();
        
	    log.info("Scheduled indexing started for collection: '" 
	    		+ book.getName() + "'");
	   
	    try {
	    	SourceTypeHandler handler = SourceTypeHandlerFactory.getHandler(book);
	    	handler.indexBook(book);
	    } catch (IOException e1) {
	        log.error("Scheduled indexing finished with errors. " + e1);
	    } catch (BookException e2) {
	    	e2.printStackTrace();
			log.error(e2);
		}     
	    log.info("Scheduled indexing finished for collection: '" 
	    		+ book.getName()+ "' Duration: " + 
	    		MilliSecFormatter.toString(
	    			(long)(System.currentTimeMillis()-start)));
	}
	
}
