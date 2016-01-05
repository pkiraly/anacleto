package com.anacleto.content;

import java.io.IOException;
import java.io.Writer;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;


/**
 * A simple example to show how SAXON can be used with a JDOM tree. It is
 * designed to be used with the source document books.xml and the stylesheet
 * total.xsl
 * 
 * @author Michael H. Kay
 */
public class JDOMTest {

    public JDOMTest() {
    }

    /**
     * Show (a) use of freestanding XPath expressions against the JDOM document,
     * and (b) the simplest possible transformation from system id to output
     * stream.
     */
/*
    public Writer transform(String sourceID, String xslID, String xpathStr)
            throws TransformerException, TransformerConfigurationException,
            JDOMException, IOException {

  
        //System.out.println(System.getProperty("org.xml.sax.driver"));
        // Build the JDOM document
        SAXBuilder builder = new SAXBuilder(false);
        builder.setEntityResolver(new MyResolver());
 
        Document doc = builder.build(new File(sourceID));

        // Give it a Saxon wrapper
        DocumentWrapper docw = new DocumentWrapper(doc, sourceID,
                new Configuration());

        // Retrieve all the ITEM elements
        XPathEvaluator xpath = new XPathEvaluator(docw);
        Iterator iter = xpath.evaluate(xpathStr).iterator();

        TransformerFactory tfactory = TransformerFactory.newInstance();

        
        Templates templates = tfactory.newTemplates(new StreamSource(xslID));
        Transformer transformer = templates.newTransformer();

        OutputStream out = new ByteArrayOutputStream(doc.getContentSize());
        Writer writer = new StringWriter();
        while (iter.hasNext()) {
            Element item = (Element) iter.next();

            transformer.transform(new JDOMSource(item),
                    new StreamResult(writer));
        }
        
        saxTest(sourceID, xslID, xpathStr);
        
        return writer;
        
        
        
    }


    public void saxTest(String sourceID, String xslID, String xpathStr)
    	throws TransformerException, TransformerConfigurationException,
    		JDOMException, IOException {

        TransformerFactory tfactory = TransformerFactory.newInstance();

        Templates templates = tfactory.newTemplates(new StreamSource(xslID));
        Transformer transformer = templates.newTransformer();

        StreamSource src = new StreamSource(sourceID);
        
        SAXResult saxRes = new SAXResult();
        FilteredContentHandler handler = new FilteredContentHandler();
        saxRes.setHandler(handler);
        
        try {
            transformer.transform(src, saxRes);
        } catch (TransformerException e1) {
        }
    }
*/
}

