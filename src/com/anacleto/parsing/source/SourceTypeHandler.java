package com.anacleto.parsing.source;

import java.io.IOException;

import com.anacleto.base.BookException;
import com.anacleto.hierarchy.Book;
import com.anacleto.hierarchy.BookPage;

public abstract class SourceTypeHandler {

	public abstract void indexBook(Book book) 
			throws BookException, IOException;
	
	public abstract ContentBean getContent(BookPage page)
		throws ContentReadException;
	
}
