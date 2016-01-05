package com.anacleto.query;

import org.apache.lucene.util.PriorityQueue;

/**
 * 
 * @author robi
 *
 */
final class PhraseTermQueue extends PriorityQueue {
	PhraseTermQueue(int size) {
	    initialize(size);
	}

  protected final boolean lessThan(Object o1, Object o2) {
  	PhraseTermPos ptp1 = (PhraseTermPos)o1;
  	PhraseTermPos ptp2 = (PhraseTermPos)o2;
  	
  	return ptp1.getTopPos() < ptp2.getTopPos();
  }
}