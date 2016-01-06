package com.anacleto.hierarchy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.w3c.dom.Document;

import com.anacleto.base.Configuration;
import com.anacleto.content.LocalContentReader;
import com.anacleto.parsing.source.ContentReadException;

/**
 * A book is a collection of HTML(or other pages). It resides in the
 * bookDirectory, has an index.xml file that describes the book's pages
 * 
 * @author robi
 */
public class Book extends BookPage {

	static final private long serialVersionUID = 635373635;

	private Document doc;

    private Shelf    parent;

    /**
     * collectionType: comes from the constants class
     */
    private String   collectionType;
    
    private String   contentTypeHandler;
    
    private String	 arg1;
    private String	 arg2;
    private String	 arg3;
    private String	 arg4;
    private String	 arg5;
    private String	 arg6;
    private String	 arg7;
    private String	 arg8;
    private String	 arg9;
    private String	 arg10;
    
    private File	 tei2IndexingStyleSheet;
    
    /**
     * NXT3 support: a file that describes the collection.
     */
    private File	 nxt3Descriptor;
    private File	 nxt3IndexingStyleSheet;

    /**
     * User and password for every resource that requires authentication 
     */
    private String   user;
    private String   password;

    
    private int      pageNum;		

    /**
     * Scheduling support:
     */
    private boolean scheduled;
    private String  schedulingCronExpression;
    

    /**
     * Directory of the book: it has to bee a directory shared by the webserver
     */
    private File bookDir;

    private HashMap pageMap = new LinkedHashMap();


    /**
     *  
     */
    public Book() {
        super();
    }

    /**
     * @param name
     */
    public Book(String name) {
        super(name);
    }


    /* (non-Javadoc)
     * @see com.anacleto.base.HierarchicalElement#getParentElements(com.anacleto.parsing.Configuration)
     */
    public HierarchicalElement getParentElement() throws IOException {
        if (parent != null)
        	return parent;
        else {
	        HierarchicalElement el = Configuration.getElement(getParentName());
	        return el;
        }
    }
    
    
    /**
	 * @param parent The parent to set.
	 */
	public void setParent(Shelf parent) {
		this.parent = parent;
	}

	/**
     * @return Returns the bookDir.
     */
    public File getBookDir() {
        return bookDir;
    }

    /**
     * @param bookDir
     *            The bookDir to set.
     */
    public void setBookDir(File bookDir) {
        this.bookDir = bookDir;
    }


    /**
     * @return Returns the doc.
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * @param doc
     *            The doc to set.
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }

    /**
     * @return Returns the pageMap.
     */
    public HashMap getPageMap() {
        return pageMap;
    }

    /**
     * @param pageMap
     *            The pageMap to set.
     */
    public void setPageMap(HashMap pageMap) {
        this.pageMap = pageMap;
    }
    
	/**
	 * @return Returns the collectionType.
	 */
	public String getCollectionType() {
		return collectionType;
	}

	/**
	 * @param collectionType The collectionType to set.
	 */
	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}
	
    /**
     * @return Returns the pageNum.
     */
    public int getPageNum() {
        return pageNum;
    }
    /**
     * @param pageNum The pageNum to set.
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    
    
	/**
	 * @return Returns the nxt3Descriptor.
	 */
	public File getNxt3Descriptor() {
		return nxt3Descriptor;
	}

	/**
	 * @param nxt3Descriptor The nxt3Descriptor to set.
	 */
	public void setNxt3Descriptor(File nxt3Descriptor) {
		this.nxt3Descriptor = nxt3Descriptor;
	}

	/**
	 * @return Returns the nxt3IndexingStyleSheet.
	 */
	public File getNxt3IndexingStyleSheet() {
		return nxt3IndexingStyleSheet;
	}

	/**
	 * @param nxt3IndexingStyleSheet The nxt3IndexingStyleSheet to set.
	 */
	public void setNxt3IndexingStyleSheet(File nxt3IndexingStyleSheet) {
		this.nxt3IndexingStyleSheet = nxt3IndexingStyleSheet;
	}


	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	/**
	 * @return Returns the tei2IndexingStyleSheet.
	 */
	public File getTei2IndexingStyleSheet() {
		return tei2IndexingStyleSheet;
	}

	/**
	 * @param tei2IndexingStyleSheet The tei2IndexingStyleSheet to set.
	 */
	public void setTei2IndexingStyleSheet(File tei2IndexingStyleSheet) {
		this.tei2IndexingStyleSheet = tei2IndexingStyleSheet;
	}
	
	/**
	 * @return Returns the scheduled.
	 */
	public boolean isScheduled() {
		return scheduled;
	}
	/**
	 * @param scheduled The scheduled to set.
	 */
	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	/**
	 * @return Returns the schedulingCronExpression.
	 */
	public String getSchedulingCronExpression() {
		return schedulingCronExpression;
	}

	/**
	 * @param schedulingCronExpression The schedulingCronExpression to set.
	 */
	public void setSchedulingCronExpression(String schedulingCronExpression) {
		this.schedulingCronExpression = schedulingCronExpression;
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

	/**
	 * @return the contentTypeHandler
	 */
	public String getContentTypeHandler() {
		return contentTypeHandler;
	}

	/**
	 * @param contentTypeHandler the contentTypeHandler to set
	 */
	public void setContentTypeHandler(String contentTypeHandler) {
		this.contentTypeHandler = contentTypeHandler;
	}

	/**
	 * @return the arg1
	 */
	public String getArg1() {
		return arg1;
	}

	/**
	 * @param arg1 the arg1 to set
	 */
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}

	/**
	 * @return the arg2
	 */
	public String getArg2() {
		return arg2;
	}

	/**
	 * @param arg2 the arg2 to set
	 */
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}

	/**
	 * @return the arg3
	 */
	public String getArg3() {
		return arg3;
	}

	/**
	 * @param arg3 the arg3 to set
	 */
	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}

	/**
	 * @return the arg4
	 */
	public String getArg4() {
		return arg4;
	}

	/**
	 * @param arg4 the arg4 to set
	 */
	public void setArg4(String arg4) {
		this.arg4 = arg4;
	}

	/**
	 * @return the arg5
	 */
	public String getArg5() {
		return arg5;
	}

	/**
	 * @param arg5 the arg5 to set
	 */
	public void setArg5(String arg5) {
		this.arg5 = arg5;
	}

	/**
	 * @return the arg6
	 */
	public String getArg6() {
		return arg6;
	}

	/**
	 * @param arg6 the arg6 to set
	 */
	public void setArg6(String arg6) {
		this.arg6 = arg6;
	}

	/**
	 * @return the arg7
	 */
	public String getArg7() {
		return arg7;
	}

	/**
	 * @param arg7 the arg7 to set
	 */
	public void setArg7(String arg7) {
		this.arg7 = arg7;
	}

	/**
	 * @return the arg8
	 */
	public String getArg8() {
		return arg8;
	}

	/**
	 * @param arg8 the arg8 to set
	 */
	public void setArg8(String arg8) {
		this.arg8 = arg8;
	}

	/**
	 * @return the arg9
	 */
	public String getArg9() {
		return arg9;
	}

	/**
	 * @param arg9 the arg9 to set
	 */
	public void setArg9(String arg9) {
		this.arg9 = arg9;
	}
	
	/**
	 * @return the arg10
	 */
	public String getArg10() {
		return arg10;
	}

	/**
	 * @param arg10 the arg10 to set
	 */
	public void setArg10(String arg10) {
		this.arg10 = arg10;
	}

}