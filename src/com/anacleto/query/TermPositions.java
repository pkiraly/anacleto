package com.anacleto.query;

import java.util.Map;

import org.apache.commons.collections.FastTreeMap;

/**
 * Term positions. Every term has a position queue assigned to it
 * @author robi
 *
 */
public final class TermPositions{
	public Map termPosMap = new FastTreeMap();
	
	public TokenQueue getTermPositions(String term){
		return (TokenQueue)termPosMap.get(term);
	}
	
	public void deleteTerm(String term){
		termPosMap.remove(term);
	}
	
	public void addTermPosition(String term, TokenPos pos){
		TokenQueue posQueue = (TokenQueue)termPosMap.get(term);
		if (posQueue == null){
			posQueue = new TokenQueue(100);  //TODO: invest this limit
		}
		posQueue.add(pos);
		termPosMap.put(term, posQueue);
	}
}

