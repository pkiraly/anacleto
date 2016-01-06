package com.anacleto.hierarchy;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.anacleto.base.Logging;
import com.anacleto.content.LocalContentReader;
import com.anacleto.parsing.source.ContentReadException;


/**
 * Shelf is an element, that
 * 
 * @author robi
 */
public class Shelf extends HierarchicalElement {
	
	private Collection childColl = new LinkedList();
	
	private Shelf parent;
	
    private static Logger logger = Logging.getIndexingLogger();
    
    /**
     * @param name
     */
    public Shelf() {
        super();
    }

    /**
     * @param name
     */
    public Shelf(String name) {
        super(name);
    }

    public void addChild(HierarchicalElement child){
    	childColl.add(child);
    }
    
    public void setParent(Shelf parent){
    	this.parent = parent;
    }
    /*
     * (non-Javadoc)
     * 
     * @see com.anacleto.parsing.HierarchicalElement#getChildElements()
     */
    public Collection getChildElements() {
    	return childColl;
    }

    public Collection getLogicalChildElements(String title) {
    	logger.info("getLogicalChildElements: " + title);
    	return childColl;
    }

    /**
     * 
     * @see com.anacleto.hierarchy.HierarchicalElement#hasChildElements()
     */
    public boolean hasChildElements() {
    	return ((childColl.size() == 0) ? false : true);
    }

    public boolean hasLogicalChildElements(String title) {
    	return hasChildElements();
    }

    /**
     * Returns the number of childs present
     */
    public int childNumber() {
    	return childColl.size();

    }
    
    /* (non-Javadoc)
     * @see com.anacleto.base.HierarchicalElement#getParentElement(com.anacleto.parsing.Configuration)
     */
    public HierarchicalElement getParentElement() {
    	if (parent != null)
        	return parent;
        else{ 
        	return null;
        }
    }
    
    /**
     * 
     * @param shelf
     * @return all books that are under the hierarchy
     */
    public Collection getChildBooks(){
    	Collection retColl = new LinkedList();
    	
    	Collection childs = getChildElements();
    	Iterator it = childs.iterator();
    	while (it.hasNext()) {
			HierarchicalElement element = (HierarchicalElement) it.next();
			if (element instanceof Book)
				retColl.add((Book)element);
			else if (element instanceof Shelf){
				Shelf sh = (Shelf)element;
				Collection childBooks = sh.getChildBooks();
				Iterator it2 = childBooks.iterator();
				while (it2.hasNext()) {
					Book elb = (Book) it2.next();
					retColl.add(elb);
				}
			}
		}
    	return retColl;
    }

	/**
	 * Get content from the titlepage attribute
	 * 
	 * @see com.anacleto.base.HierarchicalElement#getContent()
	 */
	public String getContentToShow() throws ContentReadException{
		LocalContentReader reader = new LocalContentReader();
		try {
			return reader.readContent(getTitlePage(), getEncoding());
		} catch (IOException e) {
			throw new ContentReadException(e);
		}
	}
}