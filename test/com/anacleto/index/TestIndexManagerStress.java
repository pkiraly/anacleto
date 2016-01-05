package com.anacleto.index;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

public class TestIndexManagerStress extends TestSuite{

	public static Test suite() {
		
        TestSuite suite = new TestSuite();
        suite.addTest(makeOneSecondResponseTest());
        suite.addTest(makeTenUserLoadTest());
    	
        return suite;
    }
	
	protected static Test makeTenUserLoadTest() {

		int users = 10;
		int iterations = 1;

		Test testCase = new TestIndexManager();
		Test loadTest = new LoadTest(testCase, users, iterations);

		return loadTest;
	}
	
	protected static Test makeOneSecondResponseTest() {

		int users = 1;
		int iterations = 10;
		long maxElapsedTimeInMillis = 1000;

		Test testCase = new TestIndexManager();

		Test loadTest = new LoadTest(testCase, users, iterations);
		Test timedTest = new TimedTest(loadTest, maxElapsedTimeInMillis);

		return timedTest;
	}
}
