package com.anacleto.query.highlight;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.Hits;

import com.anacleto.SzalayTestCase;
import com.anacleto.index.IndexManager;

public class TestHighlighter extends SzalayTestCase{
	
	Document doc;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		doc = new Document();
		doc.add(Field.Text("content", "esik eso karikara kossuth lajos kalapjara"));
		doc.add(Field.Text("content", "esik eso karikara kossuth lajos kalapjara"));
	}
	
	public void testTermQueryHighlight() throws Exception{
		HitlistHighlighter hl = new HitlistHighlighter("kossuth");
		System.out.println(hl.scoreDoc(doc));
	}

	public void testTermQueryHighlight2() throws Exception{
		HitlistHighlighter hl = new HitlistHighlighter("kalapjara");
		hl.scoreDoc(doc);
	}

	public void testTermQueryHighlight3() throws Exception{
		String queryStr = "püspökévé";
		
		IndexManager im = new IndexManager();
		Hits hits = im.executeQuery(queryStr);
		
		HitlistHighlighter hl = new HitlistHighlighter(queryStr);
		hl.scoreDoc(hits.doc(0));
	}
	
	public void testPhraseQueryHighlight() throws Exception{
		String queryStr = "\"püspökévé szentelt\"";
		
		IndexManager im = new IndexManager();
		Hits hits = im.executeQuery(queryStr);
		
		HitlistHighlighter hl = new HitlistHighlighter(queryStr);
		hl.scoreDoc(hits.doc(0));
	}

	public void testPhraseQueryWithSlopHighlight() throws Exception{
		String queryStr = "\"püspökévé szentelt\"~2";
		
		IndexManager im = new IndexManager();
		Hits hits = im.executeQuery(queryStr);
		
		HitlistHighlighter hl = new HitlistHighlighter(queryStr);
		hl.scoreDoc(hits.doc(0));
	}
	
	public void testPrefixQueryHighlight() throws Exception{
		String queryStr = "püspök*";
		
		IndexManager im = new IndexManager();
		Hits hits = im.executeQuery(queryStr);
		
		HitlistHighlighter hl = new HitlistHighlighter(queryStr);
		hl.scoreDoc(hits.doc(0));
	}
	
	public void testPhrasePrefixQuery() throws Exception{
		String queryStr = "\"püspök* *entelt\"";
		
		IndexManager im = new IndexManager();
		Hits hits = im.executeQuery(queryStr);
		
		HitlistHighlighter hl = new HitlistHighlighter(queryStr);
		hl.scoreDoc(hits.doc(0));
	}
	
	public void testPhrasePrefixQueryWithSlop() throws Exception{
		String queryStr = "\"püspök* *entelt\"~2";
		
		IndexManager im = new IndexManager();
		Hits hits = im.executeQuery(queryStr);
		
		HitlistHighlighter hl = new HitlistHighlighter(queryStr);
		hl.scoreDoc(hits.doc(0));
	}
}
