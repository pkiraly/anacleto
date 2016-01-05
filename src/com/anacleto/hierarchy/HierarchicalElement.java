package com.anacleto.hierarchy;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;

import com.anacleto.parsing.source.ContentReadException;

/**
 * Hierarchical element is a class to define common functionality to shelves,
 * books, bookpages, being in the same hierarchy
 * 
 * It resolves the travese in the tree
 * 
 * @author robi
 *  
 */
public abstract class HierarchicalElement implements Serializable {

    private String name;

    private String parentName;

    /**
     * this is the Nth child of its parent element
     */
    private int childCount;

    private String title;

    private String URL;
    
    private String location;
    
    private String encoding;
    
    private boolean firstChildContent;
    
    private String  contentType;

    private String  contentTypeHandler;

    private String  titlePage;

    private String  contentStyleSheet;

    public abstract boolean hasChildElements();

    public abstract boolean hasLogicalChildElements(String title);
    
    public abstract Collection getChildElements();

    public abstract Collection getLogicalChildElements(String title);

    public abstract String getContentToShow() throws ContentReadException;
    
    public abstract HierarchicalElement getParentElement()
    	throws IOException;
    
    public HierarchicalElement() {
    }

    /**
     *  
     */
    public HierarchicalElement(String name) {
        this.name = name;
    }

    /**
     * Next element in the hierarchy:
     * the first child
     * the next brother, or if it is missing, the parent's next brother, etc..
     * @param conf
     * @return
     * @throws IOException
     */
    public HierarchicalElement getNextElement() throws IOException{
        
        //If found, return with the first child:
        if (hasChildElements()){
            Collection collection = getChildElements();
            Iterator it = collection.iterator();
            return (HierarchicalElement)it.next();
        }
        
        HierarchicalElement retEl = null;
        HierarchicalElement currEl = this;
        while (retEl == null) {
            //get next brother,
            retEl = findNextBrother(currEl);
            /*
            //at root levet, we return the book
            if (currEl.getParentName().equals(retEl.getName()))
                return retEl;
            */
            //it doesn't exist, find it's parent:
            currEl = currEl.getParentElement();
            
            //at root level, there is no next element
            if (currEl.getName().equals("root"))
            	return this;
        }
        
        return retEl;
    }
    
    /**
     * Previous element in the hierarchy:
     * the first child
     * the previous brother, or if it is missing, the parent's next brother, etc..
     * @param conf
     * @return
     * @throws IOException
     */
    public HierarchicalElement getPrevElement() throws IOException{
        HierarchicalElement retEl = null;

        HierarchicalElement currEl = this;
        while (retEl == null) {
            //get previous brother,
            retEl = findPrevBrother(currEl);

            //it doesn't exist, find it's parent:
            if (retEl == null) {
                currEl = getParentElement();
                if (!currEl.isFirstChildContent())
                    retEl = currEl;
            } else {
                while (retEl.hasChildElements()){
                    //find last child:
                    Collection collection = retEl.getChildElements();
                    Iterator it = collection.iterator();
                    while (it.hasNext()) {
                        retEl = (HierarchicalElement) it.next();
                    }
                }
            }
        }
        
        //do not return with the root element
        if (retEl.name.equals("root"))
        	return this;
        
        return retEl;
    }

    /**
     * Finds the next element in the same level. If not exists, returns null
     * @param el
     * @param conf
     * @return
     * @throws IOException
     */
    private static HierarchicalElement findNextBrother(HierarchicalElement el) throws IOException{
		//find next brother:
		HierarchicalElement parentEl = el.getParentElement();
		Collection collection = parentEl.getChildElements();
		Iterator it = collection.iterator();
		while (it.hasNext()) {
		    HierarchicalElement currEl = (HierarchicalElement) it.next();
		    if (currEl.getName().equals(el.getName())){
		        if (it.hasNext())
		            return (HierarchicalElement) it.next();
		        else
		            return null;
		    }
		}
		return null;
    }

    /**
     * Finds the next element in the same level. If not exists, returns null
     * @param el
     * @param conf
     * @return
     * @throws IOException
     */
    private static HierarchicalElement findPrevBrother(HierarchicalElement el) throws IOException{
		//find next brother:
		HierarchicalElement parentEl = el.getParentElement();
		Collection collection = parentEl.getChildElements();
		
		HierarchicalElement prevEl = null;
		Iterator it = collection.iterator();
		while (it.hasNext()) {
		    HierarchicalElement currEl = (HierarchicalElement) it.next();
		    if (currEl.getName().equals(el.getName())){
		        return prevEl;
		    }
		    prevEl = currEl;
		}
		return null;
    }
    
    /**
     * Get /shelf_1/shelf2/book/page.... like expression
     * @return
     * @throws IOException 
     */
    public String getPathExpressionAllChildren() throws IOException{
    	StringBuffer retBuffer = new StringBuffer("/*");
    	
    	HierarchicalElement currEl = this;
    	while (currEl != null){
    		retBuffer.insert(0, "/" + currEl.getName());
    		currEl = currEl.getParentElement();
    	}
    	return retBuffer.toString();
    	
    }
    /**
     * @return Returns the childCount.
     */
    public int getChildCount() {
        return childCount;
    }

    /**
     * @param childCount
     *            The childCount to set.
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    /**
     * @return Returns the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     *            The location to set.
     */
    public void setLocation(String location) {
    	if (location == null)
    		location = "";
    	//only for convinience:
    	/*
    	char platformSeparator = File.separatorChar;
    	if (platformSeparator == '\\')
    		location = location.replace('/', '\\');
    	else
    		location = location.replace('\\', '/');
    	*/
        this.location = location;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the parentName.
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * @param parentName
     *            The parentName to set.
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the firstChildContent.
     */
    public boolean isFirstChildContent() {
        return firstChildContent;
    }
    /**
     * @param firstChildContent The firstChildContent to set.
     */
    public void setFirstChildContent(boolean firstChildContent) {
        this.firstChildContent = firstChildContent;
    }
    
    /**
     * @return Returns the uRL.
     */
    public String getURL() {
        return URL;
    }
    /**
     * @param url The uRL to set.
     */
    public void setURL(String url) {
        URL = url;
    }
    
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTypeHandler() {
        return contentTypeHandler;
    }

    public void setContentTypeHandler(String contentTypeHandler) {
        this.contentTypeHandler = contentTypeHandler;
    }

	/**
	 * @return Returns the encoding.
	 */
	public String getEncoding() {
		if (encoding == null) {
			return "utf-8";
		}
		return encoding;
	}
	/**
	 * @param encoding The encoding to set.
	 * @throws UnsupportedEncodingException
	 */
	public void setEncoding(String encoding) {
		
		if (encoding == null)
			encoding = "utf-8";
		
		//test if encoding is supported:
		try{
			new String("test".getBytes(), encoding);
			this.encoding = encoding;
		}catch (UnsupportedEncodingException e){
		}
	}
	
	/**
	 * @return Returns the titlePage.
	 */
	public String getTitlePage() {
		return titlePage;
	}
	/**
	 * @param titlePage The titlePage to set.
	 */
	public void setTitlePage(String titlePage) {
		this.titlePage = titlePage;
	}
	
	public String getContentStyleSheet() {
		return contentStyleSheet;
	}

	public void setContentStyleSheet(String contentStyleSheet) {
		this.contentStyleSheet = contentStyleSheet;
	}
}