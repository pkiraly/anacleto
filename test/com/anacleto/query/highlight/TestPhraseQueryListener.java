package com.anacleto.query.highlight;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;

import com.anacleto.SzalayTestCase;

public class TestPhraseQueryListener extends SzalayTestCase{
	/*
	public void testKossuthLajos(){
		PhraseQuery query = new PhraseQuery();
		query.add(new Term("field", "kossuth"));
		query.add(new Term("field", "lajos"));
		
		PhraseQueryScorer scorer = new PhraseQueryScorer(query);
		scorer.addToken("kossuth");
		scorer.addToken("lajos");
		
		assertTrue(scorer.isMatch());
		assertFalse(scorer.isActive());
	}
	
	public void testKossuthLajosSlop1(){
		PhraseQuery query = new PhraseQuery();
		query.add(new Term("field", "kossuth"));
		query.add(new Term("field", "lajos"));
		query.setSlop(1);
		
		PhraseQueryScorer scorer = new PhraseQueryScorer(query);
		scorer.addToken("kossuth");
		scorer.addToken("something");
		scorer.addToken("lajos");
		
		assertTrue(scorer.isMatch());
		assertFalse(scorer.isActive());
	}

	public void testLajosKossuthSlop2(){
		PhraseQuery query = new PhraseQuery();
		query.add(new Term("field", "lajos"));
		query.add(new Term("field", "kossuth"));
		query.setSlop(2);
		
		PhraseQueryScorer scorer = new PhraseQueryScorer(query);
		scorer.addToken("kossuth");
		scorer.addToken("something");
		scorer.addToken("lajos");
		
		assertFalse(scorer.isMatch());
		assertFalse(scorer.isActive());
	}

	public void testLajosKossuthSlop3(){
		PhraseQuery query = new PhraseQuery();
		query.add(new Term("field", "lajos"));
		query.add(new Term("field", "kossuth"));
		query.setSlop(3);
		
		PhraseQueryScorer scorer = new PhraseQueryScorer(query);
		scorer.addToken("kossuth");
		scorer.addToken("something");
		scorer.addToken("lajos");
		
		assertTrue(scorer.isMatch());
		assertFalse(scorer.isActive());
	}

	public void testKossuthKossuth(){
		PhraseQuery query = new PhraseQuery();
		query.add(new Term("field", "kossuth"));
		query.add(new Term("field", "kossuth"));
		query.setSlop(1);
		
		PhraseQueryScorer scorer = new PhraseQueryScorer(query);
		scorer.addToken("kossuth");
		scorer.addToken("something");
		scorer.addToken("kossuth");
		
		assertTrue(scorer.isMatch());
		assertFalse(scorer.isActive());
	}
	*/
}
