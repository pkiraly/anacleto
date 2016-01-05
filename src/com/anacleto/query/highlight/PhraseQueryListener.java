package com.anacleto.query.highlight;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;

public class PhraseQueryListener extends TokenListener {
	
	PhraseQuery query;

	private int tokenPos = 0;
	
	//Total distance from the original
	private int totDist  = 0;
	
	int  foundPositions[];
	
	int  positions[];
	Term terms[];
	
	
	public PhraseQueryListener(PhraseQuery query, String postText, 
				int firstTermStartPos) {
		
		this.query = query;
		this.postText = postText;
		this.firstTermStartPos = firstTermStartPos;
		
		positions = query.getPositions();
		terms     = query.getTerms();
		
		foundPositions = new int[positions.length];
		for (int i = 0; i < foundPositions.length; i++) {
			foundPositions[i] = -1;
		}
	}
	
	public void addToken(Token token){
		if (!active)
			return;
		
		//FIXME: "kossuth kossuth" type queries will fail
		int foundPos = -1;
		
		//find closest matching term,
		//that is not found already
		int minDist = Integer.MAX_VALUE;
		for (int i = 0; i < positions.length; i++) {
			if (foundPositions[i] >= 0)
				continue;
			
			if (terms[i].text().equals(token.termText())){
//				calculate distance from original position:
				int dist = Math.abs(positions[i] - tokenPos);
				if (dist < minDist) {
					minDist = dist;
					foundPos = i;
				}
			}
		}
		
		
		if (foundPos >= 0){
			foundPositions[foundPos] = minDist;
			totDist = totDist + minDist;
		}
			
		boolean allTermsFound = true;
		for (int i = 0; i < foundPositions.length; i++) {
			if (foundPositions[i] < 0)
				allTermsFound = false;
		}
		
		if (allTermsFound){
			active = false;
			lastTermEndPos = token.endOffset();
		}
		
		tokenPos++;
		
		//stop when the distance is greater than slop 
		if (totDist > query.getSlop() 
				|| tokenPos > positions.length + query.getSlop())
			active = false;
		else if (allTermsFound)
			match = true;
	}
}
