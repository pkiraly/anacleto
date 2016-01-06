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
			LocalizedTermList.initialize();
		} catch (IOException e) {
		}
	}
	
	public void testGetShortTerms() throws IOException{
		
		Collection nearColl = LocalizedTermList.
			getTermsNear("content", "al");
		
		System.out.println("Near al:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTerms2() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsNear("content", "áll");
		
		System.out.println("Near áll:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
		
	public void testGetShortTerms3() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsNear("content", "elé");
		
		System.out.println("Near elé:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTerms4() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsNear("content", ";;;");
		
		System.out.println("Near ;;:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTerms5() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsNear("content", "el");
		
		System.out.println("Near el:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsBefore() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsBefore("content", "elé");
		System.out.println("Before elé:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsBefore2() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsBefore("content", "elf");
		System.out.println("Before elf:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsAfter() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsAfter("content", "eld");
		
		System.out.println("After eld:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsAfter2() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsAfter("content", "eld");
		
		System.out.println("After eld:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	public void testGetShortTermsBeforeElx() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsBefore("content", "elx");
		
		System.out.println("Before elx:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsNearElx() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsNear("content", "elx");
		
		System.out.println("Near elx:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetShortTermsAfter3() throws IOException{
		Collection nearColl = LocalizedTermList.
			getTermsAfter("content", "elx");
		
		System.out.println("After elx:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetNearLo2() throws IOException{
		Collection nearColl = LocalizedTermList.getTermsNear("content", "ló");
		
		System.out.println("Near: ló");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void testGetNearLo() throws IOException{
		Collection nearColl = LocalizedTermList.getTermsNear("content", "lo");
		
		System.out.println("Near lo:");
		Iterator it = nearColl.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
