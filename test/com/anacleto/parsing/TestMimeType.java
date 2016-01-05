package com.anacleto.parsing;

import com.anacleto.parsing.filetype.MimeTypes;

import junit.framework.TestCase;

public class TestMimeType extends TestCase{
	
	public void testInit(){
		assertEquals("application/excel", 
				MimeTypes.getMimeForFileName("excel.xls"));
		
		assertNull(MimeTypes.getMimeForFileName("excel.xsls"));
		
		assertEquals("application/msword", 
				MimeTypes.getMimeForFileName("word.doc"));
		
		assertEquals("text/html", 
				MimeTypes.getMimeForFileName("test.html"));

		assertEquals("text/html", 
				MimeTypes.getMimeForFileName("test.htm"));
		
		assertEquals(MimeTypes.getAliasMime("text/html"),
				MimeTypes.getMimeForFileName("test.htm"));

	}

}
