package com.anacleto.parsing;

import org.apache.lucene.document.Document;

import com.anacleto.hierarchy.BookPage;

public final class DocumentContent {

	public Document doc;

	public BookPage page;

	public StringBuffer buffer;

	public int childs;

	/**
	 * 
	 */
	public DocumentContent() {
		doc = new Document();
		buffer = new StringBuffer();
	}
}
