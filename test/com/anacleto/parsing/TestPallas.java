package com.anacleto.parsing;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.cyberneko.html.parsers.DOMParser;
import org.xml.sax.InputSource;

import com.anacleto.base.Logging;
import com.anacleto.hierarchy.BookPage;

public class TestPallas {
	private javax.xml.transform.Transformer transformer;
	
	/**
     * 
     * @param xslFile
     * @param duplicateForContent Set this input field to true, if you want that
     * 			all fields are repeated in the content field (except the content field itself)
     * 			this is useful, when you want to have a field type "any field"
     * @throws TransformerConfigurationException
     */
    public TestPallas(File xslFile) throws TransformerConfigurationException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        transformer = tFactory.newTransformer(new StreamSource(xslFile));
    }
    
    public void processStream(InputStream in, OutputStream out, BookPage page) throws ParserException {

    	try {
    		DOMParser parser = new DOMParser();
        	if (page.getEncoding() != null)
        		parser.setProperty(
    				"http://cyberneko.org/html/properties/default-encoding", 
        			page.getEncoding());
    		
    		InputSource source = new InputSource(in);
            parser.parse(source);
            
            DOMSource src = new DOMSource(parser.getDocument());
            Result res = new StreamResult(out);
            transformer.transform(src, res);
			
    	} catch (Exception e) {
    		throw new ParserException(
					"Unable to execute transformation. Root cause: " + e);
    		
		}
	}

    public static void main(String[] args) {
		try {
			TestPallas pallas = new TestPallas(new File("/home/robi/work/arcanum/admin/html.xsl"));
			pallas.processStream(new FileInputStream("/home/robi/work/arcanum/czigany"),
					new FileOutputStream("/home/robi/work/arcanum/czigany.out"),
					new BookPage());
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	

