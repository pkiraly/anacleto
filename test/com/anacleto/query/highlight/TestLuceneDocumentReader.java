package com.anacleto.query.highlight;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class TestLuceneDocumentReader extends TestCase {
	
	Document doc;
	
	protected void setUp() throws Exception {
		doc = new Document();
		doc.add(Field.Keyword("keyword", "keyword, keyword"));
		doc.add(Field.Text("text", "text text text text"));
		doc.add(Field.Text("text", "text text text text"));
		doc.add(Field.UnStored("unstored", "unstored, unstored"));
		doc.add(Field.UnIndexed("unindexed", "unindexed, unindexed"));
		
	}
	
	public void testWithZeroStep() throws Exception {
		LuceneDocumentReader reader = new LuceneDocumentReader(doc);
		int	charsRead = reader.read(new char[1], 0, 0);
		assertEquals(0, charsRead);
	}
	
	public void testWithOneStep() throws Exception {
		String str = read(1);
		assertEquals(" keyword, keyword" + " text text text text" + 
				" text text text text" + 
				" unstored, unstored" + 
				" unindexed, unindexed", str);
	}

	public void testWithTenStep() throws Exception {
		String str = read(10);
		assertEquals(" keyword, keyword" + " text text text text" + 
				" text text text text" + 
				" unstored, unstored" + 
				" unindexed, unindexed", str);
	}
	
	public void testWithSixTeenStep() throws Exception {
		String str = read(10);
		assertEquals(" keyword, keyword" + " text text text text" + 
				" text text text text" + 
				" unstored, unstored" + 
				" unindexed, unindexed", str);
	}
	
	private String read(int buffer) throws IOException{
		StringBuffer retStr = new StringBuffer();
		LuceneDocumentReader reader = new LuceneDocumentReader(doc);
		int counter = 0;
		char c[] = new char[buffer];
		int charsRead;
		while (true) {
			charsRead = reader.read(c, counter, buffer);
			if (charsRead > 0)
				retStr.append(c, counter, charsRead);
			else
				return retStr.toString();
		} 
	}
	
}
