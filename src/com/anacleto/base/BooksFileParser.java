package com.anacleto.base;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.HierarchicalElement;
import com.anacleto.hierarchy.Shelf;
import com.anacleto.parsing.SAXHandlerNode;
import com.anacleto.parsing.SAXHandlerWithStack;

public class BooksFileParser {
	
	private javax.xml.transform.Transformer transformer;
	
	public Map parse(File configFile) throws TransformerConfigurationException {

		TransformerFactory tFactory = TransformerFactory.newInstance();
		transformer = tFactory.newTransformer();
		
		StreamSource src = new StreamSource(configFile);
		SAXResult saxRes = new SAXResult();
		try {
			NodeHandler handler = new NodeHandler();
			saxRes.setHandler(handler);
			transformer.transform(src, saxRes);

			return handler.getShelfColl();
		} catch (TransformerException e1) {
			return null;
		}
	}
}

final class NodeHandler extends SAXHandlerWithStack {
	
	private static Logger logger = Logging.getAdminLogger();
	
	private Map shelfColl = new TreeMap();
	
	
	
	/**
	 * @return Returns the shelfColl.
	 */
	public Map getShelfColl() {
		return shelfColl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anacleto.parsing.SAXHandlerWithStack#endElement(org.xml.sax.Attributes,
	 *      java.lang.String)
	 */
	public void startElement(SAXHandlerNode node) {
		HierarchicalElement newEl = null;
		try {
			String name    = node.getLocalName();
			Map    atts = node.getAttributes();  
			
			if (name.equals("shelf")){
				newEl = new Shelf();
			} else if (name.equals("book")){
				newEl = new Book();
			} else return;
					
			newEl.setName((String)atts.get("name"));
	        
	        newEl.setTitle((String)atts.get("title"));
	        newEl.setTitlePage(Configuration.params.getConfigDir() + "/" +
	        		(String)atts.get("titlepage"));
	        newEl.setEncoding((String)atts.get("encoding"));
	        newEl.setURL((String)atts.get("location"));
	        newEl.setLocation((String)atts.get("location"));
	        newEl.setContentType((String)atts.get("contentType"));
	        newEl.setContentStyleSheet((String)atts.get("contentStyleSheet"));
	        
	        if (name.equals("book")){
				addBookAttributes((Book)newEl, atts);
			}
	        
	        shelfColl.put(newEl.getName(), newEl);
	        
	        //Parent attributes
        	//in case of root it throws nullpointerexception
        	Map parent = getParentNode().getAttributes();
        	String parentName = (String)parent.get("name");
        	newEl.setParentName(parentName);
        	
        	
        	//in case of root it throws nullpointerexception
        	Shelf parentEl = (Shelf)shelfColl.get(parentName);   
        	
        	newEl.setChildCount(parentEl.childNumber());
        	
        	parentEl.addChild(newEl);
        	
        	if (newEl instanceof Shelf) {
				((Shelf)newEl).setParent(parentEl);
			} else if (newEl instanceof Book) {
				((Book)newEl).setParent(parentEl);
			}
        	
		} catch (NullPointerException e) {
			logger.debug("no parent: " + newEl.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void endElement(SAXHandlerNode node) {
	}
	
	private void addBookAttributes(Book newBook, Map atts){
			
		newBook.setUser((String)atts.get("user"));
		newBook.setPassword((String)atts.get("password"));
		
		if (newBook.getContentType().equals(Constants.NXT3Content)){
	    	String nxt3Descr = (String)atts.get("nxt3Descriptor");
	    	newBook.setNxt3Descriptor(
	    			new File(newBook.getURL()+"/"+nxt3Descr));
	    	
	    	String styleSheet = Configuration.params.getConfigDir() + "/" +
	    		(String)atts.get("nxt3IndexingStyleSheet");
	    	
	    	newBook.setNxt3IndexingStyleSheet(new File(styleSheet));
	    }
	    
	    //Tei indexing sheet:
		String tei2IndStyle = (String)atts.get("tei2IndexingStyleSheet");
	    if (tei2IndStyle == null || tei2IndStyle.trim().equals(""))
	    	tei2IndStyle = Configuration.params.getDefaultTei2IndexingStyleSheet();
	    
	    //TODO: check this!!!!!!!
	    if (tei2IndStyle != null)
	    	newBook.setTei2IndexingStyleSheet(
	    			new File(
	    					Configuration.params.getConfigDir()
	    					+ '/'
	    					+ tei2IndStyle));
	    
	    //scheduling expression:
	    String scheduled = (String)atts.get("scheduled");
	    if (scheduled != null){
		    newBook.setScheduled((scheduled).equals("true") ? true : false);
		    newBook.setSchedulingCronExpression(
		    		(String)atts.get("schedulingCronExpression"));
	    }
	    
	    //Handler:
	    String handler = (String)atts.get("contentTypeHandler");
	    if (handler != null)
		    newBook.setContentTypeHandler(handler);

	    //arg1:
	    String arg1 = (String)atts.get("arg1");
	    if (arg1 != null)
		    newBook.setArg1(arg1);

	    //arg2:
	    String arg2 = (String)atts.get("arg2");
	    if (arg2 != null)
		    newBook.setArg2(arg2);

	    //arg3:
	    String arg3 = (String)atts.get("arg3");
	    if (arg3 != null)
		    newBook.setArg3(arg3);

	    //arg4:
	    String arg4 = (String)atts.get("arg4");
	    if (arg4 != null)
		    newBook.setArg4(arg4);

	    //arg5:
	    String arg5 = (String)atts.get("arg5");
	    if (arg5 != null)
		    newBook.setArg5(arg5);

	}

}
