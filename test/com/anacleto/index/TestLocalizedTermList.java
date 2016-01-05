package com.anacleto.index;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import com.anacleto.SzalayTestCase;
import com.anacleto.base.Configuration;

public class TestLocalizedTermList extends SzalayTestCase {
	
	public void testInitialization(){
		
		try {
			LocalizedTermList.initialize(Configuration.params.getIndexDir()
					, new Locale("hu"));
		} catch (IOException e) {
		}
	}
	
	public void testGetShortTerms(){
		Collection nearColl = LocalizedTermList.
			getTermsNear("content", "al");
		
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTerms2(){
		Collection nearColl = LocalizedTermList.
			getTermsNear("content", "alf");
		
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
		
	public void testGetShortTerms3(){
		Collection nearColl = LocalizedTermList.
			getTermsNear("content", "elé");
		
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsBefore(){
		Collection nearColl = LocalizedTermList.
			getTermsBefore("content", "elé");
		System.out.println("Before");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsBefore2(){
		Collection nearColl = LocalizedTermList.
			getTermsBefore("content", "elf");
		System.out.println("Before");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsAfter(){
		Collection nearColl = LocalizedTermList.
			getTermsAfter("content", "elé");
		
		System.out.println("After");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsAfter2(){
		Collection nearColl = LocalizedTermList.
			getTermsAfter("content", "eld");
		
		System.out.println("After");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

}
