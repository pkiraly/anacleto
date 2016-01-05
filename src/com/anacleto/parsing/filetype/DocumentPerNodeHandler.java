/*
 * Created on Feb 11, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.IOException;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexManager;

/**
 * @author robi
 */
public class DocumentPerNodeHandler implements ContentHandler {

    private String       bookName;

    private ArrayStack   docBuffer;
    
    private IndexManager indMan;
    
    private int			 counter = 0;
    
    private Logger   logger = Logging.getIndexingLogger();
    
    /**
     * @throws IOException
     *  
     */
    public DocumentPerNodeHandler(String bookName) throws IOException {
        this.bookName = bookName;
        this.indMan = new IndexManager();
        
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] text, int start, int length)
            throws SAXException {

        if (docBuffer.size() > 0) {
            DocumentContent docCont = (DocumentContent) docBuffer.pop();
            if (docCont.doc != null && text != null)
                docCont.buffer.append(text, start, length);

            docBuffer.push(docCont);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
            String rawName, Attributes atts) throws SAXException {

        DocumentContent docCont = new DocumentContent();
        try {
            if (atts != null) {

            	docCont.page = new BookPage();
            	docCont.page.setName(bookName + counter);

            	//Get parent name ad childcount from the docBuffer:
            	if (docBuffer.size() > 0) {
                    DocumentContent parentCont = (DocumentContent) docBuffer.get();
                    docCont.page.setParentName(parentCont.page.getName());
                    docCont.page.setChildCount(parentCont.childs);
                    
                    parentCont.childs++;
                } else {
                	docCont.page.setParentName(bookName);
                }
            	
            	docCont.page.setBookName(bookName);
            	docCont.page.setContentType(Constants.TEI2XMLContent);
            	
            	//title and location comes from the transformation:
            	docCont.page.setTitle(atts.getValue("title"));
            	docCont.page.setLocation(atts.getValue("location"));
            	docCont.page.setPath(atts.getValue("location"));
            	counter++;
            		
            	for (int i = 0; i < atts.getLength(); i++) {
            		//do not index reserved fields:
            		if (!Constants.isReservedField(atts.getQName(i)))
            			docCont.page.addTextField(atts.getQName(i), 
            									  atts.getValue(i)); 
				}
            }
        } catch (Exception e) {
            logger.error(e);
        }

        docBuffer.push(docCont);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String arg0, String arg1, String arg2)
            throws SAXException {
        try {
            DocumentContent docCont = (DocumentContent) docBuffer.pop();
            if (docCont.doc != null ) {
                if (docCont.buffer != null)
                    docCont.page.addTextField("content", docCont.buffer
                            .toString());
                
                indMan.addPage(docCont.page);
                logger.info("Page added to index.");
            }
        } catch (IOException e) {
        	logger.error("Error during adding page to index. Root cause: " + e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String arg0) throws SAXException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
            throws SAXException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
     *      java.lang.String)
     */
    public void processingInstruction(String arg0, String arg1)
            throws SAXException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator arg0) {
    }

    public void skippedEntity(String arg0) throws SAXException {
    }

    public void startPrefixMapping(String arg0, String arg1)
            throws SAXException {
    }

    public void startDocument() throws SAXException {
        docBuffer = new ArrayStack(10);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
    }
}

final class DocumentContent {

    public Document     doc;

    public BookPage     page;
    
    public StringBuffer buffer;
    
    public int          childs;

    /**
     *  
     */
    public DocumentContent() {
        doc = new Document();
        buffer = new StringBuffer();
    }
}