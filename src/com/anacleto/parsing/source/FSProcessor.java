/*
 * Created on Mar 17, 2005
 *
 */
package com.anacleto.parsing.source;

import java.io.*;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Hits;

import com.anacleto.base.*;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexException;
import com.anacleto.index.IndexManager;
import com.anacleto.parsing.ParserException;
import com.anacleto.parsing.filetype.FileContentParser;

/**
 * Class to process files from a file-system
 * 
 * @author robi
 */
public class FSProcessor extends SourceTypeHandler{

    protected String collectionName;

    protected int count = 0;

    protected IndexManager indexMan;

    protected boolean searcherUpdateNeeded = false;

    protected Logger logger = Logging.getIndexingLogger();

    
    
    /** 
     * FileSystem content
	 * @see com.anacleto.parsing.source.SourceTypeHandler#getContent(com.anacleto.base.HierarchicalElement, java.io.InputStream, java.lang.String)
	 */
	public ContentBean getContent(BookPage page) throws ContentReadException {
		
		ContentBean cb = new ContentBean(new File(page.getLocation()), 
				page.getEncoding());
		
		return cb;
	}


	/**
     * Processing a filesystem means to index all files in the filesystem that
     * are under a root directory.
     * 
     * Steps to process a file system: -delete inexistent entries from the index
     * -a file will be indexed if a.) it has no corresponding index entry b.) it
     * is indexed before the date of last change of the file
     * 
     * The indexing entry is composed of: -name: content collection name + full
     * path of the document -parentName: evident, always a directory
     * -childcount: the sequential number of the file in the directory, ordered
     * by name -book: content collection name -location: full path of the
     * document -contentType:FSCONTENT
     * 
     * @param rootDir
     * @param collectionName
     * @param indexMan
     * @throws IOException
     * @throws IndexException
     * @throws ParserException
     */
    public void indexBook(Book book) throws BookException, IOException {
    	
        this.collectionName = book.getName();
        this.indexMan = new IndexManager();
        
        File rootDir =  new File(book.getURL());
        
        searcherUpdateNeeded = false;

        
        if (!rootDir.exists())
            throw new BookException("Inexistent file: " + rootDir);

        try {
			deleteInexistentIndexEntries();
		
	        if (searcherUpdateNeeded)
	            indexMan.updateSearcher();
	        
	        
	        indexMan.openBatchWriter();
	        processDir(rootDir, collectionName, 0);
	        
	        
/*	
	        if (searcherUpdateNeeded)
	            indexMan.updateSearcher();
    */    
		} catch (IOException e) {
			throw new BookException("Error while processing " +
					collectionName + ". Root cause:" + e);
		} finally {
			try {
				indexMan.closeBatchWriter();
				indexMan.updateSearcher();
			} catch (IOException e2) {
				logger.error(e2);
			}
		}

    }

    private void deleteInexistentIndexEntries()
            throws IOException {
        Hits hits = indexMan.getBookEntries(collectionName);
        IndexReader reader = IndexReader.open(Configuration.params
				.getIndexDir());
        
        for (int i = 0; i < hits.length(); i++) {
            Document currDoc = indexMan.getFastDocument(reader, hits.id(i));
            File location = new File(currDoc.get("location"));
            logger.info("Checking location: " + location.getCanonicalPath());
            
            if (!location.exists()) {
                indexMan.deletePage(currDoc.get("name"));
                searcherUpdateNeeded = true;
                logger.debug("Entity deleted from index: "
                                + currDoc.get("name"));
            }
        }
    }

    private void processDir(File currentFile, String parentName, int childCount)
            throws IOException {

    	logger.debug("Processing: " + currentFile.getName());
    	
    	BookPage page = new BookPage();
        count++;
        
        String name = parentName + "/" + currentFile.getName();
        
        page.setName(name);
        page.setParentName(parentName);
        page.setChildCount(childCount);
        page.setTitle(currentFile.getName());
        page.setBookName(collectionName);
        
        page.setLocation(currentFile.getCanonicalPath());
        page.setContentType(Constants.FileSystemContent);
        
        page.setPath("/" + name);
        
		page.addTextField("indexedAt", String.valueOf(System
                .currentTimeMillis()));

        if (currentFile.isDirectory()) {
            //directory is added only if it is not present:
            if (indexMan.getPage(page.getName()) == null) {
                indexMan.addPageInBatch(page);
                logger.debug("Directory Entity added to index: "
                        + page.getName());
            }
            logger.debug("File Entity added to index: " + page.getName());
            
            File child[] = currentFile.listFiles();
            for (int i = 0; i < child.length; i++) {
                processDir(child[i], page.getName(), i);
            }

        } else {
            try {
                //we do not want to reindex files that are not changed
                //since last indexing
                long indexedAt = 0;
                Document doc = indexMan.getPage(page.getName());
                if (doc != null)
                    indexedAt = Long.valueOf(doc.get("indexedAt")).longValue();
                if (indexedAt >= currentFile.lastModified())
                    return;

                FileContentParser fcp = new FileContentParser();
                fcp.processContentType(currentFile, page);

                searcherUpdateNeeded = true;
                
                indexMan.addPageInBatch(page);
                logger.debug("File Entity added to index: " + page.getName());
                
            } catch (ParserException e) {
                //log this event, but not block index writing
                logger.warn("Unable to parse file: "
                        + currentFile.getCanonicalPath() + ". Cause:" + e);
            } catch (Exception e) {
                //file was cancelled during the process. do not care, go on
                logger.error("Unable to parse file: "
                        + currentFile.getCanonicalPath()
                        + ". Root cause:" + e );
            }
        }
    }
}
