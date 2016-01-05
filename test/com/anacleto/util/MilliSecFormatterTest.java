package com.anacleto.util;

import java.text.NumberFormat;
import java.util.Stack;

import com.anacleto.util.MilliSecFormatter;

import junit.framework.TestCase;

public class MilliSecFormatterTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testStack() {
		Stack path = new Stack();
		path.add("egy");
		assertEquals("egy", path.lastElement());
		assertEquals("egy", path.get(path.size()-1));
		path.add("kettõ");
		assertEquals(path.get(path.size()-1), path.lastElement());
		path.add("három");
		assertEquals(path.get(path.size()-1), path.lastElement());
	}
	
	public void testNumberFormat() {
		NumberFormat nf = NumberFormat.getInstance(); 
		nf.setMinimumIntegerDigits(3); 
		assertEquals("001", nf.format(1)); 
	}

	public void testToStringLong() {
		assertEquals("0 00:00:00.001", MilliSecFormatter.toString(1));
		assertEquals("0 00:00:01.0",   MilliSecFormatter.toString(1000));
		assertEquals("0 00:00:02.999", MilliSecFormatter.toString(2999));
		assertEquals("0 00:00:03.0",   MilliSecFormatter.toString(3000));
		assertEquals("0 00:00:03.001", MilliSecFormatter.toString(3001));
		assertEquals("0 00:00:29.999", MilliSecFormatter.toString(29999));
		assertEquals("0 00:00:30.0",   MilliSecFormatter.toString(30000));
		assertEquals("0 00:00:30.001", MilliSecFormatter.toString(30001));
		assertEquals("0 00:04:59.999", MilliSecFormatter.toString(299999));
		assertEquals("0 00:05:00.0",   MilliSecFormatter.toString(300000));
		assertEquals("0 00:05:00.001", MilliSecFormatter.toString(300001));
		assertEquals("0 00:49:59.999", MilliSecFormatter.toString(2999999));
		assertEquals("0 01:11:53.313", MilliSecFormatter.toString(4313313));
		assertEquals("0 00:50:00.0",   MilliSecFormatter.toString(3000000));
		assertEquals("0 00:50:00.001", MilliSecFormatter.toString(3000001));
		assertEquals("0 08:19:59.999", MilliSecFormatter.toString(29999999));
		assertEquals("0 08:20:00.0",   MilliSecFormatter.toString(30000000));
		assertEquals("0 08:20:00.001", MilliSecFormatter.toString(30000001));
		assertEquals("3 11:19:59.999", MilliSecFormatter.toString(299999999));
		assertEquals("3 11:20:00.0",   MilliSecFormatter.toString(300000000));
		assertEquals("3 11:20:00.001", MilliSecFormatter.toString(300000001));
	}
}
