package com.anacleto.base;

import com.anacleto.SzalayTestCase;

public class TestMenuHandler extends SzalayTestCase{
	
	public void testGetChildrenOfRoot() throws Exception{
		MenuHandler handler = new MenuHandler();
		String resStr;
		resStr = handler.getChildElements("root", 0, true, "");
		
		System.out.println(resStr);
	}

	public void testGetChildrenOfSzalay() throws Exception{
		MenuHandler handler = new MenuHandler();
		String resStr;
		resStr = handler.getChildElements("szalay", 0, true, "");
		
		System.out.println(resStr);
	}

	public void testGetChildrenOfSzalay1() throws Exception{
		MenuHandler handler = new MenuHandler();
		String resStr;
		resStr = handler.getChildElements("szalay1", 0, true, "kossuth");
		
		System.out.println(resStr);
	}

	public void testGetChildrenOfRootWithQueryAz() throws Exception{
		MenuHandler handler = new MenuHandler();
		String resStr;
		resStr = handler.getChildElements("root", 0, true, "az");
	
		System.out.println(resStr);
	}
	
	public void testGetChildrenOfRootWithQueryTanul() throws Exception{
		MenuHandler handler = new MenuHandler();
		String resStr;
		resStr = handler.getChildElements("root", 0, true, "tanuló");
	
		System.out.println(resStr);
	}

	public void testGetChildrenOfPallasWithQueryTanul() throws Exception{
		MenuHandler handler = new MenuHandler();
		String resStr;
		resStr = handler.getChildElements("pallas", 0, true, "tanuló");
	
		System.out.println(resStr);
	}
	
	public void testGetChildrenOfPallas29620WithQueryTanul() throws Exception{
		MenuHandler handler = new MenuHandler();
		String resStr;
		resStr = handler.getChildElements("pallas29620", 0, true, "tanuló");
	
		System.out.println(resStr);
	}
	
	
}

