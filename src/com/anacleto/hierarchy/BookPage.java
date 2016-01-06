/*
 * Created on 24-gen-2005
 *
 */
package com.anacleto.hierarchy;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.xml.sax.Attributes;

import com.anacleto.base.Configuration;
import com.anacleto.base.Constants;
import com.anacleto.base.Logging;
import com.anacleto.index.IndexEntityException;
import com.anacleto.index.IndexManager;
import com.anacleto.parsing.source.ContentBean;
import com.anacleto.parsing.source.ContentReadException;
import com.anacleto.parsing.source.SourceTypeHandler;
import com.anacleto.parsing.source.SourceTypeHandlerFactory;
import com.anacleto.util.HtmlLinkRewriter;

/**
 * BookPage
 * @author robert
 */
public class BookPage extends HierarchicalElement implements Serializable {

	private static final long serialVersionUID = 5370587316317146991L;

	private String     bookName;
    
    private Collection fields = new LinkedList();
            
    //path within a document
    private String     path;
    
    private String     pathFromDocRoot = "";

    private static  Logger log = Logging.getIndexingLogger();

    public BookPage() {
    }

    public BookPage(String name) {
        super(name);
    }
    
    public Book getBook() throws IOException {
        HierarchicalElement el = Configuration.getElement(getBookName());
        if (el != null && el instanceof Book)
            return (Book) el;
        else
        	return null;
    }


    /**
     * Sets the BookPage object from a Lucene document:
     * 
     * @param doc
     */
    public BookPage(Document doc) {

        if (doc == null)
            return;
        
        this.setName(doc.get("name"));
        this.setParentName(doc.get("parentName"));
        this.setTitle(doc.get("title"));
        this.setLocation(doc.get("location"));
        
        if (doc.get("encoding") != null)
        	this.setEncoding(doc.get("encoding"));
		
        this.setBookName(doc.get("book"));
        
        try {
            this.setChildCount(Integer.valueOf(doc.get("childCount"))
                    .intValue());
        } catch (Exception e) {

        }
        if (doc.get("firstChildContent") != null
                && doc.get("firstChildContent").equals("true")) {
            this.setFirstChildContent(true);
        }
        
        this.setContentType(doc.get("contentType"));
        
        this.setPath(doc.get("path"));
        
        //TODO: this is a bit slow when run within a query loop
        Enumeration e = doc.fields();
        while (e.hasMoreElements()) {
			Field element = (Field) e.nextElement();
			fields.add(element);
		}
       
    }

    /**
     * Sets the BookPage object from a Lucene document
     * with given field values
     * use this if you need a subset of all fields. this type of 
     * access is significantly faster than getting all fields
     * 
     * @param doc
     */
    public BookPage(Document doc, String[] queryFields) {
//TODO: revise this....

    	for (int i = 0; i < queryFields.length; i++) {
    		if (Constants.isReservedField(queryFields[i])){
    			if (queryFields[i].equals("name"))
    				this.setName(doc.get("name"));
    				
				if (queryFields[i].equals("parentName"))
        			this.setParentName(doc.get("parentName"));
    			
    			if (queryFields[i].equals("title"))
    				this.setTitle(doc.get("title"));
    				
				if (queryFields[i].equals("location"))
        			this.setLocation(doc.get("location"));
    	        
				if (queryFields[i].equals("encoding"))
					if (doc.get("encoding") != null)
						this.setEncoding(doc.get("encoding"));

				if (queryFields[i].equals("book"))
    			    this.setBookName(doc.get("book"));
    			
    		} else {
    			Field currField = doc.getField(queryFields[i]);
    			if (currField != null){
    				fields.add(currField);
    			}
    		}
		}
    }

    public boolean validate() throws IndexEntityException{
        checkFieldMandatory("name",       this.getName());
        checkFieldMandatory("parentName", this.getParentName());
        checkFieldMandatory("title", 	  this.getTitle());
        checkFieldMandatory("book",       this.getBookName());
        
        if (!isFirstChildContent())
        	checkFieldMandatory("location",   this.getLocation());
        
        checkFieldMandatory("contentType",this.getContentType());
        return true;
    }
    
    private void checkFieldMandatory(String name, String value) throws IndexEntityException{
        if (value == null || value.trim().equals(""))
            throw new IndexEntityException("Field is mandatory. Entity: " + this.getName() 
                      + ". Field name: " + name );
    }
    
    public void addField(String fieldName, String fieldValue, boolean isIndexed, boolean isStored,
    		boolean isTokenized, boolean isTermVectorStored){
    	
    	if (isIndexed){
    		//Keyword:
            if (isStored && !isTokenized) {
                addKeyWord(fieldName, fieldValue);
                return;
            }

            //Unstored Text:
            if (!isStored && isTokenized) {
                //Reader reader = new StringReader( fieldValue );
                addUnstoredField(fieldName, fieldValue, isTermVectorStored);
                return;
            }

            //Stored Text:
            if (isStored && isTokenized) {
                addTextField(fieldName, fieldValue, isTermVectorStored);
                return;
            }
            
    	} else {
    		//Unindexed:
            if (isStored && !isTokenized) {
                addUnindexedField(fieldName, fieldValue);
                return;
            }
    	}
    }
    
    public Field getField(String fieldName){
    	Iterator it = fields.iterator();
    	while (it.hasNext()) {
			Field currField = (Field) it.next();
			if (currField.name().equals(fieldName))
				return currField;
		}
    	return null;
    }
    
    public String getFieldValue(String fieldName){
    	Field f = getField(fieldName);
    	if (f == null)
    		return "";
    	else
    		return f.stringValue();
    }
    
    public void addKeyWord(String fieldName, String fieldValue){
        if (fieldName == null || fieldName.trim().equals("") 
                || fieldValue == null || fieldValue.trim().equals(""))
            return;
            
        Field field = Field.Keyword(fieldName, fieldValue);
        fields.add(field);
    }
    
    public void addKeyWord(String fieldName, Date fieldValue){
        if (fieldName == null || fieldName.trim().equals("") 
                || fieldValue == null )
            return;
            
        Field field = Field.Keyword(fieldName, fieldValue);
        fields.add(field);
    }
    
    public void addTextField(String fieldName, String fieldValue){
        if (fieldName == null || fieldName.trim().equals("") 
                || fieldValue == null || fieldValue.trim().equals(""))
            return;
            
        Field field = Field.Text(fieldName, fieldValue);
        fields.add(field);
    }
    
    public void addTextField(String fieldName, String fieldValue,
    		boolean storeTermVector){
        if (fieldName == null || fieldName.trim().equals("") 
                || fieldValue == null || fieldValue.trim().equals(""))
            return;
            
        Field field = Field.Text(fieldName, fieldValue, 
        		storeTermVector);
        fields.add(field);
    }
    
    /**
     * Add text field from a reader. This stores the original value 
     * @param fieldName
     * @param fieldValue
     * @throws IOException
     */
    public void addTextField(String fieldName, Reader fieldValue) throws IOException{

        if (fieldName == null || fieldName.trim().equals("") 
                || fieldValue == null)
            return;

    	char[] lastBuffer = new char[0];
    	char[] buffer = new char[8192];
    	int n = 0;
        while (true) {
            n = fieldValue.read(buffer);

            if (n < 1)
                break;
            
            char[] value = new char[lastBuffer.length + n];
            System.arraycopy(lastBuffer, 0, value, 0, lastBuffer.length);
            System.arraycopy(buffer, 0, value, lastBuffer.length, n);
            
            //try to throw away binary data: see human genome project
            StringBuffer val = new StringBuffer();
            char[] store = new char[value.length];
            int storePos = 0;
            for (int i = 0; i < value.length; i++){
            	store[storePos] = value[i];
            	storePos++;
            	
            	if (Character.isWhitespace(value[i])){
					//append to the final string
            		if (storePos < 50){
            			val.append(store, 0, storePos);
            		}
            		storePos = 0;
				}            	
            }   
            if (storePos < 50){
            	lastBuffer = new char[storePos];
            	System.arraycopy(store, 0, lastBuffer, 0, lastBuffer.length);
            } else {
            	lastBuffer = new char[0];
            }
            	
            
            Field field = Field.Text(fieldName, val.toString());
            fields.add(field);

        }
            
        if (lastBuffer.length > 0){
        	Field field = Field.Text(fieldName, new String(lastBuffer));
            fields.add(field);
        }
    }

    public void addUnindexedField(String fieldName, String fieldValue){
        if (fieldName == null || fieldName.trim().equals("") 
                || fieldValue == null || fieldValue.trim().equals(""))
            return;
            
        Field field = Field.UnIndexed(fieldName, fieldValue);
        fields.add(field);
    }
    
    public void addUnstoredField(String fieldName, String fieldValue){
        if (fieldName == null || fieldName.trim().equals("") 
                || fieldValue == null || fieldValue.trim().equals(""))
            return;
            
        Field field = Field.UnStored(fieldName, fieldValue);
        fields.add(field);
    }
    
    public void addUnstoredField(String fieldName, String fieldValue, 
    		boolean storeTermVector){
        if (fieldName == null || fieldName.trim().equals("") 
                || fieldValue == null || fieldValue.trim().equals(""))
            return;
            
        Field field = Field.UnStored(fieldName, fieldValue, storeTermVector);
        fields.add(field);
    }
    
    public Collection getFields(){
    	return fields;
    }
    
    /**
     * Add node from a nodeMap
     * The field under witch we have to add is coming from the field attribute
     * @param attributes
     * @param fieldValue
     */
    public void addFieldFromNode( Attributes attributes, 
			String fieldValue) {
		
    	String fieldName = attributes.getValue("field");
		if (fieldName == null)
			return;
		
        //Empty content is filtered out:
        if (fieldValue == null || fieldValue.trim().equals("")) {
            return;
        }

        boolean isIndexed = attributes.getValue("indexed").equals("true");
        boolean isStored = attributes.getValue("stored").equals("true");
        boolean isTokenized = attributes.getValue("tokenized").equals("true");
        boolean isTermVectorStored = attributes.getValue("termVectorStored")
				.equals("true");

        addField(fieldName, fieldValue, isIndexed, 
        		isStored, isTokenized, isTermVectorStored);
    }
    
    public Document getDocument() {
        Document retDoc = new Document();
        if (getName() != null)
        	retDoc.add(Field.Keyword("name",       getName()));
        if (getParentName() != null)
            retDoc.add(Field.Keyword("parentName", getParentName()));
        if (getBookName() != null)
            retDoc.add(Field.Keyword("book",       getBookName()));
        if (getPath() != null)
            retDoc.add(Field.Keyword("path",       getPath()));
        
        if (getTitle() != null)
        	retDoc.add(Field.Text("title", getTitle()));
        else if (getField("title") == null)
        	retDoc.add(Field.Text("title", "w/o title"));
        
        if(getField("childCount") == null) {
        	retDoc.add(Field.Keyword("childCount", String.valueOf(getChildCount())));
        }

        //location is not obbligatory, es.: when firstchild displayed
        if (getLocation() != null)
        	retDoc.add(Field.Keyword("location",   getLocation()));
        
        if (getContentType() != null)
        	retDoc.add(Field.Keyword("contentType",getContentType()));
        
        if (getEncoding() != null)
        	retDoc.add(Field.Keyword("encoding",   getEncoding()));
        
        if (isFirstChildContent())
        	retDoc.add(Field.Keyword("firstChildContent", "true"));
        
        Iterator it = fields.iterator();
        while (it.hasNext()) {
            Field currField = (Field) it.next();
            retDoc.add(currField);
        }
        return retDoc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.anacleto.parsing.HierarchicalElement#getChildElements(com.anacleto.parsing.Configuration)
     */
    public Collection getChildElements() {
        
        Collection retColl = null;
        try {
            IndexManager man = new IndexManager();
            retColl = man.findChildElements(getName());
        } catch (IOException e) {
            
        }
        return retColl;
    }

    public Collection getLogicalChildElements(String title) {
    	log.info("getLogicalChildElements: " + title);
        
        Collection retColl = null;
        try {
            IndexManager man = new IndexManager();
        	log.info("->IndexManager.findLogicalChildElements(" + getName() + ", " + title);
            retColl = man.findLogicalChildElements(getName(), title);
        	log.info("//IndexManager.findLogicalChildElements");
        } catch (ParseException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return retColl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.anacleto.parsing.HierarchicalElement#hasChildElements(com.anacleto.parsing.Configuration)
     */
    public boolean hasChildElements() {
        boolean retBool = false; 
        try {
            IndexManager man = new IndexManager();
            retBool = man.hasChildElements(getName());
        } catch (IOException e) {
        }
        return retBool;
    }

    public boolean hasLogicalChildElements(String title) {
        boolean retBool = false; 
        try {
            IndexManager man = new IndexManager();
            retBool = man.hasLogicalChildElements(getName(), title);
        } catch (IOException e) {
        }
        return retBool;
    }

    
    /* (non-Javadoc)
     * @see com.anacleto.base.HierarchicalElement#getParentElements(com.anacleto.parsing.Configuration)
     */
    public HierarchicalElement getParentElement() throws IOException {
        
        HierarchicalElement el = Configuration.getElement(getParentName());
        if (el!= null)
            return el;
        
        IndexManager man = new IndexManager();
        Document retPage = man.findNamedElement(getParentName());
        
        return new BookPage(retPage);
    }


    /**
     * @return Returns the pathFromDocRoot.
     */
    public String getPathFromDocRoot() {
        return pathFromDocRoot;
    }

    /**
     * @param pathFromDocRoot
     *            The pathFromDocRoot to set.
     */
    public void setPathFromDocRoot(String pathFromDocRoot) {
        this.pathFromDocRoot = pathFromDocRoot;
    }

    /**
     * @param pathFromDocRoot
     *            The pathFromDocRoot to set.
     */
    public void prependPathFromDocRoot(String prepend) {
        pathFromDocRoot = prepend + ">" + pathFromDocRoot;
    }

	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return Returns the bookName.
	 */
	public String getBookName() {
		return bookName;
	}
	/**
	 * @param bookName The bookName to set.
	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	
	/* (non-Javadoc)
	 * @see com.anacleto.base.HierarchicalElement#getContentToShow()
	 */
	public String getContentToShow() throws ContentReadException {
		
		SourceTypeHandler sh;
		try {
			sh = SourceTypeHandlerFactory.getHandler(this);
		} catch (IOException e) {
			throw new ContentReadException(e);
		}
		
		log.info("Location: " + this.getLocation());
		ContentBean cb = sh.getContent(this);
		if (cb == null)
			return null;
		
		if (cb.getMime().equals("html") || cb.getMime().equals("htm")){
			//change internal links:
			String content = cb.getStringContent();
			HtmlLinkRewriter rew = new HtmlLinkRewriter("htmlLink.do?location=",
					"showDocument.do?name=", "");
			return rew.rewrite(this, content);
			
		} else if (cb.getMime().equals("nxt")){
			return cb.getStringContent();

		} else if (cb.getMime().equals("txt")){
			String content = cb.getStringContent();
			content = content.replaceAll("\n", "<br>");
			return content;
		}
		
		return null;
	}
}