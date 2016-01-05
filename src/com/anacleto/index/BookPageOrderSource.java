package com.anacleto.index;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.ScoreDocComparator;
import org.apache.lucene.search.SortComparatorSource;
import org.apache.lucene.search.SortField;

import com.anacleto.base.Configuration;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.Shelf;

/**
 * Search result sorting. The results will be sorted primary by the position 
 * of the book of the result, secondary by the document number of the result.
 * This second depends on the indexing order. 
 *  
 * @author robi
 *
 */
class BookPageOrderSource implements SortComparatorSource {

	private static final long serialVersionUID = 2269461990043167174L;

	//
	private static      Logger log = Logging.getIndexingLogger();
	
	
	
	/**
	 * @see org.apache.lucene.search.SortComparatorSource#newComparator(org.apache.lucene.index.IndexReader, java.lang.String)
	 */
	public ScoreDocComparator newComparator(IndexReader reader, String fieldname)
			throws IOException {
		
		log.info("Initializing ordering system... ");
		
		Runtime rt = Runtime.getRuntime();
		
		log.debug("Used memory before ordering: " +  
				(rt.totalMemory() - rt.freeMemory()));
		
		Map bookMap = new TreeMap();
		int[] docDist;
		
		long start = System.currentTimeMillis();
		Shelf rootEl = (Shelf)Configuration.getElement("root");
	    Collection bookColl = rootEl.getChildBooks();
		Iterator it = bookColl.iterator();
		int counter = 0;
		while (it.hasNext()) {
			Book book = (Book)it.next();
			bookMap.put(book.getName(), new Integer(counter));
			counter++;
		}

		log.debug("Used memory after initializing bookMap: " +  
				(rt.totalMemory() - rt.freeMemory()));

		docDist = new int[reader.maxDoc()];
		for (int i = 0; i < docDist.length; i++) {
			if (reader.isDeleted(i))
				continue;
		
			Document doc = reader.fastDocument(i);
			
		    String book = doc.getField("book").stringValue();
		    
		    Integer bookCounter = (Integer)bookMap.get(book);
		    if (bookCounter == null){
		    	//found a document without a book, put it on the end of the list:
		    	docDist[i] = Integer.MAX_VALUE;
		    } else {
		    	docDist[i] = bookCounter.intValue();
		    } 
		}
		
		long end = System.currentTimeMillis();

		bookMap = null;
		log.debug("Used memory after cleanup: " +  
				(rt.totalMemory() - rt.freeMemory()));
		log.debug("Total document number to order: " + docDist.length);
		
		log.info("Ordering system initialized in " + (end-start) + " ms");
		
		
		return new BookPageOrderComparator(docDist);
	}


	/**
	 * Order search results by the place of the book in the hierarhy
	 * @author robi
	 *
	 */
	private static class BookPageOrderComparator implements ScoreDocComparator {

		private int[] docDist;
		
		public BookPageOrderComparator(int[] docDist){
			this.docDist = docDist;
		}
		
		/* (non-Javadoc)
		 * @see org.apache.lucene.search.ScoreDocComparator#compare(org.apache.lucene.search.ScoreDoc, org.apache.lucene.search.ScoreDoc)
		 */
		public int compare(ScoreDoc i, ScoreDoc j) {
			
			if (docDist[i.doc] <  docDist[j.doc]) return -1;
			if (docDist[i.doc] >  docDist[j.doc]) return 1;
			
			//books are equal, take the indexing as a sequence:
			if (i.doc <  j.doc) return -1;
			if (i.doc >  j.doc) return 1;
			return 0;
		}
	
		/* (non-Javadoc)
		 * @see org.apache.lucene.search.ScoreDocComparator#sortValue(org.apache.lucene.search.ScoreDoc)
		 */
		public Comparable sortValue(ScoreDoc i) {
			return new Integer(docDist[i.doc]);
		}
	
		/* (non-Javadoc)
		 * @see org.apache.lucene.search.ScoreDocComparator#sortType()
		 */
		public int sortType() {
			return SortField.INT;
		}

	}
}


