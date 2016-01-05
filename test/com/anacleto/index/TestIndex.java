package com.anacleto.index;

import junit.framework.TestCase;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

import com.anacleto.base.Configuration;

public class TestIndex extends TestCase{

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		if (Configuration.params.getIndexDir() != null)
			return;
			
			
		Configuration conf = new Configuration();
		conf.init();
	}
	
	public void testNameQuery() throws Exception{
		IndexManager im = new IndexManager();
		Document doc = im.executeTermQuery(new Term("name", 
				"avirelfs/bd/autori/anonimo/roma_sette_giornate/indicazione_viaggio_roma_napoli.html"),
				0);
		assertNotNull(doc);
	}
	
	public void testLocation() throws Exception{
		IndexManager im = new IndexManager();
		Document doc = im.executeTermQuery(new Term("location", 
				"/home/robi/work/avirel/wget/www.avirel.it/bd/autori/anonimo/roma_sette_giornate/indicazione_viaggio_roma_napoli.html"),
				0);
		System.out.println(doc.getField("path").stringValue());
		assertNotNull(doc);
	}

}
