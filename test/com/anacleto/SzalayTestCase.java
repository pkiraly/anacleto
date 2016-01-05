package com.anacleto;

import java.net.URL;

import junit.framework.TestCase;

import com.anacleto.base.BaseParameters;
import com.anacleto.base.Configuration;
import com.anacleto.base.LoggingParameters;

/**
 * Base class to test Anacleto with a small size index
 * @author robi
 *
 */
public abstract class SzalayTestCase extends TestCase {
	
	protected void setUp() throws Exception {
		if (Configuration.params.getIndexDir() != null)
			return;
		
		ClassLoader cloader = getClass().getClassLoader();
		
		BaseParameters params = new BaseParameters();
		
		URL configDir = cloader.getResource("data/szalay");
		params.setConfigDir(configDir.getFile());
		
		URL indexDir = cloader.getResource("data/szalay/index");
		params.setIndexDir(indexDir.getFile());
		
		params.setLogParams(new LoggingParameters(configDir.getFile()));
		
		Configuration conf = new Configuration();
		conf.init(params);
	}
	
	public void testTermQueryHighlight() throws Exception{
		Highlighter hl = new Highlighter("kossuth");
		System.out.println(hl.scoreDoc(doc));
	}

}
