/*
 * Created on Apr 30, 2005
 *
 */
package com.anacleto.index;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.buffer.UnboundedFifoBuffer;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;

import com.anacleto.base.Logging;

/**
 * @author robi
 *
 */
public final class BookIndexQueue extends Thread{
	
	private static boolean currentlyIndexing = false;
	private static String  currentlyIndexingBook = "";
	
	private volatile Thread thread_id = this;
	
	private static Buffer bookBuffer = //new UnboundedFifoBuffer();
		BufferUtils.synchronizedBuffer(new UnboundedFifoBuffer());
	
	private Logger log = Logging.getIndexingLogger();
	
	public BookIndexQueue(){
		this.start();
	}

	public void destroy(){
		thread_id = null;
	}
	
	/**
	 * Add new book to the queue. If element is already registered
	 * do not add it again, 
	 * @param bookName
	 */
	public static synchronized void addBookToIndex(String bookName){
		
		boolean elementFound = false;
		Iterator iter = bookBuffer.iterator();
		while (iter.hasNext()) {
			String element = (String) iter.next();
			if (element.equals(bookName)){
				elementFound = true;
				break;
			}
		}
		if (!elementFound)
			bookBuffer.add(bookName);
	}
	
	public static void addBookToIndex(Collection bookNameColl){
		Iterator it = bookNameColl.iterator();
		while (it.hasNext()) {
			String bookName = (String) it.next();
			addBookToIndex(bookName);
		}
	}

	public static String getCurrentlyIndexingBook(){
		return BookIndexQueue.currentlyIndexingBook;
	}
	
	public static Collection getBooksInQueue(){
		Collection retColl = new LinkedList();
		Iterator iter = bookBuffer.iterator();
		while (iter.hasNext()) {
			String element = (String) iter.next();
			retColl.add(element);
		}
		return retColl;
	}
	
	
	/**
	 * @return Returns the currentlyIndexing.
	 */
	public static boolean isCurrentlyIndexing() {
		return currentlyIndexing;
	}

	public void run()
	{
		while(thread_id == this)
		{
			try {
				if (bookBuffer.isEmpty())
					sleep(1000);
				else {
					String bookToIndex = (String)bookBuffer.remove();
					try {
						BookIndexQueue.currentlyIndexing = true;
						BookIndexQueue.currentlyIndexingBook = bookToIndex;
						log.info("bookToIndex: " + bookToIndex);
						indexBook(bookToIndex);
						
					} catch (JobExecutionException e1) {
						log.error(e1);
					} catch (Exception e2){
						e2.printStackTrace();
						log.error(e2);
					} finally{
						BookIndexQueue.currentlyIndexing = false;
						BookIndexQueue.currentlyIndexingBook = "";
					}
				}
			} catch(InterruptedException e){
				
			}
		}
	}

	private synchronized void indexBook(String bookToIndex) throws JobExecutionException, IOException{
		BookIndexer in = new BookIndexer(bookToIndex);
		in.indexBook();
		
		IndexManager im = new IndexManager();
		im.updateSearcher();
	}

	public static synchronized void optimizeIndex() throws IOException{
		IndexManager im = new IndexManager();
		im.optimize();
	}

}
