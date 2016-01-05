package com.anacleto.parsing.filetype;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class pgHtmlParser implements FileTypeParser {
    
    private Logger log = Logging.getIndexingLogger();
    
    private static String       fieldName             = "";

    private static StringBuffer fieldValue            = new StringBuffer();

    private static StringBuffer content               = new StringBuffer();

    static String               pgHeaderStartPattern  = "The Project Gutenberg EBook of";

    static String               pgCatalogStartPattern = "*****These eBooks Were Prepared By Thousands of Volunteers!";

    static String               contentStartPattern   = "*** START OF THE PROJECT GUTENBERG EBOOK";

    static String               contentEndPattern     = "*** END OF THE PROJECT GUTENBERG EBOOK";

    static String               pgFooterEndPattern    = "*END THE SMALL PRINT! FOR PUBLIC DOMAIN EBOOKS";

    static Pattern              catalogPattern        = Pattern
                                                              .compile("^([^:]+): (.*?)$");

    private boolean isHeaderStart(String line) {
        return (line.indexOf(pgHeaderStartPattern) > -1) ? true : false;
    }

    private boolean isCatalogStart(String line) {
        return (line.indexOf(pgCatalogStartPattern) > -1) ? true : false;
    }

    private boolean isContentStart(String line) {
        return (line.indexOf(contentStartPattern) > -1) ? true : false;
    }

    private boolean isContentEnd(String line) {
        return (line.indexOf(contentEndPattern) > -1) ? true : false;
    }

    private boolean isFooterEnd(String line) {
        return (line.indexOf(pgFooterEndPattern) > -1) ? true : false;
    }

    public void addField(BookPage page) {
        if (!fieldName.equals("")) {
            page.addTextField(fieldName, fieldValue.toString());
            // System.out.println("addField: " + fieldName + ", " + fieldValue.toString());
        }
        fieldName = "";
        fieldValue.setLength(0);
    }

    public void cleanUpContent(StringBuffer text, BookPage page) {
        String line = new String();
        String filePart = "unused";
        String[] lines = text.toString().split("\\n");
        text.setLength(0);

        for (int i=0; i<lines.length; i++) {
            line = lines[i];
            if (filePart.equals("content") && isHeaderStart(line)) {
                filePart = "unused";
            } else if (filePart.equals("content") && isContentEnd(line)) {
                filePart = "unused";
            } else if (filePart.equals("unused") && isFooterEnd(line)) {
                filePart = "content";
                continue;
            } else if (filePart.equals("unused") && isCatalogStart(line)) {
                filePart = "catalog";
            } else if (filePart.equals("catalog") && isContentStart(line)) {
                addField(page);
                filePart = "content";
                continue;
            }

            if (filePart.equals("catalog")) {
                Matcher catalogMatcher = catalogPattern.matcher(line);
                if (catalogMatcher.find()) {
                    addField(page);
                    fieldName = catalogMatcher.group(1);
                    fieldValue.append(catalogMatcher.group(2));
                } else {
                    fieldValue.append(" " + line);
                }
            } else if (filePart.equals("content")) {
                content.append(line + "\n");
            }
        }
    }


    /**
     * 
     * @param xslFile
     * @param duplicateForContent Set this input field to true, if you want that
     * 			all fields are repeated in the content field (except the content field itself)
     * 			this is useful, when you want to have a field type "any field"
     * @throws TransformerConfigurationException
     */
    public pgHtmlParser() {
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
