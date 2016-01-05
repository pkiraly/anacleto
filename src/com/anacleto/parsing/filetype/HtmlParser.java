package com.anacleto.parsing.filetype;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.TransformerConfigurationException;

import org.apache.log4j.Logger;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.anacleto.base.Logging;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.parsing.ParserException;

/**
 * Index a Html file according to the schema
 */
public class HtmlParser implements FileTypeParser {
    
    private Logger log = Logging.getIndexingLogger();
    


    /**
     * 
     * @param xslFile
     * @param duplicateForContent Set this input field to true, if you want that
     * 			all fields are repeated in the content field (except the content field itself)
     * 			this is useful, when you want to have a field type "any field"
     * @throws TransformerConfigurationException
     */
    public HtmlParser() {
    }

    
    /* (non-Javadoc)
	 * @see com.anacleto.parsing.filetype.FileTypeParser#processStream(java.io.InputStream, com.anacleto.base.BookPage)
	 */
	public void processStream(InputStream in, BookPage page) throws ParserException {
		// TODO Auto-generated method stub
		
        DOMParser parser = new DOMParser();
    	
    	try {
    		//parser.setProperty("http://cyberneko.org/html/properties/default-encoding", 
        	//		page.getEncoding());
            parser.parse(new InputSource(in));
        } catch (SAXException e) {
            throw new ParserException("Sax exception occured. Root cause: " + e);
        } catch (IOException e) {
            throw new ParserException("Sax exception occured. Root cause: " + e);
        }
        
        Document domSrc = parser.getDocument();
        indexNodes(page, domSrc);
    }
    
     
    private void indexNodes(BookPage page, Node node )
    {
      node.normalize();  
      Node child = node.getFirstChild();
      
      /*
      if( ! ( node.getChildNodes().getLength() == 1 
            && child.getNodeType() == org.w3c.dom.Document.TEXT_NODE ) ){
          addNodeToDocument( page,"content", node.getNodeValue() );
      }
      */
      
      while (child != null) {
        //here we have to see the parent's attributes, how to index a
        // field:
        if (   child.getNodeType() == org.w3c.dom.Document.TEXT_NODE ){
            String nodeVal = child.getNodeValue();
            addNodeToDocument(page, "content", nodeVal);
        }
        
        indexNodes( page, child );
        child = child.getNextSibling();
      }
    }
    
    
    
    private void addNodeToDocument(BookPage page, String fieldName, String fieldValue){
       
      //Empty content is filtered out:
      if (fieldValue == null || fieldValue.trim().equals("")) {
        return;
      }
      
      //Empty content is filtered out:
      if (fieldName == null) {
        log.error("Error, field name not found");
        return;
      }
      
      page.addTextField(fieldName, fieldValue);
     }
    
}
