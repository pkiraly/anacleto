package com.anacleto.query;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

import com.anacleto.SzalayTestCase;

public class HtmlHighlighterTestCase extends SzalayTestCase {

	public void testTermQuery(){
		Term t = new Term("content", "modello");
		HtmlHighlighter hl = new HtmlHighlighter( new TermQuery(t),
						"<a>modello</a> unificato");
		System.out.println(hl.highLightText());
	}
}
