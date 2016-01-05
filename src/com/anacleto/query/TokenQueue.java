package com.anacleto.query;

import java.util.Comparator;
import java.util.PriorityQueue;



/**
 * Token positions in ascending order
 * 
 * @author: Tesuji s.r.l, 2007
 */
public final class TokenQueue extends PriorityQueue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5322866724323518637L;

	public TokenQueue(int size) {
		super(size, new TokenPosComparator());
	}
/*
	public Comparator comparator() {
		return new TokenPosComparator();
	}
*/
	/*
	protected final boolean lessThan(Object o1, Object o2) {
		TokenPos pp1 = (TokenPos) o1;
		TokenPos pp2 = (TokenPos) o2;

		return pp1.pos < pp2.pos;
	}
	 */	
}

final class TokenPosComparator implements Comparator{

	public int compare(Object o1, Object o2) {
		TokenPos pp1 = (TokenPos) o1;
		TokenPos pp2 = (TokenPos) o2;
		
		if (pp1.getPos() < pp2.getPos()) 
			return -1;
		else if (pp1.getPos() > pp2.getPos())
			return 1;
		else
			return 0;
	}
	
}