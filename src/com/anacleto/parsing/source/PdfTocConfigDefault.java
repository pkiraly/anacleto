package com.anacleto.parsing.source;

import com.anacleto.content.PDFBookmarkEntry;

public class PdfTocConfigDefault extends PdfTocConfig
{
    public PdfTocConfigDefault() {}

	public void setUp(String prefix, String year) {
		this.prefix = prefix;
		this.year = year;
	}

	public String getFieldName(String args[])
        throws PdfTocConfigException
    {
        return defaultField;
    }
	
	public void addBookmarkEntry(PDFBookmarkEntry bookmark) {
		
	}

	public void checkAllPages() {
		
	}

}
