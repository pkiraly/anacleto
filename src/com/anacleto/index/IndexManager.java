/*
 * Created on Mar 17, 2005
 *
 */
package com.anacleto.index;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.base.GlobalCache;
import com.anacleto.hierarchy.BookPage;

/**
 * Class to do certain index operations.
 * @author robi
 */
public class IndexManager {

	
    /**
     * The directory in which the index exists
     */
	private static 	Directory	  	indexDir;
    
    private static	CustomAnalyzer  analyzer;
    
    private static	String      	defaultQueryField;
    
    private static  CachedSearcher  searcher ;
    
    private static  Sort sort;
    
    private static  Logger log = Logging.getIndexingLogger();
        
    public static synchronized void initialize(String indexDirPath, 
    								CustomAnalyzer analyzer, 
								   String defaultQueryField,
								   int sortType){
    	File inDirFile = new File(indexDirPath);
    	try {
			if (!inDirFile.exists()){
				log.error("Inexistent indexing directory: \""	+ indexDirPath+
						"\". Please create this directory or change the path!");
				return;				
			} else {
				IndexManager.indexDir = FSDirectory.getDirectory(inDirFile, false);
		    	IndexManager.analyzer = analyzer;
		    	IndexManager.defaultQueryField = defaultQueryField;
	        
		    	log.info("Indexing initialized in the directory: " + indexDir);
	    	
				//TODO: how to do this when indexing
		        if (IndexReader.isLocked(indexDir))
		        	IndexReader.unlock(indexDir);
		        
				//create index if it's not created yet:
		        if (!IndexReader.indexExists(indexDir)){
		        	IndexWriter writer = new IndexWriter(indexDir, analyzer, true);
		        	log.info("Index created");
		            writer.close();	
		        }
		        
		        IndexReader reader = IndexReader.open(indexDir);
		        IndexManager.searcher = new CachedSearcher(
		        		new IndexSearcher(indexDir), reader);
		        //reader.close();
		        
		        if (sortType == Constants.SORT_BY_HIERARCHY){
		        	sort = new Sort(new SortField("book", 
		        			new BookPageOrderSource()));
			        //do a first query to init the comparator
					try {
						Query q = QueryParser.parse("dummy", defaultQueryField, analyzer);
						searcher.search(q, sort);
					} catch (ParseException e) {}
					
		        } else if (sortType == Constants.SORT_BY_RELEVANCE){
		        	sort = new Sort();
		        }
		        
			}  
    	} catch (IOException e) {
    		log.error("Error occured during indexManager initialization: " + e);
		}


    }
    
    public static synchronized void destroy(){
    	sort = null;
    }

    public IndexManager() throws IOException{
    	if (indexDir == null)
    		throw new IOException("Indexing system is not set properly: indexDir is null.");
    }
    
    public Searcher getSearcher(){
    	return searcher;
    }
    
    /**
     * Delete a book from the index.
     * @param bookName
     * @throws IOException
     */
    public synchronized void deleteBook(String bookName) throws IOException{
    	
    	IndexReader reader   = IndexReader.open(indexDir);
        
        Term t = new Term("book", bookName);
        reader.delete(t);
        
        reader.close();
        updateSearcher();
        
    }

    private IndexWriter getWriter() throws IOException{
    	IndexWriter retWriter = new IndexWriter(indexDir, analyzer, false);
     	retWriter.maxFieldLength = Integer.MAX_VALUE;
        return retWriter;
    }
      
    private IndexWriter batchWriter;
    
    public synchronized void openBatchWriter() throws IOException{
    	batchWriter = getWriter();
    }
    
    public synchronized void addPageInBatch(BookPage page) throws IOException{
        //TODO: validate page first
    	batchWriter.addDocument( page.getDocument());
    }

    public synchronized void closeBatchWriter() throws IOException{
    	batchWriter.close();
    	updateSearcher();
    }
    
    /**
     * Insert/update an entity in the index
     * @param entity
     * @throws IOException
     */
    public void addPage(BookPage page) throws IOException{
    	synchronized (IndexManager.class) {
    	    try{
    	        deletePage(page.getName());
    	        
    	        IndexWriter writer = getWriter();
    	        
    	        //TODO: validate page first
    	        writer.addDocument( page.getDocument());
    	        writer.close();
    	        
            } catch (IOException e){
                throw new IOException("Unable to update index for entity: " 
                		+ page.getName()+ ". Error: " + e);
            }
    	}
    }
    
    public synchronized void deletePage(String name) throws IOException{
        IndexReader reader   = IndexReader.open(indexDir);
        
        Term t = new Term("name", name);
        reader.delete(t);
        
        reader.close();
    }
    
    public Document getDocument(int n) throws IOException{
        IndexReader reader   = IndexReader.open(indexDir);
        Document doc = null;
        if (!reader.isDeleted(n))
        	doc = reader.document(n);
        reader.close();
        
        return doc;
    }

    public Document getFastDocument(IndexReader reader, int n) throws IOException {
    	
    	GlobalCache cache = new GlobalCache(GlobalCache.FastDocumentCache);
    	if (reader.isDeleted(n))
			return null;
    	
		Document retDoc = (Document)cache.retrieve(
				new Integer(n));
		if (retDoc == null){
			retDoc = reader.fastDocument(n);
			cache.store(new Integer(n), retDoc);
		}
        return retDoc;
    }

    public Document getPage(String name) throws IOException{
        Query query = new TermQuery(new Term("name", name));
        Hits hits = searcher.search(query);
        Document retDoc = null;
        
        if (hits.length() >= 1)
            retDoc = hits.doc(0);
         
        return retDoc;
    }
    
    public int getPageNumber(String name) throws IOException{
        Query query = new TermQuery(new Term("name", name));
        Hits hits = searcher.search(query);
        
        if (hits.length() >= 1)
            return hits.id(0);
         
        return 0;
    }
    
    public synchronized void optimize() throws IOException{

    	IndexWriter writer = getWriter();
        writer.optimize();
        writer.close();
        
        //updateSearcher();
    }
    
    public Hits getBookEntries(String book) throws IOException{
        Query query = new TermQuery(new Term("book", book));
        
        Hits hits = searcher.search(query);
        return hits;
    }

    public Document findNamedElement(String name) throws IOException{
    	//TODO: or throw an exception?
    	if (name == null)
    		return null;
    	
        Document retDoc = null;
        Query query = new TermQuery(new Term("name", name));
        Hits hits = searcher.search(query);
        if (hits.length() == 1) {
            retDoc = hits.doc(0);
        }
        return retDoc;
    }
    
    public Collection findChildElements(String name) throws IOException {
        Collection retColl = new LinkedList();
        
	    Query query = new TermQuery(new Term("parentName", name));
	    
	    Hits hits = searcher.search(query);
        for (int i = 0; i < hits.length(); i++) {
            //exclude the element itself:
            BookPage child = new BookPage(hits.doc(i));
            if (!child.getName().equals(name))
                retColl.add(child);
        }
	    return retColl;
    }

    public Collection findLogicalChildElements(String name, String title) 
    throws IOException, ParseException {
    	HashMap finalOrder = new HashMap();
        // Collection QNames = new LinkedList();
        
        String logicalParent = name + ":" + title;
        // escape characters having special meaning in regex
        String logicalParentPattern = logicalParent
			.replaceAll("\\(", "\\\\(")
			.replaceAll("\\)", "\\\\)")
			.replaceAll("\\[", "\\\\[")
			.replaceAll("\\]", "\\\\]")
			.replaceAll("\\?", "\\\\?")
			.replaceAll("\\+", "\\\\+")
			.replaceAll("\\*", "\\\\*")
			;
        log.info(logicalParent + ", " + logicalParentPattern);

	    Query q = new TermQuery(new Term("logicalParent", logicalParent));
	    log.info("findLogicalChildElements: query: " + q.toString());
	    Hits hits = searcher.search(q, new Sort("siblingCounter"));
	    log.info("hits: " + hits.length());
	    
	    int lastSibling = 0;
	    for (int i = 0; i < hits.length(); i++) {
            //exclude the element itself:
            BookPage child = new BookPage(hits.doc(i));
           	Collection fields = child.getFields();
           	Iterator it = fields.iterator();
           	while(it.hasNext()) {
           		Field currField = (Field)it.next();
            	if(currField.name().equals("path")) {
            		String path = currField.stringValue();
            		Pattern pattern = Pattern.compile(
            			logicalParentPattern + "/([0-9]+):([^/]+:.+)$");
            		Matcher matcher = pattern.matcher(path);
            		if (matcher.find()) {
            			Integer siblingCounter = new Integer(matcher.group(1));
            			String QName = matcher.group(2);
            			Collection entry = new LinkedList();
            			entry.add(child);
            			entry.add(QName);
            			if(lastSibling < siblingCounter.intValue()) {
            				lastSibling = siblingCounter.intValue();
            			}
            			finalOrder.put(siblingCounter, entry);
            		}
            	}
           	}
        }
        Collection retColl = new LinkedList();
        for(int i=1; i<=lastSibling; i++) {
        	Integer key = new Integer(i);
        	if(finalOrder.containsKey(key)) {
        		Collection entry = (Collection) finalOrder.get(key);
        		Iterator it = entry.iterator();
        		while(it.hasNext()) {
        			retColl.add(it.next());
        		}
        	} else {
        		log.error("finalOrder doesn't contain key: " + key);
        	}
        }
	    
	    log.info("/findLogicalChildElements");
	    return retColl;
    }

    public Collection findChildDocuments(String name) throws IOException{
        Collection retColl = new LinkedList();
        
	    Query query = new TermQuery(new Term("parentName", name));
	    
	    Hits hits = searcher.search(query);
        for (int i = 0; i < hits.length(); i++) {
            //exclude the element itself:
            BookPage child = new BookPage(hits.doc(i));
            if (!child.getName().equals(name))
                retColl.add(hits.doc(i));
        }
	    return retColl;
    }
    
    public boolean hasChildElements(String name) throws IOException{
        Query query = new TermQuery(new Term("parentName", name));
        Hits hits = searcher.search(query);
    	// log.info(name + " hasChildElements: " + hits.length());

        if (hits.length() > 0)
            return true;
        
        return false;
    }
    
    public boolean hasLogicalChildElements(String name, String title) throws IOException{
        String logicalParent = name + ":" + title;

        Query query = new TermQuery(new Term("logicalParent", logicalParent));
        Hits hits = searcher.search(query);
    	log.info(name + " hasLogicalChildElements: " + hits.length());

        if (hits.length() > 0)
            return true;
        
        return false;
    }

    public Document executeQuery(String q, int hitNo) throws IOException, ParseException{      
    	Query query = getQuery(q);
        Hits hits = searcher.search(query);
        return hits.doc(hitNo);
    }
    
    public Document executeTermQuery(Term t, int hitNo) throws IOException, ParseException{      
    	Document retDoc = null;
        Query query = new TermQuery(t);
        Hits hits = searcher.search(query);
        if (hits.length() == 1) {
            retDoc = hits.doc(0);
        }
        return retDoc;
    }
    /**
     * Executes a simple query. The default field is
     * results are sorted
     * @param q
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Hits executeQuery(String query) throws IOException, ParseException{
    	// log.info("executeQuery 1) input: '" + query + "'");
    	Query q = getQuery(query);
    	// log.info("executeQuery 2) executed query: '" + q + "'");
    	return searcher.search(q, sort);
    }
    
    public Query getQuery(String query) throws IOException, ParseException{
    	//log.info("getQuery 1) input: '" + query + "'");

    	HashMap pathMap = new HashMap();
    	Pattern path = Pattern.compile("(path:[^ ]+)");
    	Matcher m = path.matcher(query);
    	StringBuffer extractedQuery = new StringBuffer();
    	int start = 0, i = 0;
    	while(m.find()) {
    		extractedQuery.append(query.substring(start, m.start()));
    		String placeholder = "/path" + i + "/";
    		extractedQuery.append("path:" + placeholder + "*");
    		pathMap.put(placeholder, query.substring(m.start()+5, m.end()));
    		start = m.end();
    	}
    	extractedQuery.append(query.substring(start, query.length()));
    	query = extractedQuery.toString();
		// log.info("removed query: " + query);
        query = analyzer.translateQuery(query);
    	// log.info("getQuery 2) translated query: '" + query + "'");
        Query q = QueryParser.parse(query, defaultQueryField, analyzer);
    	// log.info("getQuery 3) parsed query: '" + q + "'");
    	
		if(pathMap.size() == 0) {
			q = searcher.rewrite(q);
		} else {
			q = searcher.rewrite(q, pathMap);
		}
    	// log.info("getQuery 4) rewriten query: '" + q + "'");
        return q;
    }
    
    public String AnalyzeQuery(Query q) {
    	StringBuffer query = new StringBuffer();
    	
    	
    	return query.toString();
    }
    
    public synchronized TermFreqVector getTermFreq(int docNo) throws IOException{
    	IndexReader reader = null;
    	
    	try{
	    	reader   = IndexReader.open(indexDir);
	    	TermFreqVector retVector = reader.getTermFreqVector(
	        		docNo,"content");
	        return retVector;
        } catch (IOException e){
        	throw e;
        } finally {
	        reader.close();
        }
    }
    
    
    public void updateSearcher() throws IOException{
    	if ( indexDir!= null)
    		IndexManager.searcher = new CachedSearcher( 
    				new IndexSearcher(indexDir), 
    				IndexReader.open(indexDir));
    }
    
    /**
     * Returns with the number of indexed documents within a book
     * @param bookName
     * @return
     * @throws IOException
     */
    public HashMap getBookCounters() throws IOException{
        HashMap retMap = new HashMap();
        IndexReader reader   = IndexReader.open(indexDir);
        
        WildcardTermEnum tes = new WildcardTermEnum(reader, 
                new Term("book", "*"));
        
        while (tes.term()!= null){
            retMap.put(tes.term().text(), new Integer(tes.docFreq()));
            tes.next();
        }        
        reader.close();
        return retMap;
    }
    
    public Collection getIndexedBooks() throws IOException{
    	Collection retColl = new LinkedList();
        IndexReader reader   = IndexReader.open(indexDir);
        
        WildcardTermEnum tes = new WildcardTermEnum(reader, 
                new Term("book", "*"));
        
        while (tes.term()!= null){
            retColl.add(tes.term().text());
            tes.next();
        }
        return retColl;
    }


}


