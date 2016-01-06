package com.anacleto.index;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BooleanTestCase extends TestCase {

    public BooleanTestCase(String name) {
        super(name);
    }

	public void testB() throws Exception {
		String a = "true";
		System.out.println(Boolean.valueOf(a));
	}
	
}
