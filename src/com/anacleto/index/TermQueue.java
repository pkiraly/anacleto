package com.anacleto.index;

import java.text.Collator;
import java.util.LinkedList;

import org.apache.lucene.util.PriorityQueue;

import com.anacleto.view.TermBean;


public class TermQueue extends PriorityQueue {

	protected Collator co;
	private int  maxSize;
	
	public TermQueue(int maxSize, Collator co) {
		super();
		this.co = co;
		this.maxSize = maxSize;
		initialize(maxSize);
	}

	
	/**
	 * @return Returns the maxSize.
	 */
	public int getMaxSize() {
		return maxSize;
	}


	/**
	 * @return Returns the co.
	 */
	public Collator getCo() {
		return co;
	}


	protected boolean lessThan(Object a, Object b) {

		
		TermBean termA = (TermBean) a;
		TermBean termB = (TermBean) b;

		// highest priority will be given to the term that is smaller in
		// the alphabetical order

		int termComp = termA.getKey().compareTo(termB.getKey());

		return (termComp >= 0) ? true : false;
	}

	/*
	 * Get a linked list with all elements and remove the queue
	 */
	public LinkedList getCollection() {
		LinkedList retColl = new LinkedList();
		while (size() > 0) {
			retColl.addFirst(pop());
		}
		return retColl;
	}
}
