package com.anacleto.parsing.filetype;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.anacleto.base.Logging;
import com.anacleto.hierarchy.BookPage;
import com.anacleto.index.IndexException;
import com.anacleto.index.Indexer;

/**
 * Index a Html file according to the schema
 */
public class HtmlIndexer implements Indexer {

    private javax.xml.transform.Transformer transformer;
    
    private Logger log = Logging.getIndexingLogger();
    
    private NamedNodeMap old_attributes;
    private String old_fieldName;
    private String old_fieldValue;


    /**
     * 
     * @param xslFile
     * @param duplicateForContent Set this input field to true, if you want that
     * 			all fields are repeated in the content field (except the content field itself)
     * 			this is useful, when you want to have a field type "any field"
     * @throws TransformerConfigurationException
     */
    public HtmlIndexer(File xslFile) throws TransformerConfigurationException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        transformer = tFactory.newTransformer(new StreamSource(xslFile));
    }

    public void getDocumentStructure(BookPage page, String fileName)
            throws IndexException {
        log.info("Processing file (DOM): " + fileName );
        
        Node htmlNode = getDOMfromHTML(page, fileName);
        indexNodes( page, htmlNode );
    }
    
    private Node getDOMfromHTML(BookPage page, String fileName) throws IndexException {
    	
    	DOMParser parser = new DOMParser();
    	if (page.getEncoding() != null){
    		try {
				parser.setProperty(
					"http://cyberneko.org/html/properties/default-encoding", 
					page.getEncoding());
			} catch (SAXNotRecognizedException e) {
			} catch (SAXNotSupportedException e) {
			}
    	}
    	
    	try {
    		parser.setProperty("http://cyberneko.org/html/properties/default-encoding", 
        			page.getEncoding());
            parser.parse("file://localhost/" + fileName);
        } catch (SAXException e) {
            throw new IndexException("Error while parsing file: " + fileName);
        } catch (IOException e) {
            throw new IndexException("Error while reading file: " + fileName);
        }
        
        Document domSrc = parser.getDocument();
        
        DOMSource src = new DOMSource(domSrc);
        DOMResult domRes = new DOMResult();
                
        try {
			transformer.transform(src, domRes);
		} catch (TransformerException e) {
			throw new IndexException("Error while transforming file: " + fileName);
		}
        return domRes.getNode();
    }
    
    private void indexNodes(BookPage page, Node node )
    {
      node.normalize();  
      NamedNodeMap parentAttrMap = node.getAttributes();
      Node child = node.getFirstChild();
      
      if( parentAttrMap != null && parentAttrMap.getNamedItem("field") != null )
      {
        if( ! ( node.getChildNodes().getLength() == 1 
            && child.getNodeType() == 3 ) )
        {
          addNodeToDocument( page, 
                             parentAttrMap, 
                             parentAttrMap.getNamedItem("field").getNodeValue(), 
                             getDeepContent( node ) );
        }
      }
      
      while (child != null) {
        //here we have to see the parent's attributes, how to index a
        // field:
        if (   parentAttrMap != null
            && child.getNodeType() == org.w3c.dom.Document.TEXT_NODE )
        {
          Node indexField = parentAttrMap.getNamedItem("field");
          if (indexField != null) {
            String nodeVal = child.getNodeValue();
            addNodeToDocument(page, parentAttrMap, indexField
                .getNodeValue(), nodeVal);
          }
        }
        
        indexNodes( page, child );
        child = child.getNextSibling();
      }
    }
    
    private String getDeepContent( Node node )
    {
      StringBuffer text = new StringBuffer();
      if( node.hasChildNodes() )
      {
        Node child = node.getFirstChild();
        while( child != null )
        {
          text.append( " " + getDeepContent( child ) + " " );
          child = child.getNextSibling();
        }
      } else {
        text.append( " " + node.getNodeValue() + " " );
      }
      
      String result = text.toString();
      result = result.replaceAll( " +", " " );
      result = result.replaceAll( "(^ +| +$)", "" );
      return result;
    }
    
    private void addNodeToDocument(BookPage page,
            NamedNodeMap attributes, String fieldName, String fieldValue)
    {
      
      // repeated index entries are filtered out:
      if( old_attributes != null
          && attributes.getNamedItem("indexed").getNodeValue().equals( old_attributes.getNamedItem("indexed").getNodeValue() )
          && attributes.getNamedItem("stored").getNodeValue().equals( old_attributes.getNamedItem("stored").getNodeValue() )
          && attributes.getNamedItem("tokenized").getNodeValue().equals( old_attributes.getNamedItem("tokenized").getNodeValue() )
          && attributes.getNamedItem("termVectorStored").getNodeValue().equals( old_attributes.getNamedItem("termVectorStored").getNodeValue() )
          && fieldName.equals( old_fieldName )
          && fieldValue.equals( old_fieldValue )
      )
      {
        return;
      }
      
      //Empty content is filtered out:
      if (fieldValue == null || fieldValue.trim().equals("")) {
        return;
      }
      
      //		Empty content is filtered out:
      if (fieldName == null) {
        log.error("Error, field name not found");
        return;
      }
      
      Node indexed = attributes.getNamedItem("indexed");
      Node stored = attributes.getNamedItem("stored");
      Node tokenized = attributes.getNamedItem("tokenized");
      Node termVectorStored = attributes.getNamedItem("termVectorStored");
      
      // check values, we accept all fields
      if (indexed == null || stored == null || tokenized == null
          || termVectorStored == null) {
        //log this event
        log.warn("indexing is not fully specified");
        return;
      }
      
      boolean isIndexed = indexed.getNodeValue().equals("true");
      boolean isStored = stored.getNodeValue().equals("true");
      boolean isTokenized = tokenized.getNodeValue().equals("true");
      boolean isTermVectorStored = termVectorStored.getNodeValue().equals(
      "true");

      page.addField(fieldName, fieldValue, isIndexed, isStored, isTokenized, isTermVectorStored);
      old_attributes = attributes;
      old_fieldName  = fieldName;
      old_fieldValue = fieldValue;
    }
    
}
