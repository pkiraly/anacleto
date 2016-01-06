package com.anacleto.index;

import java.util.Collection;
import java.util.Iterator;

import com.anacleto.SzalayTestCase;
import com.anacleto.view.TermBean;

public class TestPositionTermList extends SzalayTestCase{
	
	private PositionTermList pt;
	
	protected void setUp() throws Exception {
		super.setUp();
		pt = new PositionTermList();
	}
	
	public void testTermsNear() throws Exception{
		System.out.println("----lonyai");
		Collection coll = pt.getTermsNear("content", "lonyai", 10);
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			TermBean term = (TermBean) it.next();
			System.out.println(term.getTerm());
		}
		System.out.println("----/lonyai");
	}
	
	public void testTermsNear2() throws Exception{
		System.out.println("----lónyai");
		Collection coll = pt.getTermsNear("content", "lónyai", 10);
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			TermBean term = (TermBean) it.next();
			System.out.println(term.getTerm());
		}
		System.out.println("----/lónyai");
	}
}
