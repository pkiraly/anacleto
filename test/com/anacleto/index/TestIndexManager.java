package com.anacleto.index;

import org.apache.lucene.search.Hits;

import com.anacleto.SzalayTestCase;


public class TestIndexManager extends SzalayTestCase {

	
	private IndexManager im;
	
	protected void setUp() throws Exception {
		super.setUp();
		im = new IndexManager();
	}
	public void testTermQuery() throws Exception{
		Hits h = im.executeQuery("kossuth");
		assertEquals(6, h.length());
	}
	
	public void testPrefixQuery() throws Exception{
		Hits h = im.executeQuery("kossut*");
		assertEquals(7, h.length());
	}
	
	public void testWildCardQuery() throws Exception{
		Hits h = im.executeQuery("*ossuth");
		assertEquals(6, h.length());
	}

	public void testWildCardQuery2() throws Exception{
		Hits h = im.executeQuery("?ossuth");
		assertEquals(6, h.length());
	}

	public void testWildCardQuery3() throws Exception{
		Hits h = im.executeQuery("??ssuth");
		assertEquals(6, h.length());
	}

	public void testWildCardQuery4() throws Exception{
		Hits h = im.executeQuery("??ssut*");
		assertEquals(8, h.length());
	}
	
	public void testPhrasePrefixQuery() throws Exception{
		Hits h = im.executeQuery("\"*emzetiségi mozgalma*\"");
		assertEquals(1, h.length());
	}
	
	public void testPathQuery() throws Exception{
		Hits h = im.executeQuery("path:/szalay/");
		assertEquals(99, h.length());
	}
	
	public void testTermQueryWithPathQuery() throws Exception{
		Hits h = im.executeQuery("kossuth AND ( path:/szalay/ )");
		assertEquals(6, h.length());
	}
	
	public void testTermQueryWithPathQuery2() throws Exception{
		Hits h = im.executeQuery("száváig AND ( path:/szalay/szalay1/* )");
		assertEquals(2, h.length());
	}
	
	public void testTermProximityQuery() throws Exception{
		Hits h = im.executeQuery("\"kossuth istv*n\"~10");
		assertEquals(1, h.length());
	}

}
