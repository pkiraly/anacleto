package com.anacleto.parsing.source;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;

import org.apache.commons.collections.FastTreeMap;
import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.anacleto.base.BookException;
import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.content.LocalContentReader;
import com.anacleto.content.LocalEntityResolver;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexManager;
import com.anacleto.parsing.filetype.FastHtmlIndexer;
import com.anacleto.parsing.filetype.FileTypeParser;

/**
 * Index a book based on a defined structure
 * 
 * @author robi
 */
public class StructuredBookIndexer extends SourceTypeHandler {
    /**
     * @throws IOException
     * @throws SAXException
     *  
     */
    private Document doc;
    
     
    private Logger         log  = Logging.getIndexingLogger();
    
    private FileTypeParser indexer;
    
    private Map      	   parents = new FastTreeMap();

    
    
    /* (non-Javadoc)
	 * @see com.anacleto.parsing.source.SourceTypeHandler#getContent(com.anacleto.hierarchy.BookPage)
	 */
	public ContentBean getContent(BookPage page) throws ContentReadException {
//		url of a Bookpage is composed of the location of the
        // book+its relative location
    	String bookURL;
		try {
			bookURL = page.getBook().getURL();
			
			String pageUrl = bookURL + "/" + page.getLocation();
	        LocalContentReader reader = new LocalContentReader();
	        String content = reader.readContent(pageUrl, page.getEncoding());
	        
	        content = replaceNXT3Links(content, page);
	        
	        //Querylinks should point to the queryLink servlet
	        content = content.replaceAll(
	        		"<!-- #EXECUTIVE:SCRIPT_NAME -->", 
					"nxt3QueryLink.do");
	        
	        ContentBean cb = new ContentBean(content, "nxt");
	        return cb;
	        
		} catch (IOException e) {
			throw new ContentReadException(e);
		}
        
	}

    //replace nxt3 image links:
    //nxt3 links can point to an indexed page, or point to
    //a file in the _images directory
    private String replaceNXT3Links(String content, BookPage currPage) throws IOException{
    	int from, to;
        IndexManager im = new IndexManager();
        
        StringBuffer retContent = new StringBuffer();
        while ((from = content.indexOf("#!-- #ID=")) >= 0){
        	to =  content.indexOf(" --#");
        	if (to < 0)
        		break;
        	
        	String pageId = content.substring(from+9, to);
        	
        	String link = null;
        	if (im.findNamedElement(pageId) != null){
        		//normal link:
        		link = "showDocument.do?name=" + pageId;
        	} else{
        		//image link:
        		link = "imageLink.do?name=" + pageId +
						"&amp;book="+currPage.getBook().getName();
        	}

        	retContent.append(content.substring(0,from));
        	retContent.append(link);
        	content = content.substring(to+4);
        }
        retContent.append(content);
    	return retContent.toString();
    }
   
	public void indexBook(Book book) throws IOException{ 
    	
    	IndexManager im = new IndexManager();
    	
    	parents = new HashMap();
    	try {
    		log.info("Deleting existing book entries...");
			im.deleteBook(book.getName());
			
			im.openBatchWriter();
			indexBook(im, book);
	        
		} catch (IOException e) {
			log.error(e);
		} catch (BookException e1) {
			log.error(e1);
		} finally {
			try {
				im.closeBatchWriter();
				im.updateSearcher();
			} catch (IOException e2) {
				log.error(e2);
			}
		}
        
        
    }
    
    private void indexBook(IndexManager im, Book book)  throws BookException {
    	
    	log.info("Parsing book descriptor...");
    	
    	File bookDir = new File(book.getURL());
        //check bookDir
        if (bookDir.exists() && bookDir.isDirectory()) {
            
        } else {
            throw new BookException("Book directory not present. Book: "
                    + book.getName() + "Directory: " + bookDir);
        }

        InputSource src;
		try {
			src = new InputSource(new FileInputStream(book.getNxt3Descriptor()));
		} catch (FileNotFoundException e) {
            throw new BookException("Book descriptor not present. Book: "
                    + book.getName() + "Bookdescriptor: "
                    + book.getNxt3Descriptor());
		}
		
		DOMParser prsr = new DOMParser();
        prsr.setEntityResolver(new LocalEntityResolver());
        
        try {
			prsr.parse(src);
		} catch (SAXException e1) {
			throw new BookException("Error during parsing bookdescriptior: "
                    + book.getNxt3Descriptor() + ". Root cause: "+ e1);
		} catch (IOException e2) {
			throw new BookException("Error during parsing bookdescriptior: "
                    + book.getNxt3Descriptor() + ". Root cause: "+ e2);
		}
        doc = prsr.getDocument();
        
        Node rootNode = doc.getFirstChild();
 
        try {
        	log.info("Indexing pages...");
			this.indexer = new FastHtmlIndexer(book.getNxt3IndexingStyleSheet());
		} catch (TransformerConfigurationException e) {
			throw new BookException("Error during processing stylesheet: "
                    + book.getNxt3IndexingStyleSheet() + ". Root cause: "+ e);
		}
        //this.indexer = new SAXHtmlIndexer(styleSheet, new IndexManager(), 
        //		duplicateForContent);
        indexNode(im, book, rootNode, book.getName());
        
    }

    private void indexNode(IndexManager im, Book book, Node currNode, 
    		String parentNodeName) {

        int childCount = 0;
        while (currNode != null) {
        	String lastDocName = parentNodeName;
        	if (currNode.getNodeName().equals("document")) {
        		lastDocName = processDocumentNode( im, book, 
        				parentNodeName, currNode, childCount);
        		childCount++;
            }
        	
//      	register child:
	        Node childNode = currNode.getFirstChild();
	        if (childNode != null)
	            indexNode(im, book, childNode, lastDocName);
            
            currNode = currNode.getNextSibling();
        }
    }
    
    private String processDocumentNode(IndexManager im, Book book, String parentNodeName, 
    		Node currNode, int childCount){

	//  Found document, add to documentlist
	    NamedNodeMap attribs = currNode.getAttributes();
	    if (attribs == null)
	    	return null;
	    
	    
	    BookPage page = new BookPage();
        page.setName(getNodeAttrubute(attribs, "id"));
        
        String hiddenStr = getNodeAttrubute(attribs, "hidden");
        if (hiddenStr != null && hiddenStr.equals("yes")) {
            return page.getName();
        }
        
        page.setParentName(parentNodeName);
        page.setBookName(book.getName());

        page.setTitle(getNodeAttrubute(attribs, "title"));
        page.addKeyWord("nxt3name", getNodeAttrubute(attribs, "name"));
        
        //get path:
        parents.put(page.getName(), page.getParentName());
        String path = "/" + page.getName();
        String currEl = page.getParentName();
        while (currEl != null){
        	path = "/" + currEl + path;
        	if (currEl.equals(book.getName()))
        		break;
        	
        	currEl = (String)parents.get(currEl);
        }
        
        page.setPath(path);
        
        String location = getNodeAttrubute(attribs, "location");
        if (location == null)
        	location = "";
        
        //convert absolute to relative:
        String pattern = "\\"+book.getName()+"\\";
        int patternStart = location.indexOf(pattern);
        if (patternStart<0)
        	page.setLocation(location);
        else
        	page.setLocation(location.substring(
        			patternStart+pattern.length()));
        
        
        if (getNodeAttrubute(attribs, "encoding") != null)
        		page.setEncoding(getNodeAttrubute(
        				attribs, "encoding"));
		
        page.setChildCount(childCount);
        page.setContentType(Constants.NXT3Content);
        
        if (getNodeAttrubute(attribs, "first-child-content") != null) {
            page.setFirstChildContent(true);
        }
        
        String fileType = getNodeAttrubute(attribs, "content-type");
    	if (fileType != null &&
    			fileType.equals("application/x-html-body-text"))
    		getContent(im, book, indexer, page);
	    
    	try {
        	im.addPageInBatch(page);
         } catch (IOException e) {
         	log.error("Error during adding document to index. Root cause: " +e);
        }
         
        return page.getName();
        
	    
	}
    private void getContent(IndexManager im, Book book, 
    		FileTypeParser indexer, BookPage page) {
        
        if (!page.isFirstChildContent()) {
        
            if (page.getLocation() == null  && page.getLocation().trim().equals("")) {
                log.error("Missing location element - Name: "+page.getName());
                return;
            }
 
            //from the tile we calculate the path:           
            try {
            	InputStream content = new FileInputStream(
            			new File(book.getURL()).getCanonicalPath() + "/" 
                        + page.getLocation());
            	
                //indexer.getDocumentStructure(page, content.toString() );
                indexer.processStream(content, page);

            } catch (Exception e) {
				log.error("Error occured during processing file: " + e);
			}
        }
    }

    private String getNodeAttrubute(NamedNodeMap attribs, String itemName) {
        Node itemNode = attribs.getNamedItem(itemName);
        if (itemNode != null) {
            return itemNode.getNodeValue();
        }
        return null;
    }

    /* (non-Javadoc)
	 * @see com.anacleto.parsing.source.SourceTypeHandler#getContent(com.anacleto.base.HierarchicalElement, java.io.InputStream, java.lang.String)
	 */
	public InputStream getContent(BookPage page, String mime) {
		// TODO Auto-generated method stub
		return null;
	}

}