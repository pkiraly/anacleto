package com.anacleto.index;

import java.io.IOException;

import org.apache.lucene.search.Query;

import com.anacleto.SzalayTestCase;


public class TestPathQueryExtractor extends SzalayTestCase {
	

	public void testExtraction() throws IOException{
		
		PathQueryExtractor pqE = new PathQueryExtractor();
		Query retQuery = pqE.extractPath("/lexikonok");
		assertEquals("book:pallas", retQuery.toString());
		
		retQuery = pqE.extractPath("/lexikonok/p*");
		assertEquals("book:pallas", retQuery.toString());

		retQuery = pqE.extractPath("/pallas/*");
		assertEquals("book:pallas", retQuery.toString());
			
		retQuery = pqE.extractPath("/root/*");
		//assertEquals("book:pallas", retQuery.toString());
		
		retQuery = pqE.extractPath("/pallas/pallas29620/*");
		assertEquals("(book:pallas path:/pallas/pallas29620/*)", retQuery.toString());
		
		//retQuery = pqE.extractPath("/cs*/*");
		//assertEquals(retQuery, "(/barczay/* OR /fejerpataky/* OR /karacsonyi/* OR /nagyivan/*)");
		
	}
}
