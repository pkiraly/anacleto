package com.anacleto.content;

import java.io.FileOutputStream;

import junit.framework.TestCase;

/**
 * Base class to test Anacleto with a small size index
 * @author robi
 *
 */
public class TestPdfOperations extends TestCase {
	/*
	protected void setUp() throws Exception {
		if (Configuration.params.getIndexDir() != null)
			return;
		
		ClassLoader cloader = getClass().getClassLoader();
		
		BaseParameters params = new BaseParameters();
		
		URL configDir = cloader.getResource("c:/doku/anacletoIndexSzazadok2");
		params.setConfigDir(configDir.getFile());
		
		URL indexDir = cloader.getResource("c:/doku/anacletoConf");
		params.setIndexDir(indexDir.getFile());
		
		params.setLogParams(new LoggingParameters(configDir.getFile()));
		
		Configuration conf = new Configuration();
		conf.init(params);
	}
	*/
	
	public void testTermQueryHighlight() throws Exception{
		FileOutputStream out = new FileOutputStream("/home/moki/test.out");
		byte[] b = {100,1,1,1,1,1,1,1};
		out.write(b);
		out.close();
		
	}

}
