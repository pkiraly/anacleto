package com.anacleto.index;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ExampleTestCase extends TestCase {

    public ExampleTestCase(String name) {
        super(name);
    }

	protected void setUp() {
		
	}

	protected void tearDown() {
	}

	public void testOneSecondResponse() throws Exception {
		Thread.sleep(1000);
	}
	
	public static Test suite() {
		return new TestSuite(ExampleTestCase.class);
	}

	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}
}
