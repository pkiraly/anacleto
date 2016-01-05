package com.anacleto.base;

import junit.framework.TestCase;

public class TestMenuHandlerLarge extends TestCase{
	
	protected void setUp() throws Exception {
		Configuration conf = new Configuration();
		conf.init();
	}
	
	public void testGetChildrenOfSzalay1() throws Exception{
		MenuHandler handler = new MenuHandler();
		String resStr;
		resStr = handler.getChildElements("root", 0, true, "a");
		
		System.out.println(resStr);
	}

}
