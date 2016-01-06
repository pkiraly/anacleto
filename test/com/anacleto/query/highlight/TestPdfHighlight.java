package com.anacleto.query.highlight;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;

import com.anacleto.SzalayTestCase;

public class TestPdfHighlight extends SzalayTestCase {
	public static String szazadok_1867_page4 = 
		"De a külön nemzetiség nem lehet czél önmagában: csak eszköz az egy magasabb czélra, mely a haladásban, tökélyesülésben," + 
		" az emberiség eszménye felé való közeledésben keresendő." + 
		" A. fejlődés, a haladás a népek legközelebbi czélja; a " +
		"külön nemzetiség csak ösztön arra, hogy a haladást egyéniségük " +
		"sajátságos zománczában tüntessék fel, s az elsőbbségért versenyre" +
		" keljenek a nemzetcsalád többi tagjaival.";

	protected void setUp() throws Exception {
		super.setUp();
	}
        
	public void testTermQuery() throws IOException, ParseException{
		String q = "content:népek";
		PdfHighlighter hi = new PdfHighlighter(q);
		
		String xml = hi.highLightDocument(getDoc(), "content");
		System.out.println(xml);
	}
	
	public void testPhraseQuery() throws IOException, ParseException{
		String q = "content:\"külön nemzetiség\"";
		PdfHighlighter hi = new PdfHighlighter(q);
		
		String xml = hi.highLightDocument(getDoc(), "content");
		System.out.println(xml);
	}

	public void testProximityQuery() throws IOException, ParseException{
		String q = "content:\"külön czél\"~5";
		PdfHighlighter hi = new PdfHighlighter(q);
		
		String xml = hi.highLightDocument(getDoc(), "content");
		System.out.println(xml);
	}
	
	public void testBooleanQuery() throws IOException, ParseException{
		String q = "content:nemzetiség OR content:felé";
		PdfHighlighter hi = new PdfHighlighter(q);
		
		String xml = hi.highLightDocument(getDoc(), "content");
		System.out.println("Query:" + q);
		System.out.println(xml);
	}
	
	private Document getDoc(){
		Document doc = new Document();
		doc.add(new Field("content", szazadok_1867_page4, true, true, true));
		return doc;
	}

}
