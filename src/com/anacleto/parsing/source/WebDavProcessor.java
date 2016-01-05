/*
 * Created on Mar 17, 2005
 *
 */
package com.anacleto.parsing.source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.httpclient.HttpURL;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.webdav.lib.WebdavResource;
import org.apache.webdav.lib.WebdavResources;

import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexManager;
import com.anacleto.parsing.ParserException;
import com.anacleto.parsing.UnhandledMimeTypeException;
import com.anacleto.parsing.filetype.FileContentParser;

/**
 * Class to process files from a file-system
 * 
 * @author robi
 */
public class WebDavProcessor extends SourceTypeHandler {

    private int count = 0;

    private IndexManager indexMan;

    private boolean searcherUpdateNeeded = false;

    private Logger logger = Logging.getIndexingLogger();

    

    /**
     * Processing a webdav destination means to index all readable files in that
     * resource
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
     * @throws ParserException
     */
    public void indexBook(Book book) throws IOException {

        this.indexMan = new IndexManager();

        searcherUpdateNeeded = false;

        HttpURL hrl = new HttpURL(book.getURL().toString());

        if (book.getUser() != null) {
            hrl.setUserinfo(book.getUser(), book.getPassword());
        }

        deleteInexistentIndexEntries(book);
        
        if (searcherUpdateNeeded)
            indexMan.updateSearcher();
        searcherUpdateNeeded = false;

        try {
	        WebdavResource wdr = new WebdavResource(hrl);
	        processDir(book, wdr, book.getName(), 0);
	        wdr.close();
        } catch (ConnectException e){
            //webdavresource not available
            logger.debug("WebDav resource not available: " + book.getURL());

        }
        if (searcherUpdateNeeded)
            indexMan.updateSearcher();

    }

    private void deleteInexistentIndexEntries(Book book) throws IOException {
        HttpURL hrl = new HttpURL(book.getURL());

        Hits hits = indexMan.getBookEntries(book.getName());
        for (int i = 0; i < hits.length(); i++) {
            Document currDoc = hits.doc(i);
            String location = currDoc.get("location");
            if (location == null)
                continue;

            try {
                hrl.setPath(location);
                //WebdavResource boundItem = new WebdavResource(hrl);
            } catch (Exception e) {
                //in any case, we delete the entity from the index:
                indexMan.deletePage(currDoc.get("name"));
                searcherUpdateNeeded = true;
                logger.debug("Entity deleted from index: "
                                + currDoc.get("name"));
            }

        }
        //wdre.bindMethod("/");
    }
    
    private void processDir(Book book, WebdavResource currentFile, String parentName,
            int childCount) throws IOException {

        BookPage page = new BookPage();
        count++;
        String name = book.getName() + currentFile.getPath();
        name = name.replace('\\', '/');

        page.setName(name);
        page.setParentName(parentName.replace('\\', '/'));
        page.setChildCount(childCount);
        page.setTitle(currentFile.getName());
        page.setBookName(book.getName());

        //TODO: change to relative path:
        page.setLocation(currentFile.getPath());
        
        //entity.addTextField("location", currentFile.getPath());
        page.setContentType(Constants.WebDavContent);
        //entity.addTextField("contentType", Constants.WebDavContent);
        
        page.addTextField("indexedAt", String.valueOf(System
                .currentTimeMillis()));

        if (currentFile.isCollection()) {
            //directory is added only if it is not present:
            if (indexMan.getPage(page.getName()) == null) {
                indexMan.addPage(page);
                logger.debug("Directory Entity added to index: "
                        + page.getName());
            }

            WebdavResources res = currentFile.getChildResources();
            WebdavResource resVec[] = res.listResources();
            for (int i = 0; i < resVec.length; i++) {
                if (resVec[i].exists())
                    processDir(book, resVec[i], page.getName(), i);
            }

        } else {
            try {
                //we do not want to reindex files that are not changed
                //since last indexing
                long indexedAt = 0;
                Document doc = indexMan.getPage(page.getName());
                if (doc != null)
                    indexedAt = Long.valueOf(doc.get("indexedAt")).longValue();

                if (indexedAt >= currentFile.getGetLastModified())
                    return;

                File fn = new File(currentFile.getName());
                currentFile.getMethod(fn);

                FileContentParser ftp = new FileContentParser();
                ftp.processContentType(fn, page);

                searcherUpdateNeeded = true;
                
            } catch (UnhandledMimeTypeException e) {
            	logger.warn("Unable to parse file: " + currentFile.getPath()
                        + ". Cause: " + e);
            } catch (ParserException e) {
                //log this event, but not block index writing
                logger.warn("Unable to parse file: " + currentFile.getPath()
                        + ". Cause: " + e);
            } catch (FileNotFoundException e) {
                //file was cancelled during the process. do not care, go on
                logger.warn("Unable to find file: " + currentFile.getPath());
                
            
			}
            
            indexMan.addPage(page);
            logger.debug("File Entity added to index: " + page.getName());
        }
    }

	/* (non-Javadoc)
	 * @see com.anacleto.parsing.source.SourceTypeHandler#getContent(com.anacleto.hierarchy.BookPage)
	 */
	public ContentBean getContent(BookPage page) throws ContentReadException {
//		webdav resource:
		/*
        String bookURL = page.getBook().getURL();
        HttpURL hrl = new HttpURL(bookURL);
        hrl.setPath(page.getLocation());

        
        try {
            WebdavResource wdre = new WebdavResource(hrl);
            if (!wdre.isCollection()){
                File outFile = new File(hrl.getName());
                wdre.getMethod(outFile);
                downloadMimeContent(outFile, request, response);
                return null;
            }
        } catch (ConnectException e){
            //webdav resource not available
            sDForm.setPageBody("Webdav resource not available");
        }
        */
		return null;
	}

    

}
