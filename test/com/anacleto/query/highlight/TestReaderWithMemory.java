package com.anacleto.query.highlight;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

public class TestReaderWithMemory  extends TestCase {

	String testStr = "the quick brown fox jumps over the lazy dog";

	protected void setUp() throws Exception {
	}
	
	public void testWithOneStep() throws Exception {
		String str = read(1);
		assertEquals(testStr, str);
	}
	
	public void testWithTenStep() throws Exception {
		String str = read(10);
		assertEquals(testStr, str);
	}
	
	public void testBuffersFiftenCharStep() throws Exception {
		StringReader strReader = new StringReader(testStr);
		
		ReaderWithMemory reader = new ReaderWithMemory(strReader, 30);
		int counter = 0;
		char c[] = new char[15];
		reader.read(c, counter, 15);
		
		String pre = reader.getPreBuffer().toString();
		assertEquals(testStr.substring(15), pre);
		
		String post = reader.getPostBuffer().toString();
		assertEquals(testStr.substring(0, 15), post);
		
	}
	
	public void testBuffersHundredCharStep() throws Exception {
		StringReader strReader = new StringReader(testStr);
		
		ReaderWithMemory reader = new ReaderWithMemory(strReader, 30);
		int counter = 0;
		char c[] = new char[100];
		reader.read(c, counter, 100);
		
		String pre = reader.getPreBuffer().toString();
		assertEquals("", pre);
		
		String post = reader.getPostBuffer().toString();
		assertEquals(testStr.substring(testStr.length()-30), post);
	}

	public void testBuffersWithOffset() throws Exception {
		ReaderWithMemory reader = readOffset(10, 5);
		
		String pre = reader.getPreBuffer().toString();
		assertEquals(testStr.substring(5, 35), pre);
		
		String post = reader.getPostBuffer().toString();
		assertEquals(testStr.substring(0, 5), post);
	}
	
	private ReaderWithMemory readOffset(int offset, int len) throws IOException{
		StringReader strReader = new StringReader(testStr);
		
		ReaderWithMemory reader = new ReaderWithMemory(strReader, 30);
		char c[] = new char[offset+len];
		reader.read(c, offset, len);

		return reader;
	}
	
	private String read(int buffer) throws IOException{
		
		StringBuffer retStr = new StringBuffer();
		
		StringReader strReader = new StringReader(testStr);
		
		ReaderWithMemory reader = new ReaderWithMemory(strReader, 30);
		int counter = 0;
		char c[] = new char[buffer];
		int charsRead;
		while (true) {
			charsRead = reader.read(c, counter, buffer);
			if (charsRead > 0)
				retStr.append(c,0, charsRead);
			else
				return retStr.toString();
		} 
	}
}
