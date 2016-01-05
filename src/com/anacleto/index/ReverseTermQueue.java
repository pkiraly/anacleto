package com.anacleto.index;

import java.text.Collator;
import java.util.LinkedList;

import com.anacleto.view.TermBean;


/**
 * Term queue with reverse alphanumeric order 
 * @param a
 * @param b
 * @return
 */
public class ReverseTermQueue extends TermQueue {

	public ReverseTermQueue(int maxSize, Collator co) {
		super(maxSize, co);
	}

	protected boolean lessThan(Object a, Object b) {
		
		TermBean termA = (TermBean) a;
		TermBean termB = (TermBean) b;

		// highest priority will be given to the term that is smaller in
		// the alphabetical order
		int termComp = termA.getKey().compareTo(termB.getKey());
		
		return (termComp < 0) ? true : false;
	}

	/*
	 * Get a linked list with all elements and remove the queue
	 */
	public LinkedList getCollection() {
		LinkedList retColl = new LinkedList();
		while (size() > 0) {
			retColl.add(pop());
		}
		return retColl;
	}
}