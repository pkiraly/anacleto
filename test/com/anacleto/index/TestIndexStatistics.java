package com.anacleto.index;

import java.util.Collection;
import java.util.Iterator;


import com.anacleto.SzalayTestCase;
import com.anacleto.view.TermBean;

public class TestIndexStatistics extends SzalayTestCase {
	
	private PositionTermList pt;
	
	protected void setUp() throws Exception {
		super.setUp();
		pt = new PositionTermList();
	}
	
	public void testTermsNear() throws Exception{
		Collection coll = pt.getTermsNear("content", "millenium", 20);
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			TermBean term = (TermBean) it.next();
			System.out.println(term.getTerm());
		}
		
	}

	public void testTermsNear2() throws Exception{
		Collection coll = pt.getTermsNear("content", "tyuk", 20);
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			TermBean term = (TermBean) it.next();
			System.out.println(term.getTerm());
		}
		
	}
}
