package com.anacleto.parsing.source;

import java.util.Hashtable;

import com.anacleto.base.Logging;
import com.anacleto.content.PDFBookmarkEntry;
import org.apache.log4j.Logger;

public abstract class PdfTocConfig
{
    protected Logger log;
    protected String defaultField;
    protected String prefix;
    protected String year;
    protected Hashtable bookmarkInfo = new Hashtable();
    protected int numberOfPages;

	public PdfTocConfig()
    {
        log = Logging.getIndexingLogger();
        defaultField = "bookmark";
    }
    
    public abstract void setUp(String prefix, String year);
    
    public abstract String getFieldName(String as[])
        throws PdfTocConfigException;

    public abstract void addBookmarkEntry(PDFBookmarkEntry bookmark);

    public abstract void checkAllPages();

	public String getPrefix() {
		return prefix;
	}

	public String getYear() {
		return year;
	}
	
	public Hashtable getBookmarkInfo() {
		return bookmarkInfo;
	}

    public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
}
