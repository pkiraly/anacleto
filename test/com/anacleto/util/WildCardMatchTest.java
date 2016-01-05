package com.anacleto.util;

import junit.framework.TestCase;

public class WildCardMatchTest extends TestCase{
	
	public void testExtraction(){
		assertTrue(WildCardMatch.match("lexikonok", "lexikonok"));
		assertTrue(WildCardMatch.match("lexikonok", "l*konok"));
		assertTrue(WildCardMatch.match("lexikonok", "lexikono?"));
		assertTrue(WildCardMatch.match("lexikonok", "lexikon??"));
		
		assertTrue(WildCardMatch.match("lexikonok", "lexikon*"));
		assertTrue(WildCardMatch.match("lexikonok", "*ik?n?*"));
		
		assertFalse(WildCardMatch.match("lexikonok", "lexikono?*?"));
		assertFalse(WildCardMatch.match("lexikonok", "lexikonok?"));
		assertFalse(WildCardMatch.match("lexikonok", "?lexikonok"));
		assertFalse(WildCardMatch.match("lexikonok", "??exikonok"));
	}
}
