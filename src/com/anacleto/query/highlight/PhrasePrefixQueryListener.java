package com.anacleto.query.highlight;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhrasePrefixQuery;

public class PhrasePrefixQueryListener extends TokenListener {
	
	PhrasePrefixQuery query;

	private int tokenPos = 0;
	
	//Total distance from the original
	private int totDist  = 0;
	
	int  foundPositions[];
	
	int  positions[];
	
	
	
	public PhrasePrefixQueryListener(PhrasePrefixQuery query, String postText, 
				int firstTermStartPos) {
		
		this.query = query;
		this.postText = postText;
		this.firstTermStartPos = firstTermStartPos;
		
		positions = query.getPositions();
		
		foundPositions = new int[positions.length];
		for (int i = 0; i < foundPositions.length; i++) {
			foundPositions[i] = -1;
		}
	}
	
	public void addToken(Token token){
		if (!active)
			return;
		
		int foundPos = -1;
		
		//find closest matching term,
		//that is not found already
		int minDist = Integer.MAX_VALUE;
		for (int i = 0; i < positions.length; i++) {
			if (foundPositions[i] >= 0)
				continue;
			
			Term terms[] = query.getTerms(positions[i]);
			for (int j = 0; j < terms.length; j++) {				
				if (terms[j].text().equals(token.termText())){
	//				calculate distance from original position:
					int dist = Math.abs(positions[i] - tokenPos);
					if (dist < minDist) {
						minDist = dist;
						foundPos = i;
					}
				}
			}
		}
		
		
		if (foundPos >= 0){
			foundPositions[foundPos] = minDist;
			totDist = totDist + minDist;
		}
			
		int notMatchingPos = 0;
		for (int i = 0; i < foundPositions.length; i++) {
			if (foundPositions[i] < 0){
				notMatchingPos++;
			}
		}
		
		if (notMatchingPos == 0){
			active = false;
			lastTermEndPos = token.endOffset();
		}
		
		tokenPos++;
		
		//stop when the distance is greater than slop
		//or if the not matching positions are
		if (totDist > query.getSlop() 
				|| tokenPos > positions.length + query.getSlop())
			
			active = false;
		else if (notMatchingPos == 0)
			match = true;
	}
}
