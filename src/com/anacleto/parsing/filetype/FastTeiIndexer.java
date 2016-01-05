/*
 * Created on Feb 11, 2005
 *
 */
package com.anacleto.parsing.filetype;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import com.anacleto.index.IndexException;

/**
 * @author robi
 * 
 */
public class FastTeiIndexer {

    private javax.xml.transform.Transformer transformer;

    public FastTeiIndexer(File xslFile)
            throws IndexException {
    	
        try {
        	TransformerFactory tFactory = TransformerFactory.newInstance();
			transformer = tFactory.newTransformer(new StreamSource(xslFile));
		} catch (TransformerConfigurationException e) {
            throw new IndexException("Error while processing xsl file: "
                    + xslFile + ". Root cause: " + e);
		}
    }

    public void transform(String fileSouce, String bookName)
            throws IndexException{
        StreamSource src = new StreamSource(fileSouce);
        
        SAXResult saxRes = new SAXResult();
        
		try {
			DocumentPerNodeHandler handler = new DocumentPerNodeHandler(
			        bookName );
			saxRes.setHandler(handler);
	        transformer.transform(src, saxRes);
		} catch (IOException e) {
			throw new IndexException(e);
		
		} catch (TransformerException e1) {
            throw new IndexException("Error while transforming file: "
                    + fileSouce + ". Root cause: " + e1);
        }
    }

}