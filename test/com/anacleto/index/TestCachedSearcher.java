package com.anacleto.index;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;

import com.anacleto.base.Configuration;

public class TestCachedSearcher extends TestCase{

	private CachedSearcher searcher;
	private Sort           sort;
	private Analyzer       analyzer;
	
	protected void setUp() throws Exception {
		Configuration conf = new Configuration();
		conf.init();
		
		URL indexdir = TestCachedSearcher.class.getClassLoader().
				getResource("data/szalay");
		searcher = new CachedSearcher(
        		new IndexSearcher(indexdir.getFile()),
        			IndexReader.open(indexdir.getFile()));
		
		analyzer = new CustomAnalyzer(new File("/home/robi/java/anacleto_dev"));
		sort = new Sort(new SortField("book", new BookPageOrderSource()));
	}
	
	public void testSortedQueries() throws Exception {
		Query q = QueryParser.parse("kossuth", "content", analyzer);
		
		Hits hits = searcher.search(q, sort);
		assertEquals( 6, hits.length());
		
		hits = searcher.search(q, (Filter)null, sort);
		assertEquals( 6, hits.length());
	}
	
	public void testUnSortedQueries() throws Exception {
		Query q = QueryParser.parse("kőkorszak", "content", analyzer);
		
		Hits hits = searcher.search(q);
		assertEquals(1, hits.length());
		
		hits = searcher.search(q, (Filter)null);
		assertEquals(1, hits.length());
	}	
}
