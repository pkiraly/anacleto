package com.anacleto.api;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.lucene.queryParser.ParseException;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.anacleto.SzalayTestCase;
import com.anacleto.api.Facade;
import com.anacleto.api.QueryStats;

public class TestFacade extends SzalayTestCase{
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testSipleQuery() throws IOException, ParseException{
		Facade.executeQuery("kossuth", 0, 10, new QueryStats(), new LinkedList());
	}
	
	public void testFieldQuery() throws IOException, ParseException{
		Facade.executeQueryInAllFields("kossuth", 0, 10, new QueryStats(), new LinkedList());
	}
	
	public static Test suite() {
		return new TestSuite(TestFacade.class);
	}

	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}
}
